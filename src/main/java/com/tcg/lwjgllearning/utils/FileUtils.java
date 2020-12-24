package com.tcg.lwjgllearning.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;

public interface FileUtils {

    static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file.", e);
        }
    }

    static InputStream getResourceAsStream(String filePath, OpenOption... openOptions) {
        try {
            return Files.newInputStream(Paths.get(filePath), openOptions);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file.", e);
        }
    }

}
