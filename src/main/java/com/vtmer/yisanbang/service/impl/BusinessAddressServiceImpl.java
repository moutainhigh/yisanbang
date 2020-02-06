package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.mapper.BusinessAddressMapper;
import com.vtmer.yisanbang.service.BusinessAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Override
    public int update(BusinessAddress businessAddress) {
        return businessAddressMapper.updateByPrimaryKey(businessAddress);
    }

    @Transactional
    public int updateDefault(Integer id) {
        BusinessAddress businessAddress = businessAddressMapper.selectByPrimaryKey(id);
        if (businessAddress != null) {
            // 取消之前的默认地址
            businessAddressMapper.cancelDefault();
            // 设置新的默认地址
            return businessAddressMapper.updateDefault(id);
        } else {
            return -1;
        }

    }

    public List<BusinessAddress> selectAll() {
        return businessAddressMapper.selectAll();
    }

    @Override
    public BusinessAddress getDefault() {
        return businessAddressMapper.getDefault();
    }

    @Override
    @Transactional
    public int deleteById(Integer id) {
        BusinessAddress businessAddress = businessAddressMapper.selectByPrimaryKey(id);
        if (businessAddress == null) {
            return -1;
        } else {
            businessAddressMapper.deleteByPrimaryKey(id);
            // 如果删除的是默认收货地址,设置最新修改的收货地址为默认地址
            if (businessAddress.getIsDefault()) {
                Integer businessAddressId = businessAddressMapper.selectLatestId();
                businessAddressMapper.updateDefault(businessAddressId);
            }
            return 1;
        }
    }
}
