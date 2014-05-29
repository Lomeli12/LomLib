package net.lomeli.lomlib.client;

import codechicken.nei.api.API;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ModLoaded;

public class NEIAddon {

    @SideOnly(Side.CLIENT)
    public static void loadAddon() {
        if (ModLoaded.isModInstalled("NotEnoughItems")) {
            API.registerRecipeHandler(new ShapedFluidRecipeHandler());
            API.registerUsageHandler(new ShapedFluidRecipeHandler());
            API.registerRecipeHandler(new ShapelessFluidRecipeHandler());
            API.registerUsageHandler(new ShapelessFluidRecipeHandler());
            // API.registerRecipeHandler(new FluidAnvilRecipeHandler());
            // API.registerUsageHandler(new FluidAnvilRecipeHandler());
        }
    }
}
