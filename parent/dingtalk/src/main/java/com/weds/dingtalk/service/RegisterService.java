package com.weds.dingtalk.service;

import com.alibaba.fastjson.JSONObject;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.*;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.base.BaseService;
import com.weds.core.utils.ByteUtils;
import com.weds.core.utils.MapCacheUtils;
import com.weds.core.utils.coder.AES7Coder;
import com.weds.core.utils.coder.AESCoder;
import com.weds.dingtalk.constant.DingTalkParams;
import com.weds.dingtalk.entity.CallBackEntity;
import com.weds.dingtalk.util.DtUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class RegisterService extends BaseService {
    @Resource
    private DingTalkParams dingTalkParams;

    public OapiCallBackRegisterCallBackResponse insertRegister() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getInsertRegisterUrl());
        OapiCallBackRegisterCallBackRequest request = new OapiCallBackRegisterCallBackRequest();
        request.setUrl(dingTalkParams.getCallBackUrl());
        request.setAesKey(dingTalkParams.getAesKey());
        request.setToken(accessToken);
        request.setCallBackTag(Arrays.asList(dingTalkParams.getRegisterList().split(",")));
        return client.execute(request, accessToken);
    }

    public OapiCallBackGetCallBackResponse selectRegister() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getSelectRegisterUrl());
        OapiCallBackGetCallBackRequest request = new OapiCallBackGetCallBackRequest();
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public OapiCallBackUpdateCallBackResponse updateRegister() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getUpdateRegisterUrl());
        OapiCallBackUpdateCallBackRequest request = new OapiCallBackUpdateCallBackRequest();
        request.setUrl(dingTalkParams.getCallBackUrl());
        request.setAesKey(dingTalkParams.getAesKey());
        request.setToken(accessToken);
        request.setCallBackTag(Arrays.asList(dingTalkParams.getRegisterList().split(",")));
        return client.execute(request, accessToken);
    }

    public OapiCallBackDeleteCallBackResponse deleteRegister() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getDeleteRegisterUrl());
        OapiCallBackDeleteCallBackRequest request = new OapiCallBackDeleteCallBackRequest();
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public OapiCallBackGetCallBackFailedResultResponse failRegister() throws ApiException {
        String accessToken = MapCacheUtils.single().get("token").toString();
        DingTalkClient client = new DefaultDingTalkClient(dingTalkParams.getFailRegisterUrl());
        OapiCallBackGetCallBackFailedResultRequest request = new OapiCallBackGetCallBackFailedResultRequest();
        request.setHttpMethod("GET");
        return client.execute(request, accessToken);
    }

    public String getRegisterResp() throws Exception {
        String accessToken = MapCacheUtils.single().get("token").toString();
        Map<String, String> map = new HashMap<>();
        String timestampStr = System.currentTimeMillis() + "";
        String nonceStr = RandomStringUtils.randomAlphanumeric(6);
        String content = "success";
        byte[] bytes1 = RandomStringUtils.randomAlphanumeric(16).getBytes();
        byte[] bytes2 = ByteUtils.intToBytesH(content.length());
        byte[] bytes = ByteUtils.byteMerger(bytes1, bytes2, content.getBytes(), dingTalkParams.getCorpId().getBytes());
        String encryptStr = AES7Coder.encryptBase64(new String(bytes), dingTalkParams.getAesKey() + "=", 16);
        String singTemp = DtUtils.sign(accessToken, timestampStr, nonceStr, encryptStr);
        map.put("msg_signature", singTemp);
        map.put("timeStamp", timestampStr);
        map.put("nonce", nonceStr);
        map.put("encrypt", encryptStr);
        return toJson(map);
    }

    public CallBackEntity parseRegisterResp(String encrypt) throws Exception {
        byte[] decoded = AESCoder.decryptBase64(encrypt, dingTalkParams.getAesKey() + "=", 16, AESCoder.CIPHER_ALGORITHM_NP);
        int msgLen = ByteUtils.bytesToIntH(decoded, 16);
        String resp = new String(Arrays.copyOfRange(decoded, 20, 20 + msgLen));
        return JSONObject.parseObject(resp, CallBackEntity.class);
    }
}
