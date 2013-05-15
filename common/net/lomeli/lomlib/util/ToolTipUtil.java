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
        return "\u00a7" + color + "\u00a7o" + message;
    }
	
}
