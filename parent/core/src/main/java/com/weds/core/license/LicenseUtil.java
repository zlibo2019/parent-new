package com.weds.core.license;

import com.weds.core.utils.coder.RSACoder;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.nio.charset.StandardCharsets;

/**
 * 注册码生成、转换及验证工具
 *
 * @author SXM
 * @version 0.1
 */
public class LicenseUtil {
    /**
     * 验证License
     *
     * @param pollCode
     * @return
     * @throws Exception
     */
    public static String checkLicense(String pollCode, String pubKey) throws Exception {
        byte[] data = RSACoder.decryptBASE64(pollCode);
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(data));
        int keyNo = in.readInt();
        if (keyNo != 41191216) {
            throw new LicenseException("注册码无效!");
        }
        byte[] bData = new byte[in.readInt()];
        in.read(bData);
        byte[] bSign = new byte[in.readInt()];
        in.read(bSign);
        // 验证签名
        boolean status = RSACoder.verify(bData, pubKey, RSACoder.encryptBASE64(bSign));
        if (status) {
            return new String(bData, StandardCharsets.UTF_8);
        } else {
            throw new LicenseException("注册码无效!");
        }
    }
}
