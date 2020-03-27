package com.vtmer.yisanbang.service;

import com.vtmer.yisanbang.domain.Postage;

public interface PostageService {

    Postage get();

    void update(Postage postage);

    void delete();

    void insert(Postage postage);
}
