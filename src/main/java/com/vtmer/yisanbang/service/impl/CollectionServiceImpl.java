package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.mapper.CollectionMapper;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.CollectionService;
import com.vtmer.yisanbang.vo.CollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    @Autowired
    private SuitMapper suitMapper;

    /*
        Param goodsId、isGoods
     */
    @Override
    public int

    delete(List<Integer> collectionIdList) {
        for (Integer collectionId : collectionIdList) {
            int res = collectionMapper.deleteByPrimaryKey(collectionId);
            if (res == 0) {
                return 0;
            }
        }
        return 1;
    }

    /*
        Param goodsId、isGoods
     */
    @Override
    public Integer insertOne(Collection collection) {
        Boolean checkRes = collectionMapper.checkExist(collection);
        // 如果记录存在
        if (checkRes) {
            return 0;
        } else {
            int insertRes = collectionMapper.insert(collection);
            if (insertRes!=0) return 1;
            else return -1;
        }
    }

    @Override
    public List<CollectionVo> selectAllByUserId(Integer userId) {

        // 查询出goodsId、isGoods 集合
        List<Collection> collectionList = collectionMapper.selectAllByUserId(userId);

        // 如果查询出来不为空
        if (collectionList!=null && collectionList.size()!=0) {
            List<CollectionVo> collectionVoList = new ArrayList<>();
            for (Collection collection : collectionList) {
                int goodsId = collection.getGoodsId();
                Boolean isGoods = collection.getIsGoods();
                CollectionVo collectionVo = new CollectionVo();
                collectionVo.setGoodsId(goodsId);
                collectionVo.setIsGoods(isGoods);
                // 如果是普通商品
                if (isGoods) {
                    Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
                    System.out.println(goods);
                    collectionVo.setName(goods.getName());
                    collectionVo.setPicture(goods.getPicture());
                    collectionVo.setPrice(goods.getPrice());
                } else { // 如果是套装散件
                    Suit suit = suitMapper.selectByPrimaryKey(goodsId);
                    collectionVo.setName(suit.getName());
                    collectionVo.setPicture(suit.getPicture());
                    collectionVo.setPrice(suit.getLowestPrice());
                }
                collectionVoList.add(collectionVo);
            } //end for
            return collectionVoList;
        } else {  // 收藏夹为空，返回null
            return null;
        }
    }
}
