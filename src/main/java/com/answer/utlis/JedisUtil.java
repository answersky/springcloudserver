package com.answer.utlis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.ReentrantLock;

/**
 * created by liufeng
 * 2020/4/23
 */
@Component
public class JedisUtil {
    private static ReentrantLock lockJedis = new ReentrantLock();

    private static JedisPool jedisPool = null;

    public Jedis getJedis(){
        lockJedis.lock();
        Jedis jedis = null;
        try {
            jedisPool=initPool();
            jedis = jedisPool.getResource();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockJedis.unlock();
        }
        return jedis;
    }

    private JedisPool initPool(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(10);
        jedisPoolConfig.setMaxWaitMillis(1000);
        // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
        jedisPoolConfig.setBlockWhenExhausted(false);
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, "127.0.0.1", 6379, 300000);
        return jedisPool;
    }

    private void releaseJedis(Jedis jedis){
        if(jedis!=null && jedisPool!=null){
            jedisPool.returnResource(jedis);
        }
    }

    /**
     * 通过key获取储存在redis中的value
     * 并释放连接
     * @param key
     * @return 成功返回value 失败返回null
     */
    public synchronized String get(String key) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
            return jedis.get(key);
    }

    public synchronized String set(String key,String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.set(key,value);
    }

    /**
     * <p>
     * 设置key value,如果key已经存在则返回0,nx==> not exist
     * </p>
     *
     * @param key
     * @param value
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public synchronized Long setnx(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.setnx(key, value);
    }

    /**
     * <p>
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)。
     * </p>
     * <p>
     * 当 key 存在但不是字符串类型时，返回一个错误。
     * </p>
     *
     * @param key
     * @param value
     * @return 返回给定 key 的旧值。当 key 没有旧值时，也即是， key 不存在时，返回 nil
     */
    public synchronized String getSet(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.getSet(key, value);
    }

    public synchronized Long del(String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.del(key);
    }

    /**
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * @param key
     * @param value
     *            过期时间，单位：秒
     * @return 成功返回1 如果存在 和 发生异常 返回 0
     */
    public synchronized Long expire(String key, int value) {
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.expire(key, value);
    }

    public synchronized Long ttl(String key){
        Jedis jedis = null;
        try {
            jedis = getJedis();
            if (jedis == null) {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            releaseJedis(jedis);
        }
        return jedis.ttl(key);
    }

}
