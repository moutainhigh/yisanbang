package com.vtmer.yisanbang.vo;

import com.vtmer.yisanbang.domain.Refund;
import lombok.Data;

/**
 * 查看退款详情VO类
 */
@Data
public class RefundVo {

    private Refund refund;

    // 退款商品列表
    private CartVo orderGoodsList;

}
