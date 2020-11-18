package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.hash.HashMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService{

    private static Map<Integer,String> columnMap = new HashMap();
    static {
        columnMap.put(1, "username");
        columnMap.put(2, "phone");
        columnMap.put(3, "email");
    }

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> findUserAll() {

        return userMapper.selectList(null);
    }

    /**
     * 判断依据:  根据用户名查询 如果结果>0 用户已存在.
     * @param param
     * @param type
     * @return
     */
    @Override
    public boolean checkUser(String param, Integer type) {
        //1.需要将type类型转化为 具体字段信息  1=username  2=phone  3=email
        String column = columnMap.get(type);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(column, param);
        Integer count = userMapper.selectCount(queryWrapper);
        return  count > 0 ? true :false;
    }
}
