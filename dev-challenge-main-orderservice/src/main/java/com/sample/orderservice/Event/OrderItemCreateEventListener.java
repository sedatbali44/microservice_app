package com.sample.orderservice.Event;


import io.cloudevents.CloudEvent;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

@Component
public class OrderItemCreateEventListener extends AbstractCloudEventsKafkaBridge implements ApplicationListener<OrderItemCreateEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public OrderItemCreateEventListener(KafkaProducer<String, CloudEvent> kafkaProducer) throws URISyntaxException {

        super(kafkaProducer, new URI("http://localhost:8081/v1/api/orderItem"), "orderItem.created");

    }

    @Override
    public void onApplicationEvent(OrderItemCreateEvent event) {

        logger.info("OrderItem with id {} created", event.getItemId());


        RecordMetadata metadata = this.forwardEvent("orderitemevents-created", Collections.singletonMap("orderItemid", event.getItemId()));
        logger.info("Record sent to partition {} with offset {}", metadata.partition(), metadata.offset());

    }
}
