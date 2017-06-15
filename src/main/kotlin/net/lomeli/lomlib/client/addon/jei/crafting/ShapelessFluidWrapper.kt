package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.IJeiHelpers
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import mezz.jei.api.recipe.IRecipeWrapper
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe
import net.minecraft.item.ItemStack

class ShapelessFluidWrapper(val jeiHelper: IJeiHelpers, val recipe: ShapelessFluidRecipe) : BlankRecipeWrapper(), IRecipeWrapper {
    override fun getIngredients(ingredients: IIngredients?) {
        val stackHelper = jeiHelper.stackHelper
        val recipeOutput = recipe.recipeOutput

        try {
            val inputs = stackHelper.expandRecipeItemStackInputs(recipe.getInput())
            ingredients?.setInputLists(ItemStack::class.java, inputs)
            ingredients?.setOutput(ItemStack::class.java, recipeOutput)
        } catch (e: RuntimeException) {
            LomLib.logger.logException(e)
        }

    }
}