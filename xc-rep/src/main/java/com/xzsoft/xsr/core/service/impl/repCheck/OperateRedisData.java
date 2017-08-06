package com.xzsoft.xsr.core.service.impl.repCheck;


import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.xzsoft.xip.platform.util.PlatformUtil;

/**
 * 操作redis数据类
 * 使用spring提供的RedisTemplate操作redis数据
 * 
 * RedisTemplate实例通过平台PlatformUtil.getRedisTemplate()获取。
 * 如果希望平台返回一个真正的实例，需要在/WEB-INF/config.properties配上redis相关的参数，同时将xip.useRedis设置为true
 * 
 * @author ZhouSuRong
 * @date 2016-3-3 
 */
@Repository
public class OperateRedisData {
	private RedisTemplate redisTemplate = null;
	/**
	 * 向redis中存入数据
	 * 存入的数据结构为：<String, String>
	 */
    public void put(String key, String value) {
        if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
        if (value != null) {
            redisTemplate.opsForValue().set(key, value);
        }
    }
    /**
     * 从redis中获取数据
     * 获取的数据结构为：<String, String>
     * @param key
     * @return
     */
    public String get(String key) {
        if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
        return (String) redisTemplate.opsForValue().get(key);
    }
    /**
     * 从redis中删除数据
     * jedis.del(oldList.get(j));
     * @param key
     * 删除的数据结构为：<String, String>; <String, List<String>>; <String, Map<String, String>>
     * 目前测试这三种类型，都可以删除
     */
    public void del(String key) {
        if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
        redisTemplate.delete(key);
    }
    /**
     * 判读一个key是否在redis中存在
     * jedis.exists(modalKey.toString());
     * @param key
     * @return
     * 判断的数据结构为：<String, String>; <String, List<String>>; <String, Map<String, String>>
     */
    public boolean hasKey(String key) {
        if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
        return redisTemplate.hasKey(key);
    }
    /**
     * 向redis存入列表数据
     * 数据结构：<String, List<String>>
     * 循环list，单个插入
     * 
     * //将本次保存的状态放进redis中
		for(int j=0;j<newList.size();j++){
			jedis.lpush(modalKey.toString(), newList.get(j));
		}
     */
    public void putList(String key, List<String> dataList) {
    	if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
    	
    	for(String dataKeyV : dataList) {
    		redisTemplate.opsForList().leftPush(key, dataKeyV);
    	}
    }
    /**
     * 向redis存入列表数据
     * 数据结构：<String, List<String>>
     * 批量插入
     */
    public void putAllList(String key, List<String> dataList) {
    	if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
    	
    	redisTemplate.opsForList().leftPushAll(key, dataList);
    }
    /**
     * 从redis中取出列表数据
     * 数据结构：<String, List<String>>
     * @param key
     * @return
     * List<String> oldList=jedis.lrange(modalKey.toString(), 0, -1);
     */
    public List<String> getList(String key) {
    	if (redisTemplate == null)
            redisTemplate = PlatformUtil.getRedisTemplate();
    	
    	return redisTemplate.opsForList().range(key, 0, -1);
    }
}
