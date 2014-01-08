package net.lomeli.lomlib.client.nei;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.util.ModLoaded;

@SideOnly(Side.CLIENT)
public class NEIAddon {

    @SideOnly(Side.CLIENT)
    public static void loadAddon() {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT && ModLoaded.isModInstalled("NotEnoughItems")) {
            //API.registerRecipeHandler(new ShapedFluidRecipeHandler());
            //API.registerUsageHandler(new ShapedFluidRecipeHandler());
            //API.registerRecipeHandler(new ShapelessFluidRecipeHandler());
            //API.registerUsageHandler(new ShapelessFluidRecipeHandler());
        }
    }
}
