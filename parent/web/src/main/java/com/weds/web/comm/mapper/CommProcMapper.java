package com.weds.web.comm.mapper;


import com.weds.core.annotation.MyBatisDao;

import java.util.List;
import java.util.Map;

@MyBatisDao
public interface CommProcMapper {
    List<Map> loadProcData(Map<String, Object> paramsMap);
}


