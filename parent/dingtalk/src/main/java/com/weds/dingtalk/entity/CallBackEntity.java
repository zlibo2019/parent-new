package com.weds.dingtalk.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

public class CallBackEntity {
    @JSONField(name = "EventType")
    private String eventType;
    @JSONField(name = "TimeStamp")
    private String timeStamp;
    @JSONField(name = "CorpId")
    private String corpId;
    @JSONField(name = "StaffId")
    private String staffId;
    @JSONField(name = "UserId")
    private List<String> userId;
    @JSONField(name = "DeptId")
    private List<String> deptId;

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public List<String> getDeptId() {
        return deptId;
    }

    public void setDeptId(List<String> deptId) {
        this.deptId = deptId;
    }
}
