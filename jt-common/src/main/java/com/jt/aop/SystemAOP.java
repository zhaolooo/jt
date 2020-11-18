package com.jt.aop;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.vo.SysResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;

//@ControllerAdvice  //拦截controller层
//@ResponseBody
@RestControllerAdvice   //定义全局异常的处理类   AOP=异常通知
public class SystemAOP {

    /**
     *  定义全局异常的方法  当遇到了什么异常时,程序开始执行   参数一般class类型
     *  如果一旦发生异常,则应该输出异常的信息,之后返回错误数据即可.
     *
     *  解决跨域全局异常处理的规则: 京淘项目的跨域都是使用JSONP.   http://xxxx?callback=xxxxx
     *  如果请求中携带了callback参数 则认为是JSONP跨域请求.
     *  难点: 如何获取callback参数呢??/
     */
    @ExceptionHandler({RuntimeException.class})
    public Object systemAop(Exception e, HttpServletRequest request){
        e.printStackTrace();
        String callback  = request.getParameter("callback");
        if(StringUtils.isEmpty(callback)){
            //常规方法调用方式
            return SysResult.fail();
        }else{
            //证明是jsonp跨域请求
            return new JSONPObject(callback, SysResult.fail());
        }
    }
}
