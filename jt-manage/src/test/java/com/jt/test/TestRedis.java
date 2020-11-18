package com.jt.test;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.*;
import redis.clients.jedis.params.SetParams;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TestRedis {

    /**
     * 1.测试redis程序链接是否正常
     * 步骤:
     *      1.实例化jedis工具API对象(host:port)
     *      2.根据实例 操作redis  方法就是命令
     *
     * 关于链接不通的说明:
     *      1.检查Linux防火墙
     *      2.检查Redis配置文件修改项
     *          2.1 IP绑定
     *          2.2 保护模式
     *          2.3 后台启动
     *      3.检查redis启动方式  redis-server redis.conf
     *      4.检查IP 端口 及redis是否启动...
     *
     *      */
    @Test
    public void test01(){
        String host = "192.168.126.129";
        int port = 6379;
        Jedis jedis = new Jedis(host,port);
        jedis.set("cgb2006","好好学习 天天向上");
        System.out.println(jedis.get("cgb2006"));

        //2.练习是否存在key
        if(jedis.exists("cgb2006")){
            jedis.del("cgb2006");
        }else{
            jedis.set("cgb2006", "xxxx");
            jedis.expire("cgb2006", 100);
        }
    }

    /**
     * 2.需求:
     *      1.向redis中插入数据  k-v
     *      2.为key设定超时时间  60秒后失效.
     *      3.线程sleep 3秒
     *      4.获取key的剩余的存活时间.
     *
     *   问题描述: 数据一定会被删除吗??????
     *   问题说明: 如果使用redis 并且需要添加超时时间时 一般需要满足原子性要求.
     *   原子性:   操作时要么成功 要么失败.但是必须同时完成.
     */
    @Test
    public void test02() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129",6379);
        jedis.setex("宝可梦", 60, "小火龙 妙蛙种子");
        System.out.println(jedis.get("宝可梦"));

       /* Jedis jedis = new Jedis("192.168.126.129",6379);
        jedis.set("宝可梦", "小火龙 妙蛙种子");
        int a = 1/0;    //可能会出异常
        jedis.expire("宝可梦", 60);
        Thread.sleep(3000);
        System.out.println(jedis.ttl("宝可梦"));*/
    }

    /**
     * 3.需求如果发现key已经存在时 不修改数据.如果key不存在时才会修改数据.
     *
     */
    @Test
    public void test03() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.setnx("aaaa", "测试nx的方法");
        /*if(jedis.exists("aaa")){
            System.out.println("key已经存在 不做修改");
        }else {
            jedis.set("aaa", "测试数据");
        }*/
        System.out.println(jedis.get("aaaa"));
    }

    /**
     * 需求:
     *  1.要求用户赋值时,如果数据存在则不赋值.  setnx
     *  2.要求在赋值操作时,必须设定超时的时间 并且要求满足原子性 setex
     */
    @Test
    public void test04() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        SetParams setParams = new SetParams();
        setParams.nx().ex(60);  //加锁操作
        String result = jedis.set("bbbb", "实现业务操作AAAA", setParams);
        System.out.println(result);
        System.out.println(jedis.get("bbbb"));
        jedis.del("bbbb");      //解锁操作


    }

    @Test
    public void testList() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        jedis.lpush("list", "1","2","3");
        System.out.println(jedis.rpop("list"));
    }

    @Test
    public void testTx() throws InterruptedException {
        Jedis jedis = new Jedis("192.168.126.129", 6379);
        //1.开启事务
        Transaction transaction = jedis.multi();
        try {
            transaction.set("aa", "aa");
            //提交事务
            transaction.exec();
        }catch (Exception e){
            e.printStackTrace();
            //回滚事务
            transaction.discard();
        }
    }

    /**
     * 测试Redis分片机制
     * 思考: shards 如何确定应该存储到哪台redis中呢???
     */
    @Test
    public void testShards(){
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("192.168.126.129",6379));
        shards.add(new JedisShardInfo("192.168.126.129",6380));
        shards.add(new JedisShardInfo("192.168.126.129",6381));
        //准备分片对象
        ShardedJedis shardedJedis = new ShardedJedis(shards);
        shardedJedis.set("shards","redis分片测试");
        System.out.println(shardedJedis.get("shards"));
    }


    /**
     * 测试Redis哨兵
     */
    @Test
    public void testSentinel(){
        Set<String> set = new HashSet<>();
        //1.传递哨兵的配置信息
        set.add("192.168.126.129:26379");
        JedisSentinelPool sentinelPool =
                new JedisSentinelPool("mymaster",set);
        //2.获取池中的资源
        Jedis jedis = sentinelPool.getResource();
        jedis.set("aa","哨兵测试");
        System.out.println(jedis.get("aa"));
        jedis.close();
    }

    /**
     * key 如何保存到redis中的???   3台主机
     * redis分区算法????
     */
    @Test
    public void testCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort("192.168.126.129", 7000));
        nodes.add(new HostAndPort("192.168.126.129", 7001));
        nodes.add(new HostAndPort("192.168.126.129", 7002));
        nodes.add(new HostAndPort("192.168.126.129", 7003));
        nodes.add(new HostAndPort("192.168.126.129", 7004));
        nodes.add(new HostAndPort("192.168.126.129", 7005));
        JedisCluster jedisCluster = new JedisCluster(nodes);
        jedisCluster.set("cluster", "集群的测试!!!!");
        System.out.println(jedisCluster.get("cluster"));
    }













}
