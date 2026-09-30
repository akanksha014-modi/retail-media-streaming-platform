package com.retailmedia.platform.controller;
import com.retailmedia.platform.model.MetricResponse;
import com.retailmedia.platform.service.InsightsService;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/v1/ad")
public class InsightsController {
  private final InsightsService service;
  public InsightsController(InsightsService service) { this.service = service; }
  @GetMapping("/{campaignId}/clicks")
  public MetricResponse clicks(@RequestHeader("X-Tenant-Id") String tenant, @PathVariable String campaignId) {
    return new MetricResponse(campaignId, "clicks", service.clicks(tenant, campaignId));
  }
  @GetMapping("/{campaignId}/impressions")
  public MetricResponse impressions(@RequestHeader("X-Tenant-Id") String tenant, @PathVariable String campaignId) {
    return new MetricResponse(campaignId, "impressions", service.impressions(tenant, campaignId));
  }
  @GetMapping("/{campaignId}/click-to-basket")
  public MetricResponse clickToBasket(@RequestHeader("X-Tenant-Id") String tenant, @PathVariable String campaignId) {
    return new MetricResponse(campaignId, "clickToBasket", service.clickToBasket(tenant, campaignId));
  }
}
