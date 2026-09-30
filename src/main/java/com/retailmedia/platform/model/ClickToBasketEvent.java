package com.retailmedia.platform.model;
public record ClickToBasketEvent(String tenantId, String campaignId, String userId, String productId) {}
