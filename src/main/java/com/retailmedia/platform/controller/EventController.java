package com.retailmedia.platform.controller;
import com.retailmedia.platform.model.RetailEvent;
import com.retailmedia.platform.producer.EventProducer;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/events")
public class EventController {
  private final EventProducer producer;
  public EventController(EventProducer producer) { this.producer = producer; }
  @PostMapping
  public ResponseEntity<Void> publish(@Valid @RequestBody RetailEvent event) {
    producer.publish(event);
    return ResponseEntity.accepted().build();
  }
}
