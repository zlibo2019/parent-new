package com.weds.core.utils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class MapCacheUtils {
    /**
     * 默认存储1024个缓存
     */
    private static final int DEFAULT_CACHES = 1024;
    //缓存实体类
    private static MapCacheUtils INS = null;

    //获取缓存实体类
    public static MapCacheUtils single() {
        if (INS == null) {
            INS = new MapCacheUtils();
        }
        return INS;
    }

    /**
     * 缓存容器
     */
    //缓存池
    private Map<String, CacheObject> cachePool;

    //缓存实体类无参构造
    public MapCacheUtils() {
        this(DEFAULT_CACHES);
    }

    //缓存实体类含参构造，缓存cacheCount个数
    public MapCacheUtils(int cacheCount) {
        cachePool = new ConcurrentHashMap<>(cacheCount);
        new Thread(() -> {
            Set<String> set = cachePool.keySet();
            for (String key : set) {
                CacheObject cacheObject = cachePool.get(key);
                if (null != cacheObject) {
                    //查询缓存是否过期
                    long cur = System.currentTimeMillis() / 1000;
                    if (cacheObject.getExpired() > cur) {
                        this.del(key);
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }).start();
    }

    //缓存存储对象
    static class CacheObject {
        private String key;//缓存key
        private Object value;//缓存value
        private long expired;//缓存过期时间

        //缓存存储对象含参构造
        public CacheObject(String key, Object value, long expired) {
            this.key = key;
            this.value = value;
            this.expired = expired;
        }

        public String getKey() {
            return key;
        }

        public Object getValue() {
            return value;
        }

        public long getExpired() {
            return expired;
        }
    }

    /**
     * 设置一个缓存并带过期时间
     *
     * @param key     缓存key
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void set(String key, Object value, long expired) {
        expired = expired > 0 ? System.currentTimeMillis() / 1000 + expired : expired;
        //存入缓存对象中
        CacheObject cacheObject = new CacheObject(key, value, expired);
        //将缓存对象存入缓存池中
        cachePool.put(key, cacheObject);
    }

    /**
     * 设置一个缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, Object value) {
        this.set(key, value, -1);
    }

    /**
     * 设置一个hash缓存并带过期时间
     *
     * @param key     缓存key
     * @param field   缓存field
     * @param value   缓存value
     * @param expired 过期时间，单位为秒
     */
    public void hset(String key, String field, Object value, long expired) {
        key = key + ":" + field;
        this.set(key, value, expired);
    }

    /**
     * 设置一个hash缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @param value 缓存value
     */
    public void hset(String key, String field, Object value) {
        this.hset(key, field, value, -1);
    }


    /**
     * 读取一个缓存
     *
     * @param key 缓存key
     * @return
     */
    public Object get(String key) {
        CacheObject cacheObject = cachePool.get(key);
        if (null != cacheObject) {
            //查询缓存是否过期
            long cur = System.currentTimeMillis() / 1000;
            if (cacheObject.getExpired() <= 0 || cacheObject.getExpired() > cur) {
                return cacheObject.getValue();
            } else {
                this.del(key);
            }
        }
        return null;
    }


    /**
     * 读取一个hash类型缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     * @return
     */
    public Object hget(String key, String field) {
        key = key + ":" + field;
        return this.get(key);
    }

    /**
     * 根据key删除缓存
     *
     * @param key 缓存key
     */
    public void del(String key) {
        cachePool.remove(key);
    }

    /**
     * 根据key和field删除缓存
     *
     * @param key   缓存key
     * @param field 缓存field
     */
    public void hdel(String key, String field) {
        key = key + ":" + field;
        this.del(key);
    }

    /**
     * 清空缓存
     */
    public void clean() {
        cachePool.clear();
    }
}
