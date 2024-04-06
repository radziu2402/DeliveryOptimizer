package com.ocado.basket.loader;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ocado.basket.reader.ResourceReader;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class DeliveryOptionsLoader {

    private final Gson gson;

    private final ResourceReader resourceReader;

    public DeliveryOptionsLoader(final Gson gson, final ResourceReader resourceReader) {
        this.gson = gson;
        this.resourceReader = resourceReader;
    }

    public Map<String, List<String>> loadDeliveryOptions(final String filePath) {
        String fileContent = resourceReader.getFileContent(filePath);
        Type type = new TypeToken<Map<String, List<String>>>() {
        }.getType();
        return gson.fromJson(fileContent, type);
    }
}
