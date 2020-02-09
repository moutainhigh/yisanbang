package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.service.CollectionService;
import com.vtmer.yisanbang.vo.CollectionVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("用户收藏夹接口")
@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * @param collection:userId、goodsId、isGoods
     * @return
     */
    @ApiOperation(value = "添加商品到收藏夹")
    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody @Validated Collection collection) {
        Integer res = collectionService.insertOne(collection);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("添加收藏成功");
        } else if (res == 0) {
            return ResponseMessage.newSuccessInstance("该商品已经在收藏夹了");
        } else if (res == -1) {
            return ResponseMessage.newErrorInstance("添加收藏失败");
        }
        return ResponseMessage.newErrorInstance("未知错误~");
    }

    /**
     * 删除收藏接口
     * @param collectionIdList：收藏id list集合
     * @return
     */
    @ApiOperation(value = "批量删除收藏商品")
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody
                                    @ApiParam(name = "collectionIdList",value = "收藏夹id列表",example = "[1,2,3]")
                                  List<Integer> collectionIdList) {
        if(collectionIdList!=null&&collectionIdList.size()!=0) {
            int res = collectionService.delete(collectionIdList);
            if (res == 1) {
                return ResponseMessage.newSuccessInstance(collectionIdList,"删除收藏成功");
            } else {
                return ResponseMessage.newErrorInstance("删除收藏出错");
            }
        } else {
            return ResponseMessage.newErrorInstance("收藏夹id列表为空");
        }
    }

    /**
     * 获取用户收藏列表接口
     * @param userId：用户id
     * @return
     */
    @ApiOperation(value = "获取用户收藏商品列表")
    @GetMapping("/get/{userId}")
    public ResponseMessage<List<CollectionVo>> collectionList(@ApiParam(value = "用户id",name = "userId",example = "!",required = true)
                                              @PathVariable Integer userId) {
        if (userId!=null && userId>0) {
            List<CollectionVo> collectionVoList = collectionService.selectAllByUserId(userId);
            if (collectionVoList == null) {
                return ResponseMessage.newSuccessInstance("收藏夹为空");
            } else {
                return ResponseMessage.newSuccessInstance(collectionVoList,"获取收藏夹列表成功");
            }
        } else {
            return ResponseMessage.newErrorInstance("传入的userId有误");
        }
    }
}
