package com.weds.dingtalk.constant;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "weds.dingtalk")
public class DingTalkParams {
    // 企业ID
    private String corpId = "dingc048362c4f3b14d4";
    // 应用ID
    private Long agentId = 317949598L;
    // 应用Key
    private String appKey = "dingcqo6kb5goy8pdzkz";
    // 应用Secret
    private String appSecret = "Mxbmf64Anrl18zCTp14Cso4IlZqcZxsNMifpAujYW-wGZowzd00B92zRp7SXhe_Z";

    // APPID
    private String appId = "2016101700706657";
    // 支付网关
    private String gateway = "https://openapi.alipaydev.com/gateway.do";
    // 支付宝公钥
    private String aliPubKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv+gvM9GFQqOYVJ1kPeXy3UeVU7coIexVFUa02bB9ZPxQFvWbkYPudI8smyXrbH+hqdpkbe9+vkCx9DfbAhaZlLIhf0XDSXXCo6OTwqxqnpplptV8elC8a+rj917+hWl3xMo2375U5/RW46vRvfBEBi6hE+LzlaYmZxdYiEG9e0XYHbAkEX6tkVYM3FzHVtyNw2tXyul5wijUV2DancZ65tZBt2OEWQnQowcff9CkRZfa3PaWFkd2I/xm3pjLJnZAntyLNpbEfUjviVyI/tr1/P+JFH7+FiVhWWjqvq1r58jqn3VkSj4NJWi68eXTBbyFLdlLkdW7P/MFc3YWk8p2ZwIDAQAB";
    // 支付通知 Url
    private String notifyUrl = "http://222.173.219.54:20079/app/pay/alipayNotice";
    // 支付跳转 Url
    private String returnUrl = "http://222.173.219.54:11010/#/PayResult";
    // 支付私钥
    private String appPriKey = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCDaIC3y+gnv30lTPb6zz2fmhqVapAdmm8aUPNHwOfL8XKC3FjS/4R2MQ5tqnhLvj2q/NgsaQDn4GUoHcafsbUczAu7EvwSfgDiuwIEVPdGBAsqJ0JrW3jbzVKdZLzZNVl9hOeS+htoB0oZGEHvK96kE7x6fUkzM6WbapLxEkuF/FI2LCzDJY4lZ2OtVr8IIIWQX2hU1zrnY/FhODirs/cCaTEjRmo0xb/MJJ+LLNkQDa/zyVlzxzh8t0AFWW/9k5OPBYpG4dggd1Y+YEE62jHGNWCssqClCVpEvaxX6d5KZm2tmKwdh1Diz4ZPC6i2LXOL6emKTdyhGr6wcpiKpJ8pAgMBAAECggEAd/qpU6b0L55xp+Xutq8TZ66vSO8D6FOBz3hLt57UxOuNjxeabgPoMHW34786mIV5Dq5OvbWdoxjJo2gUWuhJIaLXrxW1Wzj6nAj83XGqV54Hh5RS22yoG6ALJzO8DWHddHItcY+0CDZnnCP+x125qPtwIjJT7FDiitUylrMKi6ivvJ0VSHh00c4hQNA7dr2WTwfoagDso+wcx3MEtoKI5T0MjVazFb9L9+ObTlIVKoEMqiV40JiiooHkJyTbtfIbtz93hILxbH+tKJqb6bGjNyd1tUbHkesXglUmI8LWRmeQeMG2KtBuOibvi4Wd5qWD6J8ciGFU2xO6lYkSbRxXBQKBgQDjdFy4EfXit8HLVD74KWdY6CcBqHPlH4snE6slqTq4bSxkVyWeBV5aTclSXVnzUbpiTn/DK0w8UZbrN9FYPZ/oaJW2PvMpPF95qGpZPfunnwUpf4Ld5K1fINNTAApZCEntf+wsgm9jfnN2SMw4zZ7eESicMv+yFwC+GWy5E9D+dwKBgQCT5l+XgsFK9deWMA7HRbSwyhhKZO9TlifTCWuQ5WSc4bdoeV9BKQX489yXLk478bcddVyJJxualrBU1SjlrXQKQl+PFQjns1Qux3G7A1pS5CyzCdQlZ+COCJZRLSeNzzxU6jv/NBzLu3O6I9tuKWjnEw/l3T32mWpUIYaFb3KXXwKBgC3I1oEW3G/unUvYgXRioTL8SB3FUs7kMpaDFwhVsEQ/dZgdEq1kV0HGNqglsl2QLfxr4Lvgk0/w/HgMnE8u2Gln4/rzYO8EABP1GrMsDuQG1nsr96PCzENB0Ef80hhp2re78EZlWxaj9YnxzWoBueDpg0st2Np4VuXtKD15B7CxAoGAEfvkSccTe+ry4QdM9LwsIYchdwJY+5/5jbbKhp7uIglsRN7lT0YOvNviSdvOlYlpy8I2k1LXD3AdVuNoiq0RvCGf/wT+xTXQGbQUxzm8JPIr4UVjr3y3GG5A4PQNqDe3zVeCHiEXE0ri4pJznyyX3ek6eKfzXeX/wOUmt1j65R8CgYAQNDA3ivWcFz3o7ZbdXkvoKgUfWtem9j+/Ps5v+goZNyOyX2QKQgsFFQ59ae0XVYJ0XMLefgTZVF2k29d/hmDFSfApM5MLY9I7mrepOI4b387uEBHR4Xa2pXJpDETprKdxFy12M6GSg8ZdZ+K6xw7qsqnksTivbuMgITwyLOrnpg==";
    // 支付ID
    private String partner = "2088102179963064";

