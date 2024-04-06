package com.ocado.basket;

import com.google.gson.Gson;
import com.ocado.basket.loader.DeliveryOptionsLoader;
import com.ocado.basket.reader.ResourceReader;
import com.ocado.basket.strategy.DeliveryStrategy;
import com.ocado.basket.strategy.DeliveryStrategyImpl;

import java.util.List;
import java.util.Map;

public class BasketSplitter {

    private final DeliveryStrategy optimizationStrategy;

    public BasketSplitter(String absolutePathToConfigFile) {
        DeliveryOptionsLoader loader = new DeliveryOptionsLoader(new Gson(), new ResourceReader());
        Map<String, List<String>> deliveryOptions = loader.loadDeliveryOptions(absolutePathToConfigFile);
        this.optimizationStrategy = new DeliveryStrategyImpl(deliveryOptions);
    }

    public Map<String, List<String>> split(List<String> items) {
        return optimizationStrategy.optimizeDeliveryGroups(items);
    }
}
