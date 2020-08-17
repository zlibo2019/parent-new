package com.weds.pay.lcsw.service;


import com.alibaba.fastjson.JSONObject;
import com.weds.core.utils.coder.Coder;
import com.weds.pay.lcsw.constants.LcswConstants;
import com.weds.pay.lcsw.constants.LcswParams;
import com.weds.pay.lcsw.entity.*;
import com.weds.pay.lcsw.utils.LcswUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.List;

@Component
public class LcswService {
    @Resource
    private LcswParams lcswParams;

    @Resource
    private RestTemplate restTemplate;

    public PayPreRstResp wxPrePay(PayPreReq payPreReq) throws Exception {
        payPreReq.setPayVer("100");
        payPreReq.setPayType("010");
        payPreReq.setServiceId("012");
        payPreReq.setMerchantNo(lcswParams.getMerchantNo());
        payPreReq.setTerminalId(lcswParams.getTerminalId());

        JSONObject temp1 = JSONObject.parseObject(JSONObject.toJSONString(payPreReq));
        StringBuilder sb1 = new StringBuilder();
        List<String> list1 = LcswUtils.sign(PayPreReq.class);
        for (String key : list1) {
            sb1.append(key).append("=").append(temp1.getString(key)).append("&");
        }
        // for (String key : temp1.keySet()) {
        //     sb1.append(key).append("=").append(temp1.getString(key)).append("&");
        // }
        sb1.append("access_token").append("=").append(lcswParams.getAccessToken());
        payPreReq.setKeySign(Coder.md5Hex(sb1.toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JSONObject.toJSONString(payPreReq), headers);
        String resp = restTemplate.postForObject(lcswParams.getWxPrePayUrl(), request, String.class);
        LcswResp lcswResp = JSONObject.parseObject(resp, LcswResp.class);
        if (lcswResp == null || !LcswConstants.HTTP_SUCCESS.equals(lcswResp.getReturnCode())) {
            return null;
        }

        PayPreResp payPreResp = JSONObject.parseObject(resp, PayPreResp.class);
        if (payPreResp == null || !LcswConstants.HTTP_SUCCESS.equals(payPreResp.getResultCode())) {
            return null;
        }

        JSONObject temp2 = JSONObject.parseObject(JSONObject.toJSONString(payPreResp));
        StringBuilder sb2 = new StringBuilder();
        List<String> list2 = LcswUtils.sign(PayPreResp.class);
        for (String key : list2) {
            sb2.append(key).append("=").append(temp2.getString(key)).append("&");
        }

        // for (String key : temp2.keySet()) {
        //     sb2.append(key).append("=").append(temp2.getString(key)).append("&");
        // }
        sb2.append("access_token").append("=").append(lcswParams.getAccessToken());
        if (!lcswResp.getKeySign().equals(Coder.md5Hex(sb2.toString()))) {
            return null;
        }

        PayPreRstResp payPreRstResp = JSONObject.parseObject(resp, PayPreRstResp.class);
        payPreRstResp.setPayPreResp(payPreResp);
        return payPreRstResp;
    }


    public NoticeReq payNotice(NoticeReq noticeReq) {
        JSONObject temp = JSONObject.parseObject(JSONObject.toJSONString(noticeReq));
        StringBuilder sb = new StringBuilder();
        List<String> list = LcswUtils.sign(NoticeReq.class);
        for (String key : list) {
            sb.append(key).append("=").append(temp.getString(key)).append("&");
        }
        sb.append("access_token").append("=").append(lcswParams.getAccessToken());
        if (!noticeReq.getKeySign().equals(Coder.md5Hex(sb.toString()))) {
            return null;
        }
        return noticeReq;
    }

    public PayQueryRstResp payQuery(PayQueryReq payQueryReq) throws Exception {
        payQueryReq.setPayVer("100");
        payQueryReq.setPayType("010");
        payQueryReq.setServiceId("020");

        payQueryReq.setMerchantNo(lcswParams.getMerchantNo());
        payQueryReq.setTerminalId(lcswParams.getTerminalId());

        JSONObject temp1 = JSONObject.parseObject(JSONObject.toJSONString(payQueryReq));
        StringBuilder sb1 = new StringBuilder();
        List<String> list1 = LcswUtils.sign(PayQueryReq.class);
        for (String key : list1) {
            sb1.append(key).append("=").append(temp1.getString(key)).append("&");
        }
        sb1.append("access_token").append("=").append(lcswParams.getAccessToken());
        payQueryReq.setKeySign(Coder.md5Hex(sb1.toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(JSONObject.toJSONString(payQueryReq), headers);
        String resp = restTemplate.postForObject(lcswParams.getPayQueryUrl(), request, String.class);
        LcswResp lcswResp = JSONObject.parseObject(resp, LcswResp.class);
        if (lcswResp == null || !LcswConstants.HTTP_SUCCESS.equals(lcswResp.getReturnCode())) {
            return null;
        }

        PayQueryResp payQueryResp = JSONObject.parseObject(resp, PayQueryResp.class);
        if (payQueryResp == null || !LcswConstants.HTTP_SUCCESS.equals(payQueryResp.getResultCode())) {
            return null;
        }

        JSONObject temp2 = JSONObject.parseObject(JSONObject.toJSONString(payQueryResp));
        StringBuilder sb2 = new StringBuilder();
        List<String> list2 = LcswUtils.sign(PayQueryResp.class);
        for (String key : list2) {
            sb2.append(key).append("=").append(temp2.getString(key)).append("&");
        }

        // for (String key : temp2.keySet()) {
        //     sb2.append(key).append("=").append(temp2.getString(key)).append("&");
        // }
        sb2.append("access_token").append("=").append(lcswParams.getAccessToken());
        if (!lcswResp.getKeySign().equals(Coder.md5Hex(sb2.toString()))) {
            return null;
        }

        PayQueryRstResp payQueryRstResp = JSONObject.parseObject(resp, PayQueryRstResp.class);
        payQueryRstResp.setPayQueryResp(payQueryResp);
        return payQueryRstResp;
    }
}
