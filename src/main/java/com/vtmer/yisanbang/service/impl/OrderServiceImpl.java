package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.OrderNumberUtil;
import com.vtmer.yisanbang.common.TokenInterceptor;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.service.RefundService;
import com.vtmer.yisanbang.vo.CartVo;
import com.vtmer.yisanbang.vo.OrderVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public OrderVo confirmCartOrder() {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        setPostage();
        OrderVo orderVo = new OrderVo();
        // 获取用户购物车清单
        CartVo cartVo = cartService.selectCartVo();
        if (cartVo == null) {
            return null;
        }
        if (cartVo.getTotalPrice() >= standardPrice) { // 包邮
            orderVo.setPostage(0);
        } else {  // 不包邮
            orderVo.setPostage(defaultPostage);
        }
        List<CartGoodsDto> cartGoodsList = cartVo.getCartGoodsList();
        // IDEA推荐方式
        cartGoodsList.removeIf(cartGoodsDto -> cartGoodsDto.getIsChosen() == Boolean.FALSE);
        /* 使用迭代器删除集合中的元素
        Iterator<CartGoodsDto> iterator = cartGoodsList.iterator();
        while (iterator.hasNext()) {
            CartGoodsDto cartGoodsDto = iterator.next();
            if (cartGoodsDto.getIsChosen() == Boolean.FALSE) {
                iterator.remove();
            }
        }
         */
        /* 使用foreach方式删除集合中的元素，不推荐
        for (int i = 0;i<cartGoodsList.size();i++) {
            if (cartGoodsList.get(i).getIsChosen() == Boolean.FALSE) { // 如果是未勾选的,删除之
                cartGoodsList.remove(i);
                i--; // 索引需要递减，这里也可以使用逆序遍历的方法
            }
        }
         */
        cartVo.setCartGoodsList(cartGoodsList);
        orderVo.setOrderGoodsList(cartVo);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVo.setUserAddress(userAddress);
        return orderVo;
    }

    /**
     * create shopping cart order after users submit order
     * @param orderVo:UserAddress(用户地址、联系人、手机号)、邮费、留言、下单商品详情信息
     * @return openid、orderNumber
     * @throws DataIntegrityViolationException：库存不足抛出异常
     */
    @Transactional
    public Map<String,String> createCartOrder(OrderVo orderVo) throws DataIntegrityViolationException {

        User user = TokenInterceptor.getLoginUser();
        // 返回map
        HashMap<String, String> orderMap = new HashMap<>();
        // 从登录信息中拿到openId
        String openId = user.getOpenId();

        orderMap.put("openId",openId);
        // 生成订单编号
        String orderNumber = OrderNumberUtil.getOrderNumber();
        // 订单编号放进map
        orderMap.put("orderNumber",orderNumber);
        // 用户地址
        UserAddress userAddress = orderVo.getUserAddress();
        // 用户购物车
        CartVo cartVo = orderVo.getOrderGoodsList();
        // 购物车商品列表
        List<CartGoodsDto> cartGoodsList = cartVo.getCartGoodsList();

        // 生成order
        Order order = new Order();
        order.setUserId(user.getId());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setOrderNumber(orderNumber);
        order.setTotalPrice(cartVo.getTotalPrice());
        order.setPostage(orderVo.getPostage());
        if (orderVo.getMessage()!=null) {
            order.setMessage(orderVo.getMessage());
        }
        orderMapper.insert(order);
        logger.info("创建订单[{}]，订单状态[未支付]---用户openid[{}]",orderNumber,openId);
        for (CartGoodsDto cartGoodsDto : cartGoodsList) {
            // 生成orderGoods
            OrderGoods orderGoods = new OrderGoods();
            Boolean isGoods = cartGoodsDto.getIsGoods();
            Integer amount = cartGoodsDto.getAmount();
            Integer colorSizeId = cartGoodsDto.getColorSizeId();
            orderGoods.setOrderId(order.getId());
            orderGoods.setIsGoods(isGoods);
            orderGoods.setAmount(amount);
            orderGoods.setTotalPrice(cartGoodsDto.getAfterTotalPrice());
            orderGoods.setSizeId(colorSizeId);
            orderGoodsMapper.insert(orderGoods);

            // 减少相应商品的库存
            // 不调用updateInventory方法，省得插入又再查一次数据库
            HashMap<String, Integer> inventoryMap = new HashMap<>();
            inventoryMap.put("amount",amount);
            inventoryMap.put("sizeId",colorSizeId);
            // 0代表减少库存
            inventoryMap.put("flag",0);
            if (isGoods == Boolean.TRUE) {
                colorSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
            } else {
                partSizeMapper.updateInventoryByPrimaryKey(inventoryMap);
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
    public List<OrderVo> listOrder(Map<String,Integer> orderMap) {

        Integer userId = TokenInterceptor.getLoginUser().getId();
        if (orderMap.get("flag") == 1) {
            // 如果是获取用户的订单
            // 向map中添加userId,查询订单
            orderMap.put("userId",userId);
        } else {
            orderMap.put("userId",null);
        }

        ArrayList<OrderVo> orderVoList = new ArrayList<>();

        List<Order> orderList = orderMapper.selectAllByUserIdAndStatus(orderMap);

        for (Order order : orderList) {
            OrderVo orderVo = getOrderVoByOrder(order);
            Refund refund = refundMapper.selectByOrderId(order.getId());
            if (refund!=null) { // 如果该订单有退款信息
                // 设置退款状态
                orderVo.setRefundStatus(refund.getStatus());
            }
            orderVoList.add(orderVo);
        }
        return orderVoList;
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
        if (order!=null) { // 如果该订单存在
            Integer status = order.getStatus();
            if (status>=0 && status<3) { // 如果订单状态是待付款、待发货、待收货
                // 更新订单状态
                int res = orderMapper.updateOrderStatus(order.getId());
                logger.info("更新订单[{}]状态：[待收货]-->[已完成]",orderNumber);
                return res;
            } else { // 订单状态不能自增修改
                return -2;
            }
        } else {
            return -1;
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
    public int setOrderStatus(Map<String, Integer> orderMap) {
        Integer orderId = orderMap.get("orderId");
        Integer status = orderMap.get("status");
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order != null) {
            if (status.equals(order.getStatus())) {  // 如果将要修改的订单状态与原状态相同
                return -1;
            } else { // 更新订单状态
                return orderMapper.setOrderStatus(orderMap);
            }
        } else {
            return -2;
        }
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    public int deleteOrder(Integer orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order!=null) {
            logger.info("删除订单[{}]",order.getOrderNumber());
            return orderMapper.deleteByPrimaryKey(orderId);
        } else {
            return -1;
        }
    }

    /**
     * 通过订单号查询订单详情信息
     * @return 订单详情信息OrderVo
     */
    public OrderVo selectOrderVoByOrderNumber(String orderNumber) {
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        return getOrderVoByOrder(order);
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
     * @param order：courierNumber、orderId
     * @return
     */
    public int setCourierNumber(Order order) {
        logger.info("设置订单[{}]的快递编号[{}]",order,order.getCourierNumber());
        return orderMapper.setCourierNumber(order);
    }

    /**
     * 通过订单表实体类获取订单详情VO对象
     * @param order:订单表实体类
     * @return OrderVo
     */
    private OrderVo getOrderVoByOrder(Order order) {

        CartVo cartVo = new CartVo();
        OrderVo orderVo = new OrderVo();
        UserAddress userAddress = new UserAddress();
        List<CartGoodsDto> cartGoodsList = new ArrayList<>();

        // 用户地址封装
        userAddress.setUserId(order.getUserId());
        userAddress.setUserName(order.getUserName());
        userAddress.setPhoneNumber(order.getPhoneNumber());
        userAddress.setAddressName(order.getAddressName());
        orderVo.setUserAddress(userAddress);

        // 订单留言
        orderVo.setMessage(order.getMessage());

        // 订单邮费
        orderVo.setPostage(order.getPostage());

        // 订单编号
        orderVo.setOrderNumber(order.getOrderNumber());

        // 订单创建时间
        orderVo.setCreateTime(order.getCreateTime());

        // 订单商品信息封装
        cartVo.setTotalPrice(order.getTotalPrice());

        // 根据订单id查询该订单的所有商品
        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());

        for (OrderGoods orderGoods : orderGoodsList) {
            CartGoodsDto cartGoodsDto = new CartGoodsDto();
            // 商品单价
            double price = orderGoods.getTotalPrice()/orderGoods.getAmount();

            // 设置商品价格
            cartGoodsDto.setPrice(price);
            cartGoodsDto.setAmount(orderGoods.getAmount());
            cartGoodsDto.setAfterTotalPrice(orderGoods.getTotalPrice());

            // 设置商品基础信息
            Integer sizeId = orderGoods.getSizeId();
            Boolean isGoods = orderGoods.getIsGoods();
            setCartGoodsDto(cartGoodsDto,sizeId,isGoods);

            cartGoodsList.add(cartGoodsDto);
        }
        cartVo.setCartGoodsList(cartGoodsList);
        orderVo.setOrderGoodsList(cartVo);
        return orderVo;
    }

    public void setCartGoodsDto(CartGoodsDto cartGoodsDto, Integer sizeId, Boolean isGoods) {
        cartGoodsDto.setColorSizeId(sizeId);
        cartGoodsDto.setIsGoods(isGoods);
        // 如果是普通商品
        if (isGoods == Boolean.TRUE) {
            ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(sizeId);
            cartGoodsDto.setSize(colorSize.getSize());
            cartGoodsDto.setPartOrColor(colorSize.getColor());
            cartGoodsDto.setId(colorSize.getGoodsId());
            Goods goods = goodsMapper.selectByPrimaryKey(colorSize.getGoodsId());
            cartGoodsDto.setTitle(goods.getName());
            cartGoodsDto.setPicture(goods.getPicture());
        } else { // 如果是套装散件
            PartSize partSize = partSizeMapper.selectByPrimaryKey(sizeId);
            cartGoodsDto.setSize(partSize.getSize());
            cartGoodsDto.setPartOrColor(partSize.getPart());
            Suit suit = suitMapper.selectByPrimaryKey(partSize.getSuitId());
            cartGoodsDto.setTitle(suit.getName());
            cartGoodsDto.setPicture(suit.getPicture());
            cartGoodsDto.setId(suit.getId());
        }
    }

    /**
     * 订单状态定义：status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--交易关闭 5--所有订单
     * @param orderVo:userAddress、orderNumber
     * @return
     */
    public int updateAddress(OrderVo orderVo) {
        String orderNumber = orderVo.getOrderNumber();
        Order order = orderMapper.selectByOrderNumber(orderNumber);
        if (order==null) {
            return -1;
        } else if (!(order.getStatus()>=0 && order.getStatus()<=1)) {
            // 订单状态不为 待付款、待发货，则不可修改地址
            logger.info("订单[{}]的状态为[{}]，不可修改收货地址",orderNumber,order.getStatus());
            return -2;
        }
        UserAddress userAddress = orderVo.getUserAddress();
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        logger.info("修改订单[{}]地址信息---收货地址：[{}],联系人姓名[{}],联系号码[{}]",
                orderNumber,order.getAddressName(),order.getUserName(),order.getPhoneNumber());
        return orderMapper.updateAddressByOrderNumber(order);
    }

    /**
     * 用户取消订单
     * @param orderId：订单id
     * @return
     */
    @Transactional
    public int cancelOrder(Integer orderId) {
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (order!=null) {
            // 如果订单状态是0未付款，可以取消订单
            if (order.getStatus()==0) {
                // 取消订单，更新订单状态为4，交易关闭
                logger.info("订单[{}]取消，状态[未付款]-->[交易关闭]",order.getOrderNumber());
                HashMap<String, Integer> orderMap = new HashMap<>();
                orderMap.put("orderId",orderId);
                orderMap.put("status",4);
                setOrderStatus(orderMap);
                // 库存归位
                updateInventory(order.getOrderNumber(),1);
                return 1;
            } else { // 订单状态不可取消订单
                logger.info("订单[{}]的状态[{}]不可取消订单",order.getOrderNumber(),order.getStatus());
                return -2;
            }
        } else { // 订单id不存在
            logger.info("订单id[{}]不存在",orderId);
            return -1;
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
