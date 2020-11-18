package com.jt.thread;

import com.jt.pojo.User;

public class UserThreadLocal {

    //static不会影响影响线程  threadLocal创建时跟随线程.
    //private static ThreadLocal<Map<k,v>> threadLocal = new ThreadLocal<>();
    private static ThreadLocal<User> threadLocal = new ThreadLocal<>();

    public static void set(User user){

        threadLocal.set(user);
    }

    public static User get(){

        return threadLocal.get();
    }

    public static void remove(){

        threadLocal.remove();
    }

}
