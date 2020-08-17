package com.weds.bean.datascope;

import com.weds.core.annotation.DataScope;
import com.weds.core.annotation.DataScopes;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class DataScopeUtils {
    public static List<DataScope> getDataScopes(Method method) {
        List<DataScope> list = new ArrayList<>();
        if (method.isAnnotationPresent(DataScopes.class)) {
            DataScopes dataScopes = method.getAnnotation(DataScopes.class);
            for (DataScope dataScope : dataScopes.value()) {
                if (dataScope.active()) {
                    list.add(dataScope);
                }
            }
        }
        if (method.isAnnotationPresent(DataScope.class)) {
            DataScope dataScope = method.getAnnotation(DataScope.class);
            if (dataScope.active()) {
                list.add(dataScope);
            }
        }
        return list;
    }
}
