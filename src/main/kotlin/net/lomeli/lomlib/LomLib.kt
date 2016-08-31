package net.lomeli.lomlib

import net.lomeli.lomlib.lib.ModLibs
import net.lomeli.lomlib.core.Proxy
import net.lomeli.lomlib.core.config.ModConfig
import net.lomeli.lomlib.core.command.CommandLomLib
import net.lomeli.lomlib.lib.Config
import net.lomeli.lomlib.util.LogUtil
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent

@Mod(modid = ModLibs.MOD_ID, name = ModLibs.MOD_NAME, version = ModLibs.VERSION, guiFactory = ModLibs.CONFIG_FACTORY,
        acceptedMinecraftVersions = ModLibs.MINECRAFT_VERSION, dependencies = ModLibs.DEPENDENCIES,
        modLanguageAdapter = ModLibs.KOTLIN_ADAPTER, modLanguage = ModLibs.LANGUAGE)
object LomLib {

    @Mod.Instance(ModLibs.MOD_ID)
    var instance: LomLib? = null

    @SidedProxy(clientSide = ModLibs.CLIENT, serverSide = ModLibs.COMMON)
    var proxy: Proxy? = null

    var logger = LogUtil(ModLibs.MOD_NAME)

    var config: ModConfig? = null

    @Mod.EventHandler
    fun serverStarting(event: FMLServerStartingEvent) {
        event.registerServerCommand(CommandLomLib())
    }

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        logger = LogUtil(ModLibs.MOD_NAME)

        config = ModConfig(ModLibs.MOD_ID, event.suggestedConfigurationFile, Config::class.java)
        config?.loadConfig()

        proxy?.preInit()
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        ModLibs.initialized = true
        proxy?.init()
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        proxy?.postInit()
    }
}