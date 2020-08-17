package com.weds.core.utils.coder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.weds.core.annotation.ColCoder;
import com.weds.core.utils.FileUtils;
import com.weds.core.utils.StringUtils;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

public class BeanCoder {

    /**
     * 请求对象加密生成JSON数据
     *
     * @param obj
     * @param key
     * @param imageRoot
     * @param imageType
     * @return
     * @throws Exception
     */
    public static String parseObjToJson(Object obj, String key, String imageRoot, String imageType) throws Exception {
        if (obj == null) {
            return null;
        }
        GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
        Gson gson = builder.create();
        encryptObject(obj, key, imageRoot, imageType);
        return gson.toJson(obj);
    }

    /**
     * 请求对象加密
     *
     * @param obj
     * @param key
     * @param imageRoot
     * @param imageType
     * @throws Exception
     */
    private static void encryptObject(Object obj, String key, String imageRoot, String imageType) throws Exception {
        Class<?> c = obj.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            boolean flag = field.isAnnotationPresent(ColCoder.class);
            if (!flag) {
                continue;
            }
            ColCoder colCoder = field.getAnnotation(ColCoder.class);
            if (colCoder.aes() || colCoder.image()) {
                field.setAccessible(true);
                Object o = field.get(obj);
                if (o != null) {
                    if (o instanceof List) {
                        List list = (List) o;
                        for (Object o1 : list) {
                            encryptObject(o1, key, imageRoot, imageType);
                        }
                    } else {
                        field.set(obj, "");
                        if (!StringUtils.isBlank(o.toString())) {
                            if (colCoder.aes()) {
                                field.set(obj, AES7Coder.encrypt(o.toString(), key));
                            }

                            if (colCoder.image()) {
                                String imagePath = imageRoot + File.separator + o.toString().substring(2);
                                File imageFile = new File(imagePath);
                                if (imageFile.exists() && imageFile.isFile()) {
                                    String imageBase64 = imageType + Coder.encryptBASE64(FileUtils.readFileToByteArray(imageFile));
                                    field.set(obj, imageBase64);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 返回解密对象
     *
     * @param obj
     * @param key
     * @throws Exception
     */
    public static void decryptObject(Object obj, String key) throws Exception {
        Class<?> c = obj.getClass();
        Field[] fields = c.getDeclaredFields();
        for (Field field : fields) {
            boolean flag = field.isAnnotationPresent(ColCoder.class);
            if (!flag) {
                continue;
            }
            ColCoder colCoder = field.getAnnotation(ColCoder.class);
            if (colCoder.aes()) {
                field.setAccessible(true);
                Object o = field.get(obj);
                if (o != null) {
                    if (o instanceof List) {
                        List list = (List) o;
                        for (Object o1 : list) {
                            decryptObject(o1, key);
                        }
                    } else {
                        if (!StringUtils.isBlank(o.toString())) {
                            field.set(obj, AES7Coder.decrypt(o.toString(), key));
                        }
                    }
                }
            }
        }
    }
}
