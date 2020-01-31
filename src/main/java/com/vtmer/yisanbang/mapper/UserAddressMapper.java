package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.UserAddress;
import com.vtmer.yisanbang.dto.UserAddressDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    UserAddress selectByPrimaryKey(Integer id);

    List<UserAddress> selectAll();

    int updateByPrimaryKey(UserAddress record);

    // Dto
    List<UserAddressDto> selectAllByUserId(Integer id);

    // Dto
    int insertDto(UserAddressDto record);

    // Dto
    int updateDtoByPrimaryKey(UserAddressDto record);

    // Dto
    UserAddressDto selectDtoByPrimaryKey(Integer id);
}