    // 监听密钥
    private String aesKey = "xxxxxxxxlvdhntotr3x9qhlbytb18zyz5zxxxxxxxxx";
    // 监听事件
    private String registerList = "user_add_org,user_modify_org,user_leave_org,org_dept_create,org_dept_modify,org_dept_remove,check_in";

    // Token Url
    private String tokenUrl = "https://oapi.dingtalk.com/gettoken";

    private String ticketUrl = "https://oapi.dingtalk.com/get_jsapi_ticket";

    // 消息 Url
    private String messageUrl = "https://oapi.dingtalk.com/topapi/message/corpconversation/asyncsend_v2";

    // 回调 Url
    private String callBackUrl = "http://47.105.169.158:20079/demo/register/callBackHandler";

    // 部门 Url
    private String insertDeptUrl = "https://oapi.dingtalk.com/department/create";

    private String updateDeptUrl = "https://oapi.dingtalk.com/department/update";

    private String deleteDeptUrl = "https://oapi.dingtalk.com/department/delete";

    private String selectDeptUrl = "https://oapi.dingtalk.com/department/get";

    private String deptListUrl = "https://oapi.dingtalk.com/department/list";

    // 用户 Url
    private String insertUserUrl = "https://oapi.dingtalk.com/user/create";

    private String updateUserUrl = "https://oapi.dingtalk.com/user/update";

    private String deleteUserUrl = "https://oapi.dingtalk.com/user/delete";

    private String selectUserUrl = "https://oapi.dingtalk.com/user/get";

    private String userUrl = "https://oapi.dingtalk.com/user/getuserinfo";

    private String userListUrl = "https://oapi.dingtalk.com/user/simplelist";

    private String userInfoListUrl = "https://oapi.dingtalk.com/user/listbypage";

    // 注册 Url
    private String insertRegisterUrl = "https://oapi.dingtalk.com/call_back/register_call_back";

    private String selectRegisterUrl = "https://oapi.dingtalk.com/call_back/get_call_back";

    private String updateRegisterUrl = "https://oapi.dingtalk.com/call_back/update_call_back";

    private String deleteRegisterUrl = "https://oapi.dingtalk.com/call_back/delete_call_back";

    private String failRegisterUrl = "https://oapi.dingtalk.com/call_back/get_call_back_failed_result";

    // 考勤 Url
    private String selectAttenListUrl = "https://oapi.dingtalk.com/attendance/listRecord";

    private String selectAttenResultUrl = "https://oapi.dingtalk.com/attendance/list";

    private String selectCheckInUrl = "https://oapi.dingtalk.com/topapi/checkin/record/get";

    private String selectDeptCheckInUrl = "https://oapi.dingtalk.com/checkin/record";

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public Long getAgentId() {
        return agentId;
    }

