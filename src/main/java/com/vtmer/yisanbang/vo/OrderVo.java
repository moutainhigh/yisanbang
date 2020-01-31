package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.UserAddress;

public class OrderVo {

    private UserAddress userAddress;

    private CartVo cartVo;

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }

    public CartVo getCartVo() {
        return cartVo;
    }

    public void setCartVo(CartVo cartVo) {
        this.cartVo = cartVo;
    }
}
