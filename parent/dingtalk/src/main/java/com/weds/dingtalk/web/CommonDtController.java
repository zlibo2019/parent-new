package com.weds.dingtalk.web;

import com.dingtalk.api.request.OapiMessageCorpconversationAsyncsendV2Request;
import com.dingtalk.api.response.OapiGetJsapiTicketResponse;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.dingtalk.api.response.OapiMessageCorpconversationAsyncsendV2Response;
import com.taobao.api.ApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.MapCacheUtils;
import com.weds.dingtalk.constant.DingTalkParams;
import com.weds.dingtalk.service.CommonDtService;
import com.weds.dingtalk.util.DtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/common")
@Api(value = "钉钉公共对接接口", description = "钉钉公共对接接口")
public class CommonDtController extends BaseController {
    @Resource
    private CommonDtService commonDtService;

    @Resource
    private DingTalkParams dingTalkParams;

    // @Logs
    @ApiOperation(value = "获取Token", notes = "获取Token")
    @RequestMapping(value = "/getToken", method = RequestMethod.GET)
    public JsonResult getToken() {
        try {
            OapiGettokenResponse response = commonDtService.getToken();
            if (response.getErrcode() == 0) {
                MapCacheUtils.single().set("token", response.getAccessToken());
                return succMsgData(response);
            } else {
                return failMsgData(response);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return failMsg();
        }
    }

    // @Logs
    @ApiOperation(value = "获取Ticket", notes = "获取Ticket")
    @RequestMapping(value = "/getTicket", method = RequestMethod.GET)
    public JsonResult getTicket() {
        try {
            OapiGetJsapiTicketResponse response = commonDtService.getTicket();
            if (response.getErrcode() == 0) {
                MapCacheUtils.single().set("ticket", response.getTicket());
                return succMsgData(response);
            } else {
                return failMsgData(response);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return failMsg();
        }
    }

    @Logs
    @ApiOperation(value = "获取公共信息", notes = "获取公共信息")
    @RequestMapping(value = "/getDtConfig", method = RequestMethod.GET)
    public JsonResult getDtConfig(String url) {
        Map<String, Object> map = new HashMap<>();
        map.put("corpId", dingTalkParams.getCorpId());
        map.put("agentId", dingTalkParams.getAgentId());
        long timeStamp = System.currentTimeMillis();
        map.put("timeStamp", timeStamp);
        String nonceStr = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
        map.put("nonceStr", nonceStr);
        String ticket = MapCacheUtils.single().get("ticket").toString();
        String signature = DtUtils.sign(ticket, nonceStr, timeStamp, url);
        map.put("signature", signature);
        return succMsgData(map);
    }

    @Logs
    @ApiOperation(value = "发送消息", notes = "发送消息")
    @RequestMapping(value = "/sendMessage", method = RequestMethod.GET)
    public JsonResult sendMessage(@RequestParam String receiveList, @RequestParam String type) {
        OapiMessageCorpconversationAsyncsendV2Request.Msg msg = new OapiMessageCorpconversationAsyncsendV2Request.Msg();
        switch (type) {
            case "1":
                msg.setMsgtype("text");
                msg.setText(new OapiMessageCorpconversationAsyncsendV2Request.Text());
                msg.getText().setContent("test123");
                break;
            case "2":
                msg.setMsgtype("image");
                msg.setImage(new OapiMessageCorpconversationAsyncsendV2Request.Image());
                msg.getImage().setMediaId("@lADOdvRYes0CbM0CbA");
                break;
            case "3":
                msg.setMsgtype("file");
                msg.setFile(new OapiMessageCorpconversationAsyncsendV2Request.File());
                msg.getFile().setMediaId("@lADOdvRYes0CbM0CbA");
                break;
            case "4":
                msg.setMsgtype("link");
                msg.setLink(new OapiMessageCorpconversationAsyncsendV2Request.Link());
                msg.getLink().setTitle("test");
                msg.getLink().setText("test");
                msg.getLink().setMessageUrl("test");
                msg.getLink().setPicUrl("test");
                break;
            case "5":
                msg.setMsgtype("markdown");
                msg.setMarkdown(new OapiMessageCorpconversationAsyncsendV2Request.Markdown());
                msg.getMarkdown().setText("##### text");
                msg.getMarkdown().setTitle("### Title");
                break;
            case "6":
                msg.setOa(new OapiMessageCorpconversationAsyncsendV2Request.OA());
                msg.getOa().setHead(new OapiMessageCorpconversationAsyncsendV2Request.Head());
                msg.getOa().getHead().setText("head");
                msg.getOa().setBody(new OapiMessageCorpconversationAsyncsendV2Request.Body());
                msg.getOa().getBody().setContent("xxx");
                msg.setMsgtype("oa");
                break;
            default:
                msg.setActionCard(new OapiMessageCorpconversationAsyncsendV2Request.ActionCard());
                msg.getActionCard().setTitle("xxx123411111");
                msg.getActionCard().setMarkdown("### 测试123111");
                msg.getActionCard().setSingleTitle("测试测试");
                msg.getActionCard().setSingleUrl("https://www.baidu.com");
                msg.setMsgtype("action_card");
        }

        try {
            OapiMessageCorpconversationAsyncsendV2Response response = commonDtService.sendMessage(receiveList, msg);
            if (response.getErrcode() == 0) {
                return succMsgData(response);
            } else {
                return failMsgData(response);
            }
        } catch (ApiException e) {
            e.printStackTrace();
            return failMsg();
        }
    }
}
