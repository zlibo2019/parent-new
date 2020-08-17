package com.weds.pay.lcsw.utils;

import com.weds.pay.lcsw.annotation.Sign;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

public class LcswUtils {
    public static List<String> sign(Class clazz) {
        List<String> list = new LinkedList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            boolean sign = field.isAnnotationPresent(Sign.class);
            if (sign) {
                list.add(field.getName());
            }
        }
        return list;
    }
}
