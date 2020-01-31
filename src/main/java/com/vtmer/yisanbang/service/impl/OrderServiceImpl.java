package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.UserAddress;
import com.vtmer.yisanbang.mapper.OrderMapper;
import com.vtmer.yisanbang.mapper.UserAddressMapper;
import com.vtmer.yisanbang.service.CartService;
import com.vtmer.yisanbang.service.OrderService;
import com.vtmer.yisanbang.vo.CartVo;
import com.vtmer.yisanbang.vo.ConfirmOrderVo;
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
    public ConfirmOrderVo confirmOrder(Integer userId) {
        CartVo cartVo = cartService.selectCartVoByUserId(userId);
        ConfirmOrderVo confirmOrderVo = new ConfirmOrderVo();
        confirmOrderVo.setCartVo(cartVo);
        List<UserAddress> addressList = userAddressMapper.selectByUserId(userId);
        confirmOrderVo.setAddressList(addressList);
        return confirmOrderVo;
    }
}
