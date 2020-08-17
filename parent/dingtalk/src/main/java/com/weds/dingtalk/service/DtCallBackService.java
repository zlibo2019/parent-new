package com.weds.dingtalk.service;

import com.dingtalk.api.response.OapiCheckinRecordGetResponse;
import com.dingtalk.api.response.OapiDepartmentGetResponse;
import com.dingtalk.api.response.OapiUserGetResponse;

import java.util.List;

public interface DtCallBackService {

    void userDtAddCallBack(List<OapiUserGetResponse> list);

    void userDtUpdateCallBack(List<OapiUserGetResponse> list);

    void userDtDeleteCallBack(List<String> list);

    void deptDtAddCallBack(List<OapiDepartmentGetResponse> list);

    void deptDtUpdateCallBack(List<OapiDepartmentGetResponse> list);

    void deptDtDeleteCallBack(List<String> list);

    void checkDtCallBack(List<OapiCheckinRecordGetResponse.CheckinRecordVo> list);
}
