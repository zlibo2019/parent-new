package com.weds.core.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class ClassUtils {

    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    public static String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    public static List<Map<String, Object>> getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap<String, Object>();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     */
    public static Object[] getFiledValues(Object o) {
        String[] fieldNames = getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }


    public static Map<String, String> transforMapToObjMap(Map<String, String> map, Map<String, Object> info) {
        Map<String, String> obj = new HashMap<>();
        Iterator iterator = info.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (map.keySet().contains(key)) {
                obj.put(map.get(key), info.get(key).toString());
            }
        }
        return obj;
    }

    /**
     * 把Map键值对转化为javaBean对象
     *
     * @param type
     * @param map
     * @return
     * @throws Exception
     */
    public static Object transforMapToObject(Class<? extends Object> type, Map<String, String> map) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type); //获取类属性
        Object obj = type.newInstance(); //创建 JavaBean 对象
        //给 JavaBean对象的属性赋值
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("GMT"));
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (map.containsKey(propertyName)) {
                try {
                    Object value = map.get(propertyName);
                    if (value != null && !"".equals(value.toString())) {
                        String typeName = descriptor.getPropertyType().getTypeName();
                        if (typeName.endsWith("Integer"))
                            value = Integer.parseInt(value.toString());
                        else if (typeName.endsWith("Long"))
                            value = Long.parseLong(value.toString());
                        else if (typeName.endsWith("Date")) {
                            if (value.toString().indexOf("T") > 0 && value.toString().indexOf("Z") > 0) {
                                value = df.parse(value.toString());
                            } else {
                                value = sdf.parse(value.toString());
                            }
                        }
                        Object[] args = new Object[1];
                        args[0] = value;
                        descriptor.getWriteMethod().invoke(obj, args);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return obj;
    }

    /**
     * 把javaBean对象转换为Map键值对
     *
     * @param bean
     * @return
     * @throws Exception
     */
    public static Map<String, String> transforObjectToMap(Object bean) throws Exception {
        Class<? extends Object> type = bean.getClass();
        Map<String, String> returnMap = new HashMap<String, String>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (int i = 0; i < propertyDescriptors.length; i++) {
            PropertyDescriptor descriptor = propertyDescriptors[i];
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result = readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result.toString());
                }
            }
        }
        return returnMap;
    }
}
