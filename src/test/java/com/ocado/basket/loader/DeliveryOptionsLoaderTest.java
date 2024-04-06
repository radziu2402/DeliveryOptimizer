package com.ocado.basket.loader;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.ocado.basket.reader.ResourceReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DeliveryOptionsLoaderTest {

    private static final Gson JSON_LIBRARY = new Gson();

    @Mock
    private ResourceReader resourceReader;

    private DeliveryOptionsLoader optionsLoader;

    @BeforeEach
    void setUp() {
        optionsLoader = new DeliveryOptionsLoader(JSON_LIBRARY, resourceReader);
    }

    @ParameterizedTest
    @MethodSource("provideLoadDeliveryOptionsData")
    void loadDeliveryOptions_shouldReturnConfig_Ok(String json, Map<String, List<String>> expectedResult) {
        // given
        String filepath = "test.json";

        when(resourceReader.getFileContent(filepath)).thenReturn(json);

        // when
        Map<String, List<String>> result = optionsLoader.loadDeliveryOptions(filepath);

        // then
        assertEquals(expectedResult, result);
    }

    @Test
    void loadDeliveryOptions_throwsException_whenFileIsNotJson() {
        // json
        String filepath = "test.json";
        String badJson = "notAJson";

        when(resourceReader.getFileContent(filepath)).thenReturn(badJson);

        // when
        assertThrows(JsonSyntaxException.class, () -> optionsLoader.loadDeliveryOptions(filepath));
    }

    private static Stream<Arguments> provideLoadDeliveryOptionsData() {
        return Stream.of(
                Arguments.of(
                        "{\"Carrots (1kg)\": [\"Express Delivery\", \"Click&Collect\"]}",
                        Map.of("Carrots (1kg)", List.of("Express Delivery", "Click&Collect"))
                ),
                Arguments.of(
                        "{}",
                        Map.of()
                ),
                Arguments.of(
                        "{\"Nonexistent Item\": [\"Nonexistent Delivery\"]}",
                        Map.of("Nonexistent Item", List.of("Nonexistent Delivery"))
                )
        );
    }
}
