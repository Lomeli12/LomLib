package net.lomeli.lomlib.core.recipes

import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object AnvilRecipeManager {
    private val recipeList = ArrayList<IAnvilRecipe>()

    @SubscribeEvent fun onAnvilUpdate(event: AnvilUpdateEvent) {
        for (i in recipeList.indices) {
            val recipe = recipeList[i]
            if (recipe != null) {
                if (recipe.matches(event.left, event.right)) {
                    val output = recipe.getCraftingResult(event.left, event.right)
                    event.cost = recipe.recipeCost()
                    event.output = output
                }
            }
        }
    }

    fun addRecipe(recipe: IAnvilRecipe?) {
        if (recipe != null)
            recipeList.add(recipe)
    }

    fun getRegistry(): List<IAnvilRecipe> = recipeList
}