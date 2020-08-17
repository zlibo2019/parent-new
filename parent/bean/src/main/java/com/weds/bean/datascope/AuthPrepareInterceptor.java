package com.weds.bean.datascope;

import com.weds.core.annotation.DataScope;
import com.weds.core.annotation.DataScopes;
import com.weds.core.utils.ReflectUtils;
import com.weds.core.utils.StringUtils;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Intercepts({@Signature(method = "prepare", type = StatementHandler.class,
        args = {Connection.class, Integer.class})})
public class AuthPrepareInterceptor implements Interceptor {
    @Resource
    private DataScopeServer dataScopeServer;

    private Logger log = LogManager.getLogger();

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (invocation.getTarget() instanceof RoutingStatementHandler) {
            RoutingStatementHandler handler = (RoutingStatementHandler) invocation.getTarget();
            StatementHandler delegate = ReflectUtils.getFieldValue(handler, "delegate");
            if (delegate == null) {
                return invocation.proceed();
            }
            MappedStatement mappedStatement = ReflectUtils.getFieldValue(delegate, "mappedStatement");
            if (mappedStatement == null) {
                return invocation.proceed();
            }
            BoundSql boundSql = delegate.getBoundSql();
            String sql = boundSql.getSql();
            List<DataScope> list = getDataScopeByDelegate(mappedStatement);
            for (DataScope dataScope : list) {
                String sqlString = dataScopeServer.dataScopeFilter(dataScope);
                if (StringUtils.isNotBlank(sqlString)) {
                    sql = sql.replace(dataScope.type().slot(), sqlString);
                }
            }
            ReflectUtils.setFieldValue(boundSql, "sql", sql);
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }

    private List<DataScope> getDataScopeByDelegate(MappedStatement mappedStatement) throws ClassNotFoundException {
        List<DataScope> list = new ArrayList<>();
        String id = mappedStatement.getId();
        String className = id.substring(0, id.lastIndexOf("."));
        String methodName = id.substring(id.lastIndexOf(".") + 1);
        final Class<?> cls = Class.forName(className);
        final Method[] methods = cls.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                list = DataScopeUtils.getDataScopes(method);
                break;
            }
        }
        return list;
    }
}
