package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.CartGoodsDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * 查看退款详情VO类
 */
@Data
@ApiModel
public class RefundVo {

    @Valid
    @ApiModelProperty(value = "退款基础信息")
    private Refund refund;

    // 退款商品列表
    @Valid
    @ApiModelProperty(value = "退款商品列表")
    private List<CartGoodsDTO> RefundGoodsList;

}
