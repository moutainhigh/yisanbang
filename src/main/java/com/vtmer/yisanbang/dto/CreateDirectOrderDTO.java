package com.vtmer.yisanbang.dto;

import com.vtmer.yisanbang.vo.CartVo;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class CreateDirectOrderDTO extends CreateOrderDTO {

    @NotEmpty(message = "订单商品列表为空")
    private CartVo cartVo;
}
