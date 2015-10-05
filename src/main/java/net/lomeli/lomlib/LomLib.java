package net.lomeli.lomlib;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

import net.lomeli.lomlib.core.CommandLomLib;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.core.config.ModConfig;
import net.lomeli.lomlib.core.config.annotations.ConfigBoolean;
import net.lomeli.lomlib.lib.ModLibs;
import net.lomeli.lomlib.util.LogHelper;

@Mod(modid = ModLibs.MOD_ID, name = ModLibs.MOD_NAME, version = ModLibs.VERSION, guiFactory = ModLibs.CONFIG_FACTORY, acceptedMinecraftVersions = ModLibs.MINECRAFT_VERSION, dependencies = ModLibs.DEPENDENCIES)
public class LomLib {

    @Mod.Instance
    public static LomLib instance;

    @SidedProxy(clientSide = ModLibs.CLIENT, serverSide = ModLibs.COMMON)
    public static Proxy proxy;

    public static LogHelper logger;

    @ConfigBoolean(defaultValue = true)
    public static boolean crown;
    @ConfigBoolean(defaultValue = true, comment = "Overrides the \"Mod Options\" button in the pause menu to go to the Mod list menu so you can edit configs in-game")
    public static boolean overrideModOptions;

    public static ModConfig config;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = LogHelper.createLogger(ModLibs.MOD_NAME);
        config = new ModConfig(ModLibs.MOD_ID, event.getSuggestedConfigurationFile(), this.getClass());
        config.loadConfig();

        proxy.preInit();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ModLibs.initialized = true;
        proxy.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit();
    }
}
