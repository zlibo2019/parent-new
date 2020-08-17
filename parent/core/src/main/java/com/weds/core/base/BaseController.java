package com.weds.core.base;

import com.github.pagehelper.PageHelper;

/**
 * 控制父类
 *
 * @author sxm
 */
public class BaseController extends BaseClass {

    /**
     * 分页设置
     *
     * @param pageSearch
     */
    protected void setPageHelper(BasePageSearch<?> pageSearch) {
        PageHelper.startPage(pageSearch.getPageIndex(), pageSearch.getPageSize(), pageSearch.isCount());
        PageHelper.orderBy(pageSearch.getOrderBy() != null ? pageSearch.getOrderBy().trim() : pageSearch.getOrderBy());
    }
}
