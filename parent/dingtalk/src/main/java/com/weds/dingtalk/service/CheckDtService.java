package com.weds.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiCheckinRecordGetRequest;
import com.dingtalk.api.request.OapiCheckinRecordRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiCheckinRecordGetResponse;
import com.dingtalk.api.response.OapiCheckinRecordResponse;
import com.taobao.api.ApiException;
import com.weds.core.utils.MapCacheUtils;
import com.weds.dingtalk.constant.DingTalkParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CheckDtService {
    @Resource
    private DingTalkParams dingTalkParams;

    public OapiAttendanceListRecordResponse getAttenDetailRecordList(OapiAttendanceListRecordRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectAttenListUrl());
        return client.execute(request, accessToken);
    }

    public OapiAttendanceListResponse getAttenResultRecordList(OapiAttendanceListRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectAttenResultUrl());
        return client.execute(request, accessToken);
    }

    public OapiCheckinRecordGetResponse getCheckInRecordList(OapiCheckinRecordGetRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectCheckInUrl());
        return client.execute(request, accessToken);
    }

    public OapiCheckinRecordResponse getDeptCheckInRecordList(OapiCheckinRecordRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectDeptCheckInUrl());
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }
}
