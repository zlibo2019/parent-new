package com.weds.bean.logs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
// @ComponentScan({"com.weds.bean.logs"})
@EnableConfigurationProperties(LogParams.class)
public class LogConfig {
    private Logger log = LogManager.getLogger();

    @Bean
    @ConditionalOnMissingBean(name = "logWriteServer")
    public LogWriteServer logWriteServer() {
        return new LogWriteServer() {
            @Override
            public void handleInfoLogs(LogEntity logEntity) {
                log.info(logEntity.toString());
                log.info("---------------------------------------------------");
            }

            @Override
            public void handleErrorLogs(LogEntity logEntity) {
                log.error(logEntity.toString());
                log.error("---------------------------------------------------");
            }
        };
    }
}
