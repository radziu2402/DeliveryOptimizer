package com.ocado.basket.strategy;

import java.util.List;
import java.util.Map;

public interface DeliveryStrategy {
    Map<String, List<String>> optimizeDeliveryGroups(final List<String> items);
}
