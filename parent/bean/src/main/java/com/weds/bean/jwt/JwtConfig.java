package com.weds.bean.jwt;

import com.weds.core.constants.CoreConstants;
import com.weds.core.resp.JsonResult;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan({"com.weds.bean.jwt"})
@ConditionalOnProperty(name = "weds.jwt.active", matchIfMissing = true, havingValue = "true")
@EnableConfigurationProperties(JwtParams.class)
public class JwtConfig {
    // @ConditionalOnProperty(value = {"weds.jwt.active"})
    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }

    @ConditionalOnMissingBean(name = "jwtHandler")
    @Bean
    public JwtHandlerService jwtHandler() {
        return data -> JsonResult.get(CoreConstants.SUCCESSED_FLAG, null);
    }
}
