package com.weds.core.utils;

import io.swagger.models.HttpMethod;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * 请求代理
 */
public class ProxyUtils {

    public static String HttpsProxy(String url, String param, String proxy, int port, String method,
                                    Map<String, String> header) throws Exception {
        PrintWriter out = null;
        BufferedReader in;
        StringBuilder result = new StringBuilder();
        URL urlClient = new URL(url);
        System.out.println("请求URL：" + urlClient);

        //创建代理虽然是https也是Type.HTTP
        Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port));
        //设置代理
        HttpsURLConnection httpsConn = (HttpsURLConnection) urlClient.openConnection(proxy1);

        SSLContext sc = SSLContext.getInstance("SSL");
        // 指定信任https
        sc.init(null, new TrustManager[]{new TrustAnyTrustManager()}, new java.security.SecureRandom());
        httpsConn.setSSLSocketFactory(sc.getSocketFactory());
        httpsConn.setHostnameVerifier(new TrustAnyHostnameVerifier());

        // 设置通用的请求属性
        httpsConn.setRequestProperty("accept", "*/*");
        httpsConn.setRequestProperty("connection", "Keep-Alive");
        httpsConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        for (String key : header.keySet()) {
            httpsConn.setRequestProperty(key, header.get(key));
        }

        httpsConn.setRequestMethod(method);
        if (HttpMethod.POST.toString().equalsIgnoreCase(method) || HttpMethod.PUT.toString().equalsIgnoreCase(method)) {
            // 发送POST请求必须设置如下两行
            httpsConn.setDoOutput(true);
            httpsConn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpsConn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
        } else if (HttpMethod.GET.toString().equalsIgnoreCase(method) || HttpMethod.DELETE.toString().equalsIgnoreCase(method)) {
            httpsConn.connect();
        }
        // 定义BufferedReader输入流来读取URL的响应
        if (httpsConn.getResponseCode() == HttpURLConnection.HTTP_OK
                || httpsConn.getResponseCode() == HttpURLConnection.HTTP_CREATED
                || httpsConn.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
            in = new BufferedReader(new InputStreamReader(httpsConn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(httpsConn.getErrorStream()));
        }
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        // 断开连接
        httpsConn.disconnect();
        // System.out.println("result：" + result);
        // System.out.println("返回结果：" + httpsConn.getResponseMessage());
        in.close();
        if (out != null) {
            out.close();
        }
        return result.toString();
    }

    public static String HttpProxy(String url, String param, String proxy, int port, String method,
                                   Map<String, String> header) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();
        URL urlClient = new URL(url);

        System.out.println("请求的URL：" + urlClient);
        //创建代理
        Proxy proxy1 = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, port));
        //设置代理
        HttpURLConnection httpConn = (HttpURLConnection) urlClient.openConnection(proxy1);

        // 设置通用的请求属性
        httpConn.setRequestProperty("accept", "*/*");
        httpConn.setRequestProperty("connection", "Keep-Alive");
        httpConn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        for (String key : header.keySet()) {
            httpConn.setRequestProperty(key, header.get(key));
        }

        httpConn.setRequestMethod(method);
        if (HttpMethod.POST.toString().equalsIgnoreCase(method) || HttpMethod.PUT.toString().equalsIgnoreCase(method)) {
            // 发送POST请求必须设置如下两行
            httpConn.setDoOutput(true);
            httpConn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
        } else if (HttpMethod.GET.toString().equalsIgnoreCase(method) || HttpMethod.DELETE.toString().equalsIgnoreCase(method)) {
            httpConn.connect();
        }

        // 定义BufferedReader输入流来读取URL的响应
        if (httpConn.getResponseCode() == HttpURLConnection.HTTP_OK
                || httpConn.getResponseCode() == HttpURLConnection.HTTP_CREATED
                || httpConn.getResponseCode() == HttpURLConnection.HTTP_ACCEPTED) {
            in = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
        } else {
            in = new BufferedReader(new InputStreamReader(httpConn.getErrorStream()));
        }

        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        // 断开连接
        httpConn.disconnect();
        // System.out.println("result：" + result);
        // System.out.println("返回结果：" + httpConn.getResponseMessage());
        in.close();
        if (out != null) {
            out.close();
        }

        return result.toString();
    }

    private static class TrustAnyTrustManager implements X509TrustManager {
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }
}
