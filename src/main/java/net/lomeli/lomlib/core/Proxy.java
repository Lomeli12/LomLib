package net.lomeli.lomlib.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.oredict.RecipeSorter;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.patreon.PatreonList;
import net.lomeli.lomlib.core.recipes.AnvilRecipeManager;
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.core.version.VersionChecker;
import net.lomeli.lomlib.util.entity.ItemCustomEgg;

public class Proxy {
    public PatreonList list;
    public VersionChecker updater;

    public void preInit() {
        LomLib.logger.logBasic("Pre-Init");
        RecipeSorter.register(Strings.NEI_SHAPED, ShapedFluidRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(Strings.NEI_SHAPELESS, ShapelessFluidRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        list = new PatreonList();
        checkForUpdate();
        ItemCustomEgg.initCustomEggs();
    }

    public void init() {
        LomLib.logger.logBasic("Init");

        list.getLatestList();
        
        MinecraftForge.EVENT_BUS.register(new AnvilRecipeManager());

        FMLCommonHandler.instance().bus().register(this);
    }

    public void postInit() {
        LomLib.logger.logBasic("Post-Init");
    }

    public void checkForUpdate() {
        updater = new VersionChecker(Strings.UPDATE_URL, Strings.MOD_ID, Strings.MOD_NAME, Strings.MAJOR, Strings.MINOR, Strings.REVISION);
        updater.checkForUpdates();
    }

    public void messageClient(String msg) {
    }
}
