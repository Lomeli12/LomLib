package net.lomeli.lomlib.util;

import java.util.logging.Level;

import net.lomeli.lomlib.LomLib;

import cpw.mods.fml.common.Loader;

public class ModLoaded
{

	public static boolean isModInstalled(String modID, String modName)
	{
		boolean isInstalled = false;
		if (Loader.isModLoaded(modID))
        {
            try
            {
            	LomLib.logger.log(Level.FINE, (modName + " is installed!"));
            	isInstalled = true;
            }
            catch(Exception ex)
            {
            	LomLib.logger.log(Level.WARNING, (modName + " is not installed!"));
            	isInstalled = false;
            }
        }
		else
		{
			LomLib.logger.log(Level.WARNING, (modName + " is not installed!"));
			isInstalled = false;
		}
		
		return isInstalled;
	}
	
	public static boolean isModInstalled(String modID)
	{
		boolean isInstalled = false;
		if (Loader.isModLoaded(modID))
        {
            try
            {
            	LomLib.logger.log(Level.FINE, (modID + " is installed!"));
            	isInstalled = true;
            }
            catch(Exception ex)
            {
            	LomLib.logger.log(Level.WARNING, (modID + " is not installed!"));
            	isInstalled = false;
            }
        }
		else
		{
			LomLib.logger.log(Level.WARNING, (modID + " is not installed!"));
			isInstalled = false;
		}
		
		return isInstalled;
	}
}
