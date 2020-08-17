package com.weds.core.license;

import java.util.Arrays;

public class InfoEntity {
    // 版本号
    private String version;
    // 模块名称
    private String[] module;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String[] getModule() {
        Arrays.sort(module);
        return module;
    }

    public void setModule(String[] module) {
        this.module = module;
    }
}