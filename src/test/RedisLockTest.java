import com.answer.RunApplication;
import com.answer.utlis.JedisUtil;
import com.answer.utlis.RedisLock;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by liufeng
 * 2020/4/24
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RunApplication.class)
public class RedisLockTest {
    @Autowired
    private RedisLock redisLock;
    @Resource
     private JedisUtil jedisUtil;
    @Autowired
    private JedisPool jedisPool;

    int count=0;
    @Test
    public void testRedisLock(){
        ExecutorService executorService= Executors.newFixedThreadPool(3);
        for(int i=1;i<=100;i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    String threadName = Thread.currentThread().getName();
                    Long threadId = Thread.currentThread().getId();
                    boolean flag=redisLock.getLock(50L,5000L,"count_key");
                    System.out.println("线程_"+threadId+":"+flag);
                    if(flag){
                        count++;
                        System.out.println(threadName+"["+threadId+"]"+"执行完的结果"+count);
                    }
                    redisLock.releaseLock("count_key");
                }
            });
        }
        executorService.shutdown();
    }

    @Test
    public void testReids(){
        Jedis jedis=jedisPool.getResource();
//        Long va=jedisUtil.setnx(jedis,"count_key1","123");
//        System.out.println(va);
        String key=jedisUtil.get("count_key1");
        System.out.println(key);
        jedis.close();
    }
}
