package net.lomeli.lomlib.client.addon.jei.crafting;

import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.client.addon.jei.BlankRecipeWrapper;
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;

public class ShapedFluidRecipeWrapper extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {
    @Nonnull
    private final ShapedFluidRecipe recipe;
    private final int width;
    private final int height;

    public ShapedFluidRecipeWrapper(ShapedFluidRecipe recipe) {
        this.recipe = recipe;
        for (Object input : this.recipe.getInput()) {
            if (input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.stackSize > 1)
                    itemStack.stackSize = 1;
            }
        }
        this.width = this.recipe.width;
        this.height = this.recipe.height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Nonnull
    @Override
    public List getInputs() {
        return Arrays.asList(recipe.getInput());
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return Collections.singletonList(recipe.getRecipeOutput());
    }

    @Override
    public void drawAnimations(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight) {

    }

    @Nullable
    @Override
    public List<String> getTooltipStrings(int mouseX, int mouseY) {
        return null;
    }

    @Override
    public void drawInfo(@Nonnull Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {

    }

    @Override
    public boolean handleClick(@Nonnull Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        return false;
    }
}
