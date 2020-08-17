package com.weds.core.utils;

import java.io.UnsupportedEncodingException;

public class UCS2Utils {
    /**
     * UCS2解码
     *
     * @param src UCS2 源串
     * @return 解码后的UTF-16BE字符串
     */
    public static String DecodeUCS2(String src, String charsetName) throws UnsupportedEncodingException {
        byte[] bytes = new byte[src.length() / 2];

        for (int i = 0; i < src.length(); i += 2) {
            bytes[i / 2] = (byte) (Integer.parseInt(src.substring(i, i + 2), 16));
        }

        String reValue;
        reValue = new String(bytes, charsetName);
        return reValue;
    }

    /**
     * UCS2编码
     *
     * @param src UTF-16BE编码的源串
     * @return 编码后的UCS2串
     */
    public static String EncodeUCS2(String src, String charsetName) throws UnsupportedEncodingException {
        byte[] bytes;
        StringBuffer reValue = new StringBuffer();
        StringBuffer tem = new StringBuffer();
        bytes = src.getBytes(charsetName);

        for (int i = 0; i < bytes.length; i++) {
            tem.delete(0, tem.length());
            tem.append(Integer.toHexString(bytes[i] & 0xFF));
            if (tem.length() == 1) {
                tem.insert(0, '0');
            }
            reValue.append(tem);
        }
        return reValue.toString().toUpperCase();
    }
}
