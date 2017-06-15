package net.lomeli.lomlib.client.addon.jei.anvil

import mezz.jei.api.recipe.IRecipeHandler
import mezz.jei.api.recipe.IRecipeWrapper

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin
import net.lomeli.lomlib.core.recipes.FluidAnvilRecipe

class FluidAnvilRecipeHandler : IRecipeHandler<FluidAnvilRecipe> {
    override fun getRecipeClass(): Class<FluidAnvilRecipe> {
        return FluidAnvilRecipe::class.java
    }

    fun getRecipeCategoryUid(): String {
        return LomLibPlugin.ANVIL
    }

    override fun getRecipeWrapper(recipe: FluidAnvilRecipe): IRecipeWrapper {
        return FluidAnvilRecipeWrapper(recipe)
    }

    override fun isRecipeValid(recipe: FluidAnvilRecipe): Boolean {
        if (recipe.recipeOutput() == null)
            return false
        var inputCount = 0
        for (input in recipe.recipeInputs()) {
            if (input is List<*>) {
                if (input.size == 0)
                    return false
            }
            if (input != null)
                inputCount++
        }
        return inputCount > 0
    }

    override fun getRecipeCategoryUid(recipe: FluidAnvilRecipe): String {
        return LomLibPlugin.ANVIL
    }
}