package com.weds.etl.config;

import com.weds.etl.service.SchedulerService;
import org.quartz.Scheduler;
import org.quartz.ee.servlet.QuartzInitializerListener;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.io.IOException;

@Configuration
@ComponentScan({"com.weds.etl.config"})
@EnableConfigurationProperties(SchedulerParams.class)
public class SchedulerConfig {
    private static final String QUARTZ_CONFIG = "/quartz.properties";

    @Bean(name = "customJobFactory")
    // @ConditionalOnProperty(value = {"weds.etl.active"})
    // @ConditionalOnProperty(name = "weds.etl.active", havingValue = "true")
    public CustomJobFactory customJobFactory() {
        return new CustomJobFactory();
    }

    @Bean(name = "schedulerFactory")
    @ConditionalOnBean(name = "customJobFactory")
    public SchedulerFactoryBean schedulerFactoryBean(CustomJobFactory customJobFactory) throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource(QUARTZ_CONFIG));
        //在quartz.properties中的属性被读取并注入后再初始化对象
        propertiesFactoryBean.afterPropertiesSet();

        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setOverwriteExistingJobs(true);
        factory.setQuartzProperties(propertiesFactoryBean.getObject());
        factory.setJobFactory(customJobFactory);
        // factory.setStartupDelay(30);
        return factory;
    }

    /*
     * quartz初始化监听器
     */
    // @Bean
    // @ConditionalOnProperty(value = {"weds.etl.active"})
    // @ConditionalOnProperty(name = "weds.etl.active", havingValue = "true")
    // public QuartzInitializerListener executorListener() {
    //     return new QuartzInitializerListener();
    // }

    /*
     * 通过SchedulerFactoryBean获取Scheduler的实例
     */
    @Bean(name = "scheduler")
    @ConditionalOnBean(name = "customJobFactory")
    public Scheduler scheduler(CustomJobFactory customJobFactory) throws IOException {
        return schedulerFactoryBean(customJobFactory).getScheduler();
    }

    @Bean
    @ConditionalOnBean(name = "scheduler")
    public SchedulerService schedulerService() {
        return new SchedulerService();
    }
}