    public void setAgentId(Long agentId) {
        this.agentId = agentId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    public String getAesKey() {
        return aesKey;
    }

    public void setAesKey(String aesKey) {
        this.aesKey = aesKey;
    }

    public String getRegisterList() {
        return registerList;
    }

    public void setRegisterList(String registerList) {
        this.registerList = registerList;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getMessageUrl() {
        return messageUrl;
    }

    public void setMessageUrl(String messageUrl) {
        this.messageUrl = messageUrl;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    public String getDeptListUrl() {
        return deptListUrl;
    }

    public void setDeptListUrl(String deptListUrl) {
        this.deptListUrl = deptListUrl;
    }

    public String getUserUrl() {
        return userUrl;
    }

    public void setUserUrl(String userUrl) {
        this.userUrl = userUrl;
    }

    public String getUserListUrl() {
        return userListUrl;
    }

    public void setUserListUrl(String userListUrl) {
        this.userListUrl = userListUrl;
    }

    public String getUserInfoListUrl() {
        return userInfoListUrl;
    }

    public void setUserInfoListUrl(String userInfoListUrl) {
        this.userInfoListUrl = userInfoListUrl;
    }

    public String getInsertRegisterUrl() {
        return insertRegisterUrl;
    }

    public void setInsertRegisterUrl(String insertRegisterUrl) {
        this.insertRegisterUrl = insertRegisterUrl;
    }

    public String getSelectRegisterUrl() {
        return selectRegisterUrl;
    }

    public void setSelectRegisterUrl(String selectRegisterUrl) {
        this.selectRegisterUrl = selectRegisterUrl;
    }

    public String getUpdateRegisterUrl() {
        return updateRegisterUrl;
    }

    public void setUpdateRegisterUrl(String updateRegisterUrl) {
        this.updateRegisterUrl = updateRegisterUrl;
    }

    public String getDeleteRegisterUrl() {
        return deleteRegisterUrl;
    }

    public void setDeleteRegisterUrl(String deleteRegisterUrl) {
        this.deleteRegisterUrl = deleteRegisterUrl;
    }

    public String getFailRegisterUrl() {
        return failRegisterUrl;
    }

    public void setFailRegisterUrl(String failRegisterUrl) {
        this.failRegisterUrl = failRegisterUrl;
    }

    public String getInsertUserUrl() {
        return insertUserUrl;
    }

    public void setInsertUserUrl(String insertUserUrl) {
        this.insertUserUrl = insertUserUrl;
    }

    public String getUpdateUserUrl() {
        return updateUserUrl;
    }

    public void setUpdateUserUrl(String updateUserUrl) {
        this.updateUserUrl = updateUserUrl;
    }

    public String getDeleteUserUrl() {
        return deleteUserUrl;
    }

    public void setDeleteUserUrl(String deleteUserUrl) {
        this.deleteUserUrl = deleteUserUrl;
    }

    public String getSelectUserUrl() {
        return selectUserUrl;
    }

    public void setSelectUserUrl(String selectUserUrl) {
        this.selectUserUrl = selectUserUrl;
    }

    public String getInsertDeptUrl() {
        return insertDeptUrl;
    }

    public void setInsertDeptUrl(String insertDeptUrl) {
        this.insertDeptUrl = insertDeptUrl;
    }

    public String getUpdateDeptUrl() {
        return updateDeptUrl;
    }

    public void setUpdateDeptUrl(String updateDeptUrl) {
        this.updateDeptUrl = updateDeptUrl;
    }

    public String getDeleteDeptUrl() {
        return deleteDeptUrl;
    }

    public void setDeleteDeptUrl(String deleteDeptUrl) {
        this.deleteDeptUrl = deleteDeptUrl;
    }

    public String getSelectDeptUrl() {
        return selectDeptUrl;
    }

    public void setSelectDeptUrl(String selectDeptUrl) {
        this.selectDeptUrl = selectDeptUrl;
    }

    public String getSelectAttenListUrl() {
        return selectAttenListUrl;
    }

    public void setSelectAttenListUrl(String selectAttenListUrl) {
        this.selectAttenListUrl = selectAttenListUrl;
    }

    public String getSelectCheckInUrl() {
        return selectCheckInUrl;
    }

    public void setSelectCheckInUrl(String selectCheckInUrl) {
        this.selectCheckInUrl = selectCheckInUrl;
    }

    public String getSelectDeptCheckInUrl() {
        return selectDeptCheckInUrl;
    }

    public void setSelectDeptCheckInUrl(String selectDeptCheckInUrl) {
        this.selectDeptCheckInUrl = selectDeptCheckInUrl;
    }

    public String getSelectAttenResultUrl() {
        return selectAttenResultUrl;
    }

    public void setSelectAttenResultUrl(String selectAttenResultUrl) {
        this.selectAttenResultUrl = selectAttenResultUrl;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getAppPriKey() {
        return appPriKey;
    }

    public void setAppPriKey(String appPriKey) {
        this.appPriKey = appPriKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getAliPubKey() {
        return aliPubKey;
    }

    public void setAliPubKey(String aliPubKey) {
        this.aliPubKey = aliPubKey;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }
}

