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
public class OrderItemChangeEventListener extends AbstractCloudEventsKafkaBridge implements ApplicationListener<OrderItemChangeEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public OrderItemChangeEventListener(KafkaProducer<String, CloudEvent> kafkaProducer) throws URISyntaxException {

        super(kafkaProducer, new URI("http://localhost:8081/v1/api/orderItem"), "orderItem.changed");

    }

    @Override
    public void onApplicationEvent(OrderItemChangeEvent event) {

        logger.info("OrderItem with id {} changed", event.getItemId());


        RecordMetadata metadata = this.forwardEvent("orderItemevents-changed", Collections.singletonMap("itemId", event.getItemId()));
        logger.info("Record sent to partition {} with offset {}", metadata.partition(), metadata.offset());

    }
}
