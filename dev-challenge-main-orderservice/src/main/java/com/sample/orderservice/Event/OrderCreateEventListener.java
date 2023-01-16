package com.sample.orderservice.Event;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import io.cloudevents.CloudEvent;

@Component
public class OrderCreateEventListener extends AbstractCloudEventsKafkaBridge implements ApplicationListener<OrderCreateEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public OrderCreateEventListener(KafkaProducer<String, CloudEvent> kafkaProducer) throws URISyntaxException {

        super(kafkaProducer, new URI("http://localhost:8081/v1/api/order"), "order.created");

    }

    @Override
    public void onApplicationEvent(OrderCreateEvent event) {

        logger.info("Order with id {} created", event.getOrderId());


        RecordMetadata metadata = this.forwardEvent("orderevents-created", Collections.singletonMap("orderid", event.getOrderId()));
        logger.info("Record sent to partition {} with offset {}", metadata.partition(), metadata.offset());

    }
}
