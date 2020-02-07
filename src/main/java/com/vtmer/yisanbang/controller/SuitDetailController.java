package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.qiniu.QiniuUpload;
import com.vtmer.yisanbang.dto.SuitDetailDto;
import com.vtmer.yisanbang.service.SuitDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/suitDetail")
public class SuitDetailController {
    @Autowired
    private SuitDetailService suitDetailService;

    @GetMapping("/selectAllSuitDetail")
    public ResponseMessage selectAll() {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDto();
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDetailDtoList, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectSuitDetailBySuitId/{id}")
    public ResponseMessage selectSuitDetailBySuitId(@PathVariable("id") Integer suitId) {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDtoBySuitId(suitId);
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty())
            return ResponseMessage.newSuccessInstance(suitDetailDtoList, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectSuitDetailById/{id}")
    public ResponseMessage selectSuitDetailById(@PathVariable("id") Integer suitDetailId) {
        SuitDetailDto suitDetailDto = suitDetailService.selectSuitDetailByID(suitDetailId);
        if (suitDetailDto != null) return ResponseMessage.newSuccessInstance(suitDetailDto, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @PostMapping("/addSuitDetail")
    public ResponseMessage addSuitDetail(@RequestBody SuitDetailDto suitDetailDto) {
        List<SuitDetailDto> suitDetailDtoList = suitDetailService.selectAllDtoBySuitId(suitDetailDto.getSuitId());
        if (suitDetailDtoList != null && !suitDetailDtoList.isEmpty()){
            boolean judgeFlag = suitDetailService.judgeSuitDetail(suitDetailDto, suitDetailDtoList);
            if (judgeFlag) return ResponseMessage.newSuccessInstance("该套装详情已经存在");
        }
        boolean addFlag = suitDetailService.addSuitDetail(suitDetailDto);
        if (addFlag) return ResponseMessage.newSuccessInstance("添加成功");
        else return ResponseMessage.newErrorInstance("添加失败");
    }

    @PutMapping("/updateSuitDetail")
    public ResponseMessage updateSuitDetail(@RequestBody SuitDetailDto suitDetailDto) {
        SuitDetailDto suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail!=null) {
            boolean updateFlag = suitDetailService.updateSuitDetail(suitDetailDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        }else return ResponseMessage.newErrorInstance("该套装详情id错误");
    }

    @DeleteMapping("/deleteSuitDetail")
    public ResponseMessage deleteSuitDetail(@RequestBody SuitDetailDto suitDetailDto) {
        SuitDetailDto suitDetail = suitDetailService.selectSuitDetailByID(suitDetailDto.getId());
        if (suitDetail!=null) {
            boolean deleteFlag = suitDetailService.deleteSuitDetail(suitDetailDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        }else return ResponseMessage.newErrorInstance("该套装详情id错误");
    }

    @GetMapping("/uploadSuitDetailPic")
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