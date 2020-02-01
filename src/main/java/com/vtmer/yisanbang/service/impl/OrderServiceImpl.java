package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.OrderNumberUtil;
import com.vtmer.yisanbang.domain.*;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.*;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.CartVo;
import com.vtmer.yisanbang.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartMapper cartMapper;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private CartGoodsMapper cartGoodsMapper;

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

    @Override
    public OrderVo confirmCartOrder(Integer userId) {
        // 获取用户购物车清单
        CartVo cartVo = cartService.selectCartVoByUserId(userId);
        List<CartGoodsDto> cartGoodsList = cartVo.getCartGoodsList();
        for (int i = 0;i<cartGoodsList.size();i++) {
            if (cartGoodsList.get(i).getIsChosen() == Boolean.FALSE) { // 如果是未勾选的,删除之
                cartGoodsList.remove(i);
                i--; // 索引需要递减，这里也可以使用逆序遍历的方法
            }
        }
        cartVo.setCartGoodsList(cartGoodsList);
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderGoodsList(cartVo);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVo.setUserAddress(userAddress);
        return orderVo;
    }

    // 开启事务
    @Transactional
    public String createCartOrder(OrderVo orderVo) {
        // 用户地址
        UserAddress userAddress = orderVo.getUserAddress();
        // 用户购物车订单列表
        CartVo cartVo = orderVo.getOrderGoodsList();
        List<CartGoodsDto> cartGoodsList = cartVo.getCartGoodsList();

        // 生成订单编号
        String orderNumber = OrderNumberUtil.getOrderNumber();

        // 生成order
        Order order = new Order();
        order.setUserId(userAddress.getUserId());
        order.setAddressName(userAddress.getAddressName());
        order.setUserName(userAddress.getUserName());
        order.setPhoneNumber(userAddress.getPhoneNumber());
        order.setOrderNumber(orderNumber);
        order.setTotalPrice(cartVo.getTotalPrice());
        order.setMessage(orderVo.getMessage());
        orderMapper.insert(order);

        System.out.println(order.getId());

        // 生成orderGoods
        OrderGoods orderGoods = new OrderGoods();
        for (CartGoodsDto cartGoodsDto : cartGoodsList) {
            orderGoods.setOrderId(order.getId());
            orderGoods.setIsGoods(cartGoodsDto.getIsGoods());
            orderGoods.setAmount(cartGoodsDto.getAmount());
            orderGoods.setTotalPrice(cartGoodsDto.getAfterTotalPrice());
            orderGoods.setSizeId(cartGoodsDto.getColorSizeId());
            orderGoodsMapper.insert(orderGoods);
        }

        // 删除购物车勾选项
        cartGoodsMapper.deleteCartGoodsByIsChosen(cartVo.getCartId());
        // 购物车总价清零
        cartMapper.updateTotalPrice(0,cartVo.getCartId());
        // 返回订单编号
        return orderNumber;
    }

    /*
        status 订单状态 0--待付款 1--待发货 2--待收货 3--已完成 4--申请退款 5--交易关闭 6--所有订单
     */
    @Transactional
    public List<OrderVo> listOrder(Map<String,Integer> orderMap) {

        // 初始化
        ArrayList<OrderVo> orderVoList = new ArrayList<>();

        List<Order> orderList = orderMapper.selectAllByUserIdAndStatus(orderMap);

        for (Order order : orderList) {

            CartVo cartVo = new CartVo();
            OrderVo orderVo = new OrderVo();
            UserAddress userAddress = new UserAddress();
            List<CartGoodsDto> cartGoodsList = new ArrayList<>();

            // 用户地址封装
            userAddress.setUserId(orderMap.get("userId"));
            userAddress.setUserName(order.getUserName());
            userAddress.setPhoneNumber(order.getPhoneNumber());
            userAddress.setAddressName(order.getAddressName());
            orderVo.setUserAddress(userAddress);

            // 订单留言
            orderVo.setMessage(order.getMessage());

            // 订单编号
            orderVo.setOrderNumber(order.getOrderNumber());

            // 订单商品信息封装
            cartVo.setTotalPrice(order.getTotalPrice());

            // 根据订单id查询该订单的所有商品
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());

            for (OrderGoods orderGoods : orderGoodsList) {
                CartGoodsDto cartGoodsDto = new CartGoodsDto();
                // 商品单价
                double price = orderGoods.getTotalPrice()/orderGoods.getAmount();
                cartGoodsDto.setPrice(price);
                cartGoodsDto.setAmount(orderGoods.getAmount());
                cartGoodsDto.setAfterTotalPrice(orderGoods.getTotalPrice());
                Integer sizeId = orderGoods.getSizeId();
                cartGoodsDto.setColorSizeId(sizeId);
                Boolean isGoods = orderGoods.getIsGoods();
                // 如果是普通商品
                if (isGoods == Boolean.TRUE) {
                    ColorSize colorSize = colorSizeMapper.selectByPrimaryKey(sizeId);
                    cartGoodsDto.setSize(colorSize.getSize());
                    cartGoodsDto.setPartOrColor(colorSize.getColor());
                    Goods goods = goodsMapper.selectByPrimaryKey(colorSize.getGoodsId());
                    cartGoodsDto.setName(goods.getName());
                    cartGoodsDto.setPicture(goods.getPicture());
                } else { // 如果是套装散件
                    PartSize partSize = partSizeMapper.selectByPrimaryKey(sizeId);
                    cartGoodsDto.setSize(partSize.getSize());
                    cartGoodsDto.setPartOrColor(partSize.getPart());
                    Suit suit = suitMapper.selectByPrimaryKey(partSize.getSuitId());
                    cartGoodsDto.setName(suit.getName());
                    cartGoodsDto.setPicture(suit.getPicture());
                }
                cartGoodsList.add(cartGoodsDto);
            }
            cartVo.setCartGoodsList(cartGoodsList);
            orderVo.setOrderGoodsList(cartVo);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }
}
