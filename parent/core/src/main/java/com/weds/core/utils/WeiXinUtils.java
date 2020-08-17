package com.weds.core.utils;

import com.weds.core.constants.WeiXinConstants;
import com.weds.core.utils.coder.Coder;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.*;

import static org.apache.commons.codec.digest.MessageDigestAlgorithms.MD5;

public class WeiXinUtils {
    private static final String SYMBOLS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new SecureRandom();

    /**
     * 获取随机数
     *
     * @return
     */
    public static String createNonceStr() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    /**
     * 签名算法 用于调js sdk
     *
     * @param jsapiTicket 用于签名的 jsapiTicket
     * @param url         用于签名的 url ，注意必须动态获取，不能 hardcode
     * @return
     */
    public static Map<String, String> sign(String jsapiTicket, String url) {
        String nonceStr = createNonceStr();//随机数
        String timestamp = createTimestamp();//时间戳
        //注意这里参数名必须全部小写，且必须有序
        String data = "jsapi_ticket=" + jsapiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timestamp +
                "&url=" + url;
        String signature = Coder.sha1Hex(data);

        Map<String, String> ret = new HashMap<String, String>();
        ret.put("url", url);
        ret.put("jsapi_ticket", jsapiTicket);
        ret.put("nonceStr", nonceStr);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);
        return ret;
    }

    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        char[] nonceChars = new char[32];
        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

    /**
     * 用于微信充值 同一订单接口的签名
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, WeiXinConstants.SignType signType) throws Exception {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals("sign")) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (WeiXinConstants.SignType.MD5.equals(signType)) {
            return Coder.md5Hex(sb.toString()).toUpperCase();
        } else if (WeiXinConstants.SignType.HMACSHA256.equals(signType)) {
            return HMACSHA256(sb.toString(), key);
        } else {
            throw new Exception(String.format("Invalid sign_type: %s", signType));
        }
    }

    /**
     * 生成 HMACSHA256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     * @throws Exception
     */
    public static String HMACSHA256(String data, String key) throws Exception {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        byte[] array = sha256_HMAC.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte item : array) {
            sb.append(Integer.toHexString((item & 0xFF) | 0x100), 1, 3);
        }
        return sb.toString().toUpperCase();
    }


    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean isSignatureValid(Map<String, String> data, String key,
                                           WeiXinConstants.SignType signType) throws Exception {
        if (!data.containsKey(WeiXinConstants.FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(WeiXinConstants.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }
}
