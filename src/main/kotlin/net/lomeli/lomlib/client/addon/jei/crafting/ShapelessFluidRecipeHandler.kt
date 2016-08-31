package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe

class ShapelessFluidRecipeHandler : IRecipeHandler<ShapelessFluidRecipe> {
    override fun getRecipeClass(): Class<ShapelessFluidRecipe> {
        return ShapelessFluidRecipe::class.java
    }

    override fun getRecipeCategoryUid(): String {
        return LomLibPlugin.CRAFTING
    }

    override fun getRecipeWrapper(recipe: ShapelessFluidRecipe): IRecipeWrapper {
        return ShapelessFluidRecipeWrapper(recipe)
    }

    override fun isRecipeValid(recipe: ShapelessFluidRecipe): Boolean {
        if (recipe.recipeOutput == null)
            return false
        var inputCount = 0
        for (input in recipe.getInput()) {
            if (input is List<*>) {
                if (input.size == 0)
                    return false
            }
            if (input != null)
                inputCount++
        }
        return inputCount > 0
    }

    override fun getRecipeCategoryUid(recipe: ShapelessFluidRecipe): String {
        return LomLibPlugin.CRAFTING
    }
}