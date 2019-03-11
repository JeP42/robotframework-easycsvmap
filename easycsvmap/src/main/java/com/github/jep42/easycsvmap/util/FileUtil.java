package com.github.jep42.easycsvmap.util;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.commons.io.ByteOrderMark;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;

import com.github.jep42.easycsvmap.csv.io.CSVFile;

public final class FileUtil {

    private FileUtil() {}

    /**
     * Reads data from the given path and creates a new {@link CSVFile} out of it.
     *
     * @param filePath Path to the file that should be read
     * @return New CSVFile containing the data from the target file
     * @throws IOException if an I/O error occurs
     */
    public static CSVFile loadFile(String filePath) throws IOException {
        BOMInputStream input = new BOMInputStream(Files.newInputStream(Paths.get(filePath)));

        // Fetch BOM (optional) and actual data from the InputStream
        ByteOrderMark bom = input.getBOM();
        String content = IOUtils.toString(input, getCharSet(bom));

        return new CSVFile(content, bom);
    }


    public static String getSystemResourcePath(String resourcePath) {
        try {
            return Paths.get(ClassLoader.getSystemResource(resourcePath).toURI()).toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a charset object which represents the given byte order mark or the default charset for this JVM if the given parameter is <code>null</code>.
     * @param bom The byte order mark to create a charset for. Can be <code>null</code>.
     * @return Charset matching the given BOM or the default JVM charset if the BOM is <code>null</code>.
     */
    private static Charset getCharSet(ByteOrderMark bom) {
        return Optional.ofNullable(bom).map(ByteOrderMark::getCharsetName).map(Charset::forName).orElse(Charset.defaultCharset());
    }

}
