import com.answer.RunApplication;
import com.answer.signin.IDoSignService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * created by liufeng
 * 2020/12/15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = RunApplication.class)
public class SignTest {
    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private IDoSignService doSignService;

    @Test
    public void getSignInfo(){
        LocalDate today=LocalDate.of(2020,12,2);
        Jedis jedis=jedisPool.getResource();
        Map<String, Boolean> signInfo = new TreeMap<>(doSignService.getSignInfo(jedis,1000, today));
        for (Map.Entry<String, Boolean> entry : signInfo.entrySet()) {
            System.out.println(entry.getKey() + ": " + (entry.getValue() ? "√" : "-"));
        }
    }

    @Test
    public void countSeries(){
        LocalDate today=LocalDate.of(2020,12,2);
        Jedis jedis=jedisPool.getResource();
        long count = doSignService.getContinuousSignCount(jedis,1000, today);
        System.out.println("连续签到次数：" + count);
    }


    @Test
    public void testSetBit(){
        Jedis jedis=jedisPool.getResource();
        jedis.setbit("redisbitmap_test",0,true);
        jedis.setbit("redisbitmap_test",1,true);
        jedis.setbit("redisbitmap_test",2,true);
        jedis.setbit("redisbitmap_test",3,true);
        jedis.setbit("redisbitmap_test",4,false);
        jedis.setbit("redisbitmap_test",5,true);
        jedis.setbit("redisbitmap_test",6,true);
        //最终redis存放的二进制表示  1111 011
    }

    @Test
    public void getBit(){
        Jedis jedis=jedisPool.getResource();
        Long count=jedis.bitcount("redisbitmap_test");
        System.out.println(count);

        //0表示从第几位开始取 U表示无符号位  6表示取6位（从高位往地位取）
        List<Long> list = jedis.bitfield("redisbitmap_test", "GET", "u6", "0");  //111101   61
        System.out.println(list);
    }

    @Test
    public void getSignSeries(){
        int signCount=0;
        Jedis jedis=jedisPool.getResource();
        List<Long> list = jedis.bitfield("redisbitmap_test", "GET", "u9", "0");
        if (list != null && list.size() > 0) {
            // 取低位连续不为0的个数即为连续签到次数，需考虑当天尚未签到的情况
            long v = list.get(0) == null ? 0 : list.get(0);
            for (int i = 0; i < 6; i++) {
                if (v >> 1 << 1 == v) {
                    // 低位为0且非当天说明连续签到中断了
                    if (i > 0){
                        break;
                    }
                } else {
                    signCount += 1;
                }
                v >>= 1;
            }
        }
        System.out.println(signCount);
    }

    @Test
    public void testInfo(){
        Jedis jedis=jedisPool.getResource();
        Map<String, Boolean> signMap = new HashMap<>();
        String type = String.format("u%d", 7);
        List<Long> list = jedis.bitfield("redisbitmap_test", "GET", type, "0");  //????
        System.out.println(list);
        if (list != null && list.size() > 0) {
            // 由低位到高位，为0表示未签，为1表示已签
            long v = list.get(0) == null ? 0 : list.get(0);
            for (int i = 7; i > 0; i--) {
                signMap.put(i+"", v >> 1 << 1 != v);
                v >>= 1;  //?????
            }
        }
        for (Map.Entry<String, Boolean> entry : signMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + (entry.getValue() ? "√" : "-"));
        }
    }

}
