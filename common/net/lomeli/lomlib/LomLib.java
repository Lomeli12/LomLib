package net.lomeli.lomlib;

import java.util.logging.Level;

import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.util.LogHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Simply here so it shows Forge Modloader will pick it up
 * and so my mods can see if it's installed
 * @author Lomeli12
 */

@Mod(modid=LibraryStrings.MOD_ID, name=LibraryStrings.MOD_NAME, version=LibraryStrings.VERSION, dependencies="required-after:Forge")
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class LomLib
{
	@Mod.Instance(LibraryStrings.MOD_ID)
	public static LomLib instance;
	
	public static LogHelper logger;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = new LogHelper(LibraryStrings.MOD_NAME);
		
		logger.log(Level.INFO, "Checking Minecraft version...");
		if(new String("Minecraft " + LibraryStrings.minecraftVersion).equals(
			Loader.instance().getMCVersionString()))logger.log(Level.FINE, "Using recommended version of Minecraft");
		else
			logger.log(Level.WARNING, ("This version of " + LibraryStrings.MOD_NAME + "(" +
				LibraryStrings.VERSION + ") is meant for Minecraft " + LibraryStrings.minecraftVersion +
				" make sure you are using the newest version of this mod or check that it's compatible" +
				" for your version of Minecraft."));
	}
	
}