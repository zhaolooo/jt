package com.jt.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("tb_user")
@Data
@Accessors(chain = true)
public class User extends BasePojo{     //必须实现序列化接口,父级代替完成

    @TableId(type = IdType.AUTO)//设定主键自增
    private Long id;            //用户ID号
    private String username;    //用户名
    private String password;    //密码 需要md5加密
    private String phone;       //电话号码
    private String email;       //暂时使用电话代替邮箱

}
