package com.weds.bean.base;

import com.weds.core.base.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BaseFastMapper<T> extends BaseMapper<T> {
    /**
     */
    T selectByPrimaryKey(T record);

    /**
     */
    List<T> selectListByEntity(T record);

    /**
     */
    int insert(T record);

    /**
     */
    int insertSelective(T record);

    /**
     */
    int insertBatch(@Param("list") List<T> list);

    /**
     */
    int updateByPrimaryKey(T record);

    /**
     */
    int updateByPrimaryKeySelective(T record);

    /**
     */
    int deleteByPrimaryKey(T record);

    /**
     */
    int deleteBatchByKeys(@Param("list") List<T> list);
}
