package com.weds.bean.locale;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;

@Configuration
// @ComponentScan({"com.weds.bean.locale"})
// extends WebMvcConfigurerAdapter
public class LocaleConfig {
    @Bean
    public LocaleResolver localeResolver() {
        return new CustomLocaleResolver();
    }

    @Bean
    public CustomMessageSource customMessageSource() {
        return new CustomMessageSource();
    }

    // @Bean
    // public LocaleChangeInterceptor localeChangeInterceptor() {
    //     LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
    //     lci.setParamName("lang");
    //     return lci;
    // }
    //
    // @Override
    // public void addInterceptors(InterceptorRegistry registry) {
    //     registry.addInterceptor(localeChangeInterceptor());
    // }
}
