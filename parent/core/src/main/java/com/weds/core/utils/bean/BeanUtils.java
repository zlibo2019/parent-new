package com.weds.core.utils.bean;

import com.weds.core.annotation.XmlElement;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {

    /**
     * Map转实体类共通方法 (Map2Bean)
     *
     * @param type
     * @param map
     * @param <T>
     * @return
     * @throws Exception
     */
    public static <T> T convertMap(Class<T> type, Map map) throws Exception {
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        Object obj = type.newInstance();
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        String fieldName;
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            XmlElement xmlElement = null;
            try {
                xmlElement = type.getDeclaredField(propertyName).getAnnotation(XmlElement.class);
            } catch (NoSuchFieldException e) {
                // e.printStackTrace();
            }
            if (xmlElement != null) {
                fieldName = xmlElement.value();
            } else {
                fieldName = propertyName;
            }
            if (map.containsKey(fieldName)) {
                Object value = map.get(fieldName);
                descriptor.getWriteMethod().invoke(obj, value);
            }
        }
        return type.cast(obj);
    }

    /**
     * 实体类转Map共通方法 (Bean2Map)
     *
     * @param bean 实体类
     * @return Map
     * @throws Exception
     */

    public static Map convertBean(Object bean) throws Exception {
        Class type = bean.getClass();
        Map returnMap = new HashMap();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : propertyDescriptors) {
            String propertyName = descriptor.getName();
            if (!propertyName.equals("class")) {
                Method readMethod = descriptor.getReadMethod();
                Object result =
                        readMethod.invoke(bean);
                if (result != null) {
                    returnMap.put(propertyName, result);
                } else {
                    returnMap.put(propertyName, "");
                }
            }
        }
        return returnMap;
    }
}
