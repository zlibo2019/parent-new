package com.weds.bank.abc;

import com.abc.pay.client.JSON;
import com.abc.pay.client.ebus.PaymentRequest;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.weds.core.utils.DateUtils;
import com.weds.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedHashMap;

@Service
public class AbcPayService {

    @Resource
    private HttpServletRequest request;

    public JSON onlinePay(String orderNo, String productName, String amt, CommodityType commodityType, String desc) {
        PaymentRequest paymentRequest = new PaymentRequest();
        Date date = new Date();

        LinkedHashMap dicOrder = paymentRequest.dicOrder;
        dicOrder.put("PayTypeID", "ImmediatePay");
        dicOrder.put("CurrencyCode", "156");
        dicOrder.put("InstallmentMark", "0");
        dicOrder.put("BuyIP", request.getRemoteAddr());
        dicOrder.put("OrderDate", DateUtils.formatDate(date, "yyyy/MM/dd"));
        dicOrder.put("OrderTime", DateUtils.formatDate(date, "hh:mm:ss"));
        dicOrder.put("OrderNo", orderNo);
        dicOrder.put("OrderAmount", amt);
        dicOrder.put("CommodityType", commodityType.getType());
        dicOrder.put("OrderDesc", desc);

        LinkedHashMap orderitems = paymentRequest.orderitems;
        orderitems.put("ProductName", productName);

        LinkedHashMap dicRequest = paymentRequest.dicRequest;
        dicRequest.put("PaymentType", "A");
        dicRequest.put("PaymentLinkType", "2");
        dicRequest.put("UnionPayLinkType", "0");
        dicRequest.put("NotifyType", "1");
        dicRequest.put("ResultNotifyURL", "");
        dicRequest.put("IsBreakAccount", "0");

        return paymentRequest.postRequest();
    }
}
