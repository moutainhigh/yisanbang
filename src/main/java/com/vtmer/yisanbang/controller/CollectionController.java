package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.vo.CollectionVo;
import com.vtmer.yisanbang.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    @PostMapping("/insert")
    public ResponseMessage insert(@RequestBody Collection collection) {
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

    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody List<Collection> collectionList) {
        if (collectionList!=null && collectionList.size()!=0) {
            Boolean deleteRes = collectionService.delete(collectionList);
            if (deleteRes) {
                return ResponseMessage.newSuccessInstance("删除收藏成功");
            } else {
                return ResponseMessage.newErrorInstance("删除收藏出错");
            }
        }
        return ResponseMessage.newErrorInstance("传入参数不能为空");
    }

    @GetMapping("/get/{id}")
    public ResponseMessage collectionList(@PathVariable("id") Integer userId) {
        if (userId!=null && userId>0) {
            List<CollectionVo> collectionVoList = collectionService.selectAllByUserId(userId);
            if (collectionVoList == null) {
                return ResponseMessage.newSuccessInstance("收藏夹为空");
            } else {
                return ResponseMessage.newSuccessInstance(collectionVoList,"获取收藏夹成功");
            }
        } else {
            return ResponseMessage.newErrorInstance("传入参数有误");
        }
    }
}
