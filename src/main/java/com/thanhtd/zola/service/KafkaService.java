package com.thanhtd.zola.service;

public interface KafkaService {
    void  sendMessage(String topic, String message) throws Exception;
}
