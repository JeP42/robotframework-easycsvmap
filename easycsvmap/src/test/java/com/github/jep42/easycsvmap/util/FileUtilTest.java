package com.github.jep42.easycsvmap.util;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.apache.commons.io.ByteOrderMark;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testGetFileUtilFor() throws Exception {
        String csvFilePath = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        FileUtil fileUtilForCsvFile = FileUtil.getFileUtilFor(csvFilePath);

        assertNull(fileUtilForCsvFile.getBom());
        assertNotNull(fileUtilForCsvFile.getContent());
    }

    @Test
    public void testGetFileUtilFor_WithUTF8ByteOrderMark() throws Exception {
        String csvFilePath = FileUtil
                .getSystemResourcePath("com/github/jep42/easycsvmap/no-header-five-lines_UTF-8_BOM.csv");

        FileUtil fileUtilForCsvFile = FileUtil.getFileUtilFor(csvFilePath);

        assertEquals(ByteOrderMark.UTF_8, fileUtilForCsvFile.getBom());
        assertNotNull(fileUtilForCsvFile.getContent());
    }

    @Test
    public void testGetFileUtilFor_streamClosed() throws Exception {

        // create temp file
        Path target = Paths.get(System.getProperty("java.io.tmpdir"), UUID.randomUUID().toString());
        Path source = Paths.get(FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv"));
        Files.copy(source, target);

        // target was created successfully
        FileUtil fileUtilForTargetFile = FileUtil.getFileUtilFor(target.toAbsolutePath().toString());
        assertNotNull(fileUtilForTargetFile.getContent());

        // assure there is no open file handle on target file
        Files.delete(target);
        assertFalse(Files.exists(target));
    }

}