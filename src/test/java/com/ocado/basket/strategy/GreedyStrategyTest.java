package com.ocado.basket.strategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class GreedyStrategyTest {

    private static final Map<String, List<String>> config = Map.of(
            "Carrots (1kg)", List.of("Express Delivery", "Click&Collect"),
            "Steak (300g)", List.of("Express Delivery", "Click&Collect"),
            "AA Battery (4 Pcs.)", List.of("Express Delivery", "Courier"),
            "Espresso Machine", List.of("Courier", "Click&Collect"),
            "Garden Chair", List.of("Courier"),
            "Cold Beer (330ml)", List.of("Express Delivery"),
            "EmptyOptionsProduct", List.of()
    );

    private DeliveryStrategy deliveryStrategy;

    @BeforeEach
    void setUp() {
        deliveryStrategy = new DeliveryStrategyImpl(config);
    }

    @Test
    void optimizeDeliveryGroups_DeliveryOptionsIsNull_ExpectingException() {
        // given
        String expectedMessage = "Delivery options cannot be null";

        // when
        Exception exception = assertThrows(NullPointerException.class, () -> new DeliveryStrategyImpl(null));

        // then
        assertEquals(expectedMessage, exception.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideDataForExceptions")
    void optimizeDeliveryGroups_ExpectingExceptions(List<String> testData, Class<Exception> expectedException, String expectedMessage) {
        // given when
        Exception exception = assertThrows(expectedException, () -> deliveryStrategy.optimizeDeliveryGroups(testData));

        // then
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> provideDataForExceptions() {
        return Stream.of(
                Arguments.of(
                        List.of(),
                        IllegalArgumentException.class,
                        "Items list cannot be null or empty."
                ),
                Arguments.of(
                        null,
                        IllegalArgumentException.class,
                        "Items list cannot be null or empty."
                ),
                Arguments.of(
                        List.of("ProductNotInConfig"),
                        IllegalArgumentException.class,
                        "Item not found in delivery options configuration: ProductNotInConfig"),
                Arguments.of(
                        List.of("EmptyOptionsProduct"),
                        IllegalArgumentException.class,
                        "Delivery options list for item EmptyOptionsProduct is empty.")
        );
    }

    @ParameterizedTest
    @MethodSource("deliveryGroupsScenarios")
    void optimizeDeliveryGroups_VariousScenarios_CorrectGrouping(List<String> testData, Map<String, List<String>> expectedResult) {
        // given when
        Map<String, List<String>> result = deliveryStrategy.optimizeDeliveryGroups(testData);

        // then
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> deliveryGroupsScenarios() {
        return Stream.of(
                Arguments.of(
                        List.of("Cold Beer (330ml)", "Garden Chair"),
                        Map.of(
                                "Courier", List.of("Garden Chair"),
                                "Express Delivery", List.of("Cold Beer (330ml)")
                        )
                ),
                Arguments.of(
                        List.of("Carrots (1kg)", "Garden Chair"),
                        Map.of(
                                "Courier", List.of("Garden Chair"),
                                "Express Delivery", List.of("Carrots (1kg)")
                        )
                ),
                Arguments.of(
                        List.of("Carrots (1kg)", "Steak (300g)"),
                        Map.of(
                                "Express Delivery", List.of("Carrots (1kg)", "Steak (300g)")
                        )
                ),
                Arguments.of(
                        List.of(
                                "Steak (300g)",
                                "Carrots (1kg)",
                                "Cold Beer (330ml)",
                                "AA Battery (4 Pcs.)",
                                "Espresso Machine",
                                "Garden Chair"),
                        Map.of(
                                "Express Delivery", List.of("Steak (300g)", "Carrots (1kg)", "Cold Beer (330ml)", "AA Battery (4 Pcs.)"),
                                "Courier", List.of("Espresso Machine", "Garden Chair")
                        )
                )
        );
    }
}
