package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.thread.UserThreadLocal;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference(timeout = 3000,check = false)
    private DubboOrderService orderService;
    @Reference(timeout = 3000,check = false)
    private DubboCartService cartService;

    /**
     * 订单页面跳转
     * url: http://www.jt.com/order/create.html
     * 页面取值: ${carts}
     */
    @RequestMapping("/create")
    public String create(Model model){

        //1.根据userId查询购物车信息
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("carts",cartList);
        return "order-cart";
    }

    /**
     * 订单提交
     * url: http://www.jt.com/order/submit
     * 参数: 整个form表单
     * 返回值: SysResult对象   携带返回值orderId
     * 业务说明:
     *   当订单入库之后,需要返回orderId.让用户查询.
     */
    @RequestMapping("/submit")
    @ResponseBody
    public SysResult saveOrder(Order order){
        Long userId = UserThreadLocal.get().getId();
        order.setUserId(userId);
        String orderId = orderService.saveOrder(order);
        if(StringUtils.isEmpty(orderId))
            return SysResult.fail();
        else
            return SysResult.success(orderId);

    }

    /**
     * 实现商品查询
     * 1.url地址: http://www.jt.com/order/success.html?id=71603356409924
     * 2.参数说明: id 订单编号
     * 3.返回值类型: success.html
     * 4.页面取值方式: ${order.orderId}
     */
    @RequestMapping("/success")
    public String findOrderById(String id,Model model){
        Order order = orderService.findOrderById(id);
        model.addAttribute("order",order);
        return "success";
    }









}
