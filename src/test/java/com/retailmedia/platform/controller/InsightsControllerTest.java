package com.retailmedia.platform.controller;
import com.retailmedia.platform.service.InsightsService;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
class InsightsControllerTest {
  @Test void returnsClickCount() {
    InsightsService service = mock(InsightsService.class);
    when(service.clicks("retailer-1", "campaign-100")).thenReturn(42L);
    var response = new InsightsController(service).clicks("retailer-1", "campaign-100");
    assertEquals(42L, response.value());
    assertEquals("clicks", response.metric());
  }
}
