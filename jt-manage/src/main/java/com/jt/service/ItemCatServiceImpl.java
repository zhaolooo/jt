package com.jt.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.anno.CacheFind;
import com.jt.mapper.ItemCatMapper;
import com.jt.pojo.ItemCat;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.EasyUITree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService{

    @Autowired
    private ItemCatMapper itemCatMapper;
    @Autowired(required = false) //保证后续操作正常执行 可以懒加载
    private Jedis jedis;

    @Override
    public ItemCat findItemCatById(Long itemCatId) {

        return itemCatMapper.selectById(itemCatId);
    }

    /**
     * 返回值:  List<EasyUITree> 集合信息
     * 数据库查询返回值:  List<ItemCat>
     * 数据类型必须手动的转化
     * @param parentId
     * @return
     */
    @Override
    @CacheFind(preKey = "ITEM_CAT_PARENTID")
    public List<EasyUITree> findItemCatList(Long parentId) {
        //1.查询数据库记录
        QueryWrapper<ItemCat> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId);
        List<ItemCat> catList = itemCatMapper.selectList(queryWrapper);

        //2.需要将catList集合转化为voList  一个一个转化
        List<EasyUITree> treeList = new ArrayList<>();
        for(ItemCat itemCat :catList){
            Long id = itemCat.getId();
            String name = itemCat.getName();
            //如果是父级 应该closed   如果不是父级 应该open
            String state = itemCat.getIsParent()?"closed":"open";
            EasyUITree tree = new EasyUITree(id, name, state);
            treeList.add(tree);
        }
        return treeList;
    }


    @Override
    public List<EasyUITree> findItemCatListCache(Long parentId) {
        //0.定义公共的返回值对象
        List<EasyUITree> treeList = new ArrayList<>();
        //1.定义key
        String key = "ITEM_CAT_PARENTID::"+parentId;
        //2.检索redis服务器,是否含有该key

        //记录时间
        Long startTime = System.currentTimeMillis();
        if(jedis.exists(key)){
            //数据存在
            String json = jedis.get(key);
            Long endTime = System.currentTimeMillis();
            //需要将json串转化为对象
            treeList = ObjectMapperUtil.toObj(json,treeList.getClass());
            System.out.println("从redis中获取数据 耗时:"+(endTime-startTime)+"毫秒");
        }else{
            //3.数据不存在  查询数据库
            treeList = findItemCatList(parentId);
            Long endTime = System.currentTimeMillis();
            //3.将数据保存到缓存中
            String json = ObjectMapperUtil.toJSON(treeList);
            jedis.set(key, json);
            System.out.println("查询数据库 耗时:"+(endTime-startTime)+"毫秒");
        }
        return treeList;
    }
}
