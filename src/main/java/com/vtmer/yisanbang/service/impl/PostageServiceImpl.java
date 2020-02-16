package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.service.postage.PostageSettingsExistException;
import com.vtmer.yisanbang.common.exception.service.postage.PostageSettingsNotFoundException;
import com.vtmer.yisanbang.domain.Postage;
import com.vtmer.yisanbang.mapper.PostageMapper;
import com.vtmer.yisanbang.service.PostageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostageServiceImpl implements PostageService {

    @Autowired
    private PostageMapper postageMapper;

    /**
     * get the postage
     * @return
     */
    public Postage get() {
        return postageMapper.select();
    }

    /**
     * Update the postage
     * @param postage
     * @return
     */
    public void update(Postage postage) {
        Postage checkExist = get();
        if (checkExist == null) {
            throw new PostageSettingsNotFoundException();
        }
        postageMapper.update(postage);
    }

    /**
     * delete the postage
     * @return
     */
    public void delete() {
        Postage postage = get();
        if (postage == null) {
            throw new PostageSettingsNotFoundException();
        }
        postageMapper.delete();
    }

    /**
     * insert the postage
     * if there is a postage,throw Exception
     * @param postage
     * @return
     */
    public void insert(Postage postage) {
        Postage checkExist = get();
        if (checkExist != null) {
            // 邮费设置已存在
            throw new PostageSettingsExistException();
        } else {
            postageMapper.insert(postage);
        }
    }
}
