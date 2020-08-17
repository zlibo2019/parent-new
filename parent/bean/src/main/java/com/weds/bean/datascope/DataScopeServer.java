package com.weds.bean.datascope;

import com.weds.core.annotation.DataScope;
import org.aspectj.lang.JoinPoint;

public interface DataScopeServer {
    String dataScopeFilter(DataScope dataScope);
}
