package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.domain.Collection;
import com.vtmer.yisanbang.service.CollectionService;
import com.vtmer.yisanbang.vo.CollectionVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionService collectionService;

    /**
     * @param collection:userId、goodsId、isGoods
     * @return
     */
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
    @DeleteMapping("/delete")
    public ResponseMessage delete(@RequestBody @NotEmpty(message = "收藏集合为空") List<Integer> collectionIdList) {
        int res = collectionService.delete(collectionIdList);
        if (res == 1) {
            return ResponseMessage.newSuccessInstance("删除收藏成功");
        } else {
            return ResponseMessage.newErrorInstance("删除收藏出错");
        }
    }

    /**
     * 获取用户收藏列表接口
     * @param userId：用户id
     * @return
     */
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
