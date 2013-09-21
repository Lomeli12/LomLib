package net.lomeli.lomlib.util;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public class KeyBoardUtil {
    /**
     * Checks if player is holding a certain key down
     * 
     * @param key
     * @return true if key is pressed.
     */
    public static boolean isKeyDown(int key) {
        if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
            return Keyboard.isKeyDown(key);
        return false;
    }
}
