package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.dto.UserAddressDto;

import java.util.List;

public interface UserAddressService {
    // 查看所有地址
    List<UserAddressDto> selectUserAddressByUserId(Integer userId);

    // 添加地址时查看是否存在该地址
    public boolean JudegAddressContent(UserAddressDto userAddress, List<UserAddressDto> userAdressDtos);

    // 添加地址
    public boolean InsertUserAddress(UserAddressDto userAddress);

    // 根据地址id查找地址
    public UserAddressDto selectUserAddressDtoByAddressId(Integer addressId);

    // 根据地址id更改地址
    public boolean updateUserAddressByAddressId(UserAddressDto userAddress);

    // 根据地址id删除地址
    public boolean deleteUserAddressByAddressId(UserAddressDto userAddress);

    // 寻找默认地址
    public UserAddressDto selectDefaultUserAddress(Integer userId);

    // 改变默认地址
    public boolean changeDefaultUserAddress(UserAddressDto oldUserAddress, UserAddressDto newUserAddress);
}
