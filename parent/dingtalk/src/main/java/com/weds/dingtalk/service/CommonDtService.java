package com.weds.dingtalk.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGetJsapiTicketRequest;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import com.weds.core.base.BaseService;
import com.weds.core.utils.MapCacheUtils;
import com.weds.dingtalk.constant.DingTalkParams;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CommonDtService extends BaseService {
    @Resource
    private DingTalkParams dingTalkParams;

    public OapiGettokenResponse getToken() throws ApiException {
        DefaultDingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getTokenUrl());
        OapiGettokenRequest request = new OapiGettokenRequest();
        request.setAppkey(dingTalkParams.getAppKey());
        request.setAppsecret(dingTalkParams.getAppSecret());
        request.setHttpMethod("GET");
        return client.execute(request);
    }

    public OapiMessageCorpconversationAsyncsendV2Response sendMessage(String receiveList,
                                                                      OapiMessageCorpconversationAsyncsendV2Request.Msg msg) throws ApiException {
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getMessageUrl());
        OapiMessageCorpconversationAsyncsendV2Request request = new OapiMessageCorpconversationAsyncsendV2Request();
        request.setUseridList(receiveList);
        request.setAgentId(dingTalkParams.getAgentId());
        request.setToAllUser(false);
        request.setMsg(msg);

        String accessToken = MapCacheUtils.single().get("token").toString();
        return client.execute(request, accessToken);
    }

    public OapiGetJsapiTicketResponse getTicket() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DefaultDingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getTicketUrl());
        OapiGetJsapiTicketRequest req = new OapiGetJsapiTicketRequest();
        req.setTopHttpMethod("GET");
        return client.execute(req, accessToken);
    }
}
