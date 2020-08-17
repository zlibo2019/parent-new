package com.weds.bean.datascope;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据过滤处理
 *
 * @author sxm
 */
@Configuration
public class DataScopeConfig {
    @Bean
    @ConditionalOnMissingBean(name = "dataScopeServer")
    public DataScopeServer dataScopeServer() {
        return (dataScope) -> null;
    }
}
