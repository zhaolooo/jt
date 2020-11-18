package com.jt.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

@Service
public class DubboCartServiceImpl implements DubboCartService{

    @Autowired
    private CartMapper cartMapper;


    @Override
    public List<Cart> findCartListByUserId(Long userId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        return cartMapper.selectList(queryWrapper);
    }

    @Override
    public void updateCartNum(Cart cart) {

        //cartMapper.updateCartNum(cart);
        //1.准备修改的数据  根据对象中不为null的元素当做set条件
        Cart cartTemp = new Cart();
        cartTemp.setNum(cart.getNum());

        //2.根据对象中不为null的元素当做where条件
        UpdateWrapper<Cart> updateWrapper =
                        new UpdateWrapper<>(cart.setNum(null));
        /*updateWrapper.eq("user_id", cart.getUserId())
                     .eq("item_id", cart.getItemId());*/
        cartMapper.update(cartTemp,updateWrapper);

    }

    /**
     *  如果重复加购则更新数量
     * 1.查询是否已经有改数据 user_id/item_id
     */
    @Override
    public void saveCart(Cart cart) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", cart.getUserId())
                    .eq("item_id", cart.getItemId());
        Cart cartDB = cartMapper.selectOne(queryWrapper);
        if(cartDB == null){
            //用户第一次加购
            cartMapper.insert(cart);
        }else {
            //用户需要修改数量
            int num = cart.getNum() + cartDB.getNum();
            cart.setNum(num);
            cartMapper.updateCartNum(cart);
        }
    }

    @Override
    public void deleteCarts(Long userId, Long itemId) {
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("item_id", itemId);
        cartMapper.delete(queryWrapper);
    }

    /*private ThreadLocal<Integer> threadLocal = new ThreadLocal<>();
    public void a(){
        int a = 100;
        threadLocal.set(a);
        b();
    }

    public void b(){
        int a = threadLocal.get();
        int b  = 100*a;
    }*/












}
