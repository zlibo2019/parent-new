package com.weds.redis;

import org.springframework.data.redis.core.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis 操作帮助类
 */
public class RedisService {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private ValueOperations<String, Object> vOps;

    @Resource(name = "redisTemplate")
    private SetOperations<String, Object> sOps;

    @Resource(name = "redisTemplate")
    private ZSetOperations<String, Object> zsOps;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Object> lOps;

    @Resource(name = "redisTemplate")
    private HashOperations<String, Object, Object> hOps;

    /**
     * 设置键值(永久)
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        vOps.set(key, value);
    }

    /**
     * 设置键值,带有效期(秒)
     *
     * @param key
     * @param value
     * @param timeout
     */
    public void set(String key, String value, long timeout) {
        set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置键值,带有效期
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void set(String key, String value, long timeout, TimeUnit timeUnit) {
        if (timeout == 0) {
            remove(key);
            return;
        }
        if (timeout == -1) {
            vOps.set(key, value);
        } else {
            vOps.set(key, value, timeout, timeUnit);
        }
    }

    /**
     * 获取键值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return (String) vOps.get(key);
    }

    /**
     * 删除键
     *
     * @param keyPattern
     */
    public void remove(String keyPattern) {
        redisTemplate.delete(redisTemplate.keys(keyPattern));
    }

    /**
     * 设置过期时间(秒)
     *
     * @param key
     * @return
     */
    public void setExpire(String key, long timeout) {
        setExpire(key, timeout, TimeUnit.SECONDS);
    }

    /**
     * 设置过期时间
     *
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    public void setExpire(String key, long timeout, TimeUnit unit) {
        if (timeout == 0) {
            remove(key);
            return;
        }
        if (timeout != -1) {
            redisTemplate.expire(key, timeout, unit);
        }
    }

    /**
     * 获取过期时间(秒)
     *
     * @param key
     * @return
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 获取过期时间
     *
     * @param key
     * @param unit
     * @return
     */
    public long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * 自增数
     *
     * @param key
     * @return
     */
    public long increment(String key) {
        return vOps.increment(key, 1);
    }

    public long inc(String key, long delta) {
        return vOps.increment(key, delta);
    }

    public long inc(String key, long delta, long timeout) {
        long jret = vOps.increment(key, delta);
        if (timeout != -1)
            setExpire(key, timeout);
        return jret;
    }

    /**
     * 获取数值
     *
     * @param key
     * @return 无效或不存在返回null
     */
    public Long get_long(String key) {
        String jgetString = (String) vOps.get(key);
        if (jgetString == null || jgetString.equals("")) {
            return null;
        }
        Long jout = null;
        try {
            jout = Long.parseLong(jgetString);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return jout;
    }

    public long hash_inc(String key, String hashKey, long delta) {
        return hOps.increment(key, hashKey, delta);
    }

    public void hash_set(String key, String hashKey, String value) {
        hOps.put(key, hashKey, value);
    }

    public void hash_set(String key, String hashKey, String value,
                         long timeout) {
        hOps.put(key, hashKey, value);
        if (timeout != -1)
            setExpire(key, timeout);
    }

    public void hash_remove(String key, String hashKey) {
        hOps.delete(key, hashKey);
    }

    public <KEY, VALUE> Map<KEY, VALUE> hash_getall(String key) {
        return (Map<KEY, VALUE>) hOps.entries(key);
    }

    public Cursor<Entry<Object, Object>> hash_scan(String key) {
        return hOps.scan(key, ScanOptions.NONE);
    }

    public <T> T hash_get(String key, Object hashKey) {
        return (T) hOps.get(key, hashKey);
    }

    /**
     * 获取Redis值，
     * isSuffxKey=false时不加redis版本及前缀,当前RedisService为了和.net程序互通，value只支持string
     */
    public String get(String key, Boolean isSuffxKey) {
        String finalKey = isSuffxKey ? suffxKey(key) : key;
        Object object = vOps.get(finalKey);
        if (object != null) {
            return object.toString();
        }
        return null;
    }


    /**
     * @param keyPattern
     * @param isSuffxKey
     */
    public void remove(String keyPattern, Boolean isSuffxKey) {
        String finalKey = isSuffxKey ? suffxKey(keyPattern) : keyPattern;
        Set<String> removeKeys = redisTemplate.keys(finalKey);
        redisTemplate.delete(removeKeys);
    }


    /**
     * 获取所有相似key
     *
     * @param keyPattern 模糊键
     * @author heyang
     * @date 2015年9月29日 13:10:05
     */
    public Set<String> keys(String keyPattern) {
        return redisTemplate.keys("*" + keyPattern + "*");
    }

    /**
     * 给key添加后缀或前缀
     *
     * @param orginKey 实际的key
     * @return
     * @author caoheyang
     * @date 20140203
     */
    private String suffxKey(String orginKey) {
        // return "java_tools_" + PropertyUtils.getProperty("GlobalVersion") +
        // "_"+ orginKey;
        return orginKey;
    }


    /**
     * set 集合 新增
     *
     * @param key
     * @param object
     */
    public void setSet(String key, Object object) {
        sOps.add(key, object);
    }

    /**
     * set 集合  根据 key获取
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getSet(String key) {
        return (Set<T>) sOps.members(key);
    }

    /**
     * hash 集合  根据 key获取 并且返回泛型
     *
     * @param key key
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public <KEY, VALUE> Map<KEY, VALUE> getHash(String key) {
        return (Map<KEY, VALUE>) hOps.entries(key);
    }

    /**
     * 添加 一个 hash集合
     *
     * @param key
     * @param map 集合
     */
    public void setHash(String key, Map map) {
        hOps.putAll(key, map);
    }

    /**
     * 根据key 获取list
     *
     * @param key
     * @param paramLong1 开始索引从0开始
     * @param paramLong2 结束所以从0开始
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key, long paramLong1, long paramLong2) {
        return (List<T>) lOps.range(key, paramLong1, paramLong2);
    }

    /**
     * 根据key 获取list  获取所有值 从 0-length-1
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getList(String key) {
        return (List<T>) lOps.range(key, 0, lOps.size(key) - 1);
    }

    /**
     * 根据key 获取list  获取所有值 从 0-length-1
     *
     * @param key
     * @return
     */
    public Long getListSize(String key) {
        return lOps.size(key);
    }

    /**
     * 添加 一个 list 列表
     *
     * @param key
     * @param value
     */
    public void setList(String key, Object value) {
        lOps.rightPush(key, value);
    }

    /**
     * 添加 一个 list 列表
     *
     * @param key
     * @param paramArrayOfV 多个 值
     */
    public void setList(String key, Object... paramArrayOfV) {
        lOps.rightPushAll(key, paramArrayOfV);
    }

    /**
     * zset 排序的集合 新增
     *
     * @param key    键
     * @param object 值
     * @param index  索引  用来排序的时候使用
     * @return
     */
    public Boolean setZSet(String key, Object object, int index) {
        return zsOps.add(key, object, index);
    }

    /**
     * zset 排序的集合  对index 从paramLong1到paramLong2的数据进行排序
     *
     * @param key
     * @param paramLong1 开始索引从paramLong1开始
     * @param paramLong2 结束索引从paramLong2开始
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getZSet(String key, long paramLong1, long paramLong2) {
        return (Set<T>) zsOps.rangeByScore(key, paramLong1, paramLong2);
    }

    /**
     * 当setZSet index是从0开始递增的时候该方法可用
     * 快捷返回排序好的set
     *
     * @param key
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getZSet(String key) {
        return (Set<T>) zsOps.rangeByScore(key, 0, zsOps.size(key) - 1);
    }


    /**
     * 根据key设置过期时常
     *
     * @param key  键
     * @param date 过期时间
     */
    public Boolean setExpire(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }
}
