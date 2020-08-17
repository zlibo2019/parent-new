package com.weds.core.db;

import java.util.HashMap;
import java.util.Map;


/**
 * <pre>
 * <dt><b>类名：</b></dt>
 * <dd>MultiPage</dd>
 * <dt><b>描述：</b></dt>
 * <dd>分页操作</dd>
 * <dt><b>日期：</b></dt>
 * <dd>2016-5-23 下午3:16:15</dd>
 * </pre>
 *
 * @author SXM
 * @version 0.1
 */
public class MultiPage {
    // 有页面传入
    private int numPerPage = 20; // 每页显示的记录数
    private int pageNum; // 当前页码
    private int totalCount = 0; // 总记录数
    private int pageNumShown = 10;// 显示的页数
    private Map<Object, Object> pageData = new HashMap<Object, Object>();

    /**
     * @param pageNum    页码
     * @param numPerPage 一页条数
     * @param totalCount 总条数
     */
    public MultiPage(int pageNum, int numPerPage, int totalCount) {
        if (pageNum == 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
        if (numPerPage == 0) {
            numPerPage = 20;
        }
        this.numPerPage = numPerPage;
        this.totalCount = totalCount;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getPageNumShow</dd>
     * <dt><b>描述：</b></dt>
     * <dd>获取总页数</dd>
     * </pre>
     *
     * @return
     */
    public int getPageNumShow() {
        int pageNumShown = (totalCount + numPerPage - 1) / numPerPage;
        pageNumShown = pageNumShown > 10 ? 10 : pageNumShown;
        return pageNumShown;
    }

    /**
     * <pre>
     * <dt><b>名称：</b></dt>
     * <dd>getPageCount</dd>
     * <dt><b>描述：</b></dt>
     * <dd>获取总页数</dd>
     * </pre>
     *
     * @return
     */
    public int getPageCount() {
        int pageNumShown = (totalCount + numPerPage - 1) / numPerPage;
        return pageNumShown;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getNumPerPage() {
        return numPerPage;
    }

    public void setNumPerPage(int numPerPage) {
        if (numPerPage == 0) {
            numPerPage = 20;
        }
        this.numPerPage = numPerPage;
    }

    public int getPageNumShown() {
        return pageNumShown;
    }

    public void setPageNumShown(int pageNumShown) {
        this.pageNumShown = pageNumShown;
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        if (pageNum == 0) {
            pageNum = 1;
        }
        this.pageNum = pageNum;
    }

    public Map<Object, Object> getPageData() {
        return pageData;
    }

    public void setPageData(Map<Object, Object> pageData) {
        this.pageData = pageData;
    }

    public int getPagecount() {
        return (pageNum - 1) * numPerPage;
    }
}
