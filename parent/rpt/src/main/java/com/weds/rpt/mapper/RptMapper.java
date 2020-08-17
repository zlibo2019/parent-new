package com.weds.rpt.mapper;

import com.weds.core.annotation.MyBatisDao;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface RptMapper {
    List<Map<String, Object>> selectDataList(@Param("querySql") String querySql);
}
