package net.lomeli.lomlib.client.addon.jei.crafting;

import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;

import javax.annotation.Nonnull;
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
}
