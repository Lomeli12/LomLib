package net.lomeli.lomlib;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.RecipeSorter;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.client.CommandLomLib;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.recipes.AnvilRecipeManager;
import net.lomeli.lomlib.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.util.LogHelper;

@Mod(modid = Strings.MOD_ID, name = Strings.MOD_NAME, version = Strings.VERSION, guiFactory = Strings.CONFIG_FACTORY)
public class LomLib {

    @Mod.Instance
    public static LomLib instance;

    @SidedProxy(clientSide = Strings.CLIENT, serverSide = Strings.COMMON)
    public static Proxy proxy;

    public static LogHelper logger;

    public static boolean debug = false, capes = true, slime = false;
    public static Configuration config;

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandLomLib());
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = new LogHelper(Strings.MOD_NAME);
        config = new Configuration(event.getSuggestedConfigurationFile());
        configureMod();

        RecipeSorter.register(Strings.NEI_SHAPED, ShapedFluidRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(Strings.NEI_SHAPELESS, ShapelessFluidRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        proxy.checkForUpdate();
        proxy.doStuffPre();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(proxy);
        MinecraftForge.EVENT_BUS.register(new AnvilRecipeManager());
        FMLCommonHandler.instance().bus().register(this);
        proxy.doStuffInit();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.doStuffPost();
    }

    public static void configureMod() {
        debug = config.getBoolean("debugMode", Configuration.CATEGORY_GENERAL, debug, Strings.DEBUG_MODE);
        capes = config.getBoolean("capes", Configuration.CATEGORY_GENERAL, capes, Strings.CAPES);
        slime = config.getBoolean("slimePistonRightClick", Configuration.CATEGORY_GENERAL, slime, Strings.SLIMES);
        if (config.hasChanged())
            config.save();
    }
    
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equalsIgnoreCase(Strings.MOD_ID)) {
            System.out.println("yay! " + Strings.MOD_NAME);
            configureMod();
        }
    }
}
