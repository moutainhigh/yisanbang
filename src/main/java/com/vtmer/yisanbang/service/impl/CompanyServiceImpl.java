package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Company;
import com.vtmer.yisanbang.mapper.CompanyMapper;
import com.vtmer.yisanbang.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyMapper companyMapper;

    @Override
    public int updatePictureById(Integer id, String picturePath) {
        return companyMapper.updatePicture(id, picturePath);
    }

    @Override
    public List<Company> listPicture() {
        return companyMapper.selectAll();
    }
}
