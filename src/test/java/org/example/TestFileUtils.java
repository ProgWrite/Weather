package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileUtils {

    public static String readFile(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get("src/test/resources", filePath)));
    }

    public static String readJsonResponse(String fileName) throws IOException {
        return readFile(fileName);
    }
}
