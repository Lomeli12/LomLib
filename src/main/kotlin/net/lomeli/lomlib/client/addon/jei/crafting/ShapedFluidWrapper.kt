package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.IJeiHelpers
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe
import net.minecraft.item.ItemStack
import java.util.*

class ShapedFluidWrapper(val jeiHelpers: IJeiHelpers, val recipe: ShapedFluidRecipe) : BlankRecipeWrapper(), IShapedCraftingRecipeWrapper {

    override fun getWidth(): Int = recipe.width

    override fun getHeight(): Int = recipe.height

    override fun getIngredients(ingredients: IIngredients?) {
        val stackHelper = jeiHelpers.stackHelper
        val recipeOutput = recipe.recipeOutput
        try {
            val inputs = stackHelper.expandRecipeItemStackInputs(Arrays.asList<Any>(recipe.getInput()))
            ingredients?.setInputLists(ItemStack::class.java, inputs)
            ingredients?.setOutput(ItemStack::class.java, recipeOutput)
        } catch (e: RuntimeException) {
            LomLib.logger.logException(e)
        }
    }
}