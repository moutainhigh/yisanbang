package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.BusinessAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Mapper
public interface BusinessAddressMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BusinessAddress record);

    BusinessAddress selectByPrimaryKey(Integer id);

    List<BusinessAddress> selectAll();

    int updateByPrimaryKey(BusinessAddress record);

    Integer checkEmpty();
}