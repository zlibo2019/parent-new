package com.weds.core.license;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 服务器物理地址获取
 *
 * @author SXM
 * @version 0.1
 */
public final class PhysicalAddress {
    public static final String PATTERN_AIX = "(?<=\\s)([\\dA-Fa-f]{1,2}\\.){5}[\\dA-Fa-f]{1,2}(?=\\s)";
    public static final String PATTERN_LINUX = "(?<=HWaddr\\s|HWADDR=)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
    public static final String PATTERN_HPUX = "(?<=\\s0x)([\\dA-Fa-f]{12})(?=\\s)";
    public static final String PATTERN_MACOS = "(?<=ether\\s)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
    public static final String PATTERN_SOLARIS = "(?<=ether\\s)([\\dA-Fa-f]{1,2}\\:){5}[\\dA-Fa-f]{1,2}";
    public static final String PATTERN_WINDOWS = "(?<=\\:\\s)([\\dA-Fa-f]{2}\\-){5}[\\dA-Fa-f]{2}(?=[\\r|\\n])";
    public static final String PATTERN_FREEBSD = "(?<=ether\\s)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
    public static final String PATTERN_OPENBSD = "([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
    public static final String PATTERN_DEFAULT = "([\\dA-Fa-f]{1,2}\\:){5}[\\dA-Fa-f]{1,2}";
    private String cmd;
    private String pattern;
    private static final String[] USELESS_STRINGS_INDEXOF = {"Bluetooth",
            "隧道", "Tunnel", "Miniport", "Microsoft ISATAP Adapter", "SVN Adapter", "VPN",
            "Local Loopback", "Virtual"};

    private static final String[] USELESS_STRINGS_STARTS = {"p2p", "fw"};

    public PhysicalAddress() {
        if (SystemUtils.IS_OS_WINDOWS) {
            this.cmd = "ipconfig /all";
            this.pattern = "(?<=\\:\\s)([\\dA-Fa-f]{2}\\-){5}[\\dA-Fa-f]{2}(?=[\\r|\\n])";
        } else if (SystemUtils.IS_OS_LINUX) {
            this.cmd = "/sbin/ifconfig -a";
            this.pattern = "(?<=HWaddr\\s|HWADDR=)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
        } else if (SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_SUN_OS) {
            this.cmd = "/sbin/ifconfig -a";
            this.pattern = "(?<=ether\\s)([\\dA-Fa-f]{1,2}\\:){5}[\\dA-Fa-f]{1,2}";
        } else if (SystemUtils.IS_OS_FREE_BSD || SystemUtils.IS_OS_NET_BSD) {
            this.cmd = "/sbin/ifconfig -a";
            this.pattern = "(?<=ether\\s)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
        } else if (SystemUtils.IS_OS_OPEN_BSD) {
            this.cmd = "netstat -in";
            this.pattern = "([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
        } else if (SystemUtils.IS_OS_HP_UX) {
            this.cmd = "/usr/sbin/lanscan";
            this.pattern = "(?<=\\s0x)([\\dA-Fa-f]{12})(?=\\s)";
        } else if (SystemUtils.IS_OS_AIX) {
            this.cmd = "netstat -ia";
            this.pattern = "(?<=\\s)([\\dA-Fa-f]{1,2}\\.){5}[\\dA-Fa-f]{1,2}(?=\\s)";
        } else if (SystemUtils.IS_OS_MAC) {
            this.cmd = "/sbin/ifconfig -a";
            this.pattern = "(?<=ether\\s)([\\dA-Fa-f]{2}\\:){5}[\\dA-Fa-f]{2}";
        } else {
            this.cmd = "/sbin/ifconfig -a";
            this.pattern = "([\\dA-Fa-f]{1,2}\\:){5}[\\dA-Fa-f]{1,2}";
        }
    }

