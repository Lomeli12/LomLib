package net.lomeli.lomlib;

import java.io.File;
import java.util.logging.Level;

import net.lomeli.lomlib.capes.CapeUtil;
import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.util.LogHelper;

import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

/**
 * Simply here so Forge Modloader will pick it up
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
	
	public static boolean debug;
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = new LogHelper(LibraryStrings.MOD_NAME);
		
		configureMod(event.getSuggestedConfigurationFile());
		
		CapeUtil.getInstance().readXML();
	}
	
	public void configureMod(File configFile){
		Configuration config = new Configuration(configFile);
		
		config.load();
		
		debug = config.get("Options", "debugMode", false, LibraryStrings.DEBUG_MODE).getBoolean(false);
		
		config.save();
		
		logger.log(Level.INFO, "Checking Minecraft Forge version...");
		
		if(LibraryStrings.recommendedForgeVersion.equalsIgnoreCase(MinecraftForge.getBrandingVersion().substring(16))){
			System.out.println("Using recommended version of Minecraft Forge");
			logger.log(Level.FINE, "Using recommended version of Minecraft Forge");
		}
		else{
			logger.log(Level.INFO, ("This version of " + LibraryStrings.MOD_NAME + "(" +
				LibraryStrings.VERSION + ") works best with Minecraft Forge v" + 
				LibraryStrings.recommendedForgeVersion + ". Using that version is recommended"));
		}
	}
	
}