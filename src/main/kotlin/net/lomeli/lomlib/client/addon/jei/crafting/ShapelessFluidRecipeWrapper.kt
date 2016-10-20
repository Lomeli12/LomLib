package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.BlankRecipeWrapper
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper
import java.util.Collections

import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe

class ShapelessFluidRecipeWrapper(private val recipe: ShapelessFluidRecipe) : BlankRecipeWrapper(), ICraftingRecipeWrapper {

    init {
        for (input in this.recipe.getInput()) {
            if (input is ItemStack && input.stackSize > 1)
                input.stackSize = 1
        }
    }

    override fun getInputs(): List<*> {
        return recipe.getInput()
    }

    override fun getOutputs(): List<ItemStack> {
        return listOf(recipe.recipeOutput!!)
    }

    override fun drawAnimations(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int) {
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String>? {
        return null
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }

    override fun getIngredients(ingredients : IIngredients?) {
    }
}
