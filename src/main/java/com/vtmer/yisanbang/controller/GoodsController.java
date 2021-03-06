package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.common.tencentcloud.COSClientUtil;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.GoodsDTO;
import com.vtmer.yisanbang.dto.GoodsDetailDTO;
import com.vtmer.yisanbang.service.ColorSizeService;
import com.vtmer.yisanbang.service.GoodsDetailService;
import com.vtmer.yisanbang.service.GoodsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Api(tags = "商品管理接口")
@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ColorSizeService colorSizeService;

    @Autowired
    private GoodsDetailService goodsDetailService;

    @RequestLog(module = "单件商品", operationDesc = "查找所有商品")
    @GetMapping("/get/selectAllGoods")
    @ApiOperation(value = "查找所有商品")
    // 查找所有商品
    public ResponseMessage selectAllGoods(@ApiParam("查询页数(第几页)")
                                          @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                          @ApiParam("单页数量")
                                          @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDto();
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据商品更新时间顺序显示商品")
    @GetMapping("/get/selectAllGoodsOrderByTime")
    @ApiOperation(value = "根据商品更新时间顺序显示商品")
    // 根据商品更新时间顺序显示商品
    public ResponseMessage selectAllGoodsOrderByTime(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                     @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                     @ApiParam("查询页数(第几页)")
                                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                     @ApiParam("单页数量")
                                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDtoOrderByTime();
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            if (ifDec > 0) {
                Collections.reverse(goodsDtos);
            }
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据商品价格顺序显示商品")
    @GetMapping("/get/selectAllGoodsOrderByPrice")
    @ApiOperation(value = "根据商品价格顺序显示商品")
    // 根据商品价格顺序显示商品
    public ResponseMessage selectAllGoodsOrderByPrice(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                      @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                      @ApiParam("查询页数(第几页)")
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @ApiParam("单页数量")
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDtoOrderByPrice();
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            if (ifDec > 0) {
                Collections.reverse(goodsDtos);
            }
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据分类id显示商品")
    @GetMapping("/get/selectAllGoodsBySortId")
    @ApiOperation(value = "根据分类id显示商品")
    // 根据商品分类显示商品
    public ResponseMessage selectAllGoodsBySortId(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                  @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                  @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                  @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                  @ApiParam("查询页数(第几页)")
                                                  @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                  @ApiParam("单页数量")
                                                  @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDtoBySort(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            if (ifDec > 0) {
                Collections.reverse(goodsDtos);
            }
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据分类id以及更新时间顺序显示商品")
    @GetMapping("/get/selectAllGoodsBySortIdOrderByTime")
    @ApiOperation(value = "根据分类id以及更新时间顺序显示商品")
    // 根据商品分类以及更新时间顺序显示商品
    public ResponseMessage selectAllGoodsBySortIdOrderByTime(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                             @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                             @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                             @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                             @ApiParam("单页数量")
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDtoBySortOrderByTime(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            if (ifDec > 0) {
                Collections.reverse(goodsDtos);
            }
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据分类id以及价格顺序显示商品")
    @GetMapping("/get/selectAllGoodsBySortIdOrderByPrice")
    @ApiOperation(value = "根据分类id以及价格顺序显示商品")
    // 根据商品分类以及价格顺序显示商品
    public ResponseMessage selectAllGoodsBySortIdOrderByPrice(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                              @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                              @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                              @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                              @ApiParam("查询页数(第几页)")
                                                              @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                              @ApiParam("单页数量")
                                                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<GoodsDTO> goodsDtos = goodsService.selectAllDtoBySortOrderByPrice(sortId);
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            if (ifDec > 0) {
                Collections.reverse(goodsDtos);
            }
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDtos), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据商品id查找商品")
    @GetMapping("/get/selectGoodsById")
    @ApiOperation(value = "根据商品id查找商品")
    // 根据商品id查找商品
    public ResponseMessage selectGoodsById(@ApiParam(name = "goodsId", value = "商品Id", required = true)
                                           @RequestParam(value = "goodsId", defaultValue = "5") Integer goodsId) {
        GoodsDTO goodsDto = goodsService.selectDtoByPrimaryKey(goodsId);
        if (goodsDto != null) {
            return ResponseMessage.newSuccessInstance(goodsDto, "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("该商品id错误");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "根据商品名称与简介查找商品")
    @GetMapping("/get/selectGoodsByContent")
    @ApiOperation(value = "根据商品名称与简介查找商品")
    // 根据商品名称与简介查找商品
    public ResponseMessage selectGoodsByContent(@ApiParam(name = "content", value = "查找内容", required = true)
                                                @RequestParam(value = "content", defaultValue = "5") String content,
                                                @ApiParam("查询页数(第几页)")
                                                @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                @ApiParam("单页数量")
                                                @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        if (content == null) {
            return ResponseMessage.newErrorInstance("输入内容为空");
        }
        List<GoodsDTO> goodsDTOS = goodsService.selectDtoByContent(content);
        if (goodsDTOS != null && !goodsDTOS.isEmpty()) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(goodsDTOS), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("该商品名称不存在");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "添加商品")
    @PostMapping("/addGoods")
    @ApiOperation(value = "添加商品")
    // 添加商品
    public ResponseMessage addGoods(@ApiParam(name = "商品Dto实体类", value = "传入Json格式", required = true)
                                    @RequestBody
                                    @Validated GoodsDTO goodsDto) {
        List<GoodsDTO> goodsDtos = goodsService.selectAllDto();
        if (goodsDtos != null && !goodsDtos.isEmpty()) {
            boolean judgeFlag = goodsService.judgeGoods(goodsDto, goodsDtos);
            if (judgeFlag) {
                return ResponseMessage.newErrorInstance("该商品名称已经存在");
            }
        }
        boolean addFlag = goodsService.addGoods(goodsDto);
        if (addFlag) {
            return ResponseMessage.newSuccessInstance("添加成功");
        } else {
            return ResponseMessage.newErrorInstance("添加失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "删除商品")
    @DeleteMapping("/deleteGoods")
    @ApiOperation(value = "删除商品")
    // 删除商品
    public ResponseMessage deleteGoods(@ApiParam(name = "商品Dto实体类", value = "传入Json格式", required = true)
                                       @RequestBody
                                       @Validated(Delete.class) GoodsDTO goodsDto) {
        GoodsDTO goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods != null) {
            boolean deleteFlag = goodsService.deleteGoodsById(goodsDto.getId());
            boolean deleteGoodsDeteilFlag = goodsDetailService.deleteAllGoodsDetailByGoodsId(goodsDto.getId());
            boolean deleteColorSizeFlag = colorSizeService.deleteAllColorSizeByGoodsId(goodsDto.getId());

            if (deleteFlag && deleteColorSizeFlag && deleteGoodsDeteilFlag) {
                return ResponseMessage.newSuccessInstance("删除成功");
            } else {
                return ResponseMessage.newErrorInstance("删除失败");
            }
        } else {
            return ResponseMessage.newSuccessInstance("该商品不存在");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "更新商品")
    @PutMapping("/updateGoods")
    @ApiOperation(value = "更新商品")
    // 更新商品
    public ResponseMessage updateGoods(@ApiParam(name = "商品Dto实体类", value = "传入Json格式", required = true)
                                       @RequestBody
                                       @Validated(Update.class) GoodsDTO goodsDto) {
        GoodsDTO goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods != null) {
            boolean updateFlag = goodsService.updateGoods(goodsDto);
            if (updateFlag) {
                return ResponseMessage.newSuccessInstance("更新成功");
            } else {
                return ResponseMessage.newErrorInstance("更新失败");
            }
        } else {
            return ResponseMessage.newSuccessInstance("该商品不存在");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "隐藏商品，即下架商品")
    @PutMapping("/hideGoods")
    @ApiOperation(value = "隐藏商品，即下架商品")
    // 隐藏商品
    public ResponseMessage hideGoods(@ApiParam(name = "商品Dto实体类", value = "传入Json格式", required = true)
                                     @RequestBody
                                     @Validated(Update.class) GoodsDTO goodsDto) {
        GoodsDTO goods = goodsService.selectDtoByPrimaryKey(goodsDto.getId());
        if (goods != null) {
            boolean hideFlag = goodsService.hideGoods(goods);
            if (hideFlag) {
                return ResponseMessage.newSuccessInstance("修改成功");
            } else {
                return ResponseMessage.newErrorInstance("修改失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该商品不存在");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "上传商品图片")
    @PostMapping("/uploadGoodsPic")
    @ApiOperation(value = "上传商品图片", notes = "执行成功后返回图片路径(img.yisanbang.com/goods/图片名称)")
    public ResponseMessage uploadGoodsPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if ("jpg".equals(picType) || "JPG".equals(picType) || "jpeg".equals(picType) || "JPEG".equals(picType) || "png".equals(picType) || "PNG".equals(picType)) {
            try {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String picPath = cosClientUtil.uploadFile(pic, "goods/");
                return ResponseMessage.newSuccessInstance(COSClientUtil.getObjectPath() + picPath, "商品图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("商品图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "设置套装发货地址")
    @PutMapping("/setGoodsAddress")
    @ApiOperation(value = "设置套装发货地址")
    // 设置套装发货地址
    public ResponseMessage setGoodsAddress(@ApiParam(name = "发货地址", value = "传入Json格式", required = true)
                                           @RequestParam(value = "address", defaultValue = "广东东莞") String address) {
        boolean setFlag = goodsService.setGoodsAddress(address);
        if (setFlag) {
            return ResponseMessage.newSuccessInstance("设置成功");
        } else {
            return ResponseMessage.newErrorInstance("设置失败");
        }
    }

    @RequestLog(module = "单件商品", operationDesc = "设置商品默认不删除")
    @PutMapping("/setGoodsNoDelete")
    @ApiOperation(value = "设置商品默认不删除")
    // 设置商品默认不删除
    public ResponseMessage setGoodsNoDelete() {
        boolean setFlag = goodsService.setGoodsNoDelete();
        if (setFlag) {
            return ResponseMessage.newSuccessInstance("设置成功");
        } else {
            return ResponseMessage.newErrorInstance("设置失败");
        }
    }
}
