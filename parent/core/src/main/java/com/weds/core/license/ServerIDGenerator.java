package com.weds.core.license;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 服务器机器码生成
 *
 * @author SXM
 * @version 0.1
 */
public class ServerIDGenerator {
    private static final String PRODUCTCLASS1_TABLE = "RQU1KY6GDTHWC82L4MBN3SXFE9A5ZVP7";
    private static final String PRODUCTCLASS2_TABLE = "LARE8K92DPSB3TGXQNZW17MC6UY5VF4H";
    private static final String PRODUCTVERSION1_TABLE = "Y8QE6NAZKRX9VW5GCT3SMBFP4D72HUL1";
    private static final String PRODUCTVERSION2_TABLE = "LFCY896XM7RHEDUSZK5Q12B3VA4TWGNP";
    private static final String ENCRYPT_TABLE = "123456789ABCDEFGHKLMNPQRSTUVWXYZ";

    public static String[] make(String productInfo) throws IOException {
        if (!(productInfo.matches("[" + ENCRYPT_TABLE + "]{4}"))) {
            throw new IllegalArgumentException();
        }

        PhysicalAddress pa = new PhysicalAddress();
        List<?> addrs = normalizeMacAddr(pa.parseMacAddr());
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        String[] r = new String[addrs.size()];
        for (int i = 0; i < addrs.size(); ++i) {
            String addr = (String) addrs.get(i);
            r[i] = make(addr, productInfo, availableProcessors);
        }
        return r;
    }

    public static String makesuper(String productInfo) {
        return make("FF:FF:FF:FF:FF:FF", productInfo, 1);
    }

    private static String make(String addr, String productInfo, int availableProcessors) {
        int seed = 0;
        seed = 31 * seed + productInfo.charAt(0);
        seed = 31 * seed + productInfo.charAt(1);
        seed = 31 * seed + productInfo.charAt(2);
        seed = 31 * seed + productInfo.charAt(3);
        seed = 31 * seed + availableProcessors;
        return encryptProductInfo(encryptMacAddr(addr, seed), productInfo);
    }

    private static List<String> normalizeMacAddr(List<String> addrs) {
        List<String> newAddrs = new ArrayList<String>();
        for (String addr : addrs) {
            if ((addr != null) && (!(addr.startsWith("00:05:69")))
                    && (!(addr.startsWith("00:0C:29")))
                    && (!(addr.startsWith("00:1C:14")))
                    && (!(addr.startsWith("00:50:56")))) {
                newAddrs.add(addr);
            }
        }

        if (newAddrs.size() == 0) {
            newAddrs.addAll(addrs);
        }
        if (newAddrs.size() == 0) {
            newAddrs.add("00:08:C7:1B:8C:02");
        }
        return newAddrs;
    }

    private static String encryptProductInfo(String s, String productInfo) {
        StringBuilder sb = new StringBuilder(s);
        sb.insert(1, encryptChar(s, productInfo.charAt(0), PRODUCTCLASS1_TABLE));
        sb.insert(5, encryptChar(s, productInfo.charAt(1), PRODUCTCLASS2_TABLE));
        sb.insert(10, encryptChar(s, productInfo.charAt(2), PRODUCTVERSION1_TABLE));
        sb.insert(14, encryptChar(s, productInfo.charAt(3), PRODUCTVERSION2_TABLE));
        sb.insert(4, '-');
        sb.insert(9, '-');
        sb.insert(14, '-');
        return sb.toString();
    }

    public static String decryptProductInfo(String id) {
        StringBuilder sb = new StringBuilder(id);
        sb.deleteCharAt(14);
        sb.deleteCharAt(9);
        sb.deleteCharAt(4);
        sb.deleteCharAt(14);
        sb.deleteCharAt(10);
        sb.deleteCharAt(5);
        sb.deleteCharAt(1);
        String s = sb.toString();
        char productClass1 = decryptChar(s, id.charAt(1), PRODUCTCLASS1_TABLE);
        char productClass2 = decryptChar(s, id.charAt(6), PRODUCTCLASS2_TABLE);
        char version1 = decryptChar(s, id.charAt(12), PRODUCTVERSION1_TABLE);
        char version2 = decryptChar(s, id.charAt(17), PRODUCTVERSION2_TABLE);
        return String.valueOf(productClass1) + productClass2 +
                version1 + version2;
    }

    private static String encryptMacAddr(String addr, int seed) {
        byte[] bytes = new byte[12];
        addr = addr.replace(":", "");
        if (addr.length() != 12) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < addr.length(); ++i) {
            char c = addr.charAt(i);
            bytes[i] = (byte) c;
        }

        StringBuilder r = new StringBuilder(16);
        int startKey = (Math.abs(strHashCode(addr)) + 4) * 15008821 * seed;
        int mulKey = startKey >>> 8;
        int addKey = startKey >>> 16;
        for (byte aByte : bytes) {
            int b = (byte) (aByte ^ startKey >>> 8);
            startKey = (b + startKey) * mulKey + addKey;
            r.append(selectchar(b));
        }
        return r.toString();
    }

    private static char selectchar(int i) {
        return ENCRYPT_TABLE.charAt(Math.abs(i % ENCRYPT_TABLE.length()));
    }

    private static char decryptChar(String s, char c, String table) {
        int h = Math.abs(strHashCode(s) * strHashCode(table));
        int k = table.indexOf(c);
        if ((h < 0) || (c < '0')) {
            throw new IllegalArgumentException();
        }
        int d = (k - h) % table.length();
        if (d < 0) {
            d += table.length();
        }
        return table.charAt(d);
    }

    private static char encryptChar(String s, char c, String table) {
        int h = Math.abs(strHashCode(s) * strHashCode(table));
        int d = table.indexOf(c);
        if ((h <= 0) || (d < 0)) {
            throw new IllegalArgumentException();
        }
        return table.charAt((h + d) % table.length());
    }

    private static int strHashCode(CharSequence s) {
        int h = 0;
        int len = s.length();
        for (int i = 0; i < len; ++i) {
            h = 31 * h + s.charAt(i);
        }
        return h;
    }
}
