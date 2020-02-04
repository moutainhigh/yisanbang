package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.domain.GoodsDetail;
import com.vtmer.yisanbang.dto.GoodsDetailDto;
import com.vtmer.yisanbang.mapper.GoodsDetailMapper;
import com.vtmer.yisanbang.service.GoodsDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Controller
public class GoodsDetailController {
    @Autowired
    private GoodsDetailService goodsDetailService;

    @GetMapping("selectAllGoodsDetail")
    public ResponseMessage selectAllGoodsDetail() {
        List<GoodsDetailDto> goodsDetailDtos = goodsDetailService.selectAllDto();
        if (goodsDetailDtos != null && !goodsDetailDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(goodsDetailDtos, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("selectAllGoodsDetailByGoodsId")
    public ResponseMessage selectAllGoodsDetailByGoodsId(Integer goodsId) {
        List<GoodsDetailDto> goodsDetailDtos = goodsDetailService.selectAllDtoByGoodsId(goodsId);
        if (goodsDetailDtos != null && !goodsDetailDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(goodsDetailDtos, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @PostMapping("addGoodsDetail")
    public ResponseMessage addGoodsDetail(GoodsDetailDto goodsDetail) {
        List<GoodsDetailDto> goodsDetailDtos = goodsDetailService.selectAllDtoByGoodsId(goodsDetail.getGoodsId());
        if (goodsDetailDtos != null && !goodsDetailDtos.isEmpty())  {
            boolean judgeFlag = goodsDetailService.judgeGoodsDetail(goodsDetail, goodsDetailDtos);
            if (judgeFlag) return ResponseMessage.newErrorInstance("该商品详细信息内容已经存在");
        }
        boolean addFlag = goodsDetailService.addGoodsDetail(goodsDetail);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @PutMapping("updateGoodsDetail")
    public ResponseMessage updateGoodsDetail(GoodsDetailDto goodsDetail) {
        GoodsDetailDto goodsDetailDto = goodsDetailService.selectGoodsDetailByID(goodsDetail.getGoodsId());
        if (goodsDetailDto != null) {
            boolean updateFlag = goodsDetailService.updateGoodsDetail(goodsDetail);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该商品详细id错误，无该商品详细信息");
    }

    @DeleteMapping("deleteGoodsDetail")
    public ResponseMessage deleteGoodsDetail(GoodsDetailDto goodsDetail) {
        GoodsDetailDto goodsDetailDto = goodsDetailService.selectGoodsDetailByID(goodsDetail.getGoodsId());
        if (goodsDetailDto != null) {
            boolean deleteFlag = goodsDetailService.deleteGoodsDetail(goodsDetail);
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该商品详细id错误，无该商品详细信息");
    }

    @GetMapping("/uploadGoodsDetailPic")
    public ResponseMessage uploadGoodsDetailPic(MultipartFile pic) {
        String picName = UUID.randomUUID().toString();
        try {
            String picPath = QiniuUpload.updateFile(pic, "goodsDetail/" + picName);
            return ResponseMessage.newSuccessInstance(picPath, "商品详细信息图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("商品详细信息图片上传失败");
        }
    }
}
