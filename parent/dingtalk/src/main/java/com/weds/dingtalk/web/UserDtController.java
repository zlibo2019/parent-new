package com.weds.dingtalk.web;

import com.dingtalk.api.request.OapiUserCreateRequest;
import com.dingtalk.api.request.OapiUserUpdateRequest;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.dingtalk.service.UserDtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/user")
@Api(value = "钉钉人员对接接口", description = "钉钉人员对接接口")
public class UserDtController extends BaseController {
    @Resource
    private UserDtService userDtService;

    @Logs
    @ApiOperation(value = "添加人员信息", notes = "添加人员信息")
    @RequestMapping(value = "/insertUserInfo", method = RequestMethod.POST)
    public JsonResult insertUserInfo(@RequestBody OapiUserCreateRequest request) {
        try {
            OapiUserCreateResponse response = userDtService.insertUserInfo(request);
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
    @ApiOperation(value = "获取人员信息", notes = "获取人员信息")
    @RequestMapping(value = "/selectUserInfo", method = RequestMethod.GET)
    public JsonResult selectUserInfo(@RequestParam String userId) {
        try {
            OapiUserGetResponse response = userDtService.selectUserInfo(userId);
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
    @ApiOperation(value = "更新人员信息", notes = "更新人员信息")
    @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST)
    public JsonResult updateUserInfo(@RequestBody OapiUserUpdateRequest request) {
        try {
            OapiUserUpdateResponse response = userDtService.updateUserInfo(request);
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
    @ApiOperation(value = "删除人员信息", notes = "删除人员信息")
    @RequestMapping(value = "/deleteUserInfo", method = RequestMethod.GET)
    public JsonResult deleteUserInfo(@RequestParam String userId) {
        try {
            OapiUserDeleteResponse response = userDtService.deleteUserInfo(userId);
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
    @ApiOperation(value = "获取用户ID", notes = "获取用户ID")
    @RequestMapping(value = "/getUserId", method = RequestMethod.GET)
    public JsonResult getUserId(@RequestParam String code) {
        try {
            OapiUserGetuserinfoResponse response = userDtService.getUserId(code);
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
    @ApiOperation(value = "获取部门用户列表", notes = "获取部门用户列表")
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public JsonResult getUserList(@RequestParam Long deptId, Long offset, Long pageSize) {
        try {
            OapiUserSimplelistResponse response = userDtService.getUserList(deptId, offset, pageSize);
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
