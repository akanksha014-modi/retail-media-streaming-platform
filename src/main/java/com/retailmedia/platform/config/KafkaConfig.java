package com.retailmedia.platform.config;
import com.retailmedia.platform.model.RetailEvent;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
@Configuration
public class KafkaConfig {
  @Bean
  public Serde<RetailEvent> retailEventSerde() {
    JsonDeserializer<RetailEvent> d = new JsonDeserializer<>(RetailEvent.class);
    d.addTrustedPackages("com.retailmedia.platform.model");
    return Serdes.serdeFrom(new JsonSerializer<>(), d);
  }
}
