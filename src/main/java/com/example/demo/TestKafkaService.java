package com.example.demo;

import brave.Span;
import brave.Tracer;
import brave.internal.codec.HexCodec;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.sleuth.annotation.NewSpan;
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

    private final Tracer tracer;

    @PostConstruct
    public void toKafka() {
        Flux.range(0, Integer.MAX_VALUE)
                .delayElements(Duration.ofSeconds(1))
                .flatMap(integer ->
                {
                    TraceContext traceContext = TraceContext.newBuilder()
                            .traceId(HexCodec.lowerHexToUnsignedLong("11111"))
                            .spanId(HexCodec.lowerHexToUnsignedLong("22222"))
                            .build();
                    Span span = tracer.toSpan(traceContext);
                    try {
                        tracer.withSpanInScope(span);
                        span.start();
                        return reactiveKafkaProducerTemplate.send("test-topic", "test-value");
                    } finally {
                        span.finish();
                    }
                })
                .doOnNext(senderResult -> log.error("senderResult = {}", senderResult))
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
