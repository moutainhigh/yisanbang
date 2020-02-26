package com.vtmer.yisanbang.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.vtmer.yisanbang.SpringBootTestAbstract;
import com.vtmer.yisanbang.vo.Token;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

class OrderControllerTest extends SpringBootTestAbstract {


    OrderController orderController;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    /**
     * 所有测试方法执行之前执行该方法
     */
    @Before
    void setUp() {
        DefaultMockMvcBuilder builder = MockMvcBuilders.webAppContextSetup(context);
        // shiroFilter是在shiroConfig中定义的bean名称
        builder.addFilters((Filter) context.getBean("jwtFilter"));
        mockMvc = builder.build(); //初始化MockMvc对象
        orderController = new OrderController();
    }

    @Test
    void testOrder() throws Exception {
        Map<String, String> map = new HashMap<>();
        // wx.login获取的code
        String code = "";
        map.put("code", code);
        String content = JSONObject.toJSONString(map);
        // 先请求微信登录接口获取token，以便后续访问
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login") // 请求方法 请求路径
                .accept(MediaType.APPLICATION_JSON) //请求头，Accept代表发送端（客户端）希望接收的数据类型
                .contentType(MediaType.APPLICATION_JSON) //实体头，Content-Type代表发送端（客户端|服务器）发送的实体数据的数据类型
                .content(content)) //发送端发送的实体数据
                .andExpect( //添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确。
                        MockMvcResultMatchers.status().isOk() //验证状态码
                )
                .andDo(//添加ResultHandler结果处理器，比如调试时打印结果到控制台。
                        MockMvcResultHandlers.print()//输出整个响应结果信息。
                        //new EncodingResultHandler()//自定义结果处理器
                )
                .andReturn(); //最后返回相应的MvcResult，然后进行自定义验证/进行下一步的异步处理。
        // 拿到json字符串
        String jsonResponse = result.getResponse().getContentAsString();
        Token token = JSON.parseObject(jsonResponse, Token.class);
        System.out.println("获取的token值为" + token);

        /*
        // 登录微信成功，调用直接下单接口
        OrderVO orderVO = new OrderVO();
        // 订单留言
        orderVO.setMessage("请尽快发货...");
        // 订单商品列表
        ArrayList<OrderGoodsDTO> orderGoodsDTOArrayList = new ArrayList<>();
        OrderGoodsDTO orderGoodsDTO = new OrderGoodsDTO();
        orderGoodsDTO.setAmount(3);
        orderGoodsDTO.setColorSizeId(1);
        orderGoodsDTO.setWhetherGoods(true);
        orderVO.setTotalPrice(3000.00);
        orderGoodsDTOArrayList.add(orderGoodsDTO);
        orderVO.setOrderGoodsDTOList(orderGoodsDTOArrayList);
        // 订单收货地址
        UserAddress userAddress = new UserAddress();
        userAddress.setAddressName("广东工业大学生活东区");
        userAddress.setPhoneNumber("17666289644");
        userAddress.setUserName("一叄邦");
        orderVO.setUserAddress(userAddress);
        String orderDTOJSON = JSONObject.toJSONString(orderVO);
        // 下单DTO封装完成，接着请求下单接口
        mockMvc.perform(MockMvcRequestBuilders.post("/order/insertDirectOrder")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderDTOJSON))
                .andExpect( //添加ResultMatcher验证规则，验证控制器执行完成后结果是否正确。
                        MockMvcResultMatchers.status().isOk() //验证状态码
                )
                .andDo(MockMvcResultHandlers.print());

        // 创建订单成功,调用微信支付接口

         */

    }

    @Test
    void confirmCartOrder() {
    }

    @Test
    void confirmDirectOrder() {
    }

    @Test
    void insertDirectOrder() {
    }

    @Test
    void insertCartOrder() {
    }

    @Test
    void wxpay() {
    }

    @Test
    void parseOrderNotifyResult() {
    }

    @Test
    void getUserOrderList() {
    }

    @Test
    void getOrderList() {
    }

    @Test
    void updateOrderStatus() {
    }

    @Test
    void setOrderStatus() {
    }

    @Test
    void delete() {
    }

    @Test
    void setCourierNumber() {
    }

    @Test
    void updateAddress() {
    }

    @Test
    void cancelOrder() {
    }
}