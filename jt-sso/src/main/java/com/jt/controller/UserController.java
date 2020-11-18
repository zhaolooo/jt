package com.jt.controller;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.CookieUtil;
import com.jt.vo.SysResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JedisCluster jedisCluster;
    /**
     * 完成测试按钮
     * 1.url地址 :findUserAll
     * 2.参数信息: null
     * 3.返回值结果: List<User>
     *
     */
    @RequestMapping("/findUserAll")
    public List<User> findUserAll(){

        return userService.findUserAll();
    }

    /**
     * 业务说明: com.jt.jt-web服务器获取jt-sso数据 JSONP跨域请求
     * url地址: http://sso.jt.com/user/check/{param}/{type}
     * 参数:    param: 需要校验的数据   type:校验的类型
     * 返回值:  SysResult对象
     * 真实的返回值: callback(SysResult的JSON)
     */
    @RequestMapping("/check/{param}/{type}")
    public JSONPObject checkUser(@PathVariable String param,
                                 @PathVariable Integer type,
                                 String callback){
        //true 表示数据存在     false 表示数据可以使用
        boolean flag = userService.checkUser(param,type);
        SysResult.success(flag);
        return new JSONPObject(callback, SysResult.success(flag));
    }

    /**
     * 业务实现:
     *  1.用户通过cookie信息查询用户数据.    通过ticket获取redis中的业务数据.
     *  2.url请求: http://sso.jt.com/user/query/+ _ticket
     *  3.参数:    参数在url中. 利用restFul获取
     *  4.返回值要求: SysResult对象(userJSON)
     */
    @RequestMapping("/query/{ticket}")
    public JSONPObject findUserByTicket(@PathVariable String ticket,
                                        HttpServletResponse response,
                                        String callback){
        String userJSON = jedisCluster.get(ticket);
        //1.lru算法清空数据   2.有可能cookie信息有误
        if(StringUtils.isEmpty(userJSON)){
            //2.应该删除cookie信息.
           /* Cookie cookie = new Cookie("JT_TICKET", "");
            cookie.setMaxAge(0);
            cookie.setDomain("com.jt.jt.com");
            cookie.setPath("/");
            response.addCookie(cookie);*/
            CookieUtil.deleteCookie(response, "JT_TICKET", "com.jt.jt.com");
            return new JSONPObject(callback,SysResult.fail());
        }
        return new JSONPObject(callback,SysResult.success(userJSON));
    }







}
