package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Refund;
import com.vtmer.yisanbang.dto.CartGoodsDto;
import lombok.Data;

import javax.validation.Valid;
import java.util.List;

/**
 * 查看退款详情VO类
 */
@Data
public class RefundVo {

    @Valid
    private Refund refund;

    // 退款商品列表
    @Valid
    private List<CartGoodsDto> RefundGoodsList;

}
