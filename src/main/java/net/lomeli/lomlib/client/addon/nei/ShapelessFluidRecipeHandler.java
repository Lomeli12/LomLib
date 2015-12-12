package net.lomeli.lomlib.client.addon.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapelessRecipeHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.lib.ModLibs;

public class ShapelessFluidRecipeHandler extends ShapelessRecipeHandler {

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(ModLibs.NEI_SHAPELESS);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == ShapelessFluidRecipeHandler.class) {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessFluidRecipe)
                    recipe = fluidShapelessRecipe((ShapelessFluidRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        } else {
            super.loadCraftingRecipes(outputId, results);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadCraftingRecipes(ItemStack result) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            if (NEIServerUtils.areStacksSameTypeCrafting(irecipe.getRecipeOutput(), result)) {
                CachedShapelessRecipe recipe = null;
                if (irecipe instanceof ShapelessFluidRecipe)
                    recipe = fluidShapelessRecipe((ShapelessFluidRecipe) irecipe);

                if (recipe == null)
                    continue;

                arecipes.add(recipe);
            }
        }
    }

    @SuppressWarnings({"unchecked"})
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            CachedShapelessRecipe recipe = null;
            if (irecipe instanceof ShapelessFluidRecipe)
                recipe = fluidShapelessRecipe((ShapelessFluidRecipe) irecipe);

            if (recipe == null)
                continue;

            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    public CachedShapelessRecipe fluidShapelessRecipe(ShapelessFluidRecipe recipe) {
        ArrayList<?> items;
        try {
            items = recipe.input;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i) instanceof List && ((List<?>) items.get(i)).isEmpty())
                return null;
        }

        return new CachedShapelessRecipe(items, recipe.getRecipeOutput());
    }
}
