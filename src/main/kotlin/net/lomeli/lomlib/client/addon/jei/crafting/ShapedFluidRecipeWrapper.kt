package net.lomeli.lomlib.client.addon.jei.crafting

import mezz.jei.api.recipe.BlankRecipeWrapper
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import java.util.*

class ShapedFluidRecipeWrapper(private val recipe: ShapedFluidRecipe) : BlankRecipeWrapper(), IShapedCraftingRecipeWrapper {
    private val width: Int
    private val height: Int

    init {
        for (input in this.recipe.getInput()) {
            if (input is ItemStack && input.stackSize > 1)
                input.stackSize = 1
        }
        this.width = this.recipe.width
        this.height = this.recipe.height
    }

    override fun getWidth(): Int {
        return this.width
    }

    override fun getHeight(): Int {
        return this.height
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
}