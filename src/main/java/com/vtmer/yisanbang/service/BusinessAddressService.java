package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.BusinessAddress;

import java.util.List;

public interface BusinessAddressService {

    int insert(BusinessAddress businessAddress);

    int update(BusinessAddress businessAddress);

    int updateDefault(Integer id);

    List<BusinessAddress> selectAll();

    BusinessAddress getDefault();

    int deleteById(Integer id);
}
