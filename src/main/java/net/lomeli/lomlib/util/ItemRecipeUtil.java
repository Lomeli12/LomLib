package net.lomeli.lomlib.util;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

public class ItemRecipeUtil {

    /**
     * Allows you to remove the recipe for an item
     * @param item
     */
    public static void removeRecipe(ItemStack item) {
        Iterator<IRecipe> recipes = CraftingManager.getInstance().getRecipeList().iterator();
        while (recipes.hasNext()) {
            ItemStack output = recipes.next().getRecipeOutput();
            if (ItemUtil.areItemsTheSame(item, output)) {
                recipes.remove();
                break;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static Object[] getItemShapedRecipe(ItemStack stack) {
        Object[] finalRecipe = new Object[9];
        if (stack != null) {
            List<IRecipe> possibleRecipe = new ArrayList<IRecipe>();

            for (Object recipe : CraftingManager.getInstance().getRecipeList()) {
                if (recipe instanceof IRecipe) {
                    ItemStack output = ((IRecipe) recipe).getRecipeOutput();
                    if (output != null && stack.getUnlocalizedName().equals(output.getUnlocalizedName()) && stack.getItemDamage() == output.getItemDamage())
                        possibleRecipe.add((IRecipe) recipe);
                }
            }

            if (!possibleRecipe.isEmpty()) {
                IRecipe main = possibleRecipe.get(0);
                if (main instanceof ShapedRecipes) {
                    ItemStack[] inputs = ((ShapedRecipes) main).recipeItems;
                    if (inputs != null) {
                        for (int i = 0; i < inputs.length; i++) {
                            ItemStack item = inputs[i];
                            if (item != null && item.getItem() != null)
                                finalRecipe[i] = item;
                        }
                    }
                } else if (main instanceof ShapelessRecipes) {
                    List<ItemStack> items = ((ShapelessRecipes) main).recipeItems;
                    if (items != null) {
                        for (int i = 0; i < items.size(); i++) {
                            if (i < finalRecipe.length)
                                finalRecipe[i] = items.get(i);
                        }
                    }
                } else if (main instanceof ShapedOreRecipe || main instanceof ShapelessOreRecipe) {
                    Object[] inputs = null;
                    if (main instanceof ShapedOreRecipe)
                        inputs = ((ShapedOreRecipe) main).getInput();
                    else
                        inputs = ((ShapelessOreRecipe) main).getInput().toArray();

                    for (int i = 0; i < inputs.length; i++) {
                        Object obj = inputs[i];
                        if (obj instanceof ArrayList<?>)
                            finalRecipe[i] = ((ArrayList<?>) obj).get(0);
                        else
                            finalRecipe[i] = obj;
                    }
                } else if (main instanceof ShapedFluidRecipe || main instanceof ShapelessFluidRecipe) {
                    Object[] inputs = null;
                    if (main instanceof ShapedFluidRecipe)
                        inputs = ((ShapedFluidRecipe) main).getInput();
                    else
                        inputs = ((ShapelessFluidRecipe) main).getInput().toArray();

                    for (int i = 0; i < inputs.length; i++) {
                        Object obj = inputs[i];
                        if (obj instanceof ArrayList<?>)
                            finalRecipe[i] = ((ArrayList<?>) obj).get(0);
                        else
                            finalRecipe[i] = obj;
                    }
                } else {
                    try {
                        Field[] fields = main.getClass().getDeclaredFields();
                        if (Loader.isModLoaded("IC2")) {
                            if (Class.forName("ic2.core.AdvRecipe").isAssignableFrom(main.getClass()) || Class.forName("ic2.core.AdvShapelessRecipe").isAssignableFrom(main.getClass())) {
                                Field inputs = fields[2];
                                if (inputs.getType().isArray()) {
                                    for (int i = 0; i < Array.getLength(inputs.get(main)); i++) {
                                        if (i < finalRecipe.length)
                                            finalRecipe[i] = Array.get(inputs.get(main), i);
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return finalRecipe;
    }
}
