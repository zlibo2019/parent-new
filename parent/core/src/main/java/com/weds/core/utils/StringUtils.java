package com.weds.core.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.FastDateFormat;

import java.time.Instant;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 字符串工具类, 继承org.apache.commons.lang3.StringUtils类
 */
public class StringUtils extends org.apache.commons.lang3.StringUtils {

    private static final FastDateFormat pattern = FastDateFormat.getInstance("yyyyMMddHHmmss");
    private static final AtomicInteger atomicInteger = new AtomicInteger(0);
    private static ThreadLocal<StringBuilder> threadLocal = new ThreadLocal<>();

    /**
     * 获取唯一ID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().toUpperCase()
                .replaceAll("-", "");
    }

    public static String getUUIDHash() {
        int hashCodeV = UUID.randomUUID().toString().hashCode();
        return String.format("%010d", Math.abs(hashCodeV));
    }

    /**
     * 拆分以给定分隔符分隔的字符串,并存入字符串数组中
     *
     * @param sSource 源字符串
     * @param sChar   分隔符
     * @return String[]
     */
    public static String[] stringToArray(String sSource, String sChar) {
        String aReturn[];
        StringTokenizer st;
        st = new StringTokenizer(sSource, sChar);
        int i = 0;
        aReturn = new String[st.countTokens()];
        while (st.hasMoreTokens()) {
            aReturn[i] = st.nextToken();
            i++;
        }
        return aReturn;
    }

    public static String stringToAscii(String value) {
        StringBuffer sbu = new StringBuffer();
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (i != chars.length - 1) {
                sbu.append((int) chars[i]).append(",");
            } else {
                sbu.append((int) chars[i]);
            }
        }
        return sbu.toString();
    }

    public static String asciiToString(String value) {
        StringBuffer sbu = new StringBuffer();
        String[] chars = value.split(",");
        for (int i = 0; i < chars.length; i++) {
            sbu.append((char) Integer.parseInt(chars[i]));
        }
        return sbu.toString();
    }

    public static String getOrderNumberC(String lock) {
        StringBuilder builder = new StringBuilder(pattern.format(Instant.now().toEpochMilli()));// 取系统当前时间作为订单号前半部分
        builder.append(Math.abs(lock.hashCode()));// HASH-CODE
        builder.append(atomicInteger.getAndIncrement());// 自增顺序
        threadLocal.set(builder);
        return threadLocal.get().toString();
    }

    public static String getOrderNumberD(String lock) {
        StringBuilder builder = new StringBuilder(ThreadLocalRandom.current().nextInt(0, 999));// 随机数
        builder.append(Math.abs(lock.hashCode()));// HASH-CODE
        builder.append(atomicInteger.getAndIncrement());// 自增顺序
        threadLocal.set(builder);
        return threadLocal.get().toString();
    }

    public static String getOrderNumber(String head, int random) {
        StringBuilder builder = new StringBuilder();
        if (!StringUtils.isBlank(head)) {
            builder.append(head);
        }
        builder.append(pattern.format(Instant.now().toEpochMilli()));
        builder.append(RandomStringUtils.randomNumeric(random));// HASH-CODE
        // builder.append(atomicInteger.getAndIncrement());// 自增顺序
        threadLocal.set(builder);
        return threadLocal.get().toString();
    }

    public static int incrementAndGet() {
        int current;
        int next;
        do {
            current = atomicInteger.get();
            //2147483647
            next = current >= 99 ? 1 : current + 1;
        } while (!atomicInteger.compareAndSet(current, next));

        return next;
    }

    public static int decrementAndGet() {
        int current;
        int next;
        do {
            current = atomicInteger.get();
            next = current <= 1 ? 99 : current - 1;
        } while (!atomicInteger.compareAndSet(current, next));

        return next;
    }
}
