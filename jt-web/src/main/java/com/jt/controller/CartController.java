package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.User;
import com.jt.service.DubboCartService;
import com.jt.thread.UserThreadLocal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference(timeout = 3000,check = false)
    private DubboCartService cartService;

    /**
     * 业务需求: 根据userId查询购物车数据
     * url地址: http://www.jt.com/cart/show.html
     * 请求参数: 动态获取userId
     * 返回值结果:  cart.jsp页面
     * 页面取值方式: ${cartList}
     */
    @RequestMapping("/show")
    public String findCartListByUserId(Model model,HttpServletRequest request){
       /* User user = (User) request.getAttribute("JT_USER");
        Long userId = user.getId();*/
        Long userId = UserThreadLocal.get().getId();
        List<Cart> cartList = cartService.findCartListByUserId(userId);
        model.addAttribute("cartList",cartList);
        return "cart";
    }

    /**
     * 业务: 实现购物车数量的更新
     * url: http://www.jt.com/cart/update/num/562379/12
     * 参数: itemId/num
     * 返回值: void
     */
    @RequestMapping("/update/num/{itemId}/{num}")
    @ResponseBody  //1.返回值转化为json 2.ajax结束标识
    public void updateCartNum(Cart cart, HttpServletRequest request){ //key名称必须与属性的名称一致.
        //User user = (User) request.getAttribute("JT_USER");
        //Long userId = user.getId();
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.updateCartNum(cart);
    }

    /**
     * 完成购物车新增
     * url: http://www.jt.com/cart/add/562379.html
     * 参数: form表单提交  对象接收
     * 返回值: 重定向到购物车列表页面中
     */
    @RequestMapping("/add/{itemId}")
    public String saveCart(Cart cart,HttpServletRequest request){

        //User user = (User) request.getAttribute("JT_USER");
        //Long userId = user.getId();
        Long userId = UserThreadLocal.get().getId();
        cart.setUserId(userId);
        cartService.saveCart(cart);
        return "redirect:/cart/show.html";
    }

    /**
     * 购物车删除操作
     * url地址: http://www.jt.com/cart/delete/562379.html
     * 参数:    获取itemId
     * 返回值:  重定向到购物车的展现页面
     */
    @RequestMapping("/delete/{itemId}")
    public String deleteCarts(@PathVariable Long itemId,HttpServletRequest request){

        //User user = (User) request.getAttribute("JT_USER");
        //Long userId = user.getId();
        Long userId = UserThreadLocal.get().getId();
        cartService.deleteCarts(userId,itemId);
        return "redirect:/cart/show.html";
    }

}
