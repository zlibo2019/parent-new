package com.weds.bean.swagger;

import com.weds.bean.jwt.JwtParams;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SWAGGER2 自动配置类
 */
@Configuration
@EnableSwagger2
@EnableConfigurationProperties({SwaggerParams.class, SwaggerParams.Authorization.class})
@ConditionalOnProperty(name = "swagger.active", matchIfMissing = true, havingValue = "true")
@Import(BeanValidatorPluginsConfiguration.class)
public class SwaggerConfig {
    @Resource
    private SwaggerParams properties;

    @Resource
    private SwaggerParams.Authorization authorization;

    @Resource
    private JwtParams jwtParams;

    @Bean
    public Docket createRestApi() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        if (null != authorization && null != authorization.getName()) {
            //添加head参数start
            ParameterBuilder tokenPar = new ParameterBuilder();
            List<Parameter> pars = new ArrayList<>();
            tokenPar.name(authorization.getName())
                    .description(authorization.getDesc())
                    .modelRef(new ModelRef(authorization.getType()))
                    .parameterType("Header")
                    .required(jwtParams.isActive())
                    .build();
            pars.add(tokenPar.build());
            docket = docket.globalOperationParameters(pars);
        }
        return docket.apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(properties.getBasePackage()))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title(properties.getTitle())
                .description(properties.getDescription())
                .termsOfServiceUrl(properties.getTermsOfServiceUrl())
                .contact(new Contact("威尔数据", "http://www.weds.com.cn", "admin@weds.com.cn")).license("Apache 2.0")
                .licenseUrl("http://www.apache.org/licenses/LICENSE-2.0.html")
                .version(properties.getVersion()).build();
    }
}
