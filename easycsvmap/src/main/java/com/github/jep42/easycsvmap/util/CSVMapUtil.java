package com.github.jep42.easycsvmap.util;


public final class CSVMapUtil {

    private CSVMapUtil() {}

    public static boolean isValidInteger(String intString) {
        try {
            Integer.parseInt(intString);

        } catch (NumberFormatException e) {
            // we can ignore this...its ok...really...
            return false;
        }
        return true;
    }

    public static String removeBracesFromString(String str) {
        return str.substring(1, str.length() - 1);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str);
    }

}
