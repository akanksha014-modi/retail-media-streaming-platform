package com.retailmedia.platform.producer;
import com.retailmedia.platform.model.RetailEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
@Service
public class EventProducer {
  public static final String TOPIC = "retail-events";
  private final KafkaTemplate<String, RetailEvent> kafkaTemplate;
  public EventProducer(KafkaTemplate<String, RetailEvent> kafkaTemplate) { this.kafkaTemplate = kafkaTemplate; }
  public void publish(RetailEvent event) {
    String key = event.tenantId() + ":" + event.userId();
    kafkaTemplate.send(TOPIC, key, event);
  }
}
