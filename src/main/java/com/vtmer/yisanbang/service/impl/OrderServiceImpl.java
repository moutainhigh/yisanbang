package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.TokenInterceptor;
import com.vtmer.yisanbang.common.exception.service.order.*;
import com.vtmer.yisanbang.common.util.OrderNumberUtil;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import com.vtmer.yisanbang.dto.CartOrderDTO;
import com.vtmer.yisanbang.dto.OrderDTO;
import com.vtmer.yisanbang.dto.OrderGoodsDTO;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.CartVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

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

    // Free the postage after standardPrice
    private double standardPrice;

    // postage
    private double defaultPostage;

    private void setPostage() {
        Postage postage = postageMapper.select();
        if (postage!=null) {
            standardPrice = postage.getPrice();
            defaultPostage = postage.getDefaultPostage();
        } else {  // 达标金额和邮费都默认为0
            standardPrice = 0;
            defaultPostage = 0;
        }
    }

    /**
     * The user clicks the settlement button,and then confirm the order
     * used in cart
     * @param
     * @return
     *
     */
    public OrderDTO confirmCartOrder() {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        setPostage();
        OrderDTO orderDTO = new OrderDTO();
        // 获取用户购物车清单
        CartVo cartVo = cartService.selectCartVo();
        if (cartVo == null) {
            throw new CartEmptyException();
        }
        if (cartVo.getTotalPrice() >= standardPrice) { // 包邮
            orderDTO.setPostage(0);
        } else {  // 不包邮
            orderDTO.setPostage(defaultPostage);
        }
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        // IDEA推荐方式，删除为勾选的购物车商品
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen() == Boolean.FALSE);
        orderDTO.setTotalPrice(cartVo.getTotalPrice());
        orderDTO.setBeforeTotalPrice(cartVo.getBeforeTotalPrice());
        ArrayList<OrderGoodsDTO> orderGoodsDTOArrayList = new ArrayList<>();
        for (CartGoodsDTO cartGoodsDTO : cartGoodsList) {
            OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
            BeanUtils.copyProperties(cartGoodsDTO,orderGoodsDTO);
            orderGoodsDTOArrayList.add(orderGoodsDTO);
        }
        orderDTO.setOrderGoodsDTOList(orderGoodsDTOArrayList);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderDTO.setUserAddress(userAddress);
        return orderDTO;
    }

    /**
     * create shopping cart order after users submit order
     * @param cartOrderDTO:UserAddress(用户地址、联系人、手机号)、邮费、留言
     * @return openid、orderNumber
     * @throws DataIntegrityViolationException：库存不足抛出异常
     */
    @Transactional
    public Map<String,String> createCartOrder(CartOrderDTO cartOrderDTO) throws DataIntegrityViolationException {

        User user = TokenInterceptor.getLoginUser();
        /* ..写多了
        // 获取用户购物车清单
        CartVo cartVo = cartService.selectCartVo();
        // 获取用户购物车商品列表
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        // 删除未勾选商品,得到购物车勾选商品列表
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen() == Boolean.FALSE);
        List<OrderGoodsDTO> orderGoodsDTOList = OrderDTO.getOrderGoodsDTOList();
        // 检查购物车商品列表和订单的商品列表是否一致
        if (orderGoodsDTOList.size() != cartGoodsList.size()) {
            // 如果购物车商品列表和订单的商品列表数量不一致
            throw new OrderGoodsCartGoodsNotMatchException();
        }
        ArrayList<Boolean> checkList = new ArrayList<>();
        // 遍历订单商品列表
        for (OrderGoodsDTO orderGoodsDTO : orderGoodsDTOList) {
            // 默认为false
            Boolean checkOrderGoods = false;
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
         */
        // 返回map
        HashMap<String, String> orderMap = new HashMap<>();
        // 从登录信息中拿到openId
        String openId = user.getOpenId();
        orderMap.put("openId",openId);
        // 生成订单编号
        String orderNumber = OrderNumberUtil.getOrderNumber();
        orderMap.put("orderNumber",orderNumber);
        // 用户地址
        UserAddress userAddress = cartOrderDTO.getUserAddress();
        // 获取用户购物车清单
        CartVo cartVo = cartService.selectCartVo();
        // 获取用户购物车商品列表
        List<CartGoodsDTO> cartGoodsList = cartVo.getCartGoodsList();
        // 删除未勾选商品,得到购物车勾选商品列表
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getWhetherChosen() == Boolean.FALSE);

        // 生成order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setOrderNumber(orderNumber);
        order.setTotalPrice(cartVo.getTotalPrice());
        order.setPostage(cartOrderDTO.getPostage());
        if (cartOrderDTO.getMessage()!=null) {
            order.setMessage(cartOrderDTO.getMessage());
        }
        orderMapper.insert(order);
        logger.info("创建订单[{}]，订单状态[未支付]---用户id[{}]",orderNumber,user.getId());
        for (CartGoodsDTO cartGoodsDTO : cartGoodsList) {
            // 生成orderGoods
            OrderGoods orderGoods = new OrderGoods();
            Boolean whetherGoods = cartGoodsDTO.getWhetherGoods();
            Integer amount = cartGoodsDTO.getAmount();
            Integer colorSizeId = cartGoodsDTO.getColorSizeId();
            orderGoods.setOrderId(order.getId());
            orderGoods.setIsGoods(whetherGoods);
            orderGoods.setAmount(amount);
            orderGoods.setTotalPrice(cartGoodsDTO.getAfterTotalPrice());
            orderGoods.setSizeId(colorSizeId);
            orderGoodsMapper.insert(orderGoods);

            // 减少相应商品的库存
            // 不调用updateInventory方法，省得插入又再查一次数据库
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            inventoryMap.put("amount",amount);
            inventoryMap.put("sizeId",colorSizeId);
            // 0代表减少库存
            inventoryMap.put("flag",0);
            int res;
            if (whetherGoods == Boolean.TRUE) {
                res = colorSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            } else {
                res = partSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            }
            if (res == 0) {
                // 如果更新失败，说明库存不足
                throw new InventoryNotEnoughException("商品库存不足--商品skuId："+colorSizeId+",是否是普通商品:"+whetherGoods+",需要数量"+amount);
            }
        } // end for
        // 删除购物车勾选项
        cartService.deleteCartGoodsByIsChosen();
        // 返回订单编号和openid
        return orderMap;
    }

    /**
     * 获取用户指定订单状态的订单
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 退款状态定义：status 退款状态 0--等待商家处理  1--退款中（待买家发货） 2--退款中（待商家收货） 3--退款成功 4--退款失败
     * @param orderMap —— flag、status
     *                 flag为0时查询商城内的订单
     *                 status传入3时同时获取退款成功、退款失败（3 4）的订单
     *                 status传入5查询所有订单
     * @return
     */
    @Transactional
    public List<OrderDTO> getOrderList(Map<String,Integer> orderMap) {

        Integer userId = TokenInterceptor.getLoginUser().getId();
        if (orderMap.get("flag") == 1) {
            // 如果是获取用户的订单
            // 向map中添加userId,查询订单
            orderMap.put("userId",userId);
        } else {
            // 查询所有订单
            orderMap.put("userId",null);
        }

        ArrayList<OrderDTO> orderDTOArrayList = new ArrayList<>();

        List<Order> orderList = orderMapper.selectAllByUserIdAndStatus(orderMap);

        for (Order order : orderList) {
            OrderDTO orderDTO = getOrderDTOByOrder(order);
            Refund refund = refundMapper.selectByOrderId(order.getId());
            if (refund!=null) { // 如果该订单有退款信息
                // 设置退款状态
                orderDTO.setRefundStatus(refund.getStatus());
            }
            orderDTOArrayList.add(orderDTO);
        }
        return orderDTOArrayList;
    }

    /**
     * 订单状态自增修改
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 0--待付款 1--待发货 2--待收货 类型订单 更新订单状态
     * @param orderNumber:订单编号
     * @return -2 —— 订单状态不能自增修改
     *         -1 —— 订单id不存在
     *         0 —— 更新订单状态失败
     *         1 —— 更新订单状态成功
     */
    public int updateOrderStatus(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        Integer userId = TokenInterceptor.getLoginUser().getId();
        if (order!=null) {
            // 如果该订单存在
            if (!userId.equals(order.getUserId())) {
                // 订单和用户不匹配
                throw new OrderAndUserNotMatchException("订单和用户不匹配--订单编号："+orderNumber+",用户id："+userId);
            }
            Integer status = order.getStatus();
            if (status>=0 && status<3) { // 如果订单状态是待付款、待发货、待收货
                // 更新订单状态
                int res = orderMapper.updateOrderStatus(order.getId());
                logger.info("更新订单[{}]状态：[待收货]-->[已完成]",orderNumber);
                return res;
            } else {
                // 订单状态不能自增修改
                throw new OrderStatusNotFitException("该订单状态["+ status +"]不能自增修改");
            }
        } else {
            // 订单不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * 非 0--待付款 1--待发货 2--待收货 状态订单 设置订单状态
     * @param orderMap—— orderId、status
     * @return -2 —— 订单id不存在
     *         -1 —— 将要修改的订单状态与原状态相同
     *         0 —— 更新订单状态失败
     *         1 —— 更新订单状态成功
     */
    public void setOrderStatus(Map<String, Integer> orderMap) {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        Integer orderId = orderMap.get("orderId");
        Integer status = orderMap.get("status");
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            // 如果该订单存在
            if (!userId.equals(order.getUserId())) {
                // 订单和用户不匹配
                throw new OrderAndUserNotMatchException("订单和用户不匹配--订单编号："+order.getOrderNumber()+",用户id："+userId);
            }else if (status.equals(order.getStatus())) {
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
     * @param orderId
     * @return
     */
    public void deleteOrder(Integer orderId) {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order!=null) {
            if (!userId.equals(order.getUserId())) {
                // 订单和用户不匹配
                throw new OrderAndUserNotMatchException("订单和用户不匹配--订单编号："+order.getOrderNumber()+",用户id："+userId);
            } else if (order.getStatus()!=3 || order.getStatus()!=4) {
                // 如果订单不是已完成或者交易关闭
                throw new OrderStatusNotFitException("该订单状态不能执行删除订单操作");
            }
            logger.info("删除订单[{}]",order.getOrderNumber());
            orderMapper.deleteByPrimaryKey(orderId);
        } else {
            // 订单不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 通过订单号查询订单详情信息
     * @return 订单详情信息OrderDTO
     */
    public OrderDTO selectOrderDTOByOrderNumber(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        return getOrderDTOByOrder(order);
    }

    /**
     * 通过订单号查询订单基础信息
     * @param orderNumber：订单号
     * @return order：订单基础信息
     */
    public Order selectOrderByOrderNumber(String orderNumber) {
        return orderMapper.selectByOrderNumber(orderNumber);
    }

    /**
     * 设置订单的快递编号
     * @param order：courierNumber、orderNumber
     * @return
     */
    @Transactional
    public void setCourierNumber(Order order) {
        String orderNumber = order.getOrderNumber();
        Order order1 = orderMapper.selectByOrderNumber(orderNumber);
        if (order1 == null) {
            throw new OrderNotFoundException();
        } else if (order1.getStatus()!= 1) {
            // 如果订单状态不是待发货状态
            throw new OrderStatusNotFitException("该订单["+ orderNumber +"]状态不是待发货状态，不可设置快递编号");
        } else {
            // 修改订单的状态为待收货
            updateOrderStatus(orderNumber);
            logger.info("设置订单[{}]的快递编号为[{}],订单状态改变[待发货]-->[待收货]",order,order.getCourierNumber());
            // 设置订单的快递编号
            orderMapper.setCourierNumber(order);
        }
    }

    /**
     * 通过订单表实体类获取订单详情VO对象
     * @param order:订单表实体类
     * @return OrderVo
     */
    private OrderDTO getOrderDTOByOrder(Order order) {

        OrderDTO orderDTO = new OrderDTO();
        UserAddress userAddress = new UserAddress();
        List<OrderGoodsDTO> orderGoodsDTOList = new ArrayList<>();

        // 用户地址封装
        userAddress.setUserId(order.getUserId());
        userAddress.setUserName(order.getUserName());
        userAddress.setPhoneNumber(order.getPhoneNumber());
        userAddress.setAddressName(order.getAddressName());
        orderDTO.setUserAddress(userAddress);

        // 订单留言
        orderDTO.setMessage(order.getMessage());

        // 订单邮费
        orderDTO.setPostage(order.getPostage());

        // 订单编号
        orderDTO.setOrderNumber(order.getOrderNumber());

        // 订单创建时间
        orderDTO.setCreateTime(order.getCreateTime());

        // 订单商品信息封装
        orderDTO.setTotalPrice(order.getTotalPrice());

        // 根据订单id查询该订单的所有商品
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());

        for (OrderGoods orderGoods : orderGoodsList) {
            OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
            // 商品单价
            double price = orderGoods.getTotalPrice()/orderGoods.getAmount();

            // 设置商品价格
            orderGoodsDTO.setPrice(price);
            orderGoodsDTO.setAmount(orderGoods.getAmount());
            orderGoodsDTO.setAfterTotalPrice(orderGoods.getTotalPrice());

            // 设置商品基础信息
            Integer sizeId = orderGoods.getSizeId();
            Boolean isGoods = orderGoods.getIsGoods();
            setOrderGoodsDTO(orderGoodsDTO,sizeId,isGoods);

            orderGoodsDTOList.add(orderGoodsDTO);
        }
        orderDTO.setOrderGoodsDTOList(orderGoodsDTOList);
        return orderDTO;
    }

    public void setOrderGoodsDTO(OrderGoodsDTO orderGoodsDTO, Integer sizeId, Boolean isGoods) {
        orderGoodsDTO.setColorSizeId(sizeId);
        orderGoodsDTO.setWhetherGoods(isGoods);
        // 如果是普通商品
        if (isGoods == Boolean.TRUE) {
            ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(sizeId);
            orderGoodsDTO.setSize(colorSize.getSize());
            orderGoodsDTO.setPartOrColor(colorSize.getColor());
            orderGoodsDTO.setId(colorSize.getGoodsId());
            Goods goods = goodsMapper.selectByPrimaryKey(colorSize.getGoodsId());
            orderGoodsDTO.setTitle(goods.getName());
            orderGoodsDTO.setPicture(goods.getPicture());
        } else { // 如果是套装散件
            PartSize partSize = partSizeMapper.selectByPrimaryKey(sizeId);
            orderGoodsDTO.setSize(partSize.getSize());
            orderGoodsDTO.setPartOrColor(partSize.getPart());
            Suit suit = suitMapper.selectByPrimaryKey(partSize.getSuitId());
            orderGoodsDTO.setTitle(suit.getName());
            orderGoodsDTO.setPicture(suit.getPicture());
            orderGoodsDTO.setId(suit.getId());
        }
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * @param orderDTO:userAddress、orderNumber
     * @return
     */
    public void updateAddress(OrderDTO orderDTO) {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        String orderNumber = orderDTO.getOrderNumber();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order==null) {
            throw new OrderNotFoundException();
        } else if (!userId.equals(order.getUserId())) {
            throw new OrderAndUserNotMatchException("订单["+orderNumber+"]和用户["+ userId+"]"+"不匹配");
        } else if (!(order.getStatus()>=0 && order.getStatus()<=1)) {
            // 订单状态不为 待付款、待发货，则不可修改地址
            logger.info("订单[{}]的状态为[{}]，不可修改收货地址",orderNumber,order.getStatus());
            throw new OrderStatusNotFitException("订单[{"+orderNumber+"}]的状态为[{"+order.getStatus()+"}]，不可修改收货地址");
        }
        // 正常执行逻辑
        UserAddress userAddress = orderDTO.getUserAddress();
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        logger.info("修改订单[{}]地址信息---收货地址：[{}],联系人姓名[{}],联系号码[{}]",
                orderNumber,order.getAddressName(),order.getUserName(),order.getPhoneNumber());
        orderMapper.updateAddressByOrderNumber(order);
    }

    /**
     * 用户取消订单
     * @param orderNumber：订单编号
     * @return
     */
    @Transactional
    public void cancelOrder(String orderNumber) {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order!=null) {
            if (!userId.equals(order.getUserId())) {
                throw new OrderAndUserNotMatchException("订单["+orderNumber+"]和用户["+ userId+"]"+"不匹配");
            } else if (order.getStatus()==0) {
                // 正确逻辑，取消订单，更新订单状态为4，交易关闭
                logger.info("订单[{}]取消，状态[未付款]-->[交易关闭]",order.getOrderNumber());
                HashMap<String, Integer> orderMap = new HashMap<>();
                orderMap.put("orderId",order.getId());
                orderMap.put("status",4);
                setOrderStatus(orderMap);
                // 库存归位
                updateInventory(order.getOrderNumber(),1);
            } else { // 订单状态不可取消订单
                throw new OrderStatusNotFitException("订单[{"+orderNumber+"}]的状态[{"+order.getStatus()+"}]不可取消订单");
            }
        } else { // 订单id不存在
            throw new OrderNotFoundException();
        }
    }

    /**
     * 更新订单商品库存
     * @param orderNumber：订单编号
     * @param flag：1代表增加库存，0代表减少库存
     * @return 返回成功失败状态
     */
    public void updateInventory(String orderNumber, Integer flag) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        // 库存归位
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());
        for (OrderGoods orderGoods : orderGoodsList) {
            Boolean isGoods = orderGoods.getIsGoods();
            Integer sizeId = orderGoods.getSizeId();
            Integer amount = orderGoods.getAmount();
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            inventoryMap.put("sizeId",sizeId);
            inventoryMap.put("amount",amount);
            // 1代表增加库存,0代表减少库存
            inventoryMap.put("flag",flag);
            if (isGoods == Boolean.TRUE) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            }
        } // end for
    }

    /**
     * 获取未付款订单
     * @return
     */
    public List<Order> getNotPayOrder() {
        return orderMapper.getNotPayOrder();
    }

    @Override
    public List<Order> getUnRefundPayOrderList() {
        List<Order> payOrderList = orderMapper.getPayOrder();
        if (payOrderList!=null) {
            return refundService.getUnRefundOrder(payOrderList);
        } else return null;
    }
}
