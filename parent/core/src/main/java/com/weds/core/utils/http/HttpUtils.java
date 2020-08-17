package com.weds.core.utils.http;

import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

@SuppressWarnings("deprecation")
public class HttpUtils {
    /**
     * POST请求
     *
     * @param urlStr  请求地址
     * @param params  参数字典
     * @param charset 编码，最好传“UTF-8”
     * @return
     * @throws RuntimeException
     */
    public static String sendPostByHttpUrlConnection(String urlStr, Map<String, String> params, String charset) {
        String result = null;
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        BufferedReader br = null;
        // 发送请求
        try {
            // 构建请求参数
            String sbParams = parseUrlParam(params, true);
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if (sbParams != null && sbParams.length() > 0) {
                osw = new OutputStreamWriter(con.getOutputStream(), charset);
                osw.write(sbParams);
                osw.flush();
            }
            result = IOUtils.toString(con.getInputStream(), charset);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    br = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
        }

        return result;
    }

    /**
     * POST请求
     *
     * @param urlStr  请求地址
     * @param params  参数字典
     * @param charset 编码，最好传“UTF-8”
     * @return
     * @throws RuntimeException
     */
    public static byte[] sendPostByHttpUrlConnectionByte(String urlStr, Map<String, String> params, String charset) {
        byte[] rtn = null;
        HttpURLConnection con = null;
        OutputStreamWriter osw = null;
        ByteArrayOutputStream baos = null;
        InputStream is = null;
        // 发送请求
        try {
            // 构建请求参数
            String sbParams = parseUrlParam(params, true);
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if (sbParams != null && sbParams.length() > 0) {
                osw = new OutputStreamWriter(con.getOutputStream(), charset);
                osw.write(sbParams);
                osw.flush();
            }
            rtn = IOUtils.toByteArray(con.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e) {
                    osw = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    baos = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    is = null;
                    throw new RuntimeException(e);
                } finally {
                    if (con != null) {
                        con.disconnect();
                        con = null;
                    }
                }
            }
        }
        return rtn;
    }

    public static String parseUrlParam(Map<String, String> params, boolean flag) throws UnsupportedEncodingException {
        StringBuffer sbParams = new StringBuffer();
        if (params != null && params.size() > 0) {
            for (Entry<String, String> e : params.entrySet()) {
                sbParams.append(e.getKey());
                sbParams.append("=");
                if (flag) {
                    sbParams.append(URLEncoder.encode(e.getValue(), "UTF-8"));
                } else {
                    sbParams.append(e.getValue());
                }
                sbParams.append("&");
            }
            return sbParams.substring(0, sbParams.length() - 1);
        }
        return "";
    }

    /**
     * 将对象转化为URL请求参数
     *
     * @param o
     * @return
     * @throws Exception
     */
    public static String parseUrlParamEntity(Object o, boolean flag) throws Exception {
        Class<?> c = o.getClass();
        Field[] fields = c.getDeclaredFields();
        Map<String, Object> map = new TreeMap<String, Object>();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = field.get(o);
            if (value != null)
                map.put(name, value);
        }

        Set<Entry<String, Object>> set = map.entrySet();
        Iterator<Entry<String, Object>> it = set.iterator();
        StringBuffer sb = new StringBuffer();
        while (it.hasNext()) {
            Map.Entry<String, Object> e = it.next();
            sb.append(e.getKey());
            sb.append("=");
            if (flag) {
                sb.append(URLEncoder.encode(e.getValue().toString(), "UTF-8"));
            } else {
                sb.append(e.getValue().toString());
            }
            sb.append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static String sendSocket(String urlStr, String content, String charset) {
        Socket socket = null;
        String ret = "";
        try {
            URL url = new URL(urlStr);
            String host = url.getHost();
            int port = url.getPort();
            if (-1 == port) {
                port = 80;
            }
            //创建一个流套接字并将其连接到指定主机上的指定端口号
            socket = new Socket(host, port);
            //读取服务器端数据
            DataInputStream input = new DataInputStream(socket.getInputStream());
            //向服务器端发送数据
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.write(content.getBytes(charset));
            out.flush();
            ret = IOUtils.toString(input, charset);
            out.close();
            input.close();
        } catch (Exception e) {
            throw new RuntimeException("客户端异常:" + e.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    socket = null;
                    throw new RuntimeException("客户端 finally 异常:" + e.getMessage());
                }
            }
        }
        return ret;
    }
}