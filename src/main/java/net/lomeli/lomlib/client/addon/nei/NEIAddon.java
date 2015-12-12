package net.lomeli.lomlib.client.addon.nei;

import codechicken.nei.api.API;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NEIAddon {

    @SideOnly(Side.CLIENT)
    public static void loadAddon() {
        if (Loader.isModLoaded("NotEnoughItems")) {
            API.registerRecipeHandler(new ShapedFluidRecipeHandler());
            API.registerUsageHandler(new ShapedFluidRecipeHandler());
            API.registerRecipeHandler(new ShapelessFluidRecipeHandler());
            API.registerUsageHandler(new ShapelessFluidRecipeHandler());
            API.registerRecipeHandler(new AnvilRecipeHandler());
            API.registerUsageHandler(new AnvilRecipeHandler());
        }
    }
}
