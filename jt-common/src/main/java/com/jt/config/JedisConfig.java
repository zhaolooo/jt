package com.jt.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import redis.clients.jedis.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class JedisConfig {

    @Value("${redis.clusters}")
    private String clusters;

    @Bean
    public JedisCluster jedisCluster(){
        Set<HostAndPort> nodes = new HashSet<>();
        String[] nodesArray = clusters.split(",");
        for (String node : nodesArray){
            String host = node.split(":")[0];
            int port = Integer.parseInt(node.split(":")[1]);
            HostAndPort hostAndPort = new HostAndPort(host,port);
            nodes.add(hostAndPort);
        }
        return new JedisCluster(nodes);
    }







    /*@Value("${redis.sentinel}")
    private String sentinel;        //暂时只有单台

    @Bean
    public JedisSentinelPool jedisSentinelPool(){

        Set<String> sentinels = new HashSet<>();
        sentinels.add(sentinel);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(100);   //最大链接数量
        jedisPoolConfig.setMaxIdle(40);     //最大空闲数量
        jedisPoolConfig.setMinIdle(20);     //最小空闲数量
        return new JedisSentinelPool("mymaster",sentinels,jedisPoolConfig);
    }*/






  /*  @Value("${redis.nodes}")
    private String nodes;  //node,node,node.....

    //配置redis分片机制
    @Bean
    public ShardedJedis shardedJedis(){
        nodes = nodes.trim();   //去除两边多余的空格
        List<JedisShardInfo> shards = new ArrayList<>();
        String[] nodeArray = nodes.split(",");
        for (String strNode : nodeArray){   //strNode = host:port
            String host = strNode.split(":")[0];
            int port = Integer.parseInt(strNode.split(":")[1]);
            JedisShardInfo info = new JedisShardInfo(host, port);
            shards.add(info);
        }
        return new ShardedJedis(shards);
    }*/




   /* @Value("${redis.host}")
    private String host;
    @Value("${redis.port}")
    private Integer port;

    @Bean
    public Jedis jedis(){

        return new Jedis(host,port);
    }*/
}
