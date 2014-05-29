package net.lomeli.lomlib.util;

import org.lwjgl.input.Keyboard;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;

public class ToolTipUtil {
    public static boolean doAdditionalInfo() {
        return KeyBoardUtil.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    /**
     * Search up the color codes on the minecraft wiki page for sign colors
     * 
     * @param color
     *            Single character that will adds in color and simple formatting
     * @author Lomeli12
     */
    public static String additionalInfoInstructions(String color) {
        String message = "<SHIFT for info>";
        return color + ITALIC + message;
    }

    public static String toolTipInfo(String color, String message) {
        return doAdditionalInfo() ? message : additionalInfoInstructions(color);
    }

    public static int getSplitStringHeight(FontRenderer renderer, String input, int width) {
        @SuppressWarnings("rawtypes")
        List stringRows = renderer.listFormattedStringToWidth(input, width);
        return stringRows.size() * renderer.FONT_HEIGHT;
    }

    public static final String BLACK = "\u00a70";
    public static final String DARK_BLUE = "\u00a71";
    public static final String GREEN = "\u00a72";
    public static final String TEAL = "\u00a73";
    public static final String RED = "\u00a74";
    public static final String PURPLE = "\u00a75";
    public static final String ORANGE = "\u00a76";
    public static final String LIGHT_GRAY = "\u00a77";
    public static final String DARK_GRAY = "\u00a78";
    public static final String BLUE = "\u00a79";
    public static final String LIME_GREEN = "\u00a7a";
    public static final String CYAN = "\u00a7b";
    public static final String LIGHT_RED = "\u00a7c";
    public static final String MAGENTA = "\u00a7d";
    public static final String YELLOW = "\u00a7e";
    public static final String WHITE = "\u00a7f";
    public static final String OBFUSCATED = "\u00a7k";
    public static final String BOLD = "\u00a7l";
    public static final String STRIKE = "\u00a7m";
    public static final String UNDERLINE = "\u00a7n";
    public static final String ITALIC = "\u00a7o";
}
