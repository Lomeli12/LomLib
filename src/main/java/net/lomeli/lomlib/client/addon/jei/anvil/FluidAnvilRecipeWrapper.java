package net.lomeli.lomlib.client.addon.jei.anvil;

import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.client.addon.jei.BlankRecipeWrapper;
import net.lomeli.lomlib.core.recipes.FluidAnvilRecipe;

public class FluidAnvilRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
    @Nonnull
    private final FluidAnvilRecipe recipe;

    public FluidAnvilRecipeWrapper(FluidAnvilRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public List getInputs() {
        return Arrays.asList(recipe.recipeInputs());
    }

    @Override
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(recipe.recipeOutput());
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {
    }
}
