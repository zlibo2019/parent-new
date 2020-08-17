package com.weds.bean.http;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriTemplateHandler;
import org.springframework.web.util.UriTemplateHandler;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

@Configuration
// @ComponentScan({"com.weds.bean.http"})
@EnableConfigurationProperties(HttpReqParams.class)
public class HttpConfig {

    @Resource
    private HttpReqParams httpReqParams;

    @Bean
    @ConditionalOnMissingBean(name = "httpInterceptor")
    public ClientHttpRequestInterceptor httpInterceptor() {
        return (request, body, execution) -> execution.execute(request, body);
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpFactory")
    public ClientHttpRequestFactory httpFactory() {
        return new SimpleClientHttpRequestFactory();
    }

    @Bean
    @ConditionalOnMissingBean(name = "httpErrorHandler")
    public ResponseErrorHandler httpErrorHandler() {
        return new DefaultResponseErrorHandler();
    }

    @Bean
    @ConditionalOnMissingBean(name = "uriTemplateHandler")
    public UriTemplateHandler uriTemplateHandler() {
        return new DefaultUriTemplateHandler();
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpFactory, ClientHttpRequestInterceptor httpInterceptor,
                                     UriTemplateHandler uriTemplateHandler, ResponseErrorHandler httpErrorHandler) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(httpReqParams.getConnectTimeout());
        requestFactory.setReadTimeout(httpReqParams.getReadTimeout());

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        restTemplate.setErrorHandler(httpErrorHandler);
        restTemplate.setInterceptors(Collections.singletonList(httpInterceptor));
        restTemplate.setRequestFactory(httpFactory);
        restTemplate.setUriTemplateHandler(uriTemplateHandler);

        List<HttpMessageConverter<?>> converterList = restTemplate.getMessageConverters();
        for (int i = 0; i < converterList.size(); i++) {
            if (converterList.get(i) instanceof StringHttpMessageConverter) {
                HttpMessageConverter<?> converter = new StringHttpMessageConverter(Charset.forName("UTF-8"));
                converterList.remove(i);
                converterList.add(i, converter);
                break;
            }
        }
        restTemplate.setMessageConverters(converterList);
        return restTemplate;
    }

    @Bean
    public HttpRequestService httpRequestService() {
        return new HttpRequestService();
    }


    @Bean
    public TomcatServletWebServerFactory tomcatServletWebServerFactory() {
        TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
        factory.addConnectorCustomizers((TomcatConnectorCustomizer) connector -> {
            connector.setProperty("relaxedPathChars", "\"<>[\\]^`{|}");
            connector.setProperty("relaxedQueryChars", "\"<>[\\]^`{|}");
        });
        return factory;
    }
}
