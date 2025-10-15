package com.yogesh.spring_boot_kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    //Creating topic using TopicBuilder class and name is wikimedia_recentchange
    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("wikimedia_recentchange")
                .build();
    }
}
