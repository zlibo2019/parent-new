package com.weds.bean.utils;

import com.weds.bean.constants.BeanConstants;
import com.weds.bean.jwt.JwtUtils;
import com.weds.core.utils.ReflectUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;


/**
 * 实体类相关工具类
 */
public class EntityUtils {

    public static <T> void setCreatAndUpdatInfo(T entity) {
        String user = getUserInfo();
        Date date = new Date();
        setCreateInfo(entity, user, date);
        setUpdatedInfo(entity, user, date);
    }

    public static <T> void setListCreatAndUpdatInfo(List<T> list) {
        String user = getUserInfo();
        Date date = new Date();
        for (T entity : list) {
            setCreateInfo(entity, user, date);
            setUpdatedInfo(entity, user, date);
        }
    }

    public static <T> void setCreateInfo(T entity) {
        setCreateInfo(entity, getUserInfo(), new Date());
    }

    public static <T> void setCreateInfo(T entity, String user, Date date) {
        Field field = ReflectUtils.getAccessibleField(entity, BeanConstants.CT_USER);
        if (field != null && field.getType().equals(String.class)) {
            ReflectUtils.invokeSetter(entity, BeanConstants.CT_USER, user);
        }

        field = ReflectUtils.getAccessibleField(entity, BeanConstants.CT_DATE);
        if (field != null && field.getType().equals(Date.class)) {
            ReflectUtils.invokeSetter(entity, BeanConstants.CT_DATE, date);
        }
    }

    public static <T> void setUpdatedInfo(T entity) {
        setUpdatedInfo(entity, getUserInfo(), new Date());
    }

    public static <T> void setUpdatedInfo(T entity, String user, Date date) {
        Field field = ReflectUtils.getAccessibleField(entity, BeanConstants.LT_USER);
        if (field != null && field.getType().equals(String.class)) {
            ReflectUtils.invokeSetter(entity, BeanConstants.LT_USER, user);
        }

        field = ReflectUtils.getAccessibleField(entity, BeanConstants.LT_DATE);
        if (field != null && field.getType().equals(Date.class)) {
            ReflectUtils.invokeSetter(entity, BeanConstants.LT_DATE, date);
        }
    }

    private static String getUserInfo() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return JwtUtils.getJwtData(request).getString(BeanConstants.USER_ID);
    }
}
