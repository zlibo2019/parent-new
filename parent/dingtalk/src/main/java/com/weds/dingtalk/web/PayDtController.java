package com.weds.dingtalk.web;

import com.alipay.api.AlipayApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.StringUtils;
import com.weds.dingtalk.service.PayDtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/pay")
@Api(value = "钉钉支付对接接口", description = "钉钉支付对接接口")
public class PayDtController extends BaseController {
    @Resource
    private PayDtService payDtService;

    @Logs
    @ApiOperation(value = "获取支付参数", notes = "获取支付参数")
    @RequestMapping(value = "/getPayInfo", method = RequestMethod.GET)
    public JsonResult getPayInfo(@RequestParam String fee) {
        String tradeNo = StringUtils.getUUID();
        try {
            String params = payDtService.panyInfo(tradeNo, fee);
            return succMsgData(params);
        } catch (Exception e) {
            e.printStackTrace();
            return failMsg();
        }
    }

    @Logs
    @ApiOperation(value = "支付宝回调方法", notes = "支付宝回调方法")
    @RequestMapping(value = "/alipayNotice", method = RequestMethod.POST)
    public String alipayNotice(@RequestBody String paramsStr) {
        System.out.println(paramsStr);
        return "success";
    }

    @Logs
    @ApiOperation(value = "支付宝支付方法", notes = "支付宝支付方法")
    @RequestMapping(value = "/redirectPay", method = RequestMethod.GET)
    public JsonResult redirectPay(@RequestParam String fee) {
        try {
            String tradeNo = StringUtils.getUUID();
            String form = payDtService.redirectPay(tradeNo, fee);
            return succMsgData(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
            return failMsg();
        }
    }
}
