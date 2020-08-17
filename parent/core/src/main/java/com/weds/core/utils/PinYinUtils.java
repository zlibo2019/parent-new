package com.weds.core.utils;

import com.weds.core.utils.coder.Coder;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

public class PinYinUtils {
    public static String getPinYin(String str) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
        StringBuilder sb = new StringBuilder();
        char[] srcArray = str.toCharArray();
        try {
            for (char aSrcArray : srcArray) {
                // 判断是否为汉字字符
                if (Character.toString(aSrcArray).matches("[\\u4E00-\\u9FA5]+")) {
                    String[] targetArray = PinyinHelper.toHanyuPinyinStringArray(aSrcArray, format);
                    sb.append(targetArray[0]);
                } else {
                    sb.append(Character.toString(aSrcArray));
                }
            }
            return sb.toString();
        } catch (BadHanyuPinyinOutputFormatCombination e1) {
            e1.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 得到中文首字母,例如"专科"得到zk返回
     *
     * @param str 中文字符串
     * @return
     */
    public static String getPinYinHeadChar(String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char word = str.charAt(i);
            String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
            if (pinyinArray != null) {
                sb.append(pinyinArray[0].charAt(0));
            } else {
                sb.append(word);
            }
        }
        return sb.toString();
    }

    /**
     * 将字符串转移为ASCII码
     *
     * @param cnStr 中文字符串
     * @return
     */
    public static String getCnASCII(String cnStr) {
        StringBuilder sb = new StringBuilder();
        byte[] bGBK = cnStr.getBytes();
        for (int i = 0; i < bGBK.length; i++) {
            sb.append(Integer.toHexString(bGBK[i] & 0xff));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int aa = 2;
        System.out.println(aa<<7);
        // System.out.println(getPinYinHeadChar("支付账户充值"));
        // System.out.println(getPinYinHeadChar("虚拟类"));
        // System.out.println(getPinYinHeadChar("传统类"));
        // System.out.println(getPinYinHeadChar("实名类"));
        // System.out.println(getPinYinHeadChar("本行转账"));
        // System.out.println(getPinYinHeadChar("他行转账"));
        // System.out.println(getPinYinHeadChar("水费"));
        // System.out.println(getPinYinHeadChar("电费"));
        // System.out.println(getPinYinHeadChar("煤气费"));
        // System.out.println(getPinYinHeadChar("有线电视费"));
        // System.out.println(getPinYinHeadChar("通讯费"));
        // System.out.println(getPinYinHeadChar("物业费"));
        // System.out.println(getPinYinHeadChar("保险费"));
        // System.out.println(getPinYinHeadChar("行政费用"));
        // System.out.println(getPinYinHeadChar("税费"));
        // System.out.println(getPinYinHeadChar("学费"));
        // System.out.println(getPinYinHeadChar("其他"));
        // System.out.println(getPinYinHeadChar("基金"));
        // System.out.println(getPinYinHeadChar("理财产品"));
        // System.out.println(getPinYinHeadChar("其他"));
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
}
