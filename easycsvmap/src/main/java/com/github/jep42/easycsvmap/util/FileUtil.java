package com.github.jep42.easycsvmap.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class FileUtil {

    private FileUtil() {}

    public static String loadFile(String filePath) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filePath));
        return new String(encoded);
    }


    public static String getSystemResourcePath(String resourcePath) {
        try {
            return Paths.get(ClassLoader.getSystemResource(resourcePath).toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
