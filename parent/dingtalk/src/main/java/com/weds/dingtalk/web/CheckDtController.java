package com.weds.dingtalk.web;

import com.dingtalk.api.request.OapiAttendanceListRecordRequest;
import com.dingtalk.api.request.OapiAttendanceListRequest;
import com.dingtalk.api.request.OapiCheckinRecordGetRequest;
import com.dingtalk.api.request.OapiCheckinRecordRequest;
import com.dingtalk.api.response.OapiAttendanceListRecordResponse;
import com.dingtalk.api.response.OapiAttendanceListResponse;
import com.dingtalk.api.response.OapiCheckinRecordGetResponse;
import com.dingtalk.api.response.OapiCheckinRecordResponse;
import com.taobao.api.ApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.dingtalk.service.CheckDtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/check")
@Api(value = "钉钉签到对接接口", description = "钉钉签到对接接口")
public class CheckDtController extends BaseController {
    @Resource
    private CheckDtService checkDtService;

    @Logs
    @ApiOperation(value = "获取考勤详情信息", notes = "获取考勤信息")
    @RequestMapping(value = "/selectRecordDetailList", method = RequestMethod.POST)
    public JsonResult selectRecordDetailList(@RequestBody OapiAttendanceListRecordRequest request) {
        try {
            OapiAttendanceListRecordResponse response = checkDtService.getAttenDetailRecordList(request);
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

    @Logs
    @ApiOperation(value = "获取考勤结果信息", notes = "获取考勤信息")
    @RequestMapping(value = "/selectRecordResultList", method = RequestMethod.POST)
    public JsonResult selectRecordResultList(@RequestBody OapiAttendanceListRequest request) {
        try {
            OapiAttendanceListResponse response = checkDtService.getAttenResultRecordList(request);
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

    @Logs
    @ApiOperation(value = "获取签到信息", notes = "获取签到信息")
    @RequestMapping(value = "/selectCheckInList", method = RequestMethod.POST)
    public JsonResult selectCheckInList(@RequestBody OapiCheckinRecordGetRequest request) {
        try {
            OapiCheckinRecordGetResponse response = checkDtService.getCheckInRecordList(request);
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

    @Logs
    @ApiOperation(value = "获取部门签到信息", notes = "获取部门签到信息")
    @RequestMapping(value = "/selectDeptCheckInList", method = RequestMethod.POST)
    public JsonResult selectDeptCheckInList(@RequestBody OapiCheckinRecordRequest request) {
        try {
            OapiCheckinRecordResponse response = checkDtService.getDeptCheckInRecordList(request);
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
