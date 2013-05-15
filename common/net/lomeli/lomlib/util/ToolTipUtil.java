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
	
	public static String additionalInfoInstructions(String color) 
	{
        String message = "Press SHIFT for more info.";
        return "\u00a7" + color + "\u00a7o" + message;
    }
	
	public static String gibberish()
	{
		return "\u00a7k";
	}
}
