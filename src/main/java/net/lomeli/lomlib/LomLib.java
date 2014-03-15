package net.lomeli.lomlib;

import java.io.File;

import net.lomeli.lomlib.command.CommandLomLib;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.LogHelper;
import net.lomeli.lomlib.util.XMLConfiguration;
import net.lomeli.lomlib.util.XMLConfiguration.ConfigEnum;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod(modid = Strings.MOD_ID, name = Strings.MOD_NAME, version = Strings.VERSION)
@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class LomLib {

    public static LogHelper logger;

    public static boolean debug, capes, optiFailSafe;

    @SidedProxy(clientSide = Strings.CLIENT, serverSide = Strings.COMMON)
    public static Proxy proxy;
    
    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(Strings.MOD_NAME);
        configureMod(event.getSuggestedConfigurationFile());
        proxy.doStuffPre();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.doStuffPost();
    }

    public void configureMod(File configFile) {
        XMLConfiguration config = new XMLConfiguration(configFile);

        config.loadXml();

        debug = config.getBoolean("debugMode", false, Strings.DEBUG_MODE, ConfigEnum.GENERAL_CONFIG);
        capes = config.getBoolean("capes", true, Strings.CAPES, ConfigEnum.GENERAL_CONFIG);

        config.saveXML();
    }
}
