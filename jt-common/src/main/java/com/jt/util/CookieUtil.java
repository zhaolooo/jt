package com.jt.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieUtil {

    //1.新增cookie
    public static void addCookie(HttpServletResponse response,String cookieName, String cookieValue, int seconds, String domain){
        Cookie cookie = new Cookie(cookieName,cookieValue);
        cookie.setMaxAge(seconds);
        cookie.setDomain(domain);
        cookie.setPath("/");
        response.addCookie(cookie);
    }


    //2.根据name查询value的值
    public static String getCookieValue(HttpServletRequest request,String cookieName){

        Cookie[] cookies = request.getCookies();
        if(cookies !=null && cookies.length >0){
            for (Cookie cookie : cookies){
                if(cookieName.equals(cookie.getName())){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }



    //3.删除cookie
    public static void deleteCookie(HttpServletResponse response,String cookieName,String domain){

        addCookie(response,cookieName,"",0, domain);
    }
}
