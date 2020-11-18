package com.jt.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jt.pojo.Cart;
import org.apache.ibatis.annotations.Update;

public interface CartMapper extends BaseMapper<Cart> {
    @Update("update tb_cart set num = #{num},updated=now() where user_id=#{userId} and item_id=#{itemId}")
    void updateCartNum(Cart cart);
}
