package net.lomeli.lomlib.client.addon.jei.anvil;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

import java.util.List;

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin;
import net.lomeli.lomlib.core.recipes.FluidAnvilRecipe;

public class FluidAnvilRecipeHandler implements IRecipeHandler<FluidAnvilRecipe> {
    @Nonnull
    @Override
    public Class<FluidAnvilRecipe> getRecipeClass() {
        return FluidAnvilRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return LomLibPlugin.ANVIL;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull FluidAnvilRecipe recipe) {
        return new FluidAnvilRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull FluidAnvilRecipe recipe) {
        if (recipe.recipeOutput() == null)
            return false;
        int inputCount = 0;
        for (Object input : recipe.recipeInputs()) {
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
    public String getRecipeCategoryUid(@Nonnull FluidAnvilRecipe recipe) {
        return LomLibPlugin.ANVIL;
    }
}
