package com.vtmer.yisanbang.service.impl;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.vtmer.yisanbang.common.exception.service.cart.CartGoodsNotExistException;
import com.vtmer.yisanbang.common.exception.service.cart.OrderGoodsCartGoodsNotMatchException;
import com.vtmer.yisanbang.common.exception.service.order.*;
import com.vtmer.yisanbang.common.util.OrderNumberUtil;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.InventoryDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import com.vtmer.yisanbang.vo.CartVO;
import com.vtmer.yisanbang.vo.OrderVO;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundZSetOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    // 订单有效时间 30分钟
    private static final Integer EFFECTIVE_TIME = 30 * 60 * 1000;

    @Autowired
    private PostageMapper postageMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Autowired
    private PartSizeMapper partSizeMapper;

    @Autowired
    private ColorSizeMapper colorSizeMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SuitMapper suitMapper;

    @Autowired
    private RefundMapper refundMapper;

    @Autowired
    private RefundService refundService;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private DiscountMapper discountMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 满X件打折
    private int discountAmount;

    // 打折率
    private double discountRate;

    // Free the postage after standardPrice
    private double standardPrice;

    // postage
    private double defaultPostage;

    private void setPostage() {
        Postage postage = postageMapper.select();
        if (postage != null) {
            standardPrice = postage.getPrice();
            defaultPostage = postage.getDefaultPostage();
        } else {  // 达标金额和邮费都默认为0
            standardPrice = 0;
            defaultPostage = 0;
        }
    }

    // 设置优惠信息
    private void setDiscount() {
        Discount discount = discountMapper.select();
        if (discount != null) {
            discountAmount = discount.getAmount();
            discountRate = discount.getDiscountRate();
        } else {  // 如果未设置优惠，则默认达标数量为0，优惠*1,，即无打折
            discountAmount = 0;
            discountRate = 1.0;
        }
    }

    @Override
    public OrderVO getOrderVOByOrderNumber(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return getOrderVOByOrder(order);

    }

    /**
     * The user clicks the settlement button,and then confirm the order
     * used in cart
     *
     * @param
     * @return
     */
    @Override
    public OrderVO confirmCartOrder() {
        Integer userId = JwtFilter.getLoginUser().getId();
        setPostage();
        OrderVO orderVO = new OrderVO();
        // 获取用户购物车清单
        CartVO cartVo = cartService.selectCartVo(userId);
        if (cartVo == null) {
            logger.info("用户[{}]确认购物车订单时购物车为空", userId);
            throw new CartEmptyException();
        }
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        if (cartGoodsList == null) {
            logger.info("用户[{}]确认购物车订单时购物车勾选商品为空", userId);
            throw new CartGoodsNotExistException("购物车勾选商品为空！");
        }
        // IDEA推荐方式，删除为勾选的购物车商品
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen().equals(Boolean.FALSE));
        orderVO.setTotalPrice(cartVo.getTotalPrice());
        orderVO.setBeforeTotalPrice(cartVo.getBeforeTotalPrice());
        ArrayList<OrderGoodsDTO> orderGoodsDTOArrayList = new ArrayList<>();
        for (CartGoodsDTO cartGoodsDTO : cartGoodsList) {
            OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
            BeanUtils.copyProperties(cartGoodsDTO, orderGoodsDTO);
            orderGoodsDTOArrayList.add(orderGoodsDTO);
        }
        // 判断订单商品是否存在
        boolean check = judgeGoodsExist(orderGoodsDTOArrayList);
        if (!check) {
            logger.info("用户[{}]确认购物车订单时订单商品找不到", userId);
            throw new OrderGoodsNotExistException();
        }
        orderVO.setOrderGoodsDTOList(orderGoodsDTOArrayList);
        if (cartVo.getTotalPrice() >= standardPrice) {
            // 包邮
            orderVO.setPostage((double) 0);
        } else {  // 不包邮
            orderVO.setPostage(defaultPostage);
        }
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVO.setUserAddress(userAddress);
        return orderVO;
    }

    @Override
    public OrderVO confirmDirectOrder(List<OrderGoodsDTO> orderGoodsDTOList) {
        boolean check = judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            throw new OrderGoodsNotExistException();
        }
        Integer userId = JwtFilter.getLoginUser().getId();
        OrderVO orderVO = new OrderVO();
        // 优惠前总价
        double beforeTotalPrice = 0;
        // 优惠后总价
        double totalPrice = 0;
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            Integer sizeId = orderGoodsDTO.getColorSizeId();
            Boolean whetherGoods = orderGoodsDTO.getWhetherGoods();
            // 设置商品属性
            setOrderGoodsDTO(orderGoodsDTO, sizeId, whetherGoods);
            beforeTotalPrice += orderGoodsDTO.getAmount() * orderGoodsDTO.getPrice();
            totalPrice += orderGoodsDTO.getAfterTotalPrice();
        }
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        df.format(totalPrice);
        df.format(beforeTotalPrice);
        orderVO.setBeforeTotalPrice(beforeTotalPrice);
        orderVO.setTotalPrice(totalPrice);
        orderVO.setOrderGoodsDTOList(orderGoodsDTOList);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVO.setUserAddress(userAddress);
        return orderVO;
    }


    /**
     * create shopping cart order after users submit order
     * @param orderDTO:UserAddress(用户地址、联系人、手机号)、邮费、留言
     * @return openid、orderNumber
     * @throws
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCartOrder(OrderDTO orderDTO) {
        logger.info("创建购物车订单");
        int userId = JwtFilter.getLoginUser().getId();
        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        boolean check = judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            throw new OrderGoodsNotExistException();
        }
        // 获取用户购物车清单
        CartVO cartVo = cartService.selectCartVo(userId);
        if (cartVo == null) {
            // 说明购物车为空
            throw new CartEmptyException("购物车为空");
        }
        // 获取用户购物车商品列表
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        if (cartGoodsList == null) {
            throw new CartGoodsNotExistException("购物车勾选商品为空");
        }
        // 删除未勾选商品,得到购物车勾选商品列表
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen().equals(Boolean.FALSE));
        // 第一步校验 —— 检查前端传递的订单总价和后台计算的订单总价是否一致
        // 前端传递的订单总价
        Double totalPrice = orderDTO.getTotalPrice();
        // 后台从redis中取出的购物车总价
        Double totalPrice1 = cartVo.getTotalPrice();
        if (!totalPrice.equals(totalPrice1)) {
            // 如果二者不一致，抛出异常
            throw new OrderPriceNotMatchException();
        }
        // 第二步校验 —— 检查购物车商品列表和订单的商品列表是否一致
        if (orderGoodsDTOList.size() != cartGoodsList.size()) {
            // 如果购物车商品列表和订单的商品列表数量不一致
            throw new OrderGoodsCartGoodsNotMatchException();
        }

        // 遍历订单商品列表
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            // 默认为false
            boolean checkOrderGoods = false;
            Integer orderColorSizeId = orderGoodsDTO.getColorSizeId();
            Boolean orderWhetherGoods = orderGoodsDTO.getWhetherGoods();
            Integer orderAmount = orderGoodsDTO.getAmount();
            // 遍历购物车勾选商品列表
            for (CartGoodsDTO cartGoodsDTO : cartGoodsList) {
                Integer cartColorSizeId = cartGoodsDTO.getColorSizeId();
                Boolean cartWhetherGoods = cartGoodsDTO.getWhetherGoods();
                Integer cartAmount = cartGoodsDTO.getAmount();
                if (orderColorSizeId.equals(cartColorSizeId) &&
                        cartWhetherGoods.equals(orderWhetherGoods) &&
                        orderAmount.equals(cartAmount)) { // 如果购物车和订单的商品列表相同并且数量一致，则正确
                    checkOrderGoods = true;
                }
            } // end for
            // 遍历完购物车勾选商品列表checkOrderGoods若还为false，说明不匹配
            if (!checkOrderGoods) {
                throw new OrderGoodsCartGoodsNotMatchException();
            }
        } // end for
        // 校验完成，开始下单逻辑
        // 生成order
        String orderNumber = createOrder(orderDTO, userId);
        // 删除购物车勾选项
        cartService.deleteCartGoodsByIsChosen(userId);
        // 返回订单编号
        return orderNumber;
    }

    @Override
    public boolean judgeGoodsExist(List<OrderGoodsDTO> orderGoodsDTOList) {
        boolean check = true;
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            boolean whetherGoods = orderGoodsDTO.getWhetherGoods();
            Integer sizeId = orderGoodsDTO.getColorSizeId();
            if (whetherGoods) {
                // 如果是普通商品
                ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(sizeId);
                if (colorSize == null) {
                    // 如果该颜色尺寸不存在
                    check = false;
                }
                Goods goods = goodsMapper.selectByPrimaryKey(colorSize.getGoodsId());
                if (goods == null) {
                    // 如果商品不存在
                    check = false;
                }
            } else {
                PartSize partSize = partSizeMapper.selectByPrimaryKey(sizeId);
                if (partSize == null) {
                    // 该部件尺寸不存在
                    check = false;
                }
                Suit suit = suitMapper.selectByPrimaryKey(partSize.getSuitId());
                if (suit == null) {
                    check = false;
                }
            }
        }
        return check;
    }

    /**
     * 创建直接下单类订单
     *
     * @param orderDTO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String createDirectOrder(OrderDTO orderDTO) {
        int userId = JwtFilter.getLoginUser().getId();
        // 判断直接下单的商品是否存在
        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        boolean check = judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            // 如果商品或商品SKU不存在
            throw new OrderGoodsNotExistException();
        }
        // 获取用户订单商品列表和优惠后的总价
        Double totalPrice = orderDTO.getTotalPrice();

        // 前端传递的订单总价，在后台校验一遍
        double totalPriceCheck = calculateTotalPrice(orderGoodsDTOList);
        if (!totalPrice.equals(totalPriceCheck)) {
            // 如果前端传递的优惠后订单总价和后台计算的优惠后订单总价不一致，说明出现了问题，抛出异常
            logger.info("前后端订单总价不一致");
            throw new OrderPriceNotMatchException();
        }
        // 创建订单
        return createOrder(orderDTO, userId);
    }

    @Override
    public void remindOrder(String orderNumber) {
        int userId = JwtFilter.getLoginUser().getId();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException();
        } else if (!order.getUserId().equals(userId)) {
            // 如果不是该用户的订单
            throw new OrderAndUserNotMatchException();
        } else if (order.getWhetherRemind()) {
            // 如果已经提醒过发货了
            throw new OrderAlreadyRemindException();
        }
        // 更新订单提醒状态
        orderMapper.updateRemind(orderNumber);
    }

    private String createOrder(OrderDTO orderDTO, int userId) {
        UserAddress userAddress = orderDTO.getUserAddress();
        double postage = orderDTO.getPostage();
        String message = orderDTO.getMessage();
        // 生成订单编号
        String orderNumber = OrderNumberUtil.getOrderNumber();
        // 生成order
        Order order = new Order();
        order.setUserId(userId);
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setOrderNumber(orderNumber);
        java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
        df.format(orderDTO.getTotalPrice());
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setPostage(postage);
        order.setMessage(message);
        orderMapper.insert(order);
        logger.info("创建订单[{}]，订单状态[未支付]---用户id[{}]", orderNumber, userId);
        BoundZSetOperations<String, String> zSetOps = redisTemplate.boundZSetOps("OrderNumber");

        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            logger.info("开始遍历订单商品列表...");
            // 生成orderGoods
            OrderGoods orderGoods = new OrderGoods();
            boolean whetherGoods = orderGoodsDTO.getWhetherGoods();
            Integer amount = orderGoodsDTO.getAmount();
            Integer colorSizeId = orderGoodsDTO.getColorSizeId();
            // 商品库存
            int inventory = 0;
            if (whetherGoods) {
                // 如果是普通商品
                ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(colorSizeId);
                inventory = colorSize.getInventory();
            } else {
                PartSize partSize = partSizeMapper.selectByPrimaryKey(colorSizeId);
                inventory = partSize.getInventory();
            }
            if (inventory < amount) {
                // 如果下单商品库存不足
                logger.info("商品库存不足--商品skuId[{}],是否是普通商品[{}],需要数量[{}],库存数量[{}]", colorSizeId, whetherGoods, amount, inventory);
                throw new InventoryNotEnoughException("商品库存不足--商品skuId：" + colorSizeId + ",是否是普通商品:" + whetherGoods + ",需要数量" + amount);
            }
            orderGoods.setOrderId(order.getId());
            orderGoods.setWhetherGoods(whetherGoods);
            orderGoods.setAmount(amount);
            orderGoods.setTotalPrice(orderGoodsDTO.getAfterTotalPrice());
            orderGoods.setSizeId(colorSizeId);
            orderGoodsMapper.insert(orderGoods);
            // 减少相应商品的库存
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setSizeId(colorSizeId);
            inventoryDTO.setAmount(amount);
            inventoryDTO.setFlag(0);
            if (Boolean.TRUE.equals(whetherGoods)) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryDTO);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryDTO);
            }
        } // end for
        return orderNumber;
    }

    /**
     * 根据前端传递过来的订单商品列表计算总价
     *
     * @param orderGoodsDTOList：商品总价
     * @return：订单总价
     */
    private double calculateTotalPrice(List<OrderGoodsDTO> orderGoodsDTOList) {
        // 订单总价
        double totalPrice = 0;
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            int sizeId = orderGoodsDTO.getColorSizeId();
            boolean whetherGoods = orderGoodsDTO.getWhetherGoods();
            setOrderGoodsDTO(orderGoodsDTO, sizeId, whetherGoods);
            logger.info("单项商品总价为[{}]", orderGoodsDTO.getAfterTotalPrice());
            totalPrice += orderGoodsDTO.getAfterTotalPrice();
        }
        logger.info("后台计算的订单总价为[{}]", totalPrice);
        return totalPrice;
    }

    /**
     * 获取用户指定订单状态的订单
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     *
     * @param status:status传入3时同时获取退款成功（3)的订单;status传入5查询所有订单
     * @return
     */
    @Override
    @Transactional
    public List<OrderVO> getUserOrderList(Integer status) {
        HashMap<String, Integer> orderMap = new HashMap<>();
        Integer userId = JwtFilter.getLoginUser().getId();
        orderMap.put("userId", userId);
        orderMap.put("status", status);
        return getOrderVOArrayList(orderMap);
    }

    @Override
    public List<OrderVO> getOrderList(Integer status) {
        HashMap<String, Integer> orderMap = new HashMap<>();
        // userId为null表示不把userId当查询条件
        orderMap.put("userId", null);
        orderMap.put("status", status);
        return getOrderVOArrayList(orderMap);
    }

    /**
     * 根据标识获取相应订单列表
     *
     * @param orderMap
     * @return
     */
    private ArrayList<OrderVO> getOrderVOArrayList(Map<String, Integer> orderMap) {
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<>();
        List<Order> orderList = orderMapper.selectAllByUserIdAndStatus(orderMap);
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVOByOrder(order);
            orderVOArrayList.add(orderVO);
        }
        return orderVOArrayList;
    }

    /**
     * 通过订单表实体类获取订单详情VO对象
     *
     * @param order:订单表实体类
     * @return OrderVo
     */
    private OrderVO getOrderVOByOrder(Order order) {
        OrderVO orderVO = new OrderVO();
        UserAddress userAddress = new UserAddress();
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();

        // 订单退款状态
        Refund refund = refundMapper.selectByOrderId(order.getId());
        if (refund != null) { // 如果该订单有退款信息
            // 设置退款状态
            orderVO.setRefundStatus(refund.getStatus());
        }

        // 订单id
        orderVO.setOrderId(order.getId());

        // 订单状态
        orderVO.setOrderStatus(order.getStatus());

        // 用户地址封装
        userAddress.setUserId(order.getUserId());
        userAddress.setUserName(order.getUserName());
        userAddress.setPhoneNumber(order.getPhoneNumber());
        userAddress.setAddressName(order.getAddressName());
        orderVO.setUserAddress(userAddress);

        BeanUtils.copyProperties(order, orderVO);
        // 根据订单id查询该订单的所有商品
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());

        for (OrderGoods orderGoods : orderGoodsList) {
            OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
            // 商品单价
            double price = orderGoods.getTotalPrice() / orderGoods.getAmount();

            // 设置商品价格
            orderGoodsDTO.setPrice(price);
            orderGoodsDTO.setAmount(orderGoods.getAmount());
            orderGoodsDTO.setAfterTotalPrice(orderGoods.getTotalPrice());

            // 设置商品基础信息
            Integer sizeId = orderGoods.getSizeId();
            Boolean isGoods = orderGoods.getWhetherGoods();
            setOrderGoodsDTO(orderGoodsDTO, sizeId, isGoods);

            orderGoodsDTOList.add(orderGoodsDTO);
        }
        orderVO.setOrderGoodsDTOList(orderGoodsDTOList);
        return orderVO;
    }

    /**
     * 订单状态自增修改
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 0--待付款 1--待发货 2--待收货 类型订单 更新订单状态
     *
     * @param orderNumber:订单编号
     * @return -2 —— 订单状态不能自增修改
     * -1 —— 订单id不存在
     * 0 —— 更新订单状态失败
     * 1 —— 更新订单状态成功
     */
    @Override
    public int updateOrderStatus(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order != null) {
            // 如果该订单存在
            Integer status = order.getStatus();
            if (status >= 0 && status < 3) { // 如果订单状态是待付款、待发货、待收货
                // 更新订单状态
                int res = orderMapper.updateOrderStatus(order.getId());
                return res;
            } else {
                // 订单状态不能自增修改
                throw new OrderStatusNotFitException("该订单状态[" + status + "]不能自增修改");
            }
        } else {
            // 订单不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     *
     * @param orderMap—— orderId、status
     * @return -2 —— 订单id不存在
     * -1 —— 将要修改的订单状态与原状态相同
     * 0 —— 更新订单状态失败
     * 1 —— 更新订单状态成功
     */
    @Override
    public void setOrderStatus(Map<String, Integer> orderMap) {
        Integer orderId = orderMap.get("orderId");
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            // 更新订单状态
            orderMapper.setOrderStatus(orderMap);
        } else {
            // 订单不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 删除订单 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     *
     * @param orderId
     * @return
     */
    @Override
    public void deleteOrder(Integer orderId) {
        Integer userId = JwtFilter.getLoginUser().getId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            if (!userId.equals(order.getUserId())) {
                // 订单和用户不匹配
                throw new OrderAndUserNotMatchException("订单和用户不匹配--订单编号：" + order.getOrderNumber() + ",用户id：" + userId);
            } else if (order.getStatus() != 3 || order.getStatus() != 4) {
                // 如果订单不是已完成或者交易关闭
                throw new OrderStatusNotFitException("该订单状态不能执行删除订单操作");
            }
            logger.info("删除订单[{}]", order.getOrderNumber());
            orderMapper.deleteOrderById(orderId);
        } else {
            // 订单不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 通过订单号查询订单详情信息
     *
     * @return 订单详情信息OrderDTO
     */
    @Override
    public OrderVO selectOrderVOByOrderNumber(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        return getOrderVOByOrder(order);
    }

    /**
     * 通过订单号查询订单基础信息
     *
     * @param orderNumber：订单号
     * @return order：订单基础信息
     */
    @Override
    public Order selectOrderByOrderNumber(String orderNumber) {
        return orderMapper.selectByOrderNumber(orderNumber);
    }

    /**
     * 设置订单的快递编号
     *
     * @param order：courierNumber、orderNumber
     * @return
     */
    @Override
    @Transactional
    public void setCourierNumber(Order order) {
        String orderNumber = order.getOrderNumber();
        Order order1 = orderMapper.selectByOrderNumber(orderNumber);
        if (order1 == null) {
            throw new OrderNotFoundException();
        } else if (order1.getStatus() != 1) {
            // 如果订单状态不是待发货状态
            throw new OrderStatusNotFitException("该订单[" + orderNumber + "]状态不是待发货状态，不可设置快递编号");
        } else {
            // 修改订单的状态为待收货
            updateOrderStatus(orderNumber);
            logger.info("设置订单[{}]的快递编号为[{}],订单状态改变[待发货]-->[待收货]", order, order.getCourierNumber());
            // 设置订单的快递编号
            orderMapper.setCourierNumber(order);
        }
    }

    @Override
    public void setOrderGoodsDTO(OrderGoodsDTO orderGoodsDTO, Integer sizeId, Boolean isGoods) {
        setDiscount();
        orderGoodsDTO.setColorSizeId(sizeId);
        orderGoodsDTO.setWhetherGoods(isGoods);
        // 如果是普通商品
        if (Boolean.TRUE.equals(isGoods)) {
            ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(sizeId);
            if (colorSize == null) {
                throw new OrderGoodsNotExistException();
            }
            orderGoodsDTO.setSize(colorSize.getSize());
            orderGoodsDTO.setPartOrColor(colorSize.getColor());
            orderGoodsDTO.setId(colorSize.getGoodsId());
            Goods goods = goodsMapper.selectByPrimaryKey(colorSize.getGoodsId());
            if (goods == null) {
                throw new OrderGoodsNotExistException();
            }
            orderGoodsDTO.setTitle(goods.getName());
            orderGoodsDTO.setPicture(goods.getPicture());
            orderGoodsDTO.setPrice(goods.getPrice());
        } else { // 如果是套装散件
            PartSize partSize = partSizeMapper.selectByPrimaryKey(sizeId);
            if (partSize == null) {
                throw new OrderGoodsNotExistException();
            }
            orderGoodsDTO.setSize(partSize.getSize());
            orderGoodsDTO.setPartOrColor(partSize.getPart());
            Suit suit = suitMapper.selectByPrimaryKey(partSize.getSuitId());
            if (suit == null) {
                throw new OrderGoodsNotExistException();
            }
            orderGoodsDTO.setTitle(suit.getName());
            orderGoodsDTO.setPicture(suit.getPicture());
            orderGoodsDTO.setId(suit.getId());
            orderGoodsDTO.setPrice(partSize.getPrice());
        }
        double totalPrice = orderGoodsDTO.getAmount() * orderGoodsDTO.getPrice();
        if (orderGoodsDTO.getAmount() >= discountAmount) {
            // 如果符合优惠条件
            totalPrice *= discountRate;
        }
        // 计算了优惠的单项商品总价
        orderGoodsDTO.setAfterTotalPrice(totalPrice);
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     *
     * @param orderVO:userAddress、orderNumber
     * @return
     */
    @Override
    public void updateAddress(OrderVO orderVO) {
        Integer userId = JwtFilter.getLoginUser().getId();
        String orderNumber = orderVO.getOrderNumber();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order == null) {
            throw new OrderNotFoundException();
        } else if (!userId.equals(order.getUserId())) {
            throw new OrderAndUserNotMatchException("订单[" + orderNumber + "]和用户[" + userId + "]" + "不匹配");
        } else if (!(order.getStatus() >= 0 && order.getStatus() <= 1)) {
            // 订单状态不为 待付款、待发货，则不可修改地址
            logger.info("订单[{}]的状态为[{}]，不可修改收货地址", orderNumber, order.getStatus());
            throw new OrderStatusNotFitException("订单[{" + orderNumber + "}]的状态为[{" + order.getStatus() + "}]，不可修改收货地址");
        }
        // 正常执行逻辑
        UserAddress userAddress = orderVO.getUserAddress();
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        logger.info("修改订单[{}]地址信息---收货地址：[{}],联系人姓名[{}],联系号码[{}]",
                orderNumber, order.getAddressName(), order.getUserName(), order.getPhoneNumber());
        orderMapper.updateAddressByOrderNumber(order);
    }

    /**
     * 用户取消订单
     *
     * @param orderNumber：订单编号
     * @return
     */
    @Override
    @Transactional
    public void cancelOrder(String orderNumber) {
        Integer userId = JwtFilter.getLoginUser().getId();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order != null) {
            if (!userId.equals(order.getUserId())) {
                throw new OrderAndUserNotMatchException("订单[" + orderNumber + "]和用户[" + userId + "]" + "不匹配");
            } else if (order.getStatus() == 0) {
                // 正确逻辑，取消订单，更新订单状态为4，交易关闭
                logger.info("订单[{}]取消，状态[未付款]-->[交易关闭]", order.getOrderNumber());
                HashMap<String, Integer> orderMap = new HashMap<>();
                orderMap.put("orderId", order.getId());
                orderMap.put("status", 4);
                setOrderStatus(orderMap);
                // 库存归位
                updateInventory(order.getOrderNumber(), 1);
            } else { // 订单状态不可取消订单
                throw new OrderStatusNotFitException("订单[{" + orderNumber + "}]的状态[{" + order.getStatus() + "}]不可取消订单");
            }
        } else { // 订单id不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 更新订单商品库存
     *
     * @param orderNumber：订单编号
     * @param flag：1代表增加库存，0代表减少库存
     * @return
     */
    @Override
    public void updateInventory(String orderNumber, Integer flag) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        // 库存归位
        logger.info("用户取消订单，库存归位");
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());
        for (OrderGoods orderGoods : orderGoodsList) {
            Boolean isGoods = orderGoods.getWhetherGoods();
            Integer sizeId = orderGoods.getSizeId();
            Integer amount = orderGoods.getAmount();
            InventoryDTO inventoryDTO = new InventoryDTO();
            inventoryDTO.setAmount(amount);
            inventoryDTO.setSizeId(sizeId);
            inventoryDTO.setFlag(flag);
            if (Boolean.TRUE.equals(isGoods)) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryDTO);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryDTO);
            }
        } // end for
    }

    @Override
    public List<Order> getUnRefundPayOrderList() {
        List<Order> payOrderList = orderMapper.getPayOrder();
        if (payOrderList != null) {
            return refundService.getUnRefundOrder(payOrderList);
        } else {
            return null;
        }
    }

    /**
     * 获取未付款订单
     *
     * @return
     */
    private List<Order> getNotPayOrder() {
        return orderMapper.getNotPayOrder();
    }

    @Override
    public void orderTimeOutLogic() {
        logger.info("执行订单超时检测任务");
        // 订单超时未付款，自动关闭
        // 超时时间（分）30
        // 得到超时的时间点
        LocalDateTime localDateTime = LocalDateTime.now().minusMinutes(EFFECTIVE_TIME);
        // 队列
        Queue<Order> queue = new LinkedList<>();
        // 未付款订单列表
        List<Order> notPayOrder = getNotPayOrder();

        // 如果未支付订单不为空
        if (!notPayOrder.isEmpty()) {
            for (Order order : notPayOrder) {
                queue.offer(order);
                // 队列去重订单id
                queue = StreamEx.of(queue).distinct(Order::getId).collect(Collectors.toCollection(LinkedList::new));
            }
        }
        // 获取队列的头元素,开始检测头订单是否失效
        Order element = queue.peek();
        while (element != null) {
            //时间差值
            Long diff = this.checkOrder(element);
            if (diff != null && diff >= EFFECTIVE_TIME) {
                try {
                    String orderNumber = element.getOrderNumber();
                    logger.info("开始关闭订单任务，订单编号{},下单时间{}", orderNumber, element.getCreateTime());
                    // 调用微信查询订单接口，确认用户是否真的未付款
                    WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, orderNumber);
                    // 更待订单状态为交易关闭
                    if ("SUCCESS".equals(wxPayOrderQueryResult.getTradeState())) {
                        // 如果微信订单结果为已支付，说明程序错误，给予补偿,更新订单状态为待发货
                        logger.info("微信订单查询结果为已支付，订单编号{},下单时间{}，给予补偿处理", orderNumber, element.getCreateTime());
                        Map<String, Integer> orderMap = new HashMap<>();
                        orderMap.put("orderId", element.getId());
                        orderMap.put("status", 1);
                        orderMapper.setOrderStatus(orderMap);
                    } else if ("NOTPAY".equals(wxPayOrderQueryResult.getTradeState())) {
                        // 关闭订单
                        logger.info("开始关闭订单任务，订单编号{},下单时间{}", orderNumber, element.getCreateTime());
                        HashMap<String, Integer> orderMap = new HashMap<>();
                        orderMap.put("orderId", element.getId());
                        orderMap.put("status", 4);
                        setOrderStatus(orderMap);
                        logger.info("订单[{}]状态更变：[未付款]-->[交易关闭]", element.getOrderNumber());
                        // 库存归位,1代表增加库存
                        updateInventory(element.getOrderNumber(), 1);
                        // 弹出队列
                        queue.poll();
                        // 取下一个元素
                        element = queue.peek();
                    } else if ("ORDERNOTEXIST".equals(wxPayOrderQueryResult.getTradeState())) {
                        // 订单不存在,说明该订单未调用微信支付接口，直接删除吧
                        orderMapper.deleteByPrimaryKey(element.getId());
                    }
                } catch (WxPayException e) {
                    logger.info("调用微信查询订单接口出错，异常信息为[{}]",e.getMessage());
                }
            } else if (diff != null) {
                // 如果diff<EFFECTIVE_TIME
                try {
                    logger.info("等待检测订单,订单编号为{}，下单时间{},已下单{}秒", element.getId(), element.getCreateTime(), diff / 1000);
                    Thread.sleep(EFFECTIVE_TIME - diff);
                } catch (InterruptedException e) {
                    logger.info("OrderAutoCancelJob.checkOrder定时任务出现问题");
                }
            } // end else if
        } // end while
    }


    /**
     * 获取订单的下单时间和现在的时间差
     *
     * @param order：订单实体类
     * @return
     */
    private Long checkOrder(Order order) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long diff = null;
        if (order != null) {
            Date createTime = order.getCreateTime();
            try {
                diff = sdf.parse(sdf.format(date)).getTime() - sdf.parse(sdf.format(createTime)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 返回值为毫秒
        return diff;
    }

}
