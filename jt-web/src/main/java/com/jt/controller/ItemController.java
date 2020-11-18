package com.jt.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;
import com.jt.service.DubboItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/items")
public class ItemController {

    @Reference(timeout = 3000,check = false) //消费者启动时不会校验是否有提供者.
    private DubboItemService itemService;

    /**
     * 实现商品详情页面跳转
     * url: http://www.jt.com/items/562379.html
     * 参数: 562379 itemId
     * 返回值:  item.jsp页面
     * 页面取值说明:
     *      ${item.title }   item对象
     *      ${itemDesc.itemDesc }  itemDesc对象
     *
     * 思路:
     *      1.重构jt-manage项目
     *      2.创建中立接口DubboItemService
     *      3.实现业务调用获取item/itemDesc对象
     */
    @RequestMapping("/{itemId}")
    public String findItemById(@PathVariable Long itemId, Model model){

        Item item = itemService.findItemById(itemId);
        ItemDesc itemDesc = itemService.findItemDescById(itemId);
        //将数据保存到request域中
        model.addAttribute("item",item);
        model.addAttribute("itemDesc",itemDesc);
        return "item";
    }
}
