package dk.anderz.blomsterplukning.configuration;

import dk.anderz.blomsterplukning.BlomsterPlukning;
import dk.anderz.blomsterplukning.utils.ColorUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class Guis {
    private static Map<String, String[]> messages;

    public static void loadALl() {
        messages = new HashMap<>();
        for (String path : BlomsterPlukning.guiYML.getKeys(true)) {
            if (!BlomsterPlukning.guiYML.isConfigurationSection(path)) {
                if (BlomsterPlukning.guiYML.getStringList(path) != null && BlomsterPlukning.guiYML.isList(path)) {
                    List<String> stringList = ColorUtils.getColored(BlomsterPlukning.guiYML.getStringList(path));
                    messages.put(path, stringList.toArray(new String[0]));
                    continue;
                }
                if (BlomsterPlukning.guiYML.getString(path) != null) {
                    List<String> stringList = Collections.singletonList(ColorUtils.getColored(BlomsterPlukning.guiYML.getString(path)));
                    messages.put(path, stringList.toArray(new String[0]));
                }
            }
        }
    }

    public static String[] get(String path) {
        return messages.getOrDefault(path, new String[]{});
    }

    public static String[] getColored(String path) {
        return ColorUtils.getColored(get(path));
    }

    public static String[] get(String path, String... replacements) {
        if (messages.containsKey(path)) {
            String[] messages = get(path);
            List<String> messageList = new ArrayList<>();
            for (String message : messages) {
                for (int i = 0; i < replacements.length; i += 2) {
                    message = message.replaceAll(replacements[i], replacements[i + 1]);
                }
                messageList.add(message);
            }
            return messageList.toArray(new String[0]);
        }
        return new String[]{};
    }

    public static String @NotNull [] get2(String path, String... replacements) {
        if (messages.containsKey(path)) {
            String[] messages = get(path);
            List<String> messageList = new ArrayList<>();
            for (String message : messages) {
                // Use replaceLore method instead of the inner loop
                String[] replacedLines = replaceLore(new String[]{message}, replacements);
                messageList.addAll(Arrays.asList(replacedLines));
            }
            return messageList.toArray(new String[0]);
        }
        return new String[]{};
    }

    public static String[] replaceLore(String[] lore, String... replacements) {
        List<String> newLore = new ArrayList<>(lore.length);
        for (String line : lore) {
            for (int i = 0; i < replacements.length; i += 2) {
                line = StringUtils.replace(line, replacements[i], replacements[i + 1]);
            }
            newLore.add(line);
        }
        return newLore.toArray(new String[0]);
    }

    public static String[] getColored(String path, String... replacements) {
        return ColorUtils.getColored(get(path, replacements));
    }
}