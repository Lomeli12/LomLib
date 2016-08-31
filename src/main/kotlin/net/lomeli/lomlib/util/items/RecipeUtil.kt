package net.lomeli.lomlib.util.items

import net.minecraft.item.ItemStack
import net.minecraft.item.crafting.CraftingManager

object RecipeUtil {
    /**
     * Allows you to remove the recipe for an item

     * @param item
     */
    fun removeRecipe(item: ItemStack) {
        val recipes = CraftingManager.getInstance().recipeList.iterator()
        while (recipes.hasNext()) {
            val output = recipes.next().recipeOutput
            if (ItemUtil.areItemsTheSame(item, output!!)) {
                recipes.remove()
                break
            }
        }
    }
}