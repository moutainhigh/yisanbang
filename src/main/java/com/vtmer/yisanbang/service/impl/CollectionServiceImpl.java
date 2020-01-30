package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.dto.CollectionDto;
import com.vtmer.yisanbang.mapper.CollectionMapper;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.CollectionService;
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
    public Boolean delete(List<Collection> collectionList) {
        for (Collection collection : collectionList) {
            Boolean deleteRes = collectionMapper.deleteOne(collection);
            if (!deleteRes) {
                return false;
            }
        }
        return true;
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
    public List<CollectionDto> selectAllByUserId(Integer userId) {

        // 查询出goodsId、isGoods 集合
        List<Collection> collectionList = collectionMapper.selectAllByUserId(userId);

        // 如果查询出来不为空
        if (collectionList!=null && collectionList.size()!=0) {
            List<CollectionDto> collectionDtoList = new ArrayList<>();
            for (Collection collection : collectionList) {
                int goodsId = collection.getGoodsId();
                int isGoods = collection.getIsGoods();
                CollectionDto collectionDto = new CollectionDto();
                collectionDto.setGoodsId(goodsId);
                collectionDto.setIsGoods(isGoods);
                // 如果是普通商品
                if (isGoods == 1) {
                    Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
                    collectionDto.setName(goods.getName());
                    collectionDto.setPicture(goods.getPicture());
                    collectionDto.setPrice(goods.getPrice());
                } else if (isGoods == 0) { // 如果是套装散件
                    Suit suit = suitMapper.selectByPrimaryKey(goodsId);
                    collectionDto.setName(suit.getName());
                    collectionDto.setPicture(suit.getPicture());
                    collectionDto.setPrice(suit.getLowestPrice());
                }
                collectionDtoList.add(collectionDto);
            } //end for
            return collectionDtoList;
        } else {  // 收藏夹为空，返回null
            return null;
        }
    }
}
