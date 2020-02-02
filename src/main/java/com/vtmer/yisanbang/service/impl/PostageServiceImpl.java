package com.vtmer.yisanbang.service.impl;

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
    public int update(Postage postage) {
        Postage postage1 = get();
        if (postage1 == null) return -1;
        return postageMapper.update(postage);
    }

    /**
     * delete the postage
     * @return
     */
    public int delete() {
        Postage postage = get();
        if (postage == null) return -1;
        return postageMapper.delete();
    }

    /**
     * insert the postage
     * if there is a postage,return -1(error)
     * @param postage
     * @return
     */
    public int insert(Postage postage) {
        Postage postage1 = get();
        if (postage1 != null) {
            return -1;
        } else {
            return postageMapper.insert(postage);
        }
    }
}
