package com.example.demo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaConsumerTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.time.Duration;

@RequiredArgsConstructor
@Slf4j
@Service
public class TestKafkaService {
    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    private final ReactiveKafkaConsumerTemplate<String, String> reactiveKafkaConsumerTemplate;

    @PostConstruct
    public void toKafka() {
        Flux.range(0, Integer.MAX_VALUE)
                .delayElements(Duration.ofSeconds(1))
                .flatMap(integer -> reactiveKafkaProducerTemplate.send("test-topic", "test-value"))
                .doOnNext(senderResult -> log.trace("senderResult = {}", senderResult))
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }

    @PostConstruct
    public void fromKafka() {
        reactiveKafkaConsumerTemplate.receiveAutoAck()
                .doOnNext(record -> {
                    log.debug("key = {} value = {} headers = {}", record.key(), record.value(), record.headers());
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe();
    }
}
