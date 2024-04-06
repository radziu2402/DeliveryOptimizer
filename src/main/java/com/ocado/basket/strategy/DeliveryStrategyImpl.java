package com.ocado.basket.strategy;

import java.util.*;

public class DeliveryStrategyImpl implements DeliveryStrategy {

    private final Map<String, List<String>> deliveryOptions;

    public DeliveryStrategyImpl(Map<String, List<String>> deliveryOptions) {
        this.deliveryOptions = Objects.requireNonNull(deliveryOptions, "Delivery options cannot be null");
    }

    @Override
    public Map<String, List<String>> optimizeDeliveryGroups(final List<String> items) {
        validateItemsList(items);
        Map<String, List<String>> optimizedDeliveryGroups = new LinkedHashMap<>();
        Set<String> assignedProducts = new HashSet<>();

        while (assignedProducts.size() < items.size()) {
            Map.Entry<String, List<String>> mostCommon = findMostCommonDeliveryGroup(items, assignedProducts);
            if (mostCommon != null) {
                optimizedDeliveryGroups.put(mostCommon.getKey(), mostCommon.getValue());
                assignedProducts.addAll(mostCommon.getValue());
            } else {
                break;
            }
        }

        warnIfNotAllItemsAssigned(items, assignedProducts);

        return optimizedDeliveryGroups;
    }

    private void validateItemsList(List<String> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Items list cannot be null or empty.");
        }
    }

    private Map.Entry<String, List<String>> findMostCommonDeliveryGroup(List<String> items, Set<String> assignedProducts) {
        String mostCommonMethod = null;
        List<String> mostCommonProducts = new ArrayList<>();

        for (String item : items) {
            checkItemDeliveryOptions(item);
            List<String> availableMethods = deliveryOptions.get(item);
            for (String method : availableMethods) {
                if (assignedProducts.contains(item)) {
                    continue;
                }

                List<String> potentialGroup = findPotentialGroupForMethod(items, method, assignedProducts);
                if (potentialGroup.size() > mostCommonProducts.size()) {
                    mostCommonMethod = method;
                    mostCommonProducts = new ArrayList<>(potentialGroup);
                }
            }
        }

        return mostCommonMethod != null ? new AbstractMap.SimpleEntry<>(mostCommonMethod, mostCommonProducts) : null;
    }

    private void checkItemDeliveryOptions(String item) {
        if (!deliveryOptions.containsKey(item)) {
            throw new IllegalArgumentException("Item not found in delivery options configuration: " + item);
        } else if (deliveryOptions.get(item).isEmpty()) {
            throw new IllegalArgumentException("Delivery options list for item " + item + " is empty.");
        }
    }

    private List<String> findPotentialGroupForMethod(List<String> items, String method, Set<String> assignedProducts) {
        List<String> potentialGroup = new ArrayList<>();
        for (String potentialItem : items) {
            if (deliveryOptions.getOrDefault(potentialItem, Collections.emptyList()).contains(method)
                    && !assignedProducts.contains(potentialItem)) {
                potentialGroup.add(potentialItem);
            }
        }
        return potentialGroup;
    }

    private void warnIfNotAllItemsAssigned(List<String> items, Set<String> assignedProducts) {
        if (assignedProducts.size() < items.size()) {
            System.out.println("Warning: Not all items have been assigned to a delivery group.");
        }
    }
}
