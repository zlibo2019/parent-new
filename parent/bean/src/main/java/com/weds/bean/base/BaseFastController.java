package com.weds.bean.base;

import com.weds.bean.utils.EntityUtils;
import com.weds.core.annotation.Logs;
import com.weds.core.base.BaseCommPager;
import com.weds.core.base.BaseController;
import com.weds.core.base.BaseEntity;
import com.weds.core.base.BasePageSearch;
import com.weds.core.resp.JsonResult;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * @param <S>
 * @param <Entity>
 */
public class BaseFastController<S extends BaseFastService<?, Entity>, Entity, D extends DicFmtService> extends BaseController {
    @Resource
    protected S baseFastService;

    @Resource
    protected D dicFmtService;

    private String[] dicType;

    private Logger log = LogManager.getLogger();

    public void setBaseFastService(S baseFastService) {
        this.baseFastService = baseFastService;
    }

    public void setDicType(String[] dicType) {
        this.dicType = dicType;
    }

    @Logs
    @ApiOperation(value = "查询信息", notes = "查询信息")
    @RequestMapping(value = "/selectByPrimaryKey", method = RequestMethod.POST)
    public JsonResult<Entity> selectByPrimaryKey(@RequestBody Entity record) {
        Entity entity = baseFastService.selectByPrimaryKey(record);
        return succMsgData(entity);
    }

    @Logs
    @ApiOperation(value = "查询列表", notes = "查询列表")
    @RequestMapping(value = "/selectListByEntity", method = RequestMethod.POST)
    public JsonResult<List<Entity>> selectListByEntity(@RequestBody Entity record) {
        List<Entity> list = baseFastService.selectListByEntity(record);
        if (dicType != null && dicType.length > 0) {
            dicFmtService.DicFormat(list.toArray(new BaseEntity[0]), dicType);
        }
        return succMsgData(list);
    }

    @Logs
    @ApiOperation(value = "查询清单", notes = "查询清单")
    @RequestMapping(value = "/selectListPageByEntity", method = RequestMethod.POST)
    public JsonResult<BaseCommPager<Entity>> selectListPageByEntity(@RequestBody BasePageSearch<Entity> record) {
        setPageHelper(record);
        Entity entity = record.getSearch();
        List<Entity> list = baseFastService.selectListByEntity(entity);
        if (dicType != null && dicType.length > 0) {
            dicFmtService.DicFormat(list.toArray(new BaseEntity[0]), dicType);
        }
        return succMsgData(new BaseCommPager<>(list));
    }

    @Logs
    @ApiOperation(value = "新增信息", notes = "新增信息")
    @RequestMapping(value = "/insert", method = RequestMethod.PUT)
    public JsonResult<Object> insert(@RequestBody @Valid Entity record) {
        EntityUtils.setCreatAndUpdatInfo(record);
        baseFastService.insertSelective(record);
        return succMsg();
    }

    @Logs
    @ApiOperation(value = "批量新增信息", notes = "批量新增信息")
    @RequestMapping(value = "/insertBatch", method = RequestMethod.PUT)
    public JsonResult<Object> insertBatch(@RequestBody @Valid List<Entity> list) {
        EntityUtils.setListCreatAndUpdatInfo(list);
        baseFastService.insertBatch(list);
        return succMsg();
    }

    @Logs
    @ApiOperation(value = "更新信息", notes = "更新信息")
    @RequestMapping(value = "/updateByPrimaryKey", method = RequestMethod.POST)
    public JsonResult<Object> updateByPrimaryKey(@RequestBody @Valid Entity record) {
        EntityUtils.setUpdatedInfo(record);
        baseFastService.updateByPrimaryKeySelective(record);
        return succMsg();
    }

    @Logs
    @ApiOperation(value = "删除信息", notes = "删除信息")
    @RequestMapping(value = "/deleteByPrimaryKey", method = RequestMethod.POST)
    public JsonResult<Object> deleteByPrimaryKey(@RequestBody Entity record) {
        baseFastService.deleteByPrimaryKey(record);
        return succMsg();
    }

    @Logs
    @ApiOperation(value = "批量删除信息", notes = "批量删除信息")
    @RequestMapping(value = "/deleteBatchByKeys", method = RequestMethod.POST)
    public JsonResult<Object> deleteBatchByKeys(@RequestBody List<Entity> list) {
        List<Entity> delList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            delList.add(list.get(i));
            if (delList.size() == 20 || i == list.size() - 1) {
                baseFastService.deleteBatchByKeys(delList);
                delList.clear();
            }
        }
        return succMsg();
    }
}
