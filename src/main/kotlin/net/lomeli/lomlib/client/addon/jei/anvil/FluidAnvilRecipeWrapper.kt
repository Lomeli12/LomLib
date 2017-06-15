package net.lomeli.lomlib.client.addon.jei.anvil

import com.google.common.collect.ImmutableList
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeWrapper
import java.util.Collections

import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack

import net.minecraftforge.fluids.FluidStack

import net.lomeli.lomlib.core.recipes.FluidAnvilRecipe

class FluidAnvilRecipeWrapper(private val recipe: FluidAnvilRecipe) : IRecipeWrapper {

    fun getInputs(): List<*> {
        return listOf(recipe.recipeInputs())
    }

    fun getOutputs(): List<ItemStack> {
        return listOf(recipe.recipeOutput())
    }

    fun drawAnimations(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int) {
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): List<String>? {
        return null
    }

    fun getFluidInputs(): List<FluidStack> {
        return ImmutableList.of<FluidStack>()
    }

    fun getFluidOutputs(): List<FluidStack> {
        return ImmutableList.of<FluidStack>()
    }

    override fun drawInfo(minecraft: Minecraft, recipeWidth: Int, recipeHeight: Int, mouseX: Int, mouseY: Int) {
    }

    override fun handleClick(minecraft: Minecraft, mouseX: Int, mouseY: Int, mouseButton: Int): Boolean {
        return false
    }

    override fun getIngredients(ingredients : IIngredients?) {
    }
}
