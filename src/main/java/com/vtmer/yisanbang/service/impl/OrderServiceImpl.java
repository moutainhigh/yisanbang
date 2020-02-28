package com.vtmer.yisanbang.service.impl;

import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.vtmer.yisanbang.common.exception.service.cart.OrderGoodsCartGoodsNotMatchException;
import com.vtmer.yisanbang.common.exception.service.order.*;
import com.vtmer.yisanbang.common.util.OrderNumberUtil;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import com.vtmer.yisanbang.vo.CartVO;
import com.vtmer.yisanbang.vo.OrderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    // 订单有效时间 30分钟
    private static final Integer EFFECTIVE_TIME = 30;

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
        CartVO cartVo = cartService.selectCartVo();
        if (cartVo == null) {
            throw new CartEmptyException();
        }
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
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
        boolean check = judgeGoodsExist(orderGoodsDTOArrayList);
        if (!check) {
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
        orderVO.setBeforeTotalPrice(beforeTotalPrice);
        orderVO.setTotalPrice(totalPrice);
        orderVO.setOrderGoodsDTOList(orderGoodsDTOList);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVO.setUserAddress(userAddress);
        return orderVO;
    }


    /**
     * create shopping cart order after users submit order
     *
     * @param orderDTO:UserAddress(用户地址、联系人、手机号)、邮费、留言
     * @return openid、orderNumber
     * @throws DataIntegrityViolationException：库存不足抛出异常
     */
    @Override
    @Transactional
    public String createCartOrder(OrderDTO orderDTO) {
        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        boolean check = judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            throw new OrderGoodsNotExistException();
        }
        // 获取用户购物车清单
        CartVO cartVo = cartService.selectCartVo();
        // 获取用户购物车商品列表
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        // 删除未勾选商品,得到购物车勾选商品列表
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen().equals(Boolean.FALSE));

        // 第一步校验 —— 检查前端传递的订单总价和后台计算的订单总价是否一致
        // 前端传递的订单总价
        double totalPrice = orderDTO.getTotalPrice();
        // 后台从redis中取出的购物车总价
        double totalPrice1 = cartVo.getTotalPrice();
        if (totalPrice != totalPrice1) {
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
        String orderNumber = createOrder(orderDTO);
        // 删除购物车勾选项
        cartService.deleteCartGoodsByIsChosen();
        // 返回订单编号和openid
        return orderNumber;
    }

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
     * @param orderDTO
     * @return
     */
    @Transactional
    @Override
    public String createDirectOrder(OrderDTO orderDTO) {
        // 判断直接下单的商品是否存在
        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        boolean check = judgeGoodsExist(orderGoodsDTOList);
        if (!check) {
            // 如果商品或商品SKU不存在
            throw new OrderGoodsNotExistException();
        }
        // 获取用户订单商品列表和优惠后的总价
        Double totalPrice = orderDTO.getTotalPrice();
        logger.info("前端传递的订单总价为[{}]", totalPrice);

        // 前端传递的订单总价，在后台校验一遍
        double totalPriceCheck = calculateTotalPrice(orderGoodsDTOList);
        if (!totalPrice.equals(totalPriceCheck)) {
            // 如果前端传递的优惠后订单总价和后台计算的优惠后订单总价不一致，说明出现了问题，抛出异常
            logger.info("前后端订单总价不一致");
            throw new OrderPriceNotMatchException();
        }
        // 创建订单
        return createOrder(orderDTO);
    }

    private String createOrder(OrderDTO orderDTO) {
        UserAddress userAddress = orderDTO.getUserAddress();
        double postage = orderDTO.getPostage();
        String message = orderDTO.getMessage();
        User user = JwtFilter.getLoginUser();
        // 生成订单编号
        String orderNumber = OrderNumberUtil.getOrderNumber();
        // 生成order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setOrderNumber(orderNumber);
        order.setTotalPrice(orderDTO.getTotalPrice());
        order.setPostage(postage);
        order.setMessage(message);
        orderMapper.insert(order);
        logger.info("创建订单[{}]，订单状态[未支付]---用户id[{}]", orderNumber, user.getId());
        BoundZSetOperations<String, String> zSetOps = redisTemplate.boundZSetOps("OrderNumber");
        // 延迟30分钟
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, EFFECTIVE_TIME);
        int minute30Later = (int) (cal.getTimeInMillis() / 1000);
        // score为超时时间戳，zset集合值orderId4的分数
        zSetOps.add(order.getOrderNumber(), minute30Later);
        List<OrderGoodsDTO> orderGoodsDTOList = orderDTO.getOrderGoodsDTOList();
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
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
                logger.info("商品库存不足--商品skuId[{}],是否是普通商品[{}],需要数量[{}],库存数量[{}]",colorSizeId,whetherGoods,amount,inventory);
                throw new InventoryNotEnoughException("商品库存不足--商品skuId：" + colorSizeId + ",是否是普通商品:" + whetherGoods + ",需要数量" + amount);
            }
            orderGoods.setOrderId(order.getId());
            orderGoods.setWhetherGoods(whetherGoods);
            orderGoods.setAmount(amount);
            orderGoods.setTotalPrice(orderGoodsDTO.getAfterTotalPrice());
            orderGoods.setSizeId(colorSizeId);
            orderGoodsMapper.insert(orderGoods);
            // 减少相应商品的库存
            // 不调用updateInventory方法，省得插入又再查一次数据库
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            inventoryMap.put("amount", amount);
            inventoryMap.put("sizeId", colorSizeId);
            // 0代表减少库存
            inventoryMap.put("flag", 0);
            if (Boolean.TRUE.equals(whetherGoods)) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
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
        Refund refund;
        for (Order order : orderList) {
            OrderVO orderVO = getOrderVOByOrder(order);
            refund = refundMapper.selectByOrderId(order.getId());
            if (refund != null) { // 如果该订单有退款信息
                // 设置退款状态
                orderVO.setRefundStatus(refund.getStatus());
            }
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
        Integer status = orderMap.get("status");
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            // 如果该订单存在
            if (status.equals(order.getStatus())) {
                // 如果将要修改的订单状态与原状态相同
                throw new OrderStatusNotFitException("将要修改的订单状态与原状态相同");
            } else { // 更新订单状态
                orderMapper.setOrderStatus(orderMap);
            }
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
     * @return 返回成功失败状态
     */
    @Override
    public void updateInventory(String orderNumber, Integer flag) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        // 库存归位
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());
        for (OrderGoods orderGoods : orderGoodsList) {
            Boolean isGoods = orderGoods.getWhetherGoods();
            Integer sizeId = orderGoods.getSizeId();
            Integer amount = orderGoods.getAmount();
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            inventoryMap.put("sizeId", sizeId);
            inventoryMap.put("amount", amount);
            // 1代表增加库存,0代表减少库存
            inventoryMap.put("flag", flag);
            if (isGoods.equals(Boolean.TRUE)) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
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

    @Override
    @Transactional
    public void orderTimeOutLogic() {
        BoundZSetOperations<String, String> zSetOps = redisTemplate.boundZSetOps("OrderNumber");
        Cursor<ZSetOperations.TypedTuple<String>> cursor;
        while (true) {
            try {
                cursor = zSetOps.scan(ScanOptions.NONE);
                if (!cursor.hasNext()) {
                    logger.debug("当前没有等待的订单取消任务");
                    Thread.sleep(500);
                    continue;
                }
                ZSetOperations.TypedTuple typedTuple = cursor.next();
                int score = Objects.requireNonNull(typedTuple.getScore()).intValue();
                Calendar cal = Calendar.getInstance();
                int nowSecond = (int) (cal.getTimeInMillis() / 1000);
                if (nowSecond >= score) {
                    Object value = typedTuple.getValue();
                    Long removeCount = zSetOps.remove(value);
                    if (removeCount != null && removeCount > 0) {
                        // 订单取消
                        String orderNumber = String.valueOf(value == null ? "" : value.toString());
                        logger.info("开始消费redis订单[{}]", orderNumber);
                        Order order = orderMapper.selectByOrderNumber(orderNumber);
                        if (order.getStatus() != 0) {
                            // 如果该订单状态不为0，即不是未付款，说明已经付款了，不需要取消订单
                            continue;
                        }
                        // 调用微信查询订单接口，确认用户是否真的未付款
                        WxPayOrderQueryResult wxPayOrderQueryResult = wxPayService.queryOrder(null, orderNumber);
                        if ("SUCCESS".equals(wxPayOrderQueryResult.getTradeState())) {
                            // 如果微信订单结果为已支付，说明程序错误，给予补偿,更新订单状态为待发货
                            Map<String, Integer> orderMap = new HashMap<>();
                            orderMap.put("orderId", order.getId());
                            orderMap.put("status", 1);
                            orderMapper.setOrderStatus(orderMap);
                        } else if ("NOTPAY".equals(wxPayOrderQueryResult.getTradeState())) {
                            // 如果结果为未支付，开始关闭订单操作
                            logger.info("开始关闭超时订单任务，订单编号[{}],下单时间[{}]", orderNumber, order.getCreateTime());
                            // 更待订单状态为交易关闭
                            HashMap<String, Integer> orderMap = new HashMap<>();
                            orderMap.put("orderId", order.getId());
                            orderMap.put("status", 4);
                            setOrderStatus(orderMap);
                            logger.info("订单[{}]状态更变：[未付款]-->[交易关闭]", orderNumber);
                            // 库存归位,1代表增加库存
                            updateInventory(orderNumber, 1);
                            // 调用微信支付关闭订单操作，以免用户继续操作支付
                            wxPayService.closeOrder(orderNumber);
                        }
                    } // end if
                } // end if 取消订单
            } catch (WxPayException wxPayEx) {
                logger.warn("调用微信查询订单和关闭订单出错[{}]", wxPayEx.getMessage());
            } catch (InterruptedException e) {
                logger.warn("Interrupted!订单超时自动取消任务出现异常[{}]", e.getMessage());
                // clean up state...
                Thread.currentThread().interrupt();
            }
        } // end while
    }

}
