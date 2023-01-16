package com.sample.orderservice.Event;
import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.cloudevents.CloudEvent;
import io.cloudevents.core.message.Encoding;
import io.cloudevents.jackson.JsonFormat;
import io.cloudevents.kafka.CloudEventSerializer;

@Configuration
public class KafkaProducerConfiguration {

    @Value( "${kafka.bootstrapServer}" )
    private String kafkaBootstrapServer;

    @Bean
    public KafkaProducer<String, CloudEvent> kafkaProducer() {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServer);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "order-cloudevents-producer");

        //props.put(ProducerConfig.CLIENT_ID_CONFIG, "orderitem-cloudevents-producer");

        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        // Configure the CloudEventSerializer to emit events as json structured events
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, CloudEventSerializer.class);
        props.put(CloudEventSerializer.ENCODING_CONFIG, Encoding.STRUCTURED);
        props.put(CloudEventSerializer.EVENT_FORMAT_CONFIG, JsonFormat.CONTENT_TYPE);

        // Create the KafkaProducer
        KafkaProducer<String, CloudEvent> producer = new KafkaProducer<>(props);

        return producer;

    }

}