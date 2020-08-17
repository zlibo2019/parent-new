package com.weds.bean.license;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan({"com.weds.bean.license"})
// @ConditionalOnProperty(name = "weds.license.active", havingValue = "true")
@EnableConfigurationProperties(LicenseParams.class)
public class LicenseConfig {

    @Bean
    public LicenseFilter licenseFilter() {
        return new LicenseFilter();
    }

    @Bean
    public LicenseService licenseService() {
        return new LicenseService();
    }
}
