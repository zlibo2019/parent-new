package com.weds.core.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FTPUtil {
    private static String localCharset = "GBK";
    private static String remoteCharset = "ISO-8859-1";

    public static void setLocalCharset(String localCharset) {
        FTPUtil.localCharset = localCharset;
    }

    public static void setRemoteCharset(String remoteCharset) {
        FTPUtil.remoteCharset = remoteCharset;
    }


    /**
     * FTP上传单个文
     *
     * @param serverIP
     * @param user
     * @param psd
     * @param srcFile
     * @param uploadDir
     * @return
     */
    public static boolean upload(String serverIP, int port, String user, String psd,
                                 File srcFile, String fileName, String uploadDir) {
        boolean flag;

        if (!srcFile.isFile()) {
            return false;
        }

        FTPClient ftpClient = new FTPClient();
        FileInputStream fis = null;

        try {
            ftpClient.connect(serverIP, port);
            ftpClient.login(user, psd);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }
            ftpClient.setBufferSize(1024);
            ftpClient.setControlEncoding(localCharset);
            // FTPClientConfig conf = new
            // FTPClientConfig(FTPClientConfig.SYST_NT);
            // conf.setServerLanguageCode("zh");
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            String fmtDir = new String(uploadDir.getBytes(localCharset),
                    remoteCharset);
            String[] paths = fmtDir.split("/");
            for (int i = 0; i < paths.length; i++) {
                ftpClient.makeDirectory(paths[i]);
                ftpClient.changeWorkingDirectory(paths[i]);
            }

            String tempName = new String(fileName.getBytes(localCharset),
                    remoteCharset);
            fis = new FileInputStream(srcFile);
            flag = ftpClient.storeFile(tempName, fis);
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fis);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }

    /**
     * FTP下载单个文件
     *
     * @param serverIP
     * @param user
     * @param psd
     * @param srcFile
     * @param uploadDir
     * @return
     */
    public static boolean download(String serverIP, int port, String user, String psd,
                                   File srcFile, String uploadDir) {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fos = null;
        boolean flag;

        try {
            ftpClient.connect(serverIP, port);
            ftpClient.login(user, psd);
            int reply = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftpClient.disconnect();
                return false;
            }

            String fmtDir = new String(uploadDir.getBytes(localCharset),
                    remoteCharset);
            String[] paths = fmtDir.split("/");
            for (int i = 0; i < paths.length; i++) {
                ftpClient.changeWorkingDirectory(paths[i]);
            }
            ftpClient.listFiles();
            ftpClient.setBufferSize(1024);
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

            String fileName = new String(srcFile.getName().getBytes(
                    localCharset), remoteCharset);
            fos = new FileOutputStream(srcFile);
            flag = ftpClient.retrieveFile(fileName, fos);
            ftpClient.logout();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("FTP客户端出错！", e);
        } finally {
            IOUtils.closeQuietly(fos);
            try {
                ftpClient.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("关闭FTP连接发生异常！", e);
            }
        }
        return flag;
    }
}
