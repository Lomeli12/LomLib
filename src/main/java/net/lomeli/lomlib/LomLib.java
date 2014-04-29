package net.lomeli.lomlib;

import java.io.File;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.RecipeSorter;

import net.lomeli.lomlib.client.CommandLomLib;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.recipes.AnvilRecipeManager;
import net.lomeli.lomlib.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.util.LogHelper;
import net.lomeli.lomlib.util.XMLConfiguration;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = Strings.MOD_ID, name = Strings.MOD_NAME, version = Strings.VERSION)
public class LomLib {
    
    @Mod.Instance
    public static LomLib instance;

    @SidedProxy(clientSide = Strings.CLIENT, serverSide = Strings.COMMON)
    public static Proxy proxy;

    public static LogHelper logger;

    public static boolean debug, capes;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(Strings.MOD_NAME);

        configureMod(event.getSuggestedConfigurationFile());
        
        RecipeSorter.register(Strings.NEI_SHAPED, ShapedFluidRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(Strings.NEI_SHAPELESS, ShapelessFluidRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        proxy.doStuffPre();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.doStuffInit();
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(new AnvilRecipeManager());
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.doStuffPost();
    }

    public void configureMod(File configFile) {
        XMLConfiguration config = new XMLConfiguration(configFile);

        config.loadXml();

        debug = config.getBoolean("debugMode", false, Strings.DEBUG_MODE, XMLConfiguration.ConfigEnum.GENERAL_CONFIG);
        capes = config.getBoolean("capes", true, Strings.CAPES, XMLConfiguration.ConfigEnum.GENERAL_CONFIG);

        config.saveXML();
    }
}
