package net.lomeli.lomlib.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.util.Map;

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

public class LangUtil {
    private static String chatFormat = "/&";
    private static Map<Character, EnumChatFormatting> formatMap;

    static {
        formatMap = Maps.newHashMap();
        for (EnumChatFormatting formatting : EnumChatFormatting.values())
            formatMap.put(formatting.formattingCode, formatting);
    }

    public static String translate(String unlocal, Object... args) {
        if (!Strings.isNullOrEmpty(unlocal))
            return (args != null && args.length > 0) ? String.format(translate(unlocal), args) : translate(unlocal);
        return unlocal;
    }

    private static String translate(String unlocal) {
        return StatCollector.translateToLocal(unlocal).replaceAll(chatFormat, "\u00a7");
    }

    private static EnumChatFormatting getFormatFromChar(char ch) {
        EnumChatFormatting format = formatMap.get(ch);
        if (format == null)
            format = EnumChatFormatting.RESET;
        return format;
    }
}
