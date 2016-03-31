package net.lomeli.lomlib.client.addon.jei;

import com.google.common.collect.Lists;
import mezz.jei.api.*;

import java.util.List;

import net.minecraft.inventory.ContainerRepair;

import net.lomeli.lomlib.client.addon.jei.anvil.AnvilRecipeCategory;
import net.lomeli.lomlib.client.addon.jei.anvil.FluidAnvilRecipeHandler;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapedFluidRecipeHandler;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapelessFluidRecipeHandler;
import net.lomeli.lomlib.core.recipes.AnvilRecipeManager;

@JEIPlugin
public class LomLibPlugin implements IModPlugin {
    public static final String CRAFTING = "minecraft.crafting";
    public static final String ANVIL = "lomlib.anvil";
    private IJeiHelpers jeiHelpers;

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new AnvilRecipeCategory(guiHelper));

        registry.addRecipeHandlers(new ShapedFluidRecipeHandler(), new ShapelessFluidRecipeHandler(), new FluidAnvilRecipeHandler());

        registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRepair.class, ANVIL, 0, 2, 3, 36);
        List<Object> recipes = Lists.newArrayList();
        recipes.addAll(AnvilRecipeManager.getRegistry());
        registry.addRecipes(recipes);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }
}
