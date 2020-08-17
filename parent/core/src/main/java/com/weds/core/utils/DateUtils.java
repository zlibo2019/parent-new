package com.weds.core.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类，继承org.apache.commons.lang3.time.DateUtils工具类
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils implements
        Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DATE_TIME_SIMPLE_FORMAT = "yyyyMMddHHmmss";
    public static final String DATE_SIMPLE_FORMAT = "yyyyMMdd";
    public static final String TIME_SIMPLE_FORMAT = "HHmmss";

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String TIME_FORMAT = "HH:mm:ss";

    private static String[] parsePatterns = {
            "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm", "yyyy-MM",
            "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM",
            "yyyy.MM.dd", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM"};

    public static Date parseDate(Object str) {
        if (str == null) {
            return null;
        }
        try {
            return parseDate(str.toString(), parsePatterns);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * @param date
     * @param sdf
     * @return
     */
    public static String formatDate(Date date, String sdf) {
        return DateFormatUtils.format(date, sdf);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateTime(Date date) {
        return DateFormatUtils.format(date, DATE_TIME_FORMAT);
    }

    /**
     * @return
     */
    public static Date getCurrentDateTimeZero() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param date
     * @return
     */
    public static Date getDateTimeZero(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param date
     * @param sdf
     * @return
     * @throws ParseException
     */
    public static Date getDateTimeZero(String date, String sdf) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parseDate(date, sdf));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * @param days
     * @return
     */
    public static Date getDateTimeZero(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        addDays(calendar.getTime(), days);
        return calendar.getTime();
    }

    /**
     * @return
     */
    public static String getCurrentDateTimeSimple() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), DATE_TIME_SIMPLE_FORMAT);
    }

    /**
     * @return
     */
    public static String getCurrentDateSimple() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), DATE_SIMPLE_FORMAT);
    }

    /**
     * @return
     */
    public static String getCurrentTimeSimple() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), TIME_SIMPLE_FORMAT);
    }

    /**
     * @param sdf
     * @return
     */
    public static String getCurrentDateTime(String sdf) {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), sdf);
    }

    /**
     * @return
     */
    public static String getCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), DATE_TIME_FORMAT);
    }

    /**
     * @return
     */
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), DATE_FORMAT);
    }

    /**
     * @return
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), TIME_FORMAT);
    }

    /**
     * 得到当前年份字符串 格式（yyyy）
     */
    public static String getYear() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), "yyyy");
    }

    /**
     * 得到当前月份字符串 格式（MM）
     */
    public static String getMonth() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), "MM");
    }

    /**
     * 得到当天字符串 格式（dd）
     */
    public static String getDay() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), "dd");
    }

    /**
     * 得到当前星期字符串 格式（E）星期几
     */
    public static String getWeek() {
        Calendar calendar = Calendar.getInstance();
        return formatDate(calendar.getTime(), "E");
    }

    public static int getCurrWeekNum() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int getWeekNum(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param before
     * @param after
     * @return
     */
    public static long getDateDiffDay(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / (1000 * 60 * 60 * 24);
    }

    public static long getDateDiffSecond(Date before, Date after) {
        long beforeTime = before.getTime();
        long afterTime = after.getTime();
        return (afterTime - beforeTime) / 1000;
    }

    public static int dateToSecond(String time) {
        String[] temp = StringUtils.stringToArray(time, ":");
        return Integer.parseInt(temp[0]) * 3600 + Integer.parseInt(temp[1]) * 60 + Integer.parseInt(temp[2]);
    }
}
