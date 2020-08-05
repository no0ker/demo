package com.example.demo.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.kafka.sender.SenderOptions;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

@Configuration
public class KafkaConfig {
    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate() {
        SenderOptions<String, String> properties = SenderOptions.<String, String>create()
                .producerProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                .withKeySerializer(new StringSerializer())
                .withValueSerializer(new StringSerializer())
                .stopOnError(false);

        return new ReactiveKafkaProducerTemplate<>(properties);
    }

    @Bean
    public ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate() {
        ReceiverOptions<String, String> properties = ReceiverOptions.<String, String>create()
                .consumerProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
                .subscription(Collections.singleton("test-topic"))
                .withKeyDeserializer(new StringDeserializer())
                .withValueDeserializer(new StringDeserializer())
                .consumerProperty(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());

        return new ReactiveKafkaConsumerTemplate<>(properties);
    }
}
