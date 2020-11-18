package com.jt.service;

import com.jt.pojo.Cart;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DubboCartService {

    List<Cart> findCartListByUserId(Long userId);
    @Transactional
    void updateCartNum(Cart cart);
    @Transactional
    void saveCart(Cart cart);

    void deleteCarts(Long userId, Long itemId);
}
