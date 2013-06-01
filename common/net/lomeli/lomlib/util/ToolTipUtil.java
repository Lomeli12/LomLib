package net.lomeli.lomlib.util;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class ToolTipUtil 
{
	public static boolean doAdditionalInfo() 
	{
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) 
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
                return true;
        }
        return false;
    }
	/** Search up the color codes on the minecraft wiki page for sign colors 
	 * @param color Single character that will adds in color and simple formatting
	 * @author Lomeli12
	 */
	public static String additionalInfoInstructions(String color) 
	{
        String message = "<SHIFT for info>";
        return "\u00a7" + color + ITALIC + message;
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
