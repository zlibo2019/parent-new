package com.weds.bean.logs;

import com.weds.core.utils.StringUtils;

import java.io.Serializable;

public class LogEntity implements Serializable {
    private String url;
    private String clazzName;
    private String position;
    private String methodName;
    private String methodDesc;
    private String argsName;
    private String argsValue;
    private String offTime;
    private String currDate;
    private String errMessage;
    private String jsonResult;
    private String ip;
    private String user;
    private String method;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClazzName() {
        return clazzName;
    }

    public void setClazzName(String clazzName) {
        this.clazzName = clazzName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getMethodDesc() {
        return methodDesc;
    }

    public void setMethodDesc(String methodDesc) {
        this.methodDesc = methodDesc;
    }

    public String getArgsName() {
        return argsName;
    }

    public void setArgsName(String argsName) {
        this.argsName = argsName;
    }

    public String getArgsValue() {
        return argsValue;
    }

    public void setArgsValue(String argsValue) {
        this.argsValue = argsValue;
    }

    public String getOffTime() {
        return offTime;
    }

    public void setOffTime(String offTime) {
        this.offTime = offTime;
    }

    public String getCurrDate() {
        return currDate;
    }

    public void setCurrDate(String currDate) {
        this.currDate = currDate;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getJsonResult() {
        return jsonResult;
    }

    public void setJsonResult(String jsonResult) {
        this.jsonResult = jsonResult;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String toString() {
        return "\n" + "类名称:" + clazzName + "\n" +
                "日志位置:" + position + "\n" +
                "请求地址:" + url + "\n" +
                "请求IP:" + ip + "\n" +
                "请求方式:" + method + "\n" +
                "请求用户:" + user + "\n" +
                "方法名称:" + methodName + "\n" +
                "方法描述:" + methodDesc + "\n" +
                "方法参数:" + argsName + "\n" +
                "参数内容:" + argsValue + "\n" +
                "返回结果:" + jsonResult + "\n" +
                "方法耗时:" + offTime + "\n" +
                "日志日期:" + currDate +
                (!StringUtils.isBlank(errMessage) ? "\n" + "错误信息：" + errMessage : "");
    }
}
