package com.weds.dingtalk.util;

import com.weds.core.utils.coder.Coder;

import java.util.ArrayList;
import java.util.List;

public class DtUtils {
    public static String sign(String accessToken, String timestamp, String nonce, String encrypt) {
        List<String> list = new ArrayList<>();
        list.add(accessToken);
        list.add(timestamp);
        list.add(nonce);
        list.add(encrypt);
        list.sort(String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(s);
        }
        return Coder.sha1Hex(sb.toString());
    }

    public static String sign(String ticket, String nonceStr, long timeStamp, String url) {
        String plain = "jsapi_ticket=" + ticket + "&noncestr=" + nonceStr + "&timestamp=" + String.valueOf(timeStamp)
                + "&url=" + url;
        return Coder.sha1Hex(plain);
    }
}
