package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Postage;

public interface PostageService {

    Postage get();

    int update(Postage postage);

    int delete();

    int insert(Postage postage);
}
