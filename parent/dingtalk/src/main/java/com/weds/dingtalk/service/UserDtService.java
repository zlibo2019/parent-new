package com.weds.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.base.BaseService;
import com.weds.core.utils.MapCacheUtils;
import com.weds.dingtalk.constant.DingTalkParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserDtService extends BaseService {
    @Resource
    private DingTalkParams dingTalkParams;

    public OapiUserGetuserinfoResponse getUserId(String code) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUserUrl());
        OapiUserGetuserinfoRequest request = new OapiUserGetuserinfoRequest();
        request.setCode(code);
        request.setHttpMethod("GET");
        String accessToken = MapCacheUtils.single().get("token").toString();
        return client.execute(request, accessToken);
    }

    public OapiUserCreateResponse insertUserInfo(OapiUserCreateRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getInsertUserUrl());
        return client.execute(request, accessToken);
    }

    public OapiUserUpdateResponse updateUserInfo(OapiUserUpdateRequest request) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUpdateUserUrl());
        return client.execute(request, accessToken);
    }

    public OapiUserDeleteResponse deleteUserInfo(String userId) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getDeleteUserUrl());
        OapiUserDeleteRequest request = new OapiUserDeleteRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public OapiUserGetResponse selectUserInfo(String userId) throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectUserUrl());
        OapiUserGetRequest request = new OapiUserGetRequest();
        request.setUserid(userId);
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public OapiUserSimplelistResponse getUserList(Long deptId, Long offset, Long pageSize) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUserListUrl());
        OapiUserSimplelistRequest request = new OapiUserSimplelistRequest();
        request.setDepartmentId(deptId);
        if (offset != null && pageSize != null) {
            request.setOffset(offset);
            request.setSize(pageSize);
        }
        request.setHttpMethod("GET");
        String accessToken = MapCacheUtils.single().get("token").toString();
        return client.execute(request, accessToken);
    }

    public OapiUserListbypageResponse getUserInfoList(Long deptId, Long offset, Long pageSize) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUserInfoListUrl());
        OapiUserListbypageRequest request = new OapiUserListbypageRequest();
        request.setDepartmentId(deptId);
        request.setOffset(offset);
        request.setSize(pageSize);
        request.setOrder("entry_desc");
        request.setHttpMethod("GET");
        String accessToken = MapCacheUtils.single().get("token").toString();
        return client.execute(request, accessToken);
    }
}
