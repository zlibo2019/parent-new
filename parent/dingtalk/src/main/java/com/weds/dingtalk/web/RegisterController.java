package com.weds.dingtalk.web;

import com.dingtalk.api.request.OapiCheckinRecordGetRequest;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.dingtalk.constant.CallBackParams;
import com.weds.dingtalk.entity.CallBackEntity;
import com.weds.dingtalk.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/register")
@Api(value = "钉钉注册对接接口", description = "钉钉注册对接接口")
public class RegisterController extends BaseController {
    @Resource
    private RegisterService registerService;

    @Resource
    private UserDtService userDtService;

    @Resource
    private DeptDtService deptDtService;

    @Resource
    private CheckDtService checkDtService;

    @Resource
    private DtCallBackService dtCallBackService;

    @Logs
    @ApiOperation(value = "添加注册事件", notes = "添加注册事件")
    @RequestMapping(value = "/insertRegister", method = RequestMethod.POST)
    public JsonResult insertRegister() {
        try {
            OapiCallBackRegisterCallBackResponse response = registerService.insertRegister();
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
    @ApiOperation(value = "获取注册事件", notes = "获取注册事件")
    @RequestMapping(value = "/selectRegister", method = RequestMethod.GET)
    public JsonResult selectRegister() {
        try {
            OapiCallBackGetCallBackResponse response = registerService.selectRegister();
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
    @ApiOperation(value = "更新注册事件", notes = "更新注册事件")
    @RequestMapping(value = "/updateRegister", method = RequestMethod.POST)
    public JsonResult updateRegister() {
        try {
            OapiCallBackUpdateCallBackResponse response = registerService.updateRegister();
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
    @ApiOperation(value = "删除注册事件", notes = "删除注册事件")
    @RequestMapping(value = "/deleteRegister", method = RequestMethod.GET)
    public JsonResult deleteRegister() {
        try {
            OapiCallBackDeleteCallBackResponse response = registerService.deleteRegister();
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
    @ApiOperation(value = "失败注册事件", notes = "失败注册事件")
    @RequestMapping(value = "/failRegister", method = RequestMethod.GET)
    public JsonResult failRegister() {
        try {
            OapiCallBackGetCallBackFailedResultResponse response = registerService.failRegister();
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
    @ApiOperation(value = "注册事件回调处理", notes = "注册事件回调处理")
    @RequestMapping(value = "/callBackHandler", method = RequestMethod.POST)
    public String callBackHandler(@RequestBody Map paramsMap, @RequestParam String signature,
                                  @RequestParam String timestamp, @RequestParam String nonce) {
        String encrypt = paramsMap.get("encrypt").toString();
        // String accessToken = MapCacheUtils.single().get("token").toString();
        // String signStr = DtUtils.sign(accessToken, timestamp, nonce, encrypt);
        // if (signStr.equals(signature)) {
        try {
            CallBackEntity callBackEntity = registerService.parseRegisterResp(encrypt);
            switch (callBackEntity.getEventType()) {
                case CallBackParams.CHECK_URL:
                    return registerService.getRegisterResp();
                case CallBackParams.USER_ADD_ORG:
                    List<OapiUserGetResponse> listUserInsert = new ArrayList<>();
                    List<String> userIdsInsert = callBackEntity.getUserId();
                    for (String userId : userIdsInsert) {
                        listUserInsert.add(userDtService.selectUserInfo(userId));
                    }
                    dtCallBackService.userDtAddCallBack(listUserInsert);
                    break;
                case CallBackParams.USER_MODIFY_ORG:
                    List<OapiUserGetResponse> listUserUpdate = new ArrayList<>();
                    List<String> userIdsUpdate = callBackEntity.getUserId();
                    for (String userId : userIdsUpdate) {
                        listUserUpdate.add(userDtService.selectUserInfo(userId));
                    }
                    dtCallBackService.userDtUpdateCallBack(listUserUpdate);
                    break;
                case CallBackParams.USER_LEAVE_ORG:
                    List<String> userIdsDelete = callBackEntity.getUserId();
                    dtCallBackService.userDtDeleteCallBack(userIdsDelete);
                    break;
                case CallBackParams.ORG_DEPT_CREATE:
                    List<OapiDepartmentGetResponse> listInsertDept = new ArrayList<>();
                    List<String> deptIdsInsert = callBackEntity.getDeptId();
                    for (String deptId : deptIdsInsert) {
                        listInsertDept.add(deptDtService.selectDeptInfo(deptId));
                    }
                    dtCallBackService.deptDtAddCallBack(listInsertDept);
                    break;
                case CallBackParams.ORG_DEPT_MODIFY:
                    List<OapiDepartmentGetResponse> listUpdateDept = new ArrayList<>();
                    List<String> deptIdsUpdate = callBackEntity.getDeptId();
                    for (String deptId : deptIdsUpdate) {
                        listUpdateDept.add(deptDtService.selectDeptInfo(deptId));
                    }
                    dtCallBackService.deptDtUpdateCallBack(listUpdateDept);
                    break;
                case CallBackParams.ORG_DEPT_REMOVE:
                    List<String> deptIdsDelete = callBackEntity.getDeptId();
                    dtCallBackService.deptDtDeleteCallBack(deptIdsDelete);
                    break;
                case CallBackParams.CHECK_IN:
                    String staffId = callBackEntity.getStaffId();
                    long timeStamp = Long.parseLong(callBackEntity.getTimeStamp());
                    OapiCheckinRecordGetRequest request = new OapiCheckinRecordGetRequest();
                    request.setStartTime(timeStamp);
                    request.setEndTime(timeStamp);
                    request.setSize(100L);
                    request.setCursor(0L);
                    request.setUseridList(staffId);
                    OapiCheckinRecordGetResponse resp = checkDtService.getCheckInRecordList(request);
                    List<OapiCheckinRecordGetResponse.CheckinRecordVo> list = resp.getResult().getPageList();
                    dtCallBackService.checkDtCallBack(list);
                    break;
                default:
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        // }
        return null;
    }
}
