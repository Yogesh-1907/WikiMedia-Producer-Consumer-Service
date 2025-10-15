package com.yogesh.spring_boot_kafka;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@Slf4j
public class WikiMediaChangesHandles implements EventHandler {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public WikiMediaChangesHandles(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void onOpen() {
        log.info("Opened connection to Wikimedia event stream");
    }

    @Override
    public void onClosed() {
        log.info("Closed connection to Wikimedia event stream");
    }

    @Override
    public void onMessage(String event, MessageEvent messageEvent) {
        String payload = messageEvent.getData();
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, payload);

        future.thenAccept(result -> log.info("Sent message to topic {} with offset {}", topic,
                        result.getRecordMetadata().offset()))
                .exceptionally(ex -> {
                    log.error("Unable to send message to Kafka: {}", ex.getMessage(), ex);
                    return null;
                });
    }

    @Override
    public void onComment(String comment) {
        // ignore comments
    }

    @Override
    public void onError(Throwable t) {
        log.error("Error in Wikimedia stream: {}", t.getMessage(), t);
    }
}
