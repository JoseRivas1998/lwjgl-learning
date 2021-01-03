package com.tcg.lwjgllearning.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public interface FileUtils {

    static String readFile(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            throw new RuntimeException("Unable to read file.", e);
        }
    }

    static List<String> readFileLines(String filePath) {
        try (final Scanner scanner = new Scanner(getResourceAsStream(filePath))) {
            List<String> lines = new ArrayList<>();
            while(scanner.hasNextLine()) {
                lines.add(scanner.nextLine());
            }
            return lines.stream().collect(Collectors.toUnmodifiableList());
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
