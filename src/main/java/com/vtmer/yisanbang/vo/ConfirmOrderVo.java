package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.UserAddress;

import java.util.List;

public class ConfirmOrderVo {

    private List<UserAddress> addressList;

    private CartVo cartVo;

    public List<UserAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<UserAddress> addressList) {
        this.addressList = addressList;
    }

    public CartVo getCartVo() {
        return cartVo;
    }

    public void setCartVo(CartVo cartVo) {
        this.cartVo = cartVo;
    }
}
