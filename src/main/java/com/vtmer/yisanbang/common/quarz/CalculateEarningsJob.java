package com.vtmer.yisanbang.common.quarz;

import com.vtmer.yisanbang.domain.Income;
import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.domain.OrderGoods;
import com.vtmer.yisanbang.mapper.OrderGoodsMapper;
import com.vtmer.yisanbang.service.IncomeService;
import com.vtmer.yisanbang.service.OrderService;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DisallowConcurrentExecution
public class CalculateEarningsJob implements Job {

    private static final Logger logger = LoggerFactory.getLogger(CalculateEarningsJob.class);

    @Autowired
    private OrderService orderService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private OrderGoodsMapper orderGoodsMapper;

    // 计算收益
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("开始计算收益");
        // 获取已支付的未退款订单
        List<Order> unRefundPayOrderList = orderService.getUnRefundPayOrderList();
        double totalPrice = 0;
        Integer totalAmount = 0;
        for (Order order : unRefundPayOrderList) {
            // 计算总价
            totalPrice += order.getTotalPrice();
            List<OrderGoods> orderGoodsList = orderGoodsMapper.selectByOrderId(order.getId());
            for (OrderGoods orderGoods : orderGoodsList) {
                // 计算总销售量
                totalAmount += orderGoods.getAmount();
            }
        }
        Income income = new Income();
        List<Income> incomeList = incomeService.selectAll();
        if (incomeList != null && incomeList.size() != 0) { // 说明数据库中已经有收益记录
            // 之前的销售信息
            double beforeTotalPrice = 0;
            Integer beforeTotalAmount = 0;
            for (Income beforeIncome : incomeList) {
                beforeTotalAmount += beforeIncome.getTotalAmount();
                beforeTotalPrice += beforeIncome.getTotalPrice();
            }
            // 计算今日的销售信息
            totalAmount -= beforeTotalAmount;
            totalPrice -= beforeTotalPrice;
        }
        // 否则说明数据库中无收益记录，这时候直接插入即可
        income.setTotalAmount(totalAmount);
        income.setTotalPrice(totalPrice);
        int res = incomeService.insert(income);
        if (res == 0) {
            logger.error("插入收益信息出错");
        } else if (res == 1) {
            logger.info("今日收益计算成功");
        }
    }
}
