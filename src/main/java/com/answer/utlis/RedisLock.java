package com.answer.utlis;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * created by liufeng
 * 2020/4/24
 * redis实现分布式锁
 */
@Component
public class RedisLock {
    @Autowired
    private JedisPool jedisPool;

    @Resource
    private JedisUtil jedisUtil;

    /**
     * 获取锁
     * @param timeout 锁的有效时间
     * @param acquireTime  获取锁这个过程的超时时间
     * @param lockKey  锁key
     * @return
     */
    public boolean getLock(long timeout,long acquireTime,String lockKey){
        try {
            String threadId = String.valueOf(Thread.currentThread().getId());
            System.out.println(threadId+"  开始获取锁");
            long current=System.currentTimeMillis();
            while (true){
                //判断程序获取锁是否超时
                if(System.currentTimeMillis()>current+acquireTime){
                    System.out.println(threadId+"  开始获取锁超时");
                    break;
                }

                //setnx==1加锁，如果key已经存在则加锁失败返回0
                if(jedisUtil.setnx(lockKey,threadId)==1){
                    System.out.println(threadId+"  获取锁成功");
                    //加锁后给锁设置一个过期时间=当前时间+过期超时时间
                    Integer expireTime=Integer.parseInt(String.valueOf((timeout)/1000));
                    jedisUtil.expire(lockKey,expireTime);
                    return true;
                }

                //防止redis在执行完setnx后宕机了，没有给锁设置过期时间
                if(jedisUtil.ttl(lockKey)==-1){
                    Integer expireTime=Integer.parseInt(String.valueOf((timeout)/1000));
                    jedisUtil.expire(lockKey,expireTime);
                }

                try {
                    System.out.println(threadId+"  未获取到锁，等待继续获取锁......");
                    //再未获取到锁时休眠20ms后再继续获取锁
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void releaseLock(String lockKey){
        try {
            //判断锁是否过期
            String threadId = String.valueOf(Thread.currentThread().getId());
            //判断当前线程id是否跟锁一样，一样表示是自己获取的锁
            String lockValue=jedisUtil.get(lockKey);
            if(threadId.equals(lockValue)){
                System.out.println(threadId+"  释放锁");
                jedisUtil.del(lockKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
