package dk.anderz.blomsterplukning.utils;

import net.md_5.bungee.api.ChatColor;

import java.util.List;

public class ColorUtils {
    /**
     * Replaces & with §
     * @param s String to be colored
     * @return Colored string
     */
    public static String plain(String s) {
        return s.replaceAll("§", "&");
    }

    /**
     * Replaces & with §
     * @param stringList List of strings to be colored
     * @return Colored string list
     */
    public static String[] getColored(String... stringList) {
        if (stringList == null)
            return null;
        for (int i = 0; i < stringList.length; i++)
            stringList[i] = getColored(stringList[i]);
        return stringList;
    }

    /**
     * Replaces & with §
     * @param stringList List of strings to be colored
     * @return Colored string list
     */

    public static List<String> getColored(List<String> stringList) {
        if (stringList == null)
            return null;
        for (int i = 0; i < stringList.size(); i++)
            stringList.set(i, getColored(stringList.get(i)));
        return stringList;
    }

    /**
     * Replaces & with §
     * @param s String to be colored
     * @return Colored string
     */

    public static String getColored(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
