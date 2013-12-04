package net.lomeli.lomlib;

import java.io.File;
import java.util.logging.Level;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

import net.lomeli.lomlib.capes.CapeUtil;
import net.lomeli.lomlib.command.CommandLomLib;
import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.nei.NEIAddon;
import net.lomeli.lomlib.util.LogHelper;
import net.lomeli.lomlib.util.ResourceUtil;
import net.lomeli.lomlib.util.XMLConfiguration;
import net.lomeli.lomlib.util.XMLConfiguration.ConfigEnum;

import net.minecraftforge.common.MinecraftForge;

public class LomLib extends DummyModContainer {
    public LomLib() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = LibraryStrings.MOD_ID;
        meta.name = LibraryStrings.MOD_NAME;
        meta.version = LibraryStrings.VERSION;
        meta.authorList.add("Lomeli12");
        meta.url = "http://www.anthony-lomeli.net/";
        meta.description = "Shared library mod required by several of Lomeli's Mods.";
    }

    public static LogHelper logger;

    public static boolean debug, capes, optiFailSafe;
    
    @Subscribe
    public void serverStarting(FMLServerStartingEvent event){
        event.registerServerCommand(new CommandLomLib());
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(LibraryStrings.MOD_NAME);

        configureMod(event.getSuggestedConfigurationFile());
        
        if(event.getSide().isClient()) {
            ResourceUtil.initResourceUtil();
            
            if(LomLib.capes)
                CapeUtil.getInstance().readXML();
        }
    }
    
    @Subscribe
    public void postInit(FMLPostInitializationEvent event) {
        if(event.getSide().isClient())
            NEIAddon.loadAddon();
    }

    public void configureMod(File configFile) {
        XMLConfiguration config = new XMLConfiguration(configFile);
        
        config.loadXml();
        
        debug = config.getBoolean("debugMode", false, LibraryStrings.DEBUG_MODE, ConfigEnum.GENERAL_CONFIG);
        capes = config.getBoolean("capes", true, LibraryStrings.CAPES, ConfigEnum.GENERAL_CONFIG);
        
        config.saveXML();

        logger.log(Level.INFO, "Checking Minecraft Forge version...");

        if(LibraryStrings.recommendedForgeVersion.equalsIgnoreCase(MinecraftForge.getBrandingVersion().substring(16))) {
            logger.log(Level.FINE, "Using recommended version of Minecraft Forge");
        }else {
            logger.log(
                    Level.INFO,
                    ("This version of " + LibraryStrings.MOD_NAME + "(" + LibraryStrings.VERSION
                            + ") works best with Minecraft Forge v" + LibraryStrings.recommendedForgeVersion + ". Using that version is recommended"));
        }
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    public boolean isOptifineInstalled() {
        try {
            return Class.forName("optifine.OptiFineForgeTweaker") != null;
        }catch(ClassNotFoundException e) {
            return false;
        }
    }

}