package com.mock.common.util;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RedisUtil {

    public static void pushToRedis(JedisPool jedisPool ,String key, String value){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.set(key, value);
        }
    }
    public static void pushToRedisEx(JedisPool jedisPool ,String key, String value, int seconds){
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(key, seconds,value);
        }
    }

    public static void pushToRedis(JedisPool jedisPool , HashMap<String, String> map){
        try (Jedis jedis = jedisPool.getResource()) {

            for (Map.Entry<String, String> entry : map.entrySet()) {
                jedis.set(entry.getKey(), entry.getValue());
            }
        }
    }

    public static <T> T getFromRedis(JedisPool jedisPool, String key, Class<T> classType){
        T result;
        try (Jedis jedis = jedisPool.getResource()) {
            String tmp = jedis.get(key);
            if (StrUtil.isEmpty(tmp)) {
                return null;
            }
            result = JSON.parseObject(tmp, classType);
        }
        return result;
    }

}
