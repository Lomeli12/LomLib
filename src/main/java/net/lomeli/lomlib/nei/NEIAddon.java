package net.lomeli.lomlib.nei;

import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import codechicken.nei.api.API;

@SideOnly(Side.CLIENT)
public class NEIAddon {

    @SideOnly(Side.CLIENT)
    public static void loadAddon() {
        System.out.println(StatCollector.translateToLocal("recipe.shapelessFluid"));
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            API.registerRecipeHandler(new ShapedFluidRecipeHandler());
            API.registerUsageHandler(new ShapedFluidRecipeHandler());
            API.registerRecipeHandler(new ShapelessFluidRecipeHandler());
            API.registerUsageHandler(new ShapelessFluidRecipeHandler());
        }
    }
}
