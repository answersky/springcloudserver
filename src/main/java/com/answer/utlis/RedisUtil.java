package com.answer.utlis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;

/**
 * created by liufeng
 * 2021/10/14
 */
public class RedisUtil {

    /*
     * list的push操作（事件入队列）
     * @Param String key list的名字，即key
     * @Param String value 将要放入的值value
     */
    public static long lpush(String key, String value){
        JedisPool jedisPool=null;
        Jedis jedis = null;
        try {
            jedisPool=initPool();
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e){
            return -1;
        } finally {
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool!=null){
                jedisPool.close();
            }
        }
    }

    /*
     * list的brpop操作
     * @Param int timeout 超时时间
     * @Param String key list对应的key
     * @reutrn List<String> 返回list的名字key和对应的元素
     */
    public static List<String> brpop(int timeout, String key){
        JedisPool jedisPool=null;
        Jedis jedis = null;
        try {
            jedisPool=initPool();
            jedis = jedisPool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e){
            return null;
        } finally {
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool!=null){
                jedisPool.close();
            }
        }
    }

    public static String rpop(String key){
        JedisPool jedisPool=null;
        Jedis jedis = null;
        try {
            jedisPool=initPool();
            jedis = jedisPool.getResource();
            return jedis.rpop(key);
        } catch (Exception e){
            return null;
        } finally {
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool!=null){
                jedisPool.close();
            }
        }
    }


    private static JedisPool initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxWaitMillis(1000);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(false);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 300000);
        return jedisPool;
    }

}
