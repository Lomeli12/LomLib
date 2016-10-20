package net.lomeli.lomlib.core.recipes

import net.minecraft.item.ItemStack

interface IAnvilRecipe {
    /**
     * Checks to see if the left and right stack match the recipe.

     * @param left
     * *
     * @param right
     * *
     * @return
     */
    fun matches(left: ItemStack, right: ItemStack?): Boolean

    /**
     * Get a copy of the result

     * @param left
     * *
     * @param right
     * *
     * @return
     */
    fun getCraftingResult(left: ItemStack, right: ItemStack?): ItemStack

    /**
     * Get the recipe result

     * @return
     */
    fun recipeOutput(): ItemStack

    /**
     * Get the inputs for the recipe

     * @return
     */
    fun recipeInputs(): Array<Any?>

    /**
     * Cost (in Experience levels) of the recipe

     * @return
     */
    fun recipeCost(): Int

    fun doItemsMatch(itemStack: ItemStack?, slot: Int): Boolean
}