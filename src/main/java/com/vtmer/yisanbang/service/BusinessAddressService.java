package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.BusinessAddress;

import java.util.List;

public interface BusinessAddressService {

    void insert(BusinessAddress businessAddress);

    void update(BusinessAddress businessAddress);

    void updateDefault(Integer id);

    List<BusinessAddress> selectAll();

    BusinessAddress getDefault();

    void deleteById(Integer id);
}
