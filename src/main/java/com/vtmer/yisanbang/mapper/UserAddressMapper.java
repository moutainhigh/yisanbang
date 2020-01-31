package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.UserAddress;
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

    List<UserAddress> selectByUserId(Integer id);
}