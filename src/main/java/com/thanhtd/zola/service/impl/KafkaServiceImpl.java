package com.thanhtd.zola.service.impl;

import com.thanhtd.zola.service.KafkaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaServiceImpl implements KafkaService {
    private static final Logger logger = LoggerFactory.getLogger(KafkaService.class);


    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void sendMessage(String topic, String message) throws Exception {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, message);
        future.whenComplete(((result, e) -> {
            if (e == null) {
                logger.info("Sent message=[{}] with offset=[{}]", message, result.getRecordMetadata().offset());
            } else {
                logger.error("Unable to send message=[{}] due to: {}", message, e.getMessage());
            }
        } ));

    }
}
