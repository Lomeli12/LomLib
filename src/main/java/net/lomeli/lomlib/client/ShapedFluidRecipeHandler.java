package net.lomeli.lomlib.client;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.recipe.ShapedRecipeHandler;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.recipes.ShapedFluidRecipe;

public class ShapedFluidRecipeHandler extends ShapedRecipeHandler {

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(Strings.NEI_SHAPED);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals("crafting") && getClass() == ShapedFluidRecipeHandler.class) {
            List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
            for (IRecipe irecipe : allrecipes) {
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof ShapedFluidRecipe)
                    recipe = fluidShapedRecipe((ShapedFluidRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
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
                CachedShapedRecipe recipe = null;
                if (irecipe instanceof ShapedFluidRecipe)
                    recipe = fluidShapedRecipe((ShapedFluidRecipe) irecipe);

                if (recipe == null)
                    continue;

                recipe.computeVisuals();
                arecipes.add(recipe);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        List<IRecipe> allrecipes = CraftingManager.getInstance().getRecipeList();
        for (IRecipe irecipe : allrecipes) {
            CachedShapedRecipe recipe = null;
            if (irecipe instanceof ShapedFluidRecipe)
                recipe = fluidShapedRecipe((ShapedFluidRecipe) irecipe);

            if (recipe == null || !recipe.contains(recipe.ingredients, ingredient))
                continue;

            recipe.computeVisuals();
            if (recipe.contains(recipe.ingredients, ingredient)) {
                recipe.setIngredientPermutation(recipe.ingredients, ingredient);
                arecipes.add(recipe);
            }
        }
    }

    public CachedShapedRecipe fluidShapedRecipe(ShapedFluidRecipe recipe) {
        int width;
        int height;
        Object[] items;
        try {
            width = recipe.width;
            height = recipe.height;
            items = recipe.input;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof List && ((List<?>) items[i]).isEmpty())
                return null;
        }

        return new CachedShapedRecipe(width, height, items, recipe.getRecipeOutput());
    }

}
