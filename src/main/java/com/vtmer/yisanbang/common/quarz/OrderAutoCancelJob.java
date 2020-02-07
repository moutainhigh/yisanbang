package com.vtmer.yisanbang.common.quarz;

import com.vtmer.yisanbang.domain.Order;
import com.vtmer.yisanbang.service.OrderService;
import one.util.streamex.StreamEx;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@DisallowConcurrentExecution
public class OrderAutoCancelJob implements Job {

    @Autowired
    OrderService orderService;

    private static final Logger logger = LoggerFactory.getLogger(OrderAutoCancelJob.class);

    // 订单有效时间 30分钟
    private static final long EFFECTIVE_TIME = 30 * 60 * 1000;

    @Transactional
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("执行订单检测任务");
        // 队列
        Queue<Order> queue = new LinkedList<>();
        // 未付款订单列表
        List<Order> notPayOrder = orderService.getNotPayOrder();

        // 如果未支付订单不为空
        if (!notPayOrder.isEmpty()) {
            for (Order order : notPayOrder) {
                queue.offer(order);
                // 队列去重订单id
                queue = StreamEx.of(queue).distinct(Order::getId).collect(Collectors.toCollection(LinkedList::new));
            }
        }
        // 获取队列的头元素,开始检测头订单是否失效
        Order element = queue.peek();
        while (element != null) {
            //时间差值
            Long diff = this.checkOrder(element);
            if (diff != null && diff >= EFFECTIVE_TIME) {
                System.out.println("开始关闭订单" + element.getId() + "下单时间" + element.getCreateTime());
                // 更待订单状态为交易关闭
                HashMap<String, Integer> orderMap = new HashMap<>();
                orderMap.put("orderId",element.getId());
                orderMap.put("status",4);
                orderService.setOrderStatus(orderMap);
                // 库存归位,1代表增加库存
                orderService.updateInventory(element.getOrderNumber(),1);
                // 弹出队列
                queue.poll();
                // 取下一个元素
                element = queue.peek();
            } else if (diff < EFFECTIVE_TIME) {
                try {
                    System.out.println("等待检测订单" + element.getId() + "下单时间" + element.getCreateTime() + "已下单"
                            + diff / 1000 + "秒");
                    Thread.sleep(EFFECTIVE_TIME - diff);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    logger.info("OrderAutoCancelJob.checkOrder定时任务出现问题");
                }
            } // end else if
        } // end while
    }

    /**
     * 获取订单的下单时间和现在的时间差
     * @param order：订单实体类
     * @return
     */
    private Long checkOrder(Order order) {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long diff = null;
        if (order != null) {
            Date createTime = order.getCreateTime();
            try {
                diff = sdf.parse(sdf.format(date)).getTime() - sdf.parse(sdf.format(createTime)).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        // 返回值为毫秒
        return diff;
    }

}
