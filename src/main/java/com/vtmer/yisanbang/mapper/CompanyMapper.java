package com.vtmer.yisanbang.mapper;

import com.vtmer.yisanbang.domain.Company;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Company record);

    Company selectByPrimaryKey(Integer id);

    List<Company> selectAll();

    int updateByPrimaryKey(Company record);

    // 修改广告图片
    int updatePicture(Integer id, String picturePath);
}