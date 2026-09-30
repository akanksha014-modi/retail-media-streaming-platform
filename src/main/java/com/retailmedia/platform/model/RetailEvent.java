package com.retailmedia.platform.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
public record RetailEvent(@NotBlank String eventId, @NotBlank String tenantId, @NotNull EventType eventType,
                          @NotBlank String userId, String sessionId, String campaignId, String adId,
                          String productId, @NotNull Instant timestamp) {}
