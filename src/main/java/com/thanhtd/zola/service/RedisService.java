package com.thanhtd.zola.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.util.List;

public interface RedisService {

    String createKey(String prefix, String key);
    void saveList(String key, List<Object> objects, int secondExpires) throws JsonProcessingException;

    void saveObject(String key, Object object, int secondExpires) throws JsonProcessingException;

    void save(String key, String value, int secondExpires);

    String getValue(String key);
    <T> Object getObject(String key, Class<T> tClass) throws IOException;

    <T> List<T> getListObjects(String key, Class<T> tClass) throws IOException;

    void delete(String key);

}
