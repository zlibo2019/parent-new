package com.weds.bean.base;

import com.weds.core.base.BaseClass;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;
import java.util.List;

/**
 * 服务父类
 *
 * @author sxm
 */
public class BaseFastService<M extends BaseFastMapper<T>, T> extends BaseClass {
    @Resource
    protected M baseFastMapper;

    private Logger log = LogManager.getLogger();

    public void setBaseMapper(M baseFastMapper) {
        this.baseFastMapper = baseFastMapper;
    }

    public T selectByPrimaryKey(T record) {
        return baseFastMapper.selectByPrimaryKey(record);
    }

    public List<T> selectListByEntity(T record) {
        return baseFastMapper.selectListByEntity(record);
    }

    public int insert(T record) {
        return baseFastMapper.insert(record);
    }

    public int insertSelective(T record) {
        return baseFastMapper.insertSelective(record);
    }

    public int insertBatch(List<T> list) {
        return baseFastMapper.insertBatch(list);
    }

    public int updateByPrimaryKey(T record) {
        return baseFastMapper.updateByPrimaryKey(record);
    }

    public int updateByPrimaryKeySelective(T record) {
        return baseFastMapper.updateByPrimaryKeySelective(record);
    }

    public int deleteByPrimaryKey(T record) {
        return baseFastMapper.deleteByPrimaryKey(record);
    }

    public int deleteBatchByKeys(List<T> list) {
        return baseFastMapper.deleteBatchByKeys(list);
    }
}
