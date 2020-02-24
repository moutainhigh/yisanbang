package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.UserAddressDTO;

import java.util.List;

public interface UserAddressService {
    // 查看所有地址
    List<UserAddressDTO> selectUserAddressByUserId(Integer userId);

    // 添加地址时查看是否存在该地址
    public boolean JudegAddressContent(UserAddressDTO userAddress, List<UserAddressDTO> userAdressDtos);

    // 添加地址
    public boolean InsertUserAddress(UserAddressDTO userAddress);

    // 根据地址id查找地址
    public UserAddressDTO selectUserAddressDtoByAddressId(Integer addressId);

    // 根据地址id更改地址
    public boolean updateUserAddressByAddressId(UserAddressDTO userAddress);

    // 根据地址id删除地址
    public boolean deleteUserAddressByAddressId(UserAddressDTO userAddress);

    // 寻找默认地址
    public UserAddressDTO selectDefaultUserAddress(Integer userId);

    // 寻找默认地址
    public UserAddressDTO selectDefaultUserAddressByToken();

    // 改变默认地址
    public boolean changeDefaultUserAddress(UserAddressDTO oldUserAddress, UserAddressDTO newUserAddress);
}
