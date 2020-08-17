package com.weds.bean.datasource.multiple;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DbCustomAspect extends DataSourceAspect {

    @Pointcut("execution(* com.weds.*.mapper..*(..))")
    @Override
    protected void datasourceAspect() {
        super.datasourceAspect();
    }
}