package net.lomeli.lomlib.client.addon.jei.crafting;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;
import java.util.List;

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

public class ShapelessFluidRecipeHandler implements IRecipeHandler<ShapelessFluidRecipe> {
    @Nonnull
    @Override
    public Class<ShapelessFluidRecipe> getRecipeClass() {
        return ShapelessFluidRecipe.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return LomLibPlugin.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull ShapelessFluidRecipe recipe) {
        return new ShapelessFluidRecipeWrapper(recipe);
    }

    @Override
    public boolean isRecipeValid(@Nonnull ShapelessFluidRecipe recipe) {
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
    public String getRecipeCategoryUid(@Nonnull ShapelessFluidRecipe recipe) {
        return LomLibPlugin.CRAFTING;
    }
}
