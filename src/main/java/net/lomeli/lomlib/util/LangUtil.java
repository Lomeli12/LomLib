package net.lomeli.lomlib.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;

public class LangUtil {
    private static String chatFormat = "/&";
    private static Map<Character, TextFormatting> formatMap;

    static {
        formatMap = Maps.newHashMap();
        for (TextFormatting formatting : TextFormatting.values())
            formatMap.put(formatting.formattingCode, formatting);
    }

    public static String translate(String unlocal, Object... args) {
        if (!Strings.isNullOrEmpty(unlocal))
            return (args != null && args.length > 0) ? String.format(translate(unlocal), args) : translate(unlocal);
        return unlocal;
    }

    private static String translate(String unlocal) {
        return I18n.translateToLocal(unlocal).replaceAll(chatFormat, "\u00a7");
    }

    private static TextFormatting getFormatFromChar(char ch) {
        TextFormatting format = formatMap.get(ch);
        if (format == null)
            format = TextFormatting.RESET;
        return format;
    }
}
