package net.lomeli.lomlib.client.addon.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;

import java.util.List;

import net.minecraft.inventory.ContainerRepair;

import net.minecraftforge.fml.common.Loader;

import net.lomeli.lomlib.client.addon.jei.anvil.AnvilRecipeCategory;
import net.lomeli.lomlib.client.addon.jei.anvil.FluidAnvilRecipeHandler;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapedFluidRecipeHandler;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapelessFluidRecipeHandler;
import net.lomeli.lomlib.core.recipes.AnvilRecipeManager;

@JEIPlugin
public class LomLibPlugin implements IModPlugin {
    public static final String CRAFTING = "minecraft.crafting";
    public static final String ANVIL = "lomlib.anvil";

    @Override
    public boolean isModLoaded() {
        return Loader.isModLoaded("LomLib");
    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategories(new AnvilRecipeCategory());

        registry.addRecipeHandlers(new ShapedFluidRecipeHandler(), new ShapelessFluidRecipeHandler(), new FluidAnvilRecipeHandler());

        IGuiHelper guiHelper = JEIManager.guiHelper;
        registry.addRecipeTransferHelpers(guiHelper.createRecipeTransferHelper(ContainerRepair.class, ANVIL, 1, 9, 10, 36));
        List<Object> recipes = Lists.newArrayList();
        recipes.addAll(AnvilRecipeManager.getRegistry());
        registry.addRecipes(recipes);
    }
}
