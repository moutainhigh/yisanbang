package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.serviceException.businessaddress.DefaultAddressYetException;
import com.vtmer.yisanbang.common.exception.serviceException.businessaddress.NotFindBusinessAddressException;
import com.vtmer.yisanbang.domain.BusinessAddress;
import com.vtmer.yisanbang.mapper.BusinessAddressMapper;
import com.vtmer.yisanbang.service.BusinessAddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BusinessAddressServiceImpl implements BusinessAddressService {

    private final Logger logger = LoggerFactory.getLogger(BusinessAddressServiceImpl.class);

    @Autowired
    private BusinessAddressMapper businessAddressMapper;

    @Override
    public void insert(BusinessAddress businessAddress) {
        // 判断地址表是不是为空
        Integer checkEmpty = businessAddressMapper.checkEmpty();
        // 地址表为空则插入的地址为默认地址
        if (checkEmpty == 0) {
            businessAddress.setIsDefault(true);
        } else {
            businessAddress.setIsDefault(false);
        }
        businessAddressMapper.insert(businessAddress);
    }

    @Override
    public void update(BusinessAddress businessAddress) {
        BusinessAddress checkExist = businessAddressMapper.selectByPrimaryKey(businessAddress.getId());
        if (checkExist == null) {
            logger.warn("更新商家收货地址传入的id[{}]不存在",businessAddress.getId());
            throw new NotFindBusinessAddressException();
        }
        businessAddressMapper.updateByPrimaryKey(businessAddress);
    }

    @Transactional
    public void updateDefault(Integer id) {
        BusinessAddress businessAddress = businessAddressMapper.selectByPrimaryKey(id);
        if (businessAddress != null) {
            if (businessAddress.getIsDefault()) { // 如果欲修改的地址已为默认收货地址
                logger.info("商家收货地址[{}]已为为默认收货地址",id);
                throw new DefaultAddressYetException();
            } else {
                // 取消之前的默认地址
                businessAddressMapper.cancelDefault();
                // 设置新的默认地址
                businessAddressMapper.updateDefault(id);
            }
        } else {
            logger.warn("设置商家默认收货地址传入的id[{}]不存在",id);
            throw new NotFindBusinessAddressException();
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
    public void deleteById(Integer id) {
        BusinessAddress businessAddress = businessAddressMapper.selectByPrimaryKey(id);
        if (businessAddress == null) {
            logger.warn("删除商家收货地址传入的id[{}]不存在",id);
            throw new NotFindBusinessAddressException();
        } else {
            businessAddressMapper.deleteByPrimaryKey(id);
            // 如果删除的是默认收货地址,设置最新修改的收货地址为默认地址
            if (businessAddress.getIsDefault()) {
                Integer businessAddressId = businessAddressMapper.selectLatestId();
                if (businessAddressId != null) {
                    // 如果还有其他收货地址，设置其为默认收货地址
                    businessAddressMapper.updateDefault(businessAddressId);
                }
            }
        }
    }
}
