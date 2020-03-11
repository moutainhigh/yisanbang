package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Company;
import org.springframework.data.relational.core.sql.In;

import java.util.List;

public interface CompanyService {
    int updatePictureById (Integer id, String picturePath);

    List<Company> listPicture();
}
