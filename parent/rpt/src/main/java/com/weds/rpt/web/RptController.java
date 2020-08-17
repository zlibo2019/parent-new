package com.weds.rpt.web;

import com.alibaba.fastjson.JSONObject;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseCommPager;
import com.weds.core.base.BaseController;
import com.weds.core.base.BasePageSearch;
import com.weds.core.resp.JsonResult;
import com.weds.core.utils.StringUtils;
import com.weds.rpt.constants.RptParams;

import com.weds.rpt.entity.ColInfo;
import com.weds.rpt.entity.GroupInfo;
import com.weds.rpt.entity.ParamsInfo;
import com.weds.rpt.entity.TabInfo;
import com.weds.rpt.service.RptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/rpt")
@Api(value = "报表管理", description = "报表管理")
public class RptController extends BaseController {

    @Resource
    private RptService rptService;

    @Resource
    private RptParams rptParams;

    private String getTabInfo(String rptId) throws IOException {
        String content = null;
        File file = new File(rptParams.getSettingPath() + File.separator + rptId + ".json");
        if (file.exists()) {
            content = IOUtils.toString(new FileInputStream(file));
        }
        return content;
    }

    private void setTabInfo(TabInfo tabInfo) throws IOException {
        File file = new File(rptParams.getSettingPath() + File.separator + tabInfo.getRptId() + ".json");
        file.deleteOnExit();
        FileOutputStream fos = new FileOutputStream(file);
        IOUtils.write(JSONObject.toJSONString(tabInfo), fos);
        IOUtils.closeQuietly(fos);
    }

    /**
     * 对list进行分组
     */
    private List<GroupInfo> getListGroup(List<ColInfo> list) {
        List<GroupInfo> groupList = new ArrayList<>();
        List<ColInfo> temp = new ArrayList<>();
        GroupInfo groupInfo = null;
        String group = null;
        for (int i = 0; i < list.size(); i++) {
            temp.add(list.get(i));
            if (i == 0) {
                group = StringUtils.defaultIfBlank(list.get(i).getGroup(), "");
                groupInfo = new GroupInfo();
                groupInfo.setLabel(group);
            }

            if (i + 1 < list.size()) {
                String next = StringUtils.defaultIfBlank(list.get(i + 1).getGroup(), "");
                if (!group.equals(next)) {
                    groupInfo.setChildren(temp);
                    groupList.add(groupInfo);

                    groupInfo = new GroupInfo();
                    temp = new ArrayList<>();
                    group = next;
                    groupInfo.setLabel(group);
                }
            }

            if (i == list.size() - 1) {
                groupInfo.setChildren(temp);
                groupList.add(groupInfo);
            }
        }
        return groupList;
    }

    @Logs
    @ApiOperation(value = "获取报表配置信息", notes = "获取报表配置信息")
    @RequestMapping(value = "/getRptSetting", method = RequestMethod.GET)
    public JsonResult<TabInfo> getRptSetting(@RequestParam String rptId) {
        try {
            String str = getTabInfo(rptId);
            if (StringUtils.isBlank(str)) {
                return failMsg();
            }
            TabInfo tabInfo = JSONObject.parseObject(str, TabInfo.class);
            List<ColInfo> list = tabInfo.getColumns().stream().sorted(Comparator.comparingInt(ColInfo::getIndex)).collect(Collectors.toList());
            // tabInfo.setGroupList(this.getListGroup(list));
            tabInfo.setColumns(list);
            return succMsgData(tabInfo);
        } catch (IOException e) {
            e.printStackTrace();
            return failMsg();
        }
    }

    @Logs
    @ApiOperation(value = "设置报表配置信息", notes = "设置报表配置信息")
    @RequestMapping(value = "/setRptSetting", method = RequestMethod.POST)
    public JsonResult<Object> setRptSetting(@RequestBody TabInfo tabInfo) {
        try {
            setTabInfo(tabInfo);
            return succMsg();
        } catch (IOException e) {
            e.printStackTrace();
            return failMsg();
        }
    }

    @Logs
    @ApiOperation(value = "获取报表数据", notes = "获取报表数据")
    @RequestMapping(value = "/getRptData", method = RequestMethod.POST)
    public JsonResult<BaseCommPager<Map<String, Object>>> getRptData(@RequestBody BasePageSearch<ParamsInfo> record) {
        try {
            setPageHelper(record);
            ParamsInfo entity = record.getSearch();
            String str = getTabInfo(entity.getRptId());
            if (StringUtils.isBlank(str)) {
                return failMsg();
            }
            TabInfo tabInfo = JSONObject.parseObject(str, TabInfo.class);
            StringBuilder sb = new StringBuilder();
            sb.append(tabInfo.getQuerySql());
            if (entity.getQueryParams() != null) {
                JSONObject jsonObject = JSONObject.parseObject(entity.getQueryParams());
                for (String key : jsonObject.keySet()) {
                    if (jsonObject.get(key) == null || StringUtils.isBlank(jsonObject.get(key).toString())) {
                        continue;
                    }
                    sb.append("and ").append(key).append(" = ").append("'").append(jsonObject.get(key)).append("' ");
                }
            }
            List<Map<String, Object>> dataList = rptService.selectDataList(sb.toString());
            // LinkedCaseInsensitiveMap map = new LinkedCaseInsensitiveMap();
            return succMsgData(new BaseCommPager<>(dataList));
        } catch (IOException e) {
            e.printStackTrace();
            return failMsg();
        }
    }
}
