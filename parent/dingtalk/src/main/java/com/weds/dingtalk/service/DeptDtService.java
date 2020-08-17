package com.weds.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.utils.MapCacheUtils;
import com.weds.dingtalk.constant.DingTalkParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class DeptDtService {
    @Resource
    private DingTalkParams dingTalkParams;

    public OapiDepartmentListResponse getDeptList(String parentId) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getDeptListUrl());
        OapiDepartmentListRequest request = new OapiDepartmentListRequest();
        request.setId(parentId);
        request.setHttpMethod("GET");
        String accessToken = MapCacheUtils.single().get("token").toString();
        return client.execute(request, accessToken);
    }

    public OapiDepartmentCreateResponse insertDeptInfo(OapiDepartmentCreateRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectDeptUrl());
        return client.execute(request, accessToken);
    }

    public OapiDepartmentUpdateResponse updateDeptInfo(OapiDepartmentUpdateRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUpdateDeptUrl());
        return client.execute(request, accessToken);
    }

    public OapiDepartmentDeleteResponse deleteDeptInfo(String deptId) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getDeleteDeptUrl());
        OapiDepartmentDeleteRequest request = new OapiDepartmentDeleteRequest();
        request.setId(deptId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public OapiDepartmentGetResponse selectDeptInfo(String deptId) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectDeptUrl());
        OapiDepartmentGetRequest request = new OapiDepartmentGetRequest();
        request.setId(deptId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }
}
