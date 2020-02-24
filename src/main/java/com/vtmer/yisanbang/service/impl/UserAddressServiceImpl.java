package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.TokenInterceptor;
import com.vtmer.yisanbang.dto.UserAddressDTO;
import com.vtmer.yisanbang.mapper.UserAddressMapper;
import com.vtmer.yisanbang.service.UserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAddressServiceImpl implements UserAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;

    @Override
    // 查看所有地址
    public List<UserAddressDTO> selectUserAddressByUserId(Integer userId) {
        return userAddressMapper.selectAllByUserId(userId);
    }

    @Override
    // 根据地址id查找地址
    public UserAddressDTO selectUserAddressDtoByAddressId(Integer addressId) {
        return userAddressMapper.selectDtoByPrimaryKey(addressId);
    }

    @Override
    // 根据地址id更新地址
    public boolean updateUserAddressByAddressId(UserAddressDTO userAddress) {
        int updateFlag = userAddressMapper.updateDtoByPrimaryKey(userAddress);
        if (updateFlag > 0) return true;
        else return false;
    }

    @Override
    // 根据地址id删除地址
    public boolean deleteUserAddressByAddressId(UserAddressDTO userAddress) {
        int deleteFlag = userAddressMapper.deleteByPrimaryKey(userAddress.getId());
        if (deleteFlag > 0) return true;
        else return false;
    }

    @Override
    // 寻找默认地址
    public UserAddressDTO selectDefaultUserAddress(Integer userId) {
        List<UserAddressDTO> userAddressDtos = userAddressMapper.selectAllByUserId(userId);
        for (UserAddressDTO addressDto : userAddressDtos) {
            if (addressDto.getIsDefault())
                return addressDto;
        }
        return null;
    }

    @Override
    public UserAddressDTO selectDefaultUserAddressByToken() {
        Integer userId = TokenInterceptor.getLoginUser().getId();
        List<UserAddressDTO> userAddressDtos = userAddressMapper.selectAllByUserId(userId);
        for (UserAddressDTO addressDto : userAddressDtos) {
            if (addressDto.getIsDefault())
                return addressDto;
        }
        return null;
    }

    @Override
    // 改变默认地址
    public boolean changeDefaultUserAddress(UserAddressDTO oldUserAddress, UserAddressDTO newUserAddress) {
        UserAddressDTO address = userAddressMapper.selectDtoByPrimaryKey(oldUserAddress.getId());
        if (address != null) {
            address.setIsDefault(false);
            userAddressMapper.updateDtoByPrimaryKey(address);
            newUserAddress.setIsDefault(true);
            userAddressMapper.updateDtoByPrimaryKey(newUserAddress);
            return true;
        }
        return false;
    }

    @Override
    // 添加地址时查看是否存在该地址
    public boolean JudegAddressContent(UserAddressDTO userAddress, List<UserAddressDTO> userAdressDtos) {
        for (UserAddressDTO userAdressList : userAdressDtos) {
            String addressName = userAdressList.getAddressName();
            if (userAddress.getAddressName().equals(addressName)) return true;
        }
        return false;
    }

    @Override
    // 添加地址
    public boolean InsertUserAddress(UserAddressDTO userAddress) {
        List<UserAddressDTO> userAddressDtos = userAddressMapper.selectAllByUserId(userAddress.getUserId());
        if (userAddressDtos != null && !userAddressDtos.isEmpty())
            userAddress.setIsDefault(false);
        else
            userAddress.setIsDefault(true);
        Integer flag = userAddressMapper.insertDto(userAddress);
        if (flag > 0) return true;
        else return false;
    }
}
