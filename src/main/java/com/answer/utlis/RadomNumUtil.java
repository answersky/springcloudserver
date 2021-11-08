package com.answer.utlis;

import java.util.Random;

/**
 * @author liufeng
 * @data 2021/11/5 17:02
 */
public class RadomNumUtil {
    public static void main(String[] args) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        System.out.println(val);
    }
}
