package net.lomeli.lomlib.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.RecipeSorter;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.patreon.PatreonList;
import net.lomeli.lomlib.core.recipes.AnvilRecipeManager;
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.core.version.VersionChecker;
import net.lomeli.lomlib.lib.ModLibs;

public class Proxy {
    public PatreonList list;
    public VersionChecker updater;

    public void preInit() {
        LomLib.logger.logBasic("Pre-Init");
        RecipeSorter.register(ModLibs.NEI_SHAPED, ShapedFluidRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped before:minecraft:shapeless");
        RecipeSorter.register(ModLibs.NEI_SHAPELESS, ShapelessFluidRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");
        list = new PatreonList();
        if (LomLib.checkForUpdates)
            checkForUpdate();
    }

    public void init() {
        LomLib.logger.logBasic("Init");
        new Thread(list).start();
        MinecraftForge.EVENT_BUS.register(new AnvilRecipeManager());
        //AnvilRecipeManager.addRecipe(new FluidAnvilRecipe(new ItemStack(Items.diamond), Items.stick, Items.paper, 13));
    }

    public void postInit() {
        LomLib.logger.logBasic("Post-Init");
    }

    public void checkForUpdate() {
        updater = new VersionChecker(ModLibs.UPDATE_URL, ModLibs.MOD_ID, ModLibs.MOD_NAME, ModLibs.VERSION);
        new Thread(updater).start();
    }

    public void messageClient(String msg) {
    }
}
