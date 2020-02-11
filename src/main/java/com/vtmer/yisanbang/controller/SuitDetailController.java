package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.common.validGroup.Delete;
import com.vtmer.yisanbang.common.validGroup.Insert;
import com.vtmer.yisanbang.common.validGroup.Update;
import com.vtmer.yisanbang.dto.SuitDetailDto;
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

    @GetMapping("/selectAllSuitDetail")
    @ApiOperation(value = "查找显示所有商品详情")
    // 查找显示所有商品详情
    public ResponseMessage selectAll() {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDto();
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDetailDtoList, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectSuitDetailBySuitId/{id}")
    @ApiOperation(value = "根据套装id查找显示该套装的所有套装详情")
    // 根据套装id查找显示该套装的所有套装详情
    public ResponseMessage selectSuitDetailBySuitId(@ApiParam(name = "suitId", value = "套装Id", required = true)
                                                        @PathVariable("id") Integer suitId) {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDtoBySuitId(suitId);
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDetailDtoList, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectSuitDetailById/{id}")
    @ApiOperation(value = "根据套装详情id查找套装详情")
    // 根据套装详情id查找套装详情
    public ResponseMessage selectSuitDetailById(@ApiParam(name = "suitDetailId", value = "套装详情Id", required = true)
                                                    @PathVariable("id") Integer suitDetailId) {
        SuitDetailDto suitDetailDto = suitDetailService.selectSuitDetailByID(suitDetailId);
        if (suitDetailDto != null) return ResponseMessage.newSuccessInstance(suitDetailDto, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @PostMapping("/addSuitDetail")
    @ApiOperation(value = "添加套装详情")
    // 添加套装详情
    public ResponseMessage addSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                             @RequestBody
                                                     @Validated(Insert.class) SuitDetailDto suitDetailDto) {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDtoBySuitId(suitDetailDto.getSuitId());
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()) {
            boolean judgeFlag = suitDetailService.judgeSuitDetail(suitDetailDto, suitDetailDtoList);
            if (judgeFlag) return ResponseMessage.newSuccessInstance("该套装详情已经存在");
        }
        boolean addFlag = suitDetailService.addSuitDetail(suitDetailDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @PutMapping("/updateSuitDetail")
    @ApiOperation(value = "更新套装详情")
    // 更新套装详情
    public ResponseMessage updateSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                                @RequestBody
                                                        @Validated(Update.class) SuitDetailDto suitDetailDto) {
        SuitDetailDto suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail != null) {
            boolean updateFlag = suitDetailService.updateSuitDetail(suitDetailDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该套装详情id错误");
    }

    @DeleteMapping("/deleteSuitDetail")
    @ApiOperation(value = "删除套装详情")
    // 删除套装详情
    public ResponseMessage deleteSuitDetail(@ApiParam(name = "套装详情Dto实体类", value = "传入Json格式", required = true)
                                                @RequestBody
                                                        @Validated(Delete.class) SuitDetailDto suitDetailDto) {
        SuitDetailDto suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail != null) {
            boolean deleteFlag = suitDetailService.deleteSuitDetail(suitDetailDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该套装详情id错误");
    }

    @GetMapping("/uploadSuitDetailPic")
    @ApiOperation(value = "上传套装详情图片至服务器")
    // 上传套装详情图片至服务器
    public ResponseMessage uploadSuitDetailPic(MultipartFile pic) {
        String picName = UUID.randomUUID().toString();
        try {
            String picPath = QiniuUpload.updateFile(pic, "suitDetail/" + picName);
            return ResponseMessage.newSuccessInstance(picPath, "套装详细信息图片上传成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseMessage.newErrorInstance("套装详细信息图片上传失败");
        }
    }
}
