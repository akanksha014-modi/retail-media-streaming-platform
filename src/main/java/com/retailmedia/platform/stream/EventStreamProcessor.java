package com.retailmedia.platform.stream;
import com.retailmedia.platform.model.*;
import com.retailmedia.platform.producer.EventProducer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import java.time.Duration;
@Configuration
@EnableKafkaStreams
public class EventStreamProcessor {
  @Bean
  public KStream<String, RetailEvent> processEvents(StreamsBuilder builder, Serde<RetailEvent> eventSerde) {
    KStream<String, RetailEvent> events = builder.stream(EventProducer.TOPIC, Consumed.with(Serdes.String(), eventSerde));
    aggregate(events, EventType.AD_CLICK, "campaign-click-counts");
    aggregate(events, EventType.AD_IMPRESSION, "campaign-impression-counts");
    clickToBasket(events, eventSerde);
    return events;
  }
  private void aggregate(KStream<String, RetailEvent> events, EventType type, String store) {
    events.filter((k,e) -> e != null && e.eventType() == type && e.campaignId() != null)
        .selectKey((k,e) -> e.tenantId() + ":" + e.campaignId())
        .groupByKey(Grouped.with(Serdes.String(), retailEventSerde()))
        .count(Materialized.as(store));
  }
  private void clickToBasket(KStream<String, RetailEvent> events, Serde<RetailEvent> eventSerde) {
    KStream<String,RetailEvent> clicks = events.filter((k,e)-> e != null && e.eventType()==EventType.AD_CLICK && e.productId()!=null)
        .selectKey((k,e)-> journeyKey(e));
    KStream<String,RetailEvent> baskets = events.filter((k,e)-> e != null && e.eventType()==EventType.ADD_TO_CART && e.productId()!=null)
        .selectKey((k,e)-> journeyKey(e));
    Serde<ClickToBasketEvent> resultSerde = Serdes.serdeFrom(new JsonSerializer<>(), new JsonDeserializer<>(ClickToBasketEvent.class));
    clicks.join(baskets,
        (click,basket)->new ClickToBasketEvent(click.tenantId(), click.campaignId(), click.userId(), click.productId()),
        JoinWindows.ofTimeDifferenceWithNoGrace(Duration.ofMinutes(30)),
        StreamJoined.with(Serdes.String(), eventSerde, eventSerde))
      .filter((k,e)-> e.campaignId()!=null)
      .selectKey((k,e)->e.tenantId()+":"+e.campaignId())
      .groupByKey(Grouped.with(Serdes.String(), resultSerde))
      .count(Materialized.as("campaign-click-to-basket-counts"));
  }
  private String journeyKey(RetailEvent e) { return e.tenantId()+":"+e.userId()+":"+e.productId(); }
  private Serde<RetailEvent> retailEventSerde() {
    JsonDeserializer<RetailEvent> d = new JsonDeserializer<>(RetailEvent.class); d.addTrustedPackages("com.retailmedia.platform.model");
    return Serdes.serdeFrom(new JsonSerializer<>(), d);
  }
}
