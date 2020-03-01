package com.vtmer.yisanbang.service.impl;

import com.vtmer.yisanbang.common.exception.service.collection.CollectionExistException;
import com.vtmer.yisanbang.common.exception.service.collection.CollectionNotFoundException;
import com.vtmer.yisanbang.common.exception.service.collection.CommodityNotExistException;
import com.vtmer.yisanbang.common.exception.service.collection.UserIdAndCollectionIdNotMatchException;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.domain.Goods;
import com.vtmer.yisanbang.domain.Suit;
import com.vtmer.yisanbang.mapper.CollectionMapper;
import com.vtmer.yisanbang.mapper.GoodsMapper;
import com.vtmer.yisanbang.mapper.SuitMapper;
import com.vtmer.yisanbang.service.CollectionService;
import com.vtmer.yisanbang.shiro.JwtFilter;
import com.vtmer.yisanbang.vo.CollectionVO;
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
    public void delete(List<Integer> collectionIdList) {
        Integer userId = JwtFilter.getLoginUser().getId();
        for (Integer collectionId : collectionIdList) {
            Collection collection = collectionMapper.selectByPrimaryKey(collectionId);
            if (collection == null) {
                // 收藏夹id不存在
                throw new CollectionNotFoundException("收藏夹商品不存在，collectionId：" + collectionId);
            }
            if (!collection.getUserId().equals(userId)) {
                // 如果userId和收藏夹id不匹对
                throw new UserIdAndCollectionIdNotMatchException("用户id和收藏夹id不匹配,userId：" + userId + ",collectionId:" + collection.getId());
            }
            // 正确逻辑，执行删除操作
            collectionMapper.deleteByPrimaryKey(collectionId);
        }
    }


    @Override
    public void insertOne(Collection collection) {
        int userId = JwtFilter.getLoginUser().getId();
        Integer goodsId = collection.getGoodsId();
        Boolean whetherGoods = collection.getWhetherGoods();
        Object checkExist;
        if (whetherGoods) {
            // 是普通商品
            checkExist = goodsMapper.selectByPrimaryKey(goodsId);
        } else {
            // 是套装
            checkExist = suitMapper.selectByPrimaryKey(goodsId);
        }
        if (checkExist == null) {
            // 如果商品不存在
            throw new CommodityNotExistException("用户["+ userId + "]欲收藏的商品不存在--商品id："+goodsId + "，是否是普通商品"+whetherGoods);
        }
        Boolean checkRes = collectionMapper.checkExist(collection);
        // 如果记录存在
        if (checkRes) {
            throw new CollectionExistException("用户["+ userId + "]该商品已在收藏夹--商品id："+ goodsId + "，是否是普通商品"+ whetherGoods);
        } else {
            // 正常执行插入操作
            collection.setUserId(userId);
            collectionMapper.insert(collection);
        }
    }

    @Override
    public List<CollectionVO> selectAllByUserId() {
        Integer userId = JwtFilter.getLoginUser().getId();
        // 查询出goodsId、isGoods 集合
        List<Collection> collectionList = collectionMapper.selectAllByUserId(userId);

        // 如果查询出来不为空
        if (collectionList!=null && collectionList.size()!=0) {
            List<CollectionVO> collectionVOList = new ArrayList<>();
            for (Collection collection : collectionList) {
                int goodsId = collection.getGoodsId();
                Boolean whetherGoods = collection.getWhetherGoods();
                CollectionVO collectionVo = new CollectionVO();
                collectionVo.setGoodsId(goodsId);
                collectionVo.setWhetherGoods(whetherGoods);
                // 如果是普通商品
                if (whetherGoods) {
                    Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
                    System.out.println(goods);
                    collectionVo.setName(goods.getName());
                    collectionVo.setPicture(goods.getPicture());
                    collectionVo.setPrice(goods.getPrice());
                    collectionVo.setIntroduce(goods.getIntroduce());
                } else { // 如果是套装散件
                    Suit suit = suitMapper.selectByPrimaryKey(goodsId);
                    collectionVo.setName(suit.getName());
                    collectionVo.setPicture(suit.getPicture());
                    collectionVo.setPrice(suit.getLowestPrice());
                    collectionVo.setIntroduce(suit.getIntroduce());
                }
                collectionVOList.add(collectionVo);
            } //end for
            return collectionVOList;
        } else {  // 收藏夹为空，返回null
            return null;
        }
    }
}
