package com.weds.dingtalk.web;

import com.dingtalk.api.request.OapiDepartmentCreateRequest;
import com.dingtalk.api.request.OapiDepartmentUpdateRequest;
import com.dingtalk.api.response.*;
import com.taobao.api.ApiException;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseController;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.StringUtils;
import com.weds.dingtalk.service.DeptDtService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/dept")
@Api(value = "钉钉部门对接接口", description = "钉钉部门对接接口")
public class DeptDtController extends BaseController {
    @Resource
    private DeptDtService deptDtService;

    @Logs
    @ApiOperation(value = "添加部门信息", notes = "添加部门信息")
    @RequestMapping(value = "/insertDeptInfo", method = RequestMethod.POST)
    public JsonResult insertDeptInfo(@RequestBody OapiDepartmentCreateRequest request) {
        try {
            OapiDepartmentCreateResponse response = deptDtService.insertDeptInfo(request);
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
    @ApiOperation(value = "获取部门信息", notes = "获取部门信息")
    @RequestMapping(value = "/selectDeptInfo", method = RequestMethod.GET)
    public JsonResult selectDeptInfo(@RequestParam String deptId) {
        try {
            OapiDepartmentGetResponse response = deptDtService.selectDeptInfo(deptId);
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
    @ApiOperation(value = "更新部门信息", notes = "更新部门信息")
    @RequestMapping(value = "/updateDeptInfo", method = RequestMethod.POST)
    public JsonResult updateDeptInfo(@RequestBody OapiDepartmentUpdateRequest request) {
        try {
            OapiDepartmentUpdateResponse response = deptDtService.updateDeptInfo(request);
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
    @ApiOperation(value = "删除部门信息", notes = "删除部门信息")
    @RequestMapping(value = "/deleteDeptInfo", method = RequestMethod.GET)
    public JsonResult deleteDeptInfo(@RequestParam String deptId) {
        try {
            OapiDepartmentDeleteResponse response = deptDtService.deleteDeptInfo(deptId);
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
    @ApiOperation(value = "获取部门列表", notes = "获取部门列表")
    @RequestMapping(value = "/getDeptList", method = RequestMethod.GET)
    public JsonResult getDeptList(String parentId) {
        if (StringUtils.isBlank(parentId)) {
            parentId = "1";
        }
        try {
            OapiDepartmentListResponse response = deptDtService.getDeptList(parentId);
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
