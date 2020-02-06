package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.mapper.BusinessAddressMapper;
import com.vtmer.yisanbang.service.BusinessAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusinessAddressServiceImpl implements BusinessAddressService {

    @Autowired
    private BusinessAddressMapper businessAddressMapper;

    @Override
    public int insert(BusinessAddress businessAddress) {
        // 判断地址表是不是为空
        Integer checkEmpty = businessAddressMapper.checkEmpty();
        // 地址表为空则插入的地址为默认地址
        if (checkEmpty == 0) {
            businessAddress.setIsDefault(true);
        } else {
            businessAddress.setIsDefault(false);
        }
        return businessAddressMapper.insert(businessAddress);
    }
}
