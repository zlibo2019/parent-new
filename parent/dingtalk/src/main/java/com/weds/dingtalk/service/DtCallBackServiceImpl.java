package com.weds.dingtalk.service;

import com.dingtalk.api.response.OapiCheckinRecordGetResponse;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;
import com.weds.core.base.BaseService;

import java.util.List;

public class DtCallBackServiceImpl extends BaseService implements DtCallBackService {
    @Override
    public void userDtAddCallBack(List<OapiUserGetResponse> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void userDtUpdateCallBack(List<OapiUserGetResponse> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void userDtDeleteCallBack(List<String> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void deptDtAddCallBack(List<OapiDepartmentGetResponse> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void deptDtUpdateCallBack(List<OapiDepartmentGetResponse> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void deptDtDeleteCallBack(List<String> list) {
        System.out.println(toJson(list));
    }

    @Override
    public void checkDtCallBack(List<OapiCheckinRecordGetResponse.CheckinRecordVo> list) {
        System.out.println(toJson(list));
    }
}
