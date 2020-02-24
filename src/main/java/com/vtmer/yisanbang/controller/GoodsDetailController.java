package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.dto.GoodsDetailDTO;
import com.vtmer.yisanbang.service.GoodsDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Api(tags = "商品详情管理接口")
@RestController
@RequestMapping("/goodsDetail")
public class GoodsDetailController {
    @Autowired
    private GoodsDetailService goodsDetailService;

    @GetMapping("/get/selectAllGoodsDetail")
    @ApiOperation(value = "查找所有商品详情信息")
    // 查找所有商品详情信息
    public ResponseMessage selectAllGoodsDetail(@ApiParam("查询页数(第几页)")
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                @ApiParam("单页数量")
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDetailDTO> goodsDetailDtos = goodsDetailService.selectAllDto();
        if (goodsDetailDtos != null && !goodsDetailDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDetailDtos), "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/get/selectAllGoodsDetailByGoodsId")
    @ApiOperation(value = "根据商品id查找该商品的所有商品详情信息")
    // 根据商品id查找该商品的所有商品详情信息
    public ResponseMessage selectAllGoodsDetailByGoodsId(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                                         @RequestParam(value = "goodsId", defaultValue = "5") Integer goodsId,
                                                         @ApiParam("查询页数(第几页)")
                                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                         @ApiParam("单页数量")
                                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDetailDTO> goodsDetailDtos = goodsDetailService.selectAllDtoByGoodsId(goodsId);
        if (goodsDetailDtos != null && !goodsDetailDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDetailDtos), "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @PostMapping("/addGoodsDetail")
    @ApiOperation(value = "添加商品详情信息")
    // 添加商品详情信息
    public ResponseMessage addGoodsDetail(@RequestBody GoodsDetailDTO goodsDetail) {
        boolean addFlag = goodsDetailService.addGoodsDetail(goodsDetail);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @PutMapping("/updateGoodsDetail")
    @ApiOperation(value = "更新商品详情信息")
    // 更新商品详情信息
    public ResponseMessage updateGoodsDetail(@RequestBody GoodsDetailDTO goodsDetail) {
        GoodsDetailDTO goodsDetailDto = goodsDetailService.selectGoodsDetailByID(goodsDetail.getId());
        if (goodsDetailDto != null) {
            boolean updateFlag = goodsDetailService.updateGoodsDetail(goodsDetail);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该商品详细id错误，无该商品详细信息");
    }

    @DeleteMapping("/deleteGoodsDetail")
    @ApiOperation(value = "删除商品详情信息")
    // 删除商品详情信息
    public ResponseMessage deleteGoodsDetail(@RequestBody GoodsDetailDTO goodsDetail) {
        GoodsDetailDTO goodsDetailDto = goodsDetailService.selectGoodsDetailByID(goodsDetail.getId());
        if (goodsDetailDto != null) {
            boolean deleteFlag = goodsDetailService.deleteGoodsDetail(goodsDetail.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该商品详细id错误，无该商品详细信息");
    }

    @PostMapping("/uploadGoodsDetailPic")
    @ApiOperation(value = "上传商品详情信息图片", notes = "执行成功后返回图片路径(img.yisanbang.com/goodsDetail/图片名称)")
    // 上传商品详情信息图片
    public ResponseMessage uploadGoodsDetailPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if (picType.equals("jpg") || picType.equals("JPG") || picType.equals("jpeg") || picType.equals("JPEG") || picType.equals("png") || picType.equals("PNG")) {
            String picName = UUID.randomUUID().toString();
            try {
                String picPath = QiniuUpload.updateFile(pic, "goodsDetail/" + picName);
                return ResponseMessage.newSuccessInstance(picPath, "广告图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("广告图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }
    }
}
