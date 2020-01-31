package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.UserAddress;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import com.vtmer.yisanbang.mapper.OrderMapper;
import com.vtmer.yisanbang.mapper.UserAddressMapper;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.CartVo;
import com.vtmer.yisanbang.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CartService cartService;

    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    public OrderVo confirmOrder(Integer userId) {
        CartVo cartVo = cartService.selectCartVoByUserId(userId);
        List<CartGoodsDto> cartGoodsList = cartVo.getCartGoodsList();
        for (int i = 0;i<cartGoodsList.size();i++) {
            if (cartGoodsList.get(i).getIsChosen() == 0) { // 如果是未勾选的,删除其
                cartGoodsList.remove(i);
                i--; // 索引需要递减，这里也可以使用逆序遍历的方法
            }
        }
        cartVo.setCartGoodsList(cartGoodsList);
        OrderVo orderVo = new OrderVo();
        orderVo.setCartVo(cartVo);
        UserAddress userAddress = userAddressMapper.selectDefaultByUserId(userId);
        orderVo.setUserAddress(userAddress);
        return orderVo;
    }
}
