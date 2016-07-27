package net.lomeli.lomlib.client.addon.jei.crafting;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

import java.util.List;

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin;
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;

public class ShapedFluidRecipeHandler implements IRecipeHandler<ShapedFluidRecipe> {
    @Nonnull
    @Override
    public Class<ShapedFluidRecipe> getRecipeClass() {
        return ShapedFluidRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return LomLibPlugin.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapedFluidRecipe recipe) {
        return new ShapedFluidRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapedFluidRecipe recipe) {
        if (recipe.getRecipeOutput() == null)
            return false;
        int inputCount = 0;
        for (Object input : recipe.getInput()) {
            if (input instanceof List) {
                if (((List) input).size() == 0)
                    return false;
            }
            if (input != null)
                inputCount++;
        }
        return inputCount > 0;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull ShapedFluidRecipe recipe) {
        return LomLibPlugin.CRAFTING;
    }
}
