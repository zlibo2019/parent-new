package com.weds.rpt.service;

import com.weds.core.base.BaseService;
import com.weds.rpt.mapper.RptMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class RptService extends BaseService {
    @Resource
    private RptMapper rptMapper;

    public List<Map<String, Object>> selectDataList(String querySql) {
        return rptMapper.selectDataList(querySql);
    }
}
