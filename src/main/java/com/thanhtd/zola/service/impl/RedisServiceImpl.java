package com.thanhtd.zola.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thanhtd.zola.core.util.RedisTemplateUtil;
import com.thanhtd.zola.service.RedisService;
import jodd.util.StringPool;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplateUtil<String> redisTemplateUtil;

    @Autowired
    private ObjectMapper jsonMapper;

    @Override
    public String createKey(String prefix, String key) {
        return redisTemplateUtil.createKey(prefix, key);
    }

    @Override
    public void saveList(String key, List<Object> objects, int secondExpires) throws JsonProcessingException {
        String value = StringPool.EMPTY;
        if (!ObjectUtils.isEmpty(objects)) {
            value = jsonMapper.writeValueAsString(objects);
        }
        save(key, value, secondExpires);
    }

    @Override
    public void saveObject(String key, Object object, int secondExpires) throws JsonProcessingException {
        String value = StringPool.EMPTY;
        if (!ObjectUtils.isEmpty(object)) {
            value = jsonMapper.writeValueAsString(object);
        }
        save(key, value, secondExpires);
    }

    @Override
    public void save(String key, String value, int secondExpires) {
        redisTemplateUtil.setWithExpire(key, value, secondExpires, TimeUnit.SECONDS);
    }

    @Override
    public String getValue(String key) {
        if (redisTemplateUtil.exist(key))
            return redisTemplateUtil.get(key);
        return null;
    }

    @Override
    public <T> Object getObject(String key, Class<T> tClass) throws IOException {
        String value = getValue(key);
        if (StringUtil.isBlank(value))
            return null;
        return jsonMapper.readValue(value, tClass);
    }

    @Override
    public <T> List<T> getListObjects(String key, Class<T> tClass) throws IOException {
        String value = getValue(key);
        if (StringUtil.isBlank(value))
            return null;
        return jsonMapper.readValue(value, jsonMapper.getTypeFactory().constructCollectionType(List.class, tClass));
    }

    @Override
    public void delete(String key) {
        if (redisTemplateUtil.exist(key))
            redisTemplateUtil.delete(key);
    }
}
