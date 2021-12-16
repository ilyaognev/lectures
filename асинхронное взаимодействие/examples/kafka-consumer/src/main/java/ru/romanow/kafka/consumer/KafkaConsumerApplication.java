package ru.romanow.kafka.consumer;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class KafkaConsumerApplication {
    private static final Logger logger = getLogger(KafkaConsumerApplication.class);

    private static final String TOPIC_NAME = "my-topic";

    public static void main(String[] args) {
        SpringApplication.run(KafkaConsumerApplication.class, args);
    }

    @KafkaListener(topics = TOPIC_NAME)
    public void listener(@Payload String message,
                         @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                         @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition) {
        logger.info("Received message (key: '{}', partition {}): '{}'", key, partition, message);
    }
}
