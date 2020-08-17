package com.weds.bean.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * SwaggerParams  自动配置 属性类
 *
 * @author Administrator
 */
@ConfigurationProperties(prefix = "swagger")
public class SwaggerParams {
    // 包名
    private String basePackage = "com.weds";
    // 标题
    private String title = "API";
    // 描述
    private String description = "API";
    //
    private String termsOfServiceUrl = "";
    // 作者署名
    private String contact = "";
    // 版本
    private String version = "";

    private boolean active;

    public String getBasePackage() {
        return basePackage;
    }

    public void setBasePackage(String basePackage) {
        this.basePackage = basePackage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTermsOfServiceUrl() {
        return termsOfServiceUrl;
    }

    public void setTermsOfServiceUrl(String termsOfServiceUrl) {
        this.termsOfServiceUrl = termsOfServiceUrl;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @ConfigurationProperties(prefix = "swagger.authorization")
    public class Authorization {
        private String name = "Authorization";
        private String type = "String";
        private String desc = "Jwt Token";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}

