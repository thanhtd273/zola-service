package com.thanhtd.zola.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaAppListener {

    private static final Logger logger = LoggerFactory.getLogger(KafkaAppListener.class);

    @KafkaListener(topics = "test-kafka", groupId = "test-kafka")
    public void listenGroupTest(String message) {
        logger.info("Received message in group test: {}", message);
    }
}
