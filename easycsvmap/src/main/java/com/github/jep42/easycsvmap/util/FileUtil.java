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

public final class FileUtil {

    private String filePath;

    private ByteOrderMark bom;

    private String content;

    private FileUtil(String filePath) {
        this.filePath = filePath;

    }

    public static FileUtil getFileUtilFor(String filePath) throws IOException {
        FileUtil fileUtilInstance = new FileUtil(filePath);

        fileUtilInstance.initialize();

        return fileUtilInstance;
    }

    private void initialize() throws IOException {
        try (BOMInputStream input = new BOMInputStream(Files.newInputStream(Paths.get(this.filePath)))) {
            // Fetch BOM (optional) and actual data from the InputStream
            this.bom = input.getBOM();
            this.content = IOUtils.toString(input, getCharSet(bom));
        }
    }

    public String getContent() {
        return this.content;
    }

    public ByteOrderMark getBom() {
        return this.bom;
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
