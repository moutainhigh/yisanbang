package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.UserAddress;
import com.vtmer.yisanbang.dto.UserAddressDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@Mapper
public interface UserAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserAddress record);

    UserAddress selectByPrimaryKey(Integer id);

    List<UserAddress> selectAll();

    int updateByPrimaryKey(UserAddress record);


    UserAddress selectDefaultByUserId(Integer id);
    // Dto
    List<UserAddressDTO> selectAllByUserId(Integer id);

    // Dto
    int insertDto(UserAddressDTO record);

    // Dto
    int updateDtoByPrimaryKey(UserAddressDTO record);

    // Dto
    UserAddressDTO selectDtoByPrimaryKey(Integer id);

}