package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe

class ShapedFluidRecipeHandler : IRecipeHandler<ShapedFluidRecipe> {
    override fun getRecipeClass(): Class<ShapedFluidRecipe> {
        return ShapedFluidRecipe::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return LomLibPlugin.CRAFTING
    }

    override fun getRecipeWrapper(recipe: ShapedFluidRecipe): IRecipeWrapper {
        return ShapedFluidRecipeWrapper(recipe)
    }

    override fun isRecipeValid(recipe: ShapedFluidRecipe): Boolean {
        if (recipe.recipeOutput == null)
            return false
        var inputCount = 0
        if (recipe.getInput() != null) {
            for (input in recipe.getInput()) {
                if (input is List<*>) {
                    if (input.size == 0)
                        return false
                }
                if (input != null)
                    inputCount++
            }
        }
        return inputCount > 0
    }

    override fun getRecipeCategoryUid(recipe: ShapedFluidRecipe): String {
        return LomLibPlugin.CRAFTING
    }
}