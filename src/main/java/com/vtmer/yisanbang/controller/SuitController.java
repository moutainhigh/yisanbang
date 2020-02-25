package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.SuitDTO;
import com.vtmer.yisanbang.service.SuitService;
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

@Api(tags = "套装管理接口")
@RestController
@RequestMapping("/suit")
public class SuitController {
    @Autowired
    private SuitService suitService;

    @GetMapping("/get/selectAllSuit")
    @ApiOperation(value = "查找显示所有套装")
    // 查找所有套装
    public ResponseMessage selectAllSuit(@ApiParam("查询页数(第几页)")
                                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         @ApiParam("单页数量")
                                         @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectAll();
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/get/selectAllSuitOrderByTime")
    @ApiOperation(value = "根据套装更新时间顺序查找显示套装")
    // 根据套装更新时间顺序显示套装
    public ResponseMessage selectAllSuitOrderByTime(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                    @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                    @ApiParam("查询页数(第几页)")
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @ApiParam("单页数量")
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitOrderByTime();
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            if (ifDec > 0)
                Collections.reverse(suitDtoList);
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        } else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/get/selectAllSuitOrderByPrice")
    @ApiOperation(value = "根据套装价格顺序查找显示套装")
    // 根据套装价格顺序显示套装
    public ResponseMessage selectAllGoodsOrderByPrice(@ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                      @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                      @ApiParam("查询页数(第几页)")
                                                      @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                      @ApiParam("单页数量")
                                                      @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitOrderByPrice();
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            if (ifDec > 0)
                Collections.reverse(suitDtoList);
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        } else
            return ResponseMessage.newErrorInstance("无商品信息，查找失败");
    }

    @GetMapping("/get/selectAllSuitBySortIdOrderByPrice")
    @ApiOperation(value = "根据分类id以及价格顺序显示套装")
    // 根据分类id以及价格顺序显示套装
    public ResponseMessage selectAllSuitBySortIdOrderByPrice(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                             @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                             @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                             @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                             @ApiParam("查询页数(第几页)")
                                                             @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                             @ApiParam("单页数量")
                                                             @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitBySortIdOrderByPrice(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            if (ifDec > 0)
                Collections.reverse(suitDtoList);
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        } else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/get/selectAllSuitBySortIdOrderByTime")
    @ApiOperation(value = "根据分类id以及更新时间顺序显示商品套装")
    // 根据分类id以及更新时间顺序显示商品套装
    public ResponseMessage selectAllSuitBySortIdOrderByTime(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                            @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                            @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                            @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                            @ApiParam("查询页数(第几页)")
                                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                            @ApiParam("单页数量")
                                                            @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitBySortIdOrderByTime(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            if (ifDec > 0)
                Collections.reverse(suitDtoList);
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        } else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/get/selectAllSuitBySortId")
    @ApiOperation(value = "根据套装分类id查找套装")
    // 根据套装分类查找套装
    public ResponseMessage selectAllSuitBySortId(@ApiParam(name = "sortId", value = "分类Id", required = true)
                                                 @RequestParam(value = "sortId", defaultValue = "5") Integer sortId,
                                                 @ApiParam(name = "ifDec", value = "降序标志", required = true)
                                                 @RequestParam(value = "ifDec", defaultValue = "1") Integer ifDec,
                                                 @ApiParam("查询页数(第几页)")
                                                 @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                 @ApiParam("单页数量")
                                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitBySort(sortId);
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            if (ifDec > 0)
                Collections.reverse(suitDtoList);
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        } else
            return ResponseMessage.newErrorInstance("无套装信息，查找失败");
    }

    @GetMapping("/get/selectSuitById")
    @ApiOperation(value = "根据套装id查找套装")
    // 根据套装id查找套装
    public ResponseMessage selectSuitById(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                          @RequestParam(value = "suitId", defaultValue = "5") Integer suitId) {
        SuitDTO suitDto = suitService.selectSuitById(suitId);
        if (suitDto != null)
            return ResponseMessage.newSuccessInstance(suitDto, "查找成功");
        else return ResponseMessage.newErrorInstance("该套装id错误");
    }

    @GetMapping("/get/selectSuitByContent")
    @ApiOperation(value = "根据套装名称与简介查找套装")
    // 根据套装名称与简介查找套装
    public ResponseMessage selectSuitByContent(@ApiParam(name = "content", value = "查找内容", required = true)
                                               @RequestParam(value = "content", defaultValue = "学生") String content,
                                               @ApiParam("查询页数(第几页)")
                                               @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                               @ApiParam("单页数量")
                                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDTO> suitDtoList = suitService.selectSuitByContent(content);
        if (suitDtoList != null && !suitDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDtoList), "查找成功");
        else return ResponseMessage.newErrorInstance("该套装名称不存在");
    }

    @PostMapping("/addSuit")
    @ApiOperation(value = "添加套装")
    // 添加套装
    public ResponseMessage addGoods(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                    @RequestBody
                                    @Validated SuitDTO suitDto) {
        List<SuitDTO> suitDtoList = suitService.selectAll();
        if (suitDtoList != null && !suitDtoList.isEmpty()) {
            boolean judgeFlag = suitService.judgeSuit(suitDto, suitDtoList);
            if (judgeFlag) return ResponseMessage.newErrorInstance("该套装名称已经存在");
        }
        boolean addFlag = suitService.addSuit(suitDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @DeleteMapping("/deleteSuit")
    @ApiOperation(value = "删除套装")
    // 删除套装
    public ResponseMessage deleteGoods(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                       @RequestBody
                                       @Validated(Delete.class) SuitDTO suitDto) {
        SuitDTO suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean deleteFlag = suitService.deleteSuitById(suitDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/updateSuit")
    @ApiOperation(value = "更新套装")
    // 更新套装
    public ResponseMessage updateSuit(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                      @RequestBody
                                      @Validated(Update.class) SuitDTO suitDto) {
        SuitDTO suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean updateFlag = suitService.updateSuitById(suitDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newSuccessInstance("该商品不存在");
    }

    @PutMapping("/hideSuit")
    @ApiOperation(value = "隐藏套装，即下架")
    // 隐藏套装
    public ResponseMessage hideSuit(@ApiParam(name = "套装Dto实体类", value = "传入Json格式", required = true)
                                    @RequestBody
                                    @Validated(Update.class) SuitDTO suitDto) {
        SuitDTO suit = suitService.selectSuitById(suitDto.getId());
        if (suit != null) {
            boolean hideFlag = suitService.hideSuit(suit);
            if (hideFlag) return ResponseMessage.newSuccessInstance("修改成功");
            else return ResponseMessage.newErrorInstance("修改失败");
        } else return ResponseMessage.newErrorInstance("该商品不存在");
    }

    @PostMapping("/uploadGoodsPic")
    @ApiOperation(value = "上传商品图片", notes = "执行成功后返回图片路径(img.yisanbang.com/suit/图片名称)")
    public ResponseMessage uploadGoodsPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if (picType.equals("jpg") || picType.equals("JPG") || picType.equals("jpeg") || picType.equals("JPEG") || picType.equals("png") || picType.equals("PNG")) {
            String picName = UUID.randomUUID().toString();
            try {
                String picPath = QiniuUpload.updateFile(pic, "suit/" + picName);
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
