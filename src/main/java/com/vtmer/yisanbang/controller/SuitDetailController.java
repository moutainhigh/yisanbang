package com.vtmer.yisanbang.controller;

import com.github.pagehelper.PageHelper;
import com.vtmer.yisanbang.common.PageResponseMessage;
import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.common.tencentcloud.COSClientUtil;
import com.vtmer.yisanbang.common.valid.group.Delete;
import com.vtmer.yisanbang.common.valid.group.Insert;
import com.vtmer.yisanbang.common.valid.group.Update;
import com.vtmer.yisanbang.dto.SuitDetailDTO;
import com.vtmer.yisanbang.service.SuitDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Api(tags = "套装详情管理接口")
@RestController
@RequestMapping("/suitDetail")
public class SuitDetailController {
    @Autowired
    private SuitDetailService suitDetailService;

    @RequestLog(module = "套装详情", operationDesc = "查找所有套装详情")
    @GetMapping("/get/selectAllSuitDetail")
    @ApiOperation(value = "查找显示所有套装详情")
    // 查找显示所有套装详情
    public ResponseMessage selectAll(@ApiParam("查询页数(第几页)")
                                     @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                     @ApiParam("单页数量")
                                     @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDetailDTO> suitDetailDtoList = suitDetailService.selectAllDto();
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDetailDtoList), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("查找失败");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "查找套装的所有套装详情")
    @GetMapping("/get/selectSuitDetailBySuitId")
    @ApiOperation(value = "根据套装id查找显示该套装的所有套装详情")
    // 根据套装id查找显示该套装的所有套装详情
    public ResponseMessage selectSuitDetailBySuitId(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                    @RequestParam(value = "suitId", defaultValue = "5") Integer suitId,
                                                    @ApiParam("查询页数(第几页)")
                                                    @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                                    @ApiParam("单页数量")
                                                    @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SuitDetailDTO> suitDetailDtoList = suitDetailService.selectAllDtoBySuitId(suitId);
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()) {
            return ResponseMessage.newSuccessInstance(PageResponseMessage.restPage(suitDetailDtoList), "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("查找失败");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "查找套装详情")
    @GetMapping("/get/selectSuitDetailById")
    @ApiOperation(value = "根据套装详情id查找套装详情")
    // 根据套装详情id查找套装详情
    public ResponseMessage selectSuitDetailById(@ApiParam(name = "suitDetailId", value = "套装详情Id", required = true)
                                                @RequestParam(value = "suitDetailId", defaultValue = "5") Integer suitDetailId) {
        SuitDetailDTO suitDetailDto = suitDetailService.selectSuitDetailByID(suitDetailId);
        if (suitDetailDto != null) {
            return ResponseMessage.newSuccessInstance(suitDetailDto, "查找成功");
        } else {
            return ResponseMessage.newErrorInstance("查找失败");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "添加套装详情")
    @PostMapping("/addSuitDetail")
    @ApiOperation(value = "添加套装详情")
    // 添加套装详情
    public ResponseMessage addSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                         @RequestBody
                                         @Validated(Insert.class) SuitDetailDTO suitDetailDto) {
        boolean addFlag = suitDetailService.addSuitDetail(suitDetailDto);
        if (addFlag) {
            return ResponseMessage.newSuccessInstance("添加成功");
        } else {
            return ResponseMessage.newErrorInstance("添加失败");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "更新套装详情")
    @PutMapping("/updateSuitDetail")
    @ApiOperation(value = "更新套装详情")
    // 更新套装详情
    public ResponseMessage updateSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                            @RequestBody
                                            @Validated(Update.class) SuitDetailDTO suitDetailDto) {
        SuitDetailDTO suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail != null) {
            boolean updateFlag = suitDetailService.updateSuitDetail(suitDetailDto);
            if (updateFlag) {
                return ResponseMessage.newSuccessInstance("更新成功");
            } else {
                return ResponseMessage.newErrorInstance("更新失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装详情id错误");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "删除套装详情")
    @DeleteMapping("/deleteSuitDetail")
    @ApiOperation(value = "删除套装详情")
    // 删除套装详情
    public ResponseMessage deleteSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                            @RequestBody
                                            @Validated(Delete.class) SuitDetailDTO suitDetailDto) {
        SuitDetailDTO suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail != null) {
            boolean deleteFlag = suitDetailService.deleteSuitDetail(suitDetailDto.getId());
            if (deleteFlag) {
                return ResponseMessage.newSuccessInstance("删除成功");
            } else {
                return ResponseMessage.newErrorInstance("删除失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装详情id错误");
        }
    }

    @RequestLog(module = "套装详情", operationDesc = "上传套装详情图片")
    @PostMapping("/uploadSuitDetailPic")
    @ApiOperation(value = "上传套装详情图片至服务器", notes = "执行成功后返回图片路径(img.yisanbang.com/suitDetail/图片名称)")
    // 上传套装详情图片至服务器
    public ResponseMessage uploadSuitDetailPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if ("jpg".equals(picType) || "JPG".equals(picType) || "jpeg".equals(picType) || "JPEG".equals(picType) || "png".equals(picType) || "PNG".equals(picType)) {
            try {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String picPath = cosClientUtil.uploadFile(pic, "suitDetail/");
                return ResponseMessage.newSuccessInstance(COSClientUtil.getObjectPath() + picPath, "套装详情图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("套装详情图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }
}
