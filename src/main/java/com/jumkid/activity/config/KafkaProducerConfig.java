package com.jumkid.activity.config;

import com.jumkid.share.event.ActivityEvent;
import com.jumkid.share.event.ContentEvent;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${spring.kafka.topic.name.activity.notify}")
    private String kafkaTopicActivityNotify;

    @Value("${spring.kafka.topic.name.content.delete}")
    private String kafkaTopicContentDelete;

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name(kafkaTopicActivityNotify).build();
    }

    @Bean
    public NewTopic topic2() {
        return TopicBuilder.name(kafkaTopicContentDelete).build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }

    @Bean
    public ProducerFactory<String, ActivityEvent> activityProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigMap());
    }

    @Bean
    public ProducerFactory<String, ContentEvent> contentResourceProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getConfigMap());
    }

    private Map<String, Object> getConfigMap() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.CONNECTIONS_MAX_IDLE_MS_CONFIG, 10000);
        configProps.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, 7000);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 1);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        return configProps;
    }

    @Bean
    public KafkaTemplate<String, ActivityEvent> kafkaTemplate1() {
        return new KafkaTemplate<>(activityProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, ContentEvent> kafkaTemplate2() {
        return new KafkaTemplate<>(contentResourceProducerFactory());
    }

}
