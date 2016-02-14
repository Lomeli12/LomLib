package net.lomeli.lomlib.client.addon.jei.crafting;

import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import net.lomeli.lomlib.client.addon.jei.BlankRecipeWrapper;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

public class ShapelessFluidRecipeWrapper extends BlankRecipeWrapper implements ICraftingRecipeWrapper {
    @Nonnull
    private final ShapelessFluidRecipe recipe;

    public ShapelessFluidRecipeWrapper(ShapelessFluidRecipe recipe) {
        this.recipe = recipe;
        for (Object input : this.recipe.getInput()) {
            if (input instanceof ItemStack) {
                ItemStack itemStack = (ItemStack) input;
                if (itemStack.stackSize > 1) {
                    itemStack.stackSize = 1;
                }
            }
        }
    }

    @Nonnull
    @Override
    public List getInputs() {
        return recipe.getInput();
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
