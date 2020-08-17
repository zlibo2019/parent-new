package com.weds.core.base;

import java.util.HashMap;
import java.util.Map;

/**
 * 实体父类
 *
 * @author sxm
 */
public class BaseEntity extends BaseOrderBy {
    @Override
    public Map<String, String> getOrderByFieldMap() {
        return null;
    }

    private Map<String, String> dicFmtMap = new HashMap<>();

    private Map<String, Object> params;

    public Map<String, String> getDicFmtMap() {
        return dicFmtMap;
    }

    public void setDicFmtMap(Map<String, String> dicFmtMap) {
        this.dicFmtMap = dicFmtMap;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
