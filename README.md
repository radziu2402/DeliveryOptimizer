
# Basket Splitter Library

## Overview

The Basket Splitter Library is designed for an online supermarket aiming to expand its product range to include non-food items, such as electronics and home goods, which may require specialized delivery methods. This library optimizes the splitting of items in a customer's shopping basket into delivery groups, reducing the number of required deliveries and ensuring that the largest possible delivery groups are created.

## Problem Statement

In scenarios where products in a customer's basket cannot be delivered by standard means due to size constraints or because they are sourced from external suppliers, it becomes necessary to split the basket into different delivery groups based on the delivery options available for each product.

## Features

- Dynamically loads delivery options from a configuration file.
- Splits the shopping basket into the minimal number of delivery groups.
- Aims to maximize the size of each delivery group.

## Input

The program takes a list of product names from the customer's basket as input and uses a configuration file that defines possible delivery methods for all products offered in the store.

### Example

Input list:

```json
[
  "Steak (300g)",
  "Carrots (1kg)",
  "Cold Beer (330ml)",
  "AA Battery (4 Pcs.)",
  "Espresso Machine",
  "Garden Chair"
]
```

Configuration file content (`config.json`):

```json
{
  "Carrots (1kg)": ["Express Delivery", "Click&Collect"],
  "Cold Beer (330ml)": ["Express Delivery"],
  "Steak (300g)": ["Express Delivery", "Click&Collect"],
  "AA Battery (4 Pcs.)": ["Express Delivery", "Courier"],
  "Espresso Machine": ["Courier", "Click&Collect"],
  "Garden Chair": ["Courier"]
}
```

## Expected Output

The algorithm returns a map with the delivery method as the key and a list of products as the value.

### Example

Output (JSON representation):

```json
{
  "Express Delivery": ["Steak (300g)", "Carrot (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)"],
  "Courier": ["Espresso Machine", "Garden Chair"]
}
```

## Setup and Configuration

- Ensure Java 17 is installed on your system.
- Download `DeliveryOptimizer.jar` to your local machine.
- Load the library into your Java project.
- Initialize the `BasketSplitter` class with the absolute path to your `config.json`.

## Usage

```java
BasketSplitter splitter = new BasketSplitter("absolutePathToConfigFile");
Map<String, List<String>> deliveryGroups = splitter.split(items);
```

## Dependencies
The library is developed using Maven, ensuring easy management of dependencies and project build configuration. Key dependencies include:
- Gson for JSON parsing.
- JUnit 5 for unit testing, providing a modern and flexible testing framework.
- Mockito for mocking objects in tests, allowing for more comprehensive and isolated testing scenarios.

## Testing
The library includes both unit and integration tests developed with JUnit 5 and Mockito, ensuring comprehensive coverage and reliability. 
These tests, vital for verifying the library's functionality across various scenarios, are not packaged in the Fat JAR. 
For development and maintenance, developers can access these tests by either cloning the library's repository or downloading the project's ZIP file. 
This setup facilitates easy verification and extension of the library's capabilities, ensuring it meets the required standards for robustness and functionality.