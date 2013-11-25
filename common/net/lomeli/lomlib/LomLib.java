package net.lomeli.lomlib;

import java.io.File;
import java.util.logging.Level;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

import net.lomeli.lomlib.capes.CapeUtil;
import net.lomeli.lomlib.entity.EntityBlock;
import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.render.RenderEntityBlock;
import net.lomeli.lomlib.util.DeofUtil;
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
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(LibraryStrings.MOD_NAME);

        configureMod(event.getSuggestedConfigurationFile());
        if(event.getSide().isClient()) {
            ResourceUtil.initResourceUtil();
            RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
            if(LomLib.capes) {

                DeofUtil.setFieldAccess("net.minecraft.client.entity.AbstractClientPlayer", "downloadImageCape", true);
                DeofUtil.setFieldAccess("net.minecraft.client.entity.AbstractClientPlayer", "locationCape", true);

                if(isOptifineInstalled()) {
                    logger.log(Level.WARNING,
                            "Optifine detected, capes disabled due to possible crash. If you want LomLib capes, please remove Optifine.");
                    logger.log(Level.WARNING,
                            "Or complain to Optifine's creator about that illegalAccess check thing, it's annoying. >_<");
                }else
                    CapeUtil.getInstance().readXML();
            }
        }
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