    public List<String> parseMacAddrCmd(List<String> r) throws IOException {
        if (r == null) {
            r = new ArrayList<>(3);
        }
        Process p = Runtime.getRuntime().exec(this.cmd);
        if (p == null) {
            return r;
        }
        InputStream inputStream = p.getInputStream();
        if (inputStream == null) {
            return r;
        }
        String s = IOUtils.toString(inputStream, "UTF-8");
        if ((SystemUtils.IS_OS_WINDOWS) || (SystemUtils.IS_OS_LINUX) || (SystemUtils.IS_OS_MAC)) {
            s = removeUselessInformation(s);
        }
        parseMacAddrFromString(r, s, this.pattern);
        inputStream.close();
        return r;
    }

    public String removeUselessInformation(String cmdOut) {
        String[] sections = cmdOut.split("[\\r\\n]{1,2}(?=\\S)");
        StringBuilder result = new StringBuilder(cmdOut.length());
        for (String s : sections) {
            if (!(isUseless(s))) {
                result.append(s).append('\n');
            }
        }
        return result.toString();
    }

    public boolean isUseless(String s) {
        for (String u : USELESS_STRINGS_INDEXOF) {
            if (s.contains(u)) {
                return true;
            }
        }
        for (String u : USELESS_STRINGS_STARTS) {
            if (s.startsWith(u)) {
                return true;
            }
        }
        return false;
    }

    public List<String> parseMacAddrCfg(List<String> list) throws IOException {
        if (list == null) {
            list = new ArrayList<>(3);
        }
        boolean linux = File.separatorChar == '/';
        if (!(linux)) {
            return list;
        }
        String path = "/etc/sysconfig/network-scripts/";
        Collection<File> cfgfns = FileUtils.listFiles(new File(path), null, false);

        for (File f : cfgfns) {
            if (f.getAbsolutePath().startsWith("ifcfg-")) {
                String s = FileUtils.readFileToString(f);
                parseMacAddrFromString(list, s, this.pattern);
            }
        }
        return list;
    }

    public void parseMacAddrFromString(List<String> r, String s, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher mt = pattern.matcher(s);
        int end = 0;
        while (mt.find(end)) {
            end = mt.end();
            String addr = mt.group().trim().toUpperCase().replace('-', ':')
                    .replace('.', ':');
            if (addr.length() == 12) {
                addr = addr.substring(0, 2) + ":" + addr.substring(2, 4) + ":"
                        + addr.substring(4, 6) + ":" + addr.substring(6, 8) + ":"
                        + addr.substring(8, 10) + ":" + addr.substring(10);
            }

            if (addr.length() < 17) {
                addr = addr.replaceAll("(?<=^|:)[\\dA-F](?=:|$)", "0$0");
            }

            if ("00:00:00:00:00:00".equals(addr)) {
                continue;
            }

            if (!(r.contains(addr))) {
                r.add(addr);
            }
        }
    }

    public List<String> parseMacAddr() throws IOException {
        List<String> r = parseMacAddrCfg(null);
        r = parseMacAddrCmd(r);
        return r;
    }

    // public String[] getMacAddresses() throws IOException {
    //     List<?> l = parseMacAddr();
    //     String[] r = new String[l.size()];
    //     l.toArray(r);
    //     return r;
    // }
    //
    // public String getMacAddressByJdk6() throws SocketException, UnknownHostException {
    //     if (SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_6)) {
    //         InetAddress ip = InetAddress.getLocalHost();
    //         NetworkInterface network = NetworkInterface.getByInetAddress(ip);
    //         byte[] mac = network.getHardwareAddress();
    //         StringBuilder sb = new StringBuilder();
    //         for (int i = 0; i < mac.length; ++i) {
    //             sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? ":" : ""));
    //         }
    //         return sb.toString();
    //     }
    //     return null;
    // }
    //
    // public String getMacAddress() throws IOException {
    //     String r = getMacAddressByJdk6();
    //     if ((r != null) && (r.length() > 0)) {
    //         return r;
    //     }
    //     String[] l = getMacAddresses();
    //     return (((l != null) && (l.length > 0)) ? l[0] : "");
    // }
}
