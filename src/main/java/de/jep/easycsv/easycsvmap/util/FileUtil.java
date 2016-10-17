package de.jep.easycsv.easycsvmap.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class FileUtil {

    private FileUtil() {}

    public static String loadFile(String filePath) throws IOException {

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            return sb.toString();
        }
    }


}
