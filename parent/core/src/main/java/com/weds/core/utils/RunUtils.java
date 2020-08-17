package com.weds.core.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author sxm
 */
public class RunUtils {

    /**
     * @param cmd 执行的普通shell 命令
     */

    public static void run(String cmd) {
        String inline;
        BufferedReader br = null;
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            br = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            while (null != (inline = br.readLine())) {
                // 在控制台打印执行命令输出的信息
                System.out.println(inline);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.toString());
                }
            }
        }
    }


    /**
     * 执行系统命令
     *
     * @param cmd 系统命令
     * @return 返回执行结果
     */
    public static boolean runString(String cmd) {
        Runtime run = Runtime.getRuntime();// 返回与当前 Java 应用程序相关的运行时对象
        try {
            Process p = run.exec(cmd);// 启动另一个进程来执行命令
            BufferedInputStream in = new BufferedInputStream(p.getInputStream());
            BufferedReader inBr = new BufferedReader(new InputStreamReader(in));

            // 检查命令是否执行失败。
            if (p.waitFor() != 0) {
                if (p.exitValue() == 1) {// p.exitValue()==0表示正常结束，1：非正常结束
                    System.err.println("命令执行失败!");
                    return false;
                }
            }
            inBr.close();
            in.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
