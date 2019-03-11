package com.github.jep42.easycsvmap.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.apache.commons.io.ByteOrderMark;
import org.junit.Test;

import com.github.jep42.easycsvmap.csv.io.CSVFile;

public class FileUtilTest {

    @Test
    public void testLoadFile() throws Exception {
        String csvFilePath = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/header-0-five-lines.csv");

        CSVFile csvFile = FileUtil.loadFile(csvFilePath);

        assertNull(csvFile.getBom());
        assertNotNull(csvFile.getContent());
    }

    @Test
    public void testLoadFile_WithUTF8ByteOrderMark() throws Exception {
        String csvFilePath = FileUtil.getSystemResourcePath("com/github/jep42/easycsvmap/no-header-five-lines_UTF-8_BOM.csv");

        CSVFile csvFile = FileUtil.loadFile(csvFilePath);

        assertEquals(ByteOrderMark.UTF_8, csvFile.getBom());
        assertNotNull(csvFile.getContent());
    }

}