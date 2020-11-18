package com.jt.service;

import com.jt.pojo.Order;
import org.springframework.transaction.annotation.Transactional;

public interface DubboOrderService {
    @Transactional
    String saveOrder(Order order);
    Order findOrderById(String id);
}
