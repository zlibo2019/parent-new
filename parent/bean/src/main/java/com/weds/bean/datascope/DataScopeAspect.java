package com.weds.bean.datascope;

import com.weds.core.annotation.DataScope;
import com.weds.core.base.BaseEntity;
import com.weds.core.utils.AspectUtils;
import com.weds.core.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;

/**
 * 数据过滤处理
 *
 * @author sxm
 */
@Aspect
@Component
public class DataScopeAspect {

    @Resource
    private DataScopeServer dataScopeServer;

    /**
     * 数据权限过滤关键字
     */
    private static final String DATA_SCOPE = "dataScope";

    // 配置织入点
    @Pointcut("@annotation(com.weds.core.annotation.DataScope) || @annotation(com.weds.core.annotation.DataScopes)")
    public void dataScopePointCut() {
    }

    @Before("dataScopePointCut()")
    public void doBefore(JoinPoint point) throws Throwable {
        handleDataScope(point);
    }

    private void handleDataScope(final JoinPoint joinPoint) throws Throwable {
        // 获得注解
        Method method = AspectUtils.findMethodByPoint(joinPoint);
        if (method == null) {
            return;
        }
        List<DataScope> list = DataScopeUtils.getDataScopes(method);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            String sqlString = dataScopeServer.dataScopeFilter(list.get(i));
            if (StringUtils.isNotBlank(sqlString)) {
                sb.append(sqlString);
                if (i != list.size() - 1) {
                    sb.append(" AND ");
                }
            }
        }

        if (StringUtils.isNotBlank(sb.toString())) {
            Object[] objects = joinPoint.getArgs();
            for (Object object : objects) {
                if (object instanceof BaseEntity) {
                    BaseEntity baseEntity = (BaseEntity) object;
                    baseEntity.getParams().put(DATA_SCOPE, " AND (" + sb.toString() + ")");
                    break;
                }
            }
        }
    }
}
