package com.ocado.basket.integration;

import com.ocado.basket.BasketSplitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BasketSplitterIntegrationTest {

    private BasketSplitter basketSplitter;

    @BeforeEach
    void setUp() {
        basketSplitter = new BasketSplitter("src/main/resources/config.json");
    }

    @ParameterizedTest
    @MethodSource("provideTestCases")
    void split_goodProducts_resultOk(List<String> data, Map<String, List<String>> expectedResult) {
        // given when
        Map<String, List<String>> result = basketSplitter.split(data);

        // then
        assertEquals(expectedResult, result);
    }

    private static Stream<Arguments> provideTestCases() {
        return Stream.of(
                Arguments.of(
                        List.of("Fond - Chocolate", "Chocolate - Unsweetened", "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen",
                                "Longan", "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear"),
                        Map.of("Express Collection", List.of("Fond - Chocolate", "Chocolate - Unsweetened",
                                "Nut - Almond, Blanched, Whole", "Haggis", "Mushroom - Porcini Frozen", "Longan",
                                "Bag Clear 10 Lb", "Nantucket - Pomegranate Pear"))
                ),
                Arguments.of(
                        List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies",
                                "Fond - Chocolate", "Cookies - Englishbay Wht"),
                        Map.of(
                                "Courier", List.of("Cocoa Butter", "Tart - Raisin And Pecan", "Table Cloth 54x72 White", "Flower - Daisies",
                                        "Cookies - Englishbay Wht"),
                                "Pick-up point", List.of("Fond - Chocolate")
                        )
                )
        );
    }

    @ParameterizedTest
    @MethodSource("provideDataForExceptions")
    public void split_wrongInputData_throwsException(List<String> data, Class<Exception> expectedException, String expectedMessage) {
        // given when
        Exception exception = assertThrows(expectedException, () -> basketSplitter.split(data));

        // then
        assertEquals(expectedMessage, exception.getMessage());
    }

    private static Stream<Arguments> provideDataForExceptions() {
        return Stream.of(
                Arguments.of(List.of(), IllegalArgumentException.class, "Items list cannot be null or empty."),
                Arguments.of(List.of("Product Not In Config"), IllegalArgumentException.class, "Item not found in delivery options configuration: Product Not In Config")
        );
    }
}
