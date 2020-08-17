package com.weds.core.base;

import java.util.Map;

/**
 * 分页排序抽象类
 * 
 * @author Administrator
 *
 */
public abstract class BaseOrderBy {
	/**
	 * orderby map
	 * <p>
	 * key：调用方orderby字符串包含的字段
	 * </p>
	 * 
	 * <p>
	 * value：数据库中的字段名，如a.id,b.insert_time
	 * </p>
	 * 
	 * @return
	 */
	public abstract Map<String, String> getOrderByFieldMap();
}
