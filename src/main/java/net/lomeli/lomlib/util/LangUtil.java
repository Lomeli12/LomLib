package net.lomeli.lomlib.util;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

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
        return (args != null && args.length > 0) ? String.format(translate(unlocal), args) : translate(unlocal);
    }

    private static String translate(String unlocal) {
        String translation = StatCollector.translateToLocal(unlocal);
        if (!Strings.isNullOrEmpty(translation) && translation.contains(chatFormat)) {
            int count = StringUtils.countMatches(translation, chatFormat);
            for (int j = 0; j < count; j++) {
                int i = translation.indexOf(chatFormat);
                char value = translation.charAt(i + chatFormat.length());
                translation = translation.replace(chatFormat + value, getFormatFromChar(value) + "");
            }
        }
        return translation;
    }

    private static EnumChatFormatting getFormatFromChar(char ch) {
        EnumChatFormatting format = formatMap.get(ch);
        if (format == null)
            format = EnumChatFormatting.RESET;
        return format;
    }
}
