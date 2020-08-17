package com.weds.rpt.constants;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.rpt")
public class RptParams {
    // 报表配置地址
    private String settingPath;
    // 报表模板地址
    private String templatePath;

    public String getSettingPath() {
        return settingPath;
    }

    public void setSettingPath(String settingPath) {
        this.settingPath = settingPath;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }
}
