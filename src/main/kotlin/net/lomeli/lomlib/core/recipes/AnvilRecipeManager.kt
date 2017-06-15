package net.lomeli.lomlib.core.recipes

import net.minecraftforge.event.AnvilUpdateEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object AnvilRecipeManager {
    private val recipeList = ArrayList<IAnvilRecipe>()

    @SubscribeEvent fun onAnvilUpdate(event: AnvilUpdateEvent) {
        recipeList.filter{recipe -> recipe != null && recipe.matches(event.left, event.right)}.forEach{recipe ->
            val output = recipe.getCraftingResult(event.left, event.right)
            event.cost = recipe.recipeCost()
            event.output = output
        }
    }

    fun addRecipe(recipe: IAnvilRecipe?) {
        if (recipe != null)
            recipeList.add(recipe)
    }

    fun getRegistry(): List<IAnvilRecipe> = recipeList
}