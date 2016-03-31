package net.lomeli.lomlib.core.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;

import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class AnvilRecipeManager {
    private static List<IAnvilRecipe> recipeList = new ArrayList<IAnvilRecipe>();

    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        for (int i = 0; i < recipeList.size(); i++) {
            IAnvilRecipe recipe = recipeList.get(i);
            if (recipe != null) {
                if (recipe.matches(event.getLeft(), event.getRight())) {
                    ItemStack output = recipe.getCraftingResult(event.getLeft(), event.getRight());
                    event.setCost(recipe.recipeCost());
                    event.setOutput(output);
                }
            }
        }
    }

    public static void addRecipe(IAnvilRecipe recipe) {
        if (recipe != null)
            recipeList.add(recipe);
    }

    public static List<IAnvilRecipe> getRegistry() {
        return recipeList;
    }
}
