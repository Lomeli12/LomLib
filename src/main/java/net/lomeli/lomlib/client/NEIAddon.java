package net.lomeli.lomlib.client;

import codechicken.nei.api.API;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class NEIAddon {

    @SideOnly(Side.CLIENT)
    public static void loadAddon() {
        if (Loader.isModLoaded("NotEnoughItems")) {
            API.registerRecipeHandler(new ShapedFluidRecipeHandler());
            API.registerUsageHandler(new ShapedFluidRecipeHandler());
            API.registerRecipeHandler(new ShapelessFluidRecipeHandler());
            API.registerUsageHandler(new ShapelessFluidRecipeHandler());
            // API.registerRecipeHandler(new FluidAnvilRecipeHandler());
            // API.registerUsageHandler(new FluidAnvilRecipeHandler());
        }
    }
}
