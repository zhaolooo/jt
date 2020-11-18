package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;
import com.jt.mapper.OrderShippingMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(timeout = 3000)
public class DubboOrderServiceImpl implements DubboOrderService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderShippingMapper orderShippingMapper;

    /**
     * Order{order订单本身/order物流信息/order商品信息}
     * 难点:  操作3张表完成入库操作
     * 主键信息: orderId
     * @param order
     * @return
     */
    @Override
    public String saveOrder(Order order) {
        //1.拼接OrderId
        String orderId =
                "" + order.getUserId() + System.currentTimeMillis();
        //2.完成订单入库
        order.setOrderId(orderId).setStatus(1);
        orderMapper.insert(order);

        //3.完成订单物流入库
        OrderShipping orderShipping = order.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShippingMapper.insert(orderShipping);

        //4.完成订单商品入库
        List<OrderItem> orderItems = order.getOrderItems();
        //批量入库  sql: insert into xxx(xxx,xx,xx)values (xx,xx,xx),(xx,xx,xx)....
        for (OrderItem orderItem : orderItems){
            orderItem.setOrderId(orderId);
            orderItemMapper.insert(orderItem);
        }
        System.out.println("订单入库成功!!!!");
        return orderId;
    }

    @Override
    public Order findOrderById(String id) {
        //1.查询订单信息
        Order order  = orderMapper.selectById(id);
        //2.查询订单物流信息
        OrderShipping orderShipping = orderShippingMapper.selectById(id);
        //3.查询订单商品
        QueryWrapper<OrderItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("order_id",id);
        List<OrderItem> lists =orderItemMapper.selectList(queryWrapper);
        return order.setOrderItems(lists).setOrderShipping(orderShipping);
    }
}
