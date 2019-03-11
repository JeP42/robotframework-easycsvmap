package com.github.jep42.easycsvmap.util;

import static org.junit.Assert.*;

import org.apache.commons.io.ByteOrderMark;
import org.junit.Test;

public class FileUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String csvFilePath = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        FileUtil fileUtilForCsvFile = FileUtil.getFileUtilFor(csvFilePath);

        assertNull(fileUtilForCsvFile.getBom());
        assertNotNull(fileUtilForCsvFile.getContent());
    }

    @Test
    public void testLoadFile_WithUTF8ByteOrderMark() throws Exception {
        String csvFilePath = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/no-header-five-lines_UTF-8_BOM.csv");

        FileUtil fileUtilForCsvFile = FileUtil.getFileUtilFor(csvFilePath);

        assertEquals(ByteOrderMark.UTF_8, fileUtilForCsvFile.getBom());
        assertNotNull(fileUtilForCsvFile.getContent());
    }

}