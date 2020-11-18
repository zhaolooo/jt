package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import redis.clients.jedis.JedisCluster;

import java.util.UUID;

@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService{

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JedisCluster jedisCluster;


    /**
     * 注意事项:
     *  1.暂时使用电话号码代替邮箱
     *  2.密码进行md5加密.
     *  3.入库操作注意事务控制
     * @param user
     */

    @Override
    public void saveUser(User user) {
        String md5Pass =
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setEmail(user.getPhone())
            .setPassword(md5Pass);
        userMapper.insert(user);
    }

    /**
     * 1.获取用户信息校验数据库中是否有记录
     * 2.有  开始执行单点登录流程
     * 3.没有 直接返回null即可
     * @param user
     * @return
     */
    @Override
    public String doLogin(User user) {  //username/password
        //1.将明文加密
        String md5Pass =
                DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Pass);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>(user);
        //根据对象中不为null的属性当做where条件.
        User userDB = userMapper.selectOne(queryWrapper);
        if(userDB == null){
            //用户名或密码错误
            return null;
        }else{ //用户名和密码正确  实现单点登录操作
            String ticket = UUID.randomUUID().toString();
            //如果将数据保存到第三方 一般需要脱敏处理
            userDB.setPassword("123456你信不??");
            String userJSON = ObjectMapperUtil.toJSON(userDB);
            jedisCluster.setex(ticket, 7*24*60*60, userJSON);
            return ticket;
        }
    }
}
