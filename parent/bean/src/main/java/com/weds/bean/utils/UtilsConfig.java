package com.weds.bean.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan({"com.weds.bean.utils"})
public class UtilsConfig {
    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
