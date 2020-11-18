package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller     //需要进行页面跳转
@RequestMapping("/user")
public class UserController {

    @Reference(timeout = 3000,check = false)
    private DubboUserService userService;
    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 实现用户模块页面跳转
     * url1: http://www.jt.com/user/login.html     页面:login.jsp
     * url2: http://www.jt.com/user/register.html  页面:register.jsp
     * 要求:实现通用页面跳转
     * restFul方式: 1.动态获取url中的参数,之后实现通用的跳转.
     */
    @RequestMapping("/{moduleName}")
    public String module(@PathVariable String moduleName){

        return moduleName;
    }

    /**
     * 需求: 实现用户信息注册
     * 1.url请求地址:  http://www.jt.com/user/doRegister
     * 2.请求参数:     {password:_password,username:_username,phone:_phone},
     * 3.返回值结果:   SysResult对象
     */
    @RequestMapping("/doRegister")
    @ResponseBody   //将数据转化为JSON
    public SysResult saveUser(User user){
        //消费者给予dubbo协议将user对象进行远程网络数据传输.
        userService.saveUser(user);
        return SysResult.success();
    }

    /**
     * 完成用户登录操作
     * 1.url地址: http://www.jt.com/user/doLogin?r=0.9309436837648131
     * 2.参数:    {username:_username,password:_password},
     * 3.返回值结果:  SysResult对象
     *
     * 4.Cookie:
     *   4.1 setPath("/")  path表示如果需要获取cookie中的数据,则url地址所在路径设定.
     *       url:http://www.jt.com/person/findAll
     *       cookie.setPath("/");   一般都是/
     *       cookie.setPath("/person");
     *   4.2 setDomain("xxxxx")  设定cookie共享的域名地址.
     */
    @RequestMapping("/doLogin")
    @ResponseBody
    public SysResult doLogin(User user, HttpServletResponse response){
        String ticket = userService.doLogin(user);
        if(StringUtils.isEmpty(ticket)){
            //说明用户名或者密码错误
            return SysResult.fail();
        }else{
            //1.创建Cookie
            /*Cookie cookie = new Cookie("JT_TICKET",ticket);
            cookie.setMaxAge(7*24*60*60);   //设定cookie存活有效期
            cookie.setPath("/");            //设定cookie有效范围
            cookie.setDomain("com.jt.jt.com");     //设定cookie共享的域名 是实现单点登录必备要素
            response.addCookie(cookie);*/
            CookieUtil.addCookie(response, "JT_TICKET",ticket,7*24*60*60,"com.jt.jt.com");
            return SysResult.success();     //表示用户登录成功!!
        }
    }


    /**
     * 实现用户的退出操作.重定向到系统首页
     * url: http://www.jt.com/user/logout.html
     * 业务:
     *      1.删除Redis中的数据  key
     *      2.删除Cookie记录
     */
    @RequestMapping("logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //1.根据JT_TICKET获取指定的ticket
        String ticket =
                CookieUtil.getCookieValue(request,"JT_TICKET");
        //2.判断ticket是否为null
        if(!StringUtils.isEmpty(ticket)){
            jedisCluster.del(ticket);
            CookieUtil.deleteCookie(response,"JT_TICKET","com.jt.jt.com");
        }

        return "redirect:/";
    }





}
