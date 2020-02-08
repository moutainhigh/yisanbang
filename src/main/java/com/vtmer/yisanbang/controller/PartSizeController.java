package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.dto.PartSizeDto;
import com.vtmer.yisanbang.service.PartSizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/partSize")
public class PartSizeController {
    @Autowired
    private PartSizeService partSizeService;

    @GetMapping("/selectAllPartSize")
    // 查找所有部件尺寸
    public ResponseMessage selectAll() {
        List<PartSizeDto> partSizeDtos = partSizeService.selectAll();
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(partSizeDtos, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectPartBySuitId/{id}")
    // 根据套装id查找所有该套装的部件尺寸
    public ResponseMessage selectPartBySuitId(@PathVariable("id") Integer suitId) {
        List<PartSizeDto> partSizeDtos = partSizeService.selectAllBySuitId(suitId);
        if (partSizeDtos != null && !partSizeDtos.isEmpty())
            return ResponseMessage.newSuccessInstance(partSizeDtos, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @GetMapping("/selectPartById/{id}")
    // 根据部件尺寸id查找部件尺寸
    public ResponseMessage selectPartById(@PathVariable("id") Integer partSizeId) {
        PartSizeDto partSizeDto = partSizeService.selectPartSizeById(partSizeId);
        if (partSizeDto != null)
            return ResponseMessage.newSuccessInstance(partSizeDto, "查找成功");
        else return ResponseMessage.newErrorInstance("查找失败");
    }

    @DeleteMapping("/deletePartSize")
    // 删除部件尺寸
    public ResponseMessage deletePartSize(@RequestBody PartSizeDto partSizeDto) {
        PartSizeDto partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean deleteFlag = partSizeService.deletePartSize(partSizeDto.getId());
            if (deleteFlag) return ResponseMessage.newSuccessInstance("删除成功");
            else return ResponseMessage.newErrorInstance("删除失败");
        } else return ResponseMessage.newErrorInstance("该部件尺寸id错误");
    }

    @PutMapping("/updatePartSize")
    // 更新部件尺寸
    public ResponseMessage updatePartSize(@RequestBody PartSizeDto partSizeDto) {
        PartSizeDto partSize = partSizeService.selectPartSizeById(partSizeDto.getId());
        if (partSize != null) {
            boolean updateFlag = partSizeService.updatePartSize(partSizeDto);
            if (updateFlag) return ResponseMessage.newSuccessInstance("更新成功");
            else return ResponseMessage.newErrorInstance("更新失败");
        } else return ResponseMessage.newErrorInstance("该部件尺寸id错误");
    }

    @GetMapping("/selectAllPartById/{id}")
    // 查找所有部件
    public ResponseMessage selectAllPartById(@PathVariable("id") Integer suitId) {
        PartSizeDto partSizeDto = partSizeService.selectPartSizeById(suitId);
        if (partSizeDto != null) {
            List<String> list = partSizeService.selectAllPartById(suitId);
            if (list != null && !list.isEmpty()) {
                return ResponseMessage.newSuccessInstance(list, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }

    @GetMapping("/selectAllSizeById/{id}")
    // 查找所有尺寸
    public ResponseMessage selectAllSizeById(@PathVariable("id") Integer suitId) {
        PartSizeDto partSizeDto = partSizeService.selectPartSizeById(suitId);
        if (partSizeDto != null) {
            List<String> list = partSizeService.selectAllSizeById(suitId);
            if (list != null && !list.isEmpty()) {
                return ResponseMessage.newSuccessInstance(list, "查找成功");
            } else {
                return ResponseMessage.newErrorInstance("查找失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("该套装id错误，查找无结果");
        }
    }
}
