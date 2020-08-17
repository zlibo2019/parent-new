package com.weds.core.utils;

import com.weds.core.annotation.ColFile;
import org.springframework.util.ResourceUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @Author sxm
 * @Description 文件管理
 * @Date 2018年3月22日
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

    /**
     * 文件生成实体对象
     *
     * @param type
     * @param content
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T fileToBean(Class<T> type, String content) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        Object obj = type.newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        if (!StringUtils.isBlank(content)) {
            int maxLen = content.length();
            for (PropertyDescriptor descriptor : propertyDescriptors) {
                String propertyName = descriptor.getName();
                try {
                    boolean flag = type.getDeclaredField(propertyName).isAnnotationPresent(ColFile.class);
                    if (!flag) {
                        continue;
                    }
                    ColFile colFile = type.getDeclaredField(propertyName).getAnnotation(ColFile.class);
                    if (colFile != null) {
                        int length = colFile.value();
                        int start = colFile.start();
                        Object value = content.substring(start, Math.min(start + length, maxLen)).trim();
                        descriptor.getWriteMethod().invoke(obj, value);
                    }
                } catch (NoSuchFieldException e) {
                    // e.printStackTrace();
                }
            }
        }
        return type.cast(obj);
    }

    /**
     * 实体对象生成文件
     *
     * @param objs
     * @return
     * @throws Exception
     */
    public static String beanToFile(Object[] objs, String splitChar) throws Exception {
        StringBuilder sb = new StringBuilder();
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < objs.length; i++) {
            Field[] properties = objs[i].getClass().getDeclaredFields();
            for (Field property : properties) {
                boolean flag = property.isAnnotationPresent(ColFile.class);
                if (!flag) {
                    continue;
                }
                ColFile colFile = property.getAnnotation(ColFile.class);
                Map<String, Object> map = new HashMap<>();
                map.put("colFile", colFile);
                map.put("property", property);
                map.put("index", colFile.order());
                list.add(map);
            }

            list.sort(Comparator.comparingInt(o -> Integer.parseInt(o.get("index").toString())));
            for (Map map : list) {
                ColFile colFile = (ColFile) map.get("colFile");
                Field property = (Field) map.get("property");
                // 反射get方法
                String methodName = "get"
                        + property.getName().substring(0, 1).toUpperCase()
                        + property.getName().substring(1);
                Method method = objs[i].getClass().getMethod(methodName);
                Object subObj = method.invoke(objs[i]);
                if (subObj != null) {
                    if (colFile.value() == -1) {
                        sb.append(subObj.toString());
                    } else {
                        // sb.append(String.format("%-" + colFile.value() + "s", subObj.toString()));
                        sb.append(strPadding(subObj.toString(), " ", colFile.value(), colFile.flag()));
                    }
                    sb.append(splitChar);
                } else {
                    sb.append(splitChar);
                }
            }
            sb.delete(sb.length() - splitChar.length(), sb.length());
            if (i != objs.length - 1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    /**
     * 补位
     *
     * @param strContent
     * @param len
     * @param strPad
     * @param flag
     * @return
     */
    public static String strPadding(String strContent, String strPad, int len, boolean flag) {
        //取得字符串的长度，注意汉字占两个字节的问题
        int length = 0;
        for (int i = 0; i < strContent.length(); i++) {
            //取得ascii编码 据此判断
            int ASCII = Character.codePointAt(strContent, i);
            if (ASCII >= 0 && ASCII <= 255) {
                length++;
            } else {
                length += 2;
            }
        }

        StringBuilder res = new StringBuilder();

        StringBuilder bf = new StringBuilder();
        //准确的字节长度拿到后 再据此补位
        for (int j = 0; j < len - length; j++) {
            bf.append(strPad);
        }
        if (flag) {
            res.append(strContent);
            res.append(bf);
        } else {
            res.append(bf);
            res.append(strContent);
        }
        return res.toString();
    }

    /**
     * 生成文件
     *
     * @param content
     * @param fileName
     * @throws Exception
     */
    public static String makeFile(String content, String fileName, String templateFile)
            throws Exception {
        String head = "";
        if (!StringUtils.isBlank(templateFile)) {
            File template = null;
            URL pathUrl = Thread.currentThread().getContextClassLoader()
                    .getResource("/" + templateFile);
            if (pathUrl == null) {
                template = ResourceUtils.getFile("classpath:" + templateFile);
            } else {
                template = new File(pathUrl.toURI());
            }

            FileReader fr = new FileReader(template);
            BufferedReader br = new BufferedReader(fr);
            head = br.readLine();
            fr.close();
            br.close();
        }

        String path = System.getProperty("user.dir");
        String targetPath = path + File.separator + "send";
        File dir = new File(targetPath);
        if (!dir.exists() || !dir.isDirectory()) {
            dir.mkdirs();
        }

        String filePath = targetPath + File.separator + fileName;
        File file = new File(filePath);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, "GBK");
        PrintWriter pw = new PrintWriter(osw);

        if (!"".equals(head)) {
            pw.println(head);
        }

        pw.println(content);
        pw.flush();
        pw.close();
        osw.close();
        fos.close();
        return filePath;
    }
}
