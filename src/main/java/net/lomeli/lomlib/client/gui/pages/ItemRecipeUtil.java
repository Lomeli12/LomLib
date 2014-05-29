package net.lomeli.lomlib.client.gui.pages;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import net.lomeli.lomlib.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.recipes.ShapelessFluidRecipe;
import net.lomeli.lomlib.util.ModLoaded;

public class ItemRecipeUtil {

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
                try {
                    Field[] fields = main.getClass().getDeclaredFields();
                    if (main instanceof ShapedRecipes) {
                        Field inputs = fields[2];
                        if (inputs.getType().isArray()) {
                            for (int i = 0; i < Array.getLength(inputs.get(main)); i++) {
                                if ((Array.get(inputs.get(main), i) instanceof ItemStack) && i < finalRecipe.length)
                                    finalRecipe[i] = Array.get(inputs.get(main), i);
                            }
                        }
                    }else if (main instanceof ShapelessRecipes) {
                        Field inputs = fields[1];
                        if (List.class.isAssignableFrom(inputs.getType())) {
                            inputs.setAccessible(true);

                            List<ItemStack> items = getField(ShapelessRecipes.class, List.class, main, 1);
                            if (items != null) {
                                for (int i = 0; i < items.size(); i++) {
                                    if (i < finalRecipe.length)
                                        finalRecipe[i] = items.get(i);
                                }
                            }
                        }
                    }else if (main instanceof ShapedOreRecipe || main instanceof ShapelessOreRecipe) {
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
                    }else if (ModLoaded.isModInstalled("IC2")) {
                        if (Class.forName("ic2.core.AdvRecipe").isAssignableFrom(main.getClass()) || Class.forName("ic2.core.AdvShapelessRecipe").isAssignableFrom(main.getClass())) {
                            Field inputs = fields[2];
                            if (inputs.getType().isArray()) {
                                for (int i = 0; i < Array.getLength(inputs.get(main)); i++) {
                                    if (i < finalRecipe.length)
                                        finalRecipe[i] = Array.get(inputs.get(main), i);
                                }
                            }
                        }
                    }else if (main instanceof ShapedFluidRecipe || main instanceof ShapelessFluidRecipe) {
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
                    }
                }catch (Exception e) {
                }
            }
        }
        return finalRecipe;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Class<?> class1, Class<T> fieldType, Object instance, int i) {
        try {
            Field[] fields = class1.getDeclaredFields();
            Field field = fields[i];
            field.setAccessible(true);
            return (T) field.get(instance);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
