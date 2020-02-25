package com.vtmer.yisanbang.controller;

import com.alibaba.fastjson.JSONObject;
import com.vtmer.yisanbang.SpringBootTestAbstract;
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