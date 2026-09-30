package com.retailmedia.platform.service;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.stereotype.Service;
@Service
public class InsightsService {
  private final StreamsBuilderFactoryBean streamsFactory;
  public InsightsService(StreamsBuilderFactoryBean streamsFactory) { this.streamsFactory = streamsFactory; }
  public long clicks(String tenant, String campaign) { return metric("campaign-click-counts", tenant, campaign); }
  public long impressions(String tenant, String campaign) { return metric("campaign-impression-counts", tenant, campaign); }
  public long clickToBasket(String tenant, String campaign) { return metric("campaign-click-to-basket-counts", tenant, campaign); }
  private long metric(String storeName, String tenant, String campaign) {
    KafkaStreams streams = streamsFactory.getKafkaStreams();
    if (streams == null) throw new IllegalStateException("Kafka Streams is not running");
    ReadOnlyKeyValueStore<String, Long> store = streams.store(StoreQueryParameters.fromNameAndType(storeName, QueryableStoreTypes.keyValueStore()));
    Long value = store.get(tenant + ":" + campaign);
    return value == null ? 0L : value;
  }
}
