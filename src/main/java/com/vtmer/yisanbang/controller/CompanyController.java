package com.vtmer.yisanbang.controller;

import com.vtmer.yisanbang.common.ResponseMessage;
import com.vtmer.yisanbang.common.annotation.RequestLog;
import com.vtmer.yisanbang.common.tencentcloud.COSClientUtil;
import com.vtmer.yisanbang.domain.Company;
import com.vtmer.yisanbang.service.CompanyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@Api(tags = "公司简介接口")
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    private CompanyService companyService;

    @RequestLog(module = "公司介绍", operationDesc = "上传公司介绍图片")
    @ApiOperation(value = "上传公司介绍图片", notes = "执行成功后返回图片路径")
    @PostMapping("/upload")
    public ResponseMessage uploadPic(@ApiParam("选择上传图片") MultipartFile pic) {
        String picType = pic.getOriginalFilename().substring(pic.getOriginalFilename().lastIndexOf(".") + 1);
        System.out.println(picType);
        if ("jpg".equals(picType) || "JPG".equals(picType) || "jpeg".equals(picType) || "JPEG".equals(picType) || "png".equals(picType) || "PNG".equals(picType)) {
            try {
                COSClientUtil cosClientUtil = new COSClientUtil();
                String picPath = cosClientUtil.uploadFile(pic, "company/");
                return ResponseMessage.newSuccessInstance(COSClientUtil.getObjectPath() + picPath, "公司介绍图片上传成功");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseMessage.newErrorInstance("公司介绍图片上传失败");
            }
        } else {
            return ResponseMessage.newErrorInstance("请选择.jpg/.JPG/.jpeg/.JPEG/.png/.PNG图片文件");
        }
    }

    @RequestLog(module = "公司介绍", operationDesc = "获取公司介绍图片")
    @ApiOperation(value = "获取公司介绍图片")
    @GetMapping("/get/getPicture")
    public ResponseMessage getPicture() {
        List<Company> company = companyService.listPicture();
        if (company != null) {
            return ResponseMessage.newSuccessInstance(company, "获取公司介绍图片成功");
        }
        return ResponseMessage.newErrorInstance("获取公司介绍图片失败");
    }

    @RequestLog(module = "公司介绍", operationDesc = "修改公司介绍图片")
    @ApiOperation(value = "修改公司介绍图片")
    @PutMapping("/{id}")
    public ResponseMessage updatePicture(@PathVariable("id") Integer id, @RequestBody Map<String, String> request) {
        if (!request.containsKey("picturePath") || request.get("picturePath") == null || "".equals(request.get("picturePath"))) {
            return ResponseMessage.newErrorInstance("缺少参数picturePath或picturePath不合法");
        }
        if (companyService.updatePictureById(id, request.get("picturePath")) != 0) {
            return ResponseMessage.newSuccessInstance("公司介绍图片修改成功");
        }
        return ResponseMessage.newErrorInstance("公司介绍图片修改失败");
    }
}
