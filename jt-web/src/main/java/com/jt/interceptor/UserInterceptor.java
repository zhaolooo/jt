package com.jt.interceptor;

import com.jt.pojo.User;
import com.jt.thread.UserThreadLocal;
import com.jt.util.CookieUtil;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@Component  //spring容器管理对象
public class UserInterceptor implements HandlerInterceptor {

    @Autowired
    private JedisCluster jedisCluster;

    //Spring版本升级 4 必须实现所有的方法  spring 5 只需要重写指定的方法即可.

    /**
     * 需求:   拦截/cart开头的所有的请求进行拦截.,并且校验用户是否登录.....
     * 拦截器选择: preHandler
     * 如何判断用户是否登录:  1.检查cookie信息   2.检查Redis中是否有记录.
     *          true : 请求应该放行
     *          false: 请求应该拦截 则配合重定向的语法实现页面跳转到登录页面 使得程序流转起来

     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.判断用户是否登录  检查cookie是否有值
        String ticket = CookieUtil.getCookieValue(request,"JT_TICKET");
        //2.校验ticket
        if(!StringUtils.isEmpty(ticket)){
            //3.判断redis中是否有值.
            if(jedisCluster.exists(ticket)){
                //4.动态获取json信息
                String userJSON = jedisCluster.get(ticket);
                User user = ObjectMapperUtil.toObj(userJSON,User.class);
                request.setAttribute("JT_USER",user);
                UserThreadLocal.set(user);
                return true;
            }
        }
        response.sendRedirect("/user/login.html");
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //销毁数据
        request.removeAttribute("JT_USER");
        UserThreadLocal.remove();
    }
}
