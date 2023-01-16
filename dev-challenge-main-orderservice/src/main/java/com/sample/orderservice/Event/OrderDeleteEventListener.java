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
public class OrderDeleteEventListener  extends AbstractCloudEventsKafkaBridge implements ApplicationListener<OrderDeleteEvent> {

    private final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    public OrderDeleteEventListener(KafkaProducer<String, CloudEvent> kafkaProducer) throws URISyntaxException {

        super(kafkaProducer, new URI("http://localhost:8081/v1/api/order"), "order.deleted");

    }

    @Override
    public void onApplicationEvent(OrderDeleteEvent event) {

        logger.info("Order with id {} deleted", event.getOrderId());


        RecordMetadata metadata = this.forwardEvent("orderevents-deleted", Collections.singletonMap("orderid", event.getOrderId()));
        logger.info("Record sent to partition {} with offset {}", metadata.partition(), metadata.offset());

    }
}
