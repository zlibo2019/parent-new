package com.weds.web.comm.service;

import com.weds.core.base.BaseService;
import com.weds.web.comm.entity.CommProcEntity;
import com.weds.web.comm.mapper.CommProcMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 根据CommProc参数进行call存储过程
 */
@Service
public class CommProcService extends BaseService {
    @Autowired
    private CommProcMapper commProcMapper;

    public List<List<Map>> loadProcData(CommProcEntity commProcEntity) {
        List<List<Map>> list = new ArrayList<>();
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("procName", commProcEntity.getProcName());
        paramsMap.put("params", commProcEntity.getParams());
        Integer outNum = commProcEntity.getOutNum();
        if (outNum != null && outNum > 0) {
            List<String> outs = new ArrayList<String>();
            for (int i = 1; i <= outNum; i++) {
                outs.add("p_out_" + i);
            }
            paramsMap.put("outs", outs);
            List<Map> resp = commProcMapper.loadProcData(paramsMap);
            for (int i = 1; i <= outNum; i++) {
                list.add((List<Map>) paramsMap.get("p_out_" + i));
            }
            if (resp != null && resp.size() > 0) {
                list.add(resp);
            }
        } else {
            List<Map> resp = commProcMapper.loadProcData(paramsMap);
            if (resp != null && resp.size() > 0) {
                list.add(resp);
            }
        }
        return list;
    }
}
