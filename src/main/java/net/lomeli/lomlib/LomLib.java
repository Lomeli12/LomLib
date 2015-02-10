package net.lomeli.lomlib;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import net.lomeli.lomlib.core.CommandLomLib;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.LogHelper;
import net.lomeli.lomlib.util.SimpleConfig;

@Mod(modid = Strings.MOD_ID, name = Strings.MOD_NAME, version = Strings.VERSION, guiFactory = Strings.CONFIG_FACTORY)
public class LomLib {

    @Mod.Instance
    public static LomLib instance;

    @SidedProxy(clientSide = Strings.CLIENT, serverSide = Strings.COMMON)
    public static Proxy proxy;

    public static LogHelper logger;

    public static boolean debug = false, crown = true;
    public static SimpleConfig config;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(Strings.MOD_NAME);
        config = new SimpleConfig(Strings.MOD_ID, new Configuration(event.getSuggestedConfigurationFile())) {
            @Override
            public void loadConfig() {
                debug = getConfig().getBoolean("debugMode", Configuration.CATEGORY_GENERAL, debug, Strings.DEBUG_MODE);
                crown = getConfig().getBoolean("patreon", Configuration.CATEGORY_GENERAL, crown, Strings.CROWN);
                if (getConfig().hasChanged())
                    getConfig().save();
            }
        };
        config.loadConfig();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
