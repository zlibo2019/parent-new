package com.weds.core.utils.coder;

import com.weds.core.utils.StringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @Author sxm
 * @Description DES对称加密算法
 * @Date 2018年3月22日
 */
public class AESCoder extends Coder {
    /**
     * 密钥算法
     */
    private static final String KEY_ALGORITHM = "AES";

    /**
     * 加密/解密算法 / 工作模式 / 填充方式
     * Java 6支持PKCS5Padding填充方式
     * Bouncy Castle支持PKCS7Padding填充方式
     */
    public static final String CIPHER_ALGORITHM_5P = "AES/CBC/PKCS5Padding";

    public static final String CIPHER_ALGORITHM_NP = "AES/CBC/NoPadding";

    /**
     * 生成密钥
     *
     * @return 密钥
     * @throws Exception
     */
    public static String generateKey(String password) throws Exception {
        //实例化密钥生成器
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        /**
         * 设置AES密钥长度
         * AES要求密钥长度为128位或192位或256位，java默认限制AES密钥长度最多128位
         * 如需192位或256位，则需要到oracle官网找到对应版本的jdk下载页，在"Additional Resources"中找到
         * "Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files",点击[DOWNLOAD]下载
         * 将下载后的local_policy.jar和US_export_policy.jar放到jdk安装目录下的jre/lib/security/目录下，替换该目录下的同名文件
         */
        if (StringUtils.isBlank(password)) {
            kg.init(128);
        } else {
            kg.init(128, new SecureRandom(password.getBytes(StandardCharsets.UTF_8)));
        }
        //生成密钥
        SecretKey secretKey = kg.generateKey();
        //获得密钥的字符串形式
        return encryptBASE64(secretKey.getEncoded());
    }

    /**
     * AES加密
     *
     * @param source 源字符串
     * @param key    密钥
     * @return 加密后的密文
     * @throws Exception
     */
    public static String encrypt(String source, String key, String iv) throws Exception {
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = key.getBytes();
        return encrypt(sourceBytes, keyBytes, iv.getBytes(StandardCharsets.UTF_8), CIPHER_ALGORITHM_5P);
    }

    public static String encryptBase64(String source, String key, int iv, String cipherType) throws Exception {
        byte[] sourceBytes = source.getBytes(StandardCharsets.UTF_8);
        byte[] keyBytes = Base64.decodeBase64(key);
        return encrypt(sourceBytes, keyBytes, Arrays.copyOfRange(keyBytes, 0, iv), cipherType);
    }

    public static String encrypt(byte[] sourceBytes, byte[] keyBytes, byte[] ivBytes, String cipherType) throws Exception {
        Cipher cipher = Cipher.getInstance(cipherType);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM),
                new IvParameterSpec(ivBytes));
        byte[] decrypted = cipher.doFinal(sourceBytes);
        return Base64.encodeBase64String(decrypted);
    }

    /**
     * AES解密
     *
     * @param encryptStr 加密后的密文
     * @param key        密钥
     * @return 源字符串
     * @throws Exception
     */
    public static String decrypt(String encryptStr, String key, String iv) throws Exception {
        byte[] keyBytes = key.getBytes();
        byte[] decoded = decrypt(encryptStr, keyBytes, iv.getBytes(StandardCharsets.UTF_8), CIPHER_ALGORITHM_5P);
        return new String(decoded, StandardCharsets.UTF_8);
    }

    public static byte[] decryptBase64(String encryptStr, String key, int iv, String cipherType) throws Exception {
        byte[] keyBytes = Base64.decodeBase64(key);
        return decrypt(encryptStr, keyBytes, Arrays.copyOfRange(keyBytes, 0, iv), cipherType);
    }

    public static byte[] decrypt(String encryptStr, byte[] keyBytes, byte[] ivBytes, String cipherType) throws Exception {
        byte[] sourceBytes = Base64.decodeBase64(encryptStr);
        Cipher cipher = Cipher.getInstance(cipherType);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyBytes, KEY_ALGORITHM),
                new IvParameterSpec(ivBytes));
        return cipher.doFinal(sourceBytes);
    }
}