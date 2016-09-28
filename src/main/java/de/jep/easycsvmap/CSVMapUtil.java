package de.jep.easycsvmap;


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

}
