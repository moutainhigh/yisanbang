package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.exception.api.ApiException;
import com.vtmer.yisanbang.common.exception.api.collection.ApiCollectionExistException;
import com.vtmer.yisanbang.common.exception.api.collection.ApiCollectionNotFoundException;
import com.vtmer.yisanbang.common.exception.api.collection.ApiCommodityNotExistException;
import com.vtmer.yisanbang.common.exception.api.collection.ApiUserIdAndCollectionIdNotMatchException;
import com.vtmer.yisanbang.common.exception.service.collection.CollectionExistException;
import com.vtmer.yisanbang.common.exception.service.collection.CollectionNotFoundException;
import com.vtmer.yisanbang.common.exception.service.collection.CommodityNotExistException;
import com.vtmer.yisanbang.common.exception.service.collection.UserIdAndCollectionIdNotMatchException;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.dto.insert.InsertCollectionDTO;
import com.vtmer.yisanbang.service.CollectionService;
import com.vtmer.yisanbang.vo.CollectionVo;
import io.swagger.annotations.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "用户收藏夹接口",value = "用户部分")
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * @param insertCollectionDTO:goodsId、isGoods
     * @return
     */
    @RequestLog(module = "收藏夹", operationDesc = "添加商品到收藏夹")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "添加商品到收藏夹")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated InsertCollectionDTO insertCollectionDTO) {
        Collection collection = new Collection();
        BeanUtils.copyProperties(insertCollectionDTO, collection);
        try {
            collectionService.insertOne(collection);
        } catch (CommodityNotExistException e) {
            throw new ApiCommodityNotExistException(e.getMessage());
        } catch (CollectionExistException e) {
            throw new ApiCollectionExistException(e.getMessage());
        } catch (Exception e) {
            throw new ApiException(e);
        }
        return ResponseMessage.newSuccessInstance("添加收藏成功");
    }

    /**
     * 删除收藏接口
     *
     * @param collectionIdList：收藏id list集合
     * @return
     */
    @RequestLog(module = "收藏夹", operationDesc = "批量删除收藏夹中的商品")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "批量删除收藏商品")
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody
                                  @ApiParam(name = "collectionIdList", value = "收藏夹id列表", example = "[1,2,3]")
                                          List<Integer> collectionIdList) {
        if (collectionIdList != null && collectionIdList.size() != 0) {
            try {
                collectionService.delete(collectionIdList);
            } catch (CollectionNotFoundException e) {
                throw new ApiCollectionNotFoundException(e.getMessage());
            } catch (UserIdAndCollectionIdNotMatchException e) {
                throw new ApiUserIdAndCollectionIdNotMatchException(e.getMessage());
            } catch (Exception e) {
                throw new ApiException(e);
            }
            return ResponseMessage.newSuccessInstance(collectionIdList, "删除收藏成功");
        } else {
            return ResponseMessage.newErrorInstance("传入的收藏夹id集合为空");
        }
    }

    /**
     * 获取用户收藏列表接口
     *
     * @param
     * @return
     */
    @RequestLog(module = "收藏夹", operationDesc = "获取用户收藏商品列表")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "校验token", name = "Authorization", paramType = "header", required = true)
    })
    @ApiOperation(value = "获取用户收藏商品列表")
    @GetMapping("/get")
    public ResponseMessage<List<CollectionVo>> collectionList() {
        List<CollectionVo> collectionVoList = collectionService.selectAllByUserId();
        if (collectionVoList == null) {
            return ResponseMessage.newSuccessInstance("收藏夹为空");
        } else {
            return ResponseMessage.newSuccessInstance(collectionVoList, "获取收藏夹列表成功");
        }
    }
}
