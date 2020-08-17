package com.weds.dingtalk.service;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.weds.core.base.BaseService;
import com.weds.core.utils.StringUtils;
import com.weds.core.utils.coder.RSACoder;
import com.weds.dingtalk.constant.DingTalkParams;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

@Service
public class PayDtService extends BaseService {
    @Resource
    private DingTalkParams dingTalkParams;

    public String panyInfo(String tradeNo, String fee) throws Exception {
        StringBuilder sb = new StringBuilder();
        Map<String, Object> params = new HashMap<>();
        params.put("service", "mobile.securitypay.pay");
        params.put("partner", dingTalkParams.getPartner());
        params.put("_input_charset", "UTF-8");
        params.put("notify_url", dingTalkParams.getNotifyUrl());
        params.put("out_trade_no", tradeNo);
        params.put("subject", "充值");
        params.put("payment_type", "1");
        params.put("seller_id", dingTalkParams.getPartner());
        params.put("total_fee", fee);
        params.put("body", "威尔-联机充值");
        for (String key : params.keySet()) {
            sb.append(key).append("=\"").append(params.get(key)).append("\"&");
        }
        String sign = RSACoder.sign(sb.substring(0, sb.length() - 1).getBytes(), dingTalkParams.getAppPriKey());
        sb.append("sign_type=\"RSA\"&");
        sb.append("sign=\"").append(URLEncoder.encode(sign, "UTF-8")).append("\"");
        return sb.toString();
    }

    public String redirectPay(String tradeNo, String fee) throws AlipayApiException {
        AlipayClient alipayClient = new DefaultAlipayClient(dingTalkParams.getGateway(), dingTalkParams.getAppId(),
                dingTalkParams.getAppPriKey(), "json", "UTF-8", dingTalkParams.getAliPubKey(), "RSA2"); //获得初始化的AlipayClient
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        String returnUrl = MessageFormat.format(dingTalkParams.getReturnUrl(), tradeNo);
        alipayRequest.setReturnUrl(returnUrl);
        alipayRequest.setNotifyUrl(dingTalkParams.getNotifyUrl());//在公共参数中设置回跳和通知地址
        Map<String, Object> params = new HashMap<>();
        params.put("product_code", "QUICK_WAP_PAY");
        params.put("out_trade_no", tradeNo);
        params.put("subject", "充值");
        params.put("total_amount", fee);
        alipayRequest.setBizContent(toJson(params));
        return alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
    }
}
