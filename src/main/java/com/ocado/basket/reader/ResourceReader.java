package com.ocado.basket.reader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ResourceReader {

    public String getFileContent(String filename) {
        try {
            return Files.readString(Path.of(filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
