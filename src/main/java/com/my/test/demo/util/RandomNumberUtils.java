package com.my.test.demo.util;

import java.util.Random;
/**
 * 支付服务工具类
 *
 * @author TokyoCold
 */

public class RandomNumberUtils {

    private static final String baseString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int baseStringLength = baseString.length();

    public static String generateRandomString(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(baseString.charAt(r.nextInt(baseStringLength)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(generateRandomString(8));
    }
}
