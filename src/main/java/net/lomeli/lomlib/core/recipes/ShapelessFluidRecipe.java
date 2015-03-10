package net.lomeli.lomlib.core.recipes;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

/**
 * Based off of OreDict's ShapelessOreRecipe
 *
 * @author Lomeli12
 */
public class ShapelessFluidRecipe implements IRecipe {

    @SuppressWarnings("rawtypes")
    public ArrayList input = new ArrayList();
    private ItemStack output = null;

    @SuppressWarnings("unchecked")
    public ShapelessFluidRecipe(ItemStack result, Object... recipe) {
        output = result.copy();
        for (Object in : recipe) {
            if (in instanceof ItemStack) {
                if (FluidContainerRegistry.isFilledContainer((ItemStack) in))
                    input.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid((ItemStack) in)));
                else
                    input.add(((ItemStack) in).copy());
            } else if (in instanceof Item) {
                if (FluidContainerRegistry.isFilledContainer(new ItemStack((Item) in)))
                    input.add(FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(new ItemStack((Item) in))));
                else
                    input.add(new ItemStack((Item) in));
            } else if (in instanceof Block)
                input.add(new ItemStack((Block) in));
            else if (in instanceof String) {
                if (((String) in).startsWith("fluid$")) {
                    String fluidName = ((String) in).substring(6);
                    if (FluidRegistry.isFluidRegistered(fluidName))
                        input.add(FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName)));
                } else
                    input.add(OreDictionary.getOres((String) in));
            } else {
                String ret = "Invalid shapeless ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }
    }

    public ShapelessFluidRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapelessFluidRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    @SuppressWarnings("unchecked")
    ShapelessFluidRecipe(ShapelessRecipes recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();

        for (ItemStack ingred : ((List<ItemStack>) recipe.recipeItems)) {
            Object finalObj = ingred;
            for (Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (OreDictionary.itemMatches(replace.getKey(), ingred, false)) {
                    finalObj = OreDictionary.getOres(replace.getValue());
                    break;
                } else if (FluidContainerRegistry.isFilledContainer(replace.getKey())) {
                    finalObj = FluidUtil.getContainersForFluid(FluidContainerRegistry.getFluidForFilledItem(replace.getKey()).getFluid());
                    break;
                }
            }
            input.add(finalObj);
        }
    }

    @Override
    public int getRecipeSize() {
        return input.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output.copy();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
        ArrayList<Object> required = new ArrayList<Object>(input);

        for (int x = 0; x < var1.getSizeInventory(); x++) {
            ItemStack slot = var1.getStackInSlot(x);

            if (slot != null) {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext()) {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                        match = OreDictionary.itemMatches((ItemStack) next, slot, false);
                    else if (next instanceof List) {
                        Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
                        while (itr.hasNext() && !match) {
                            match = OreDictionary.itemMatches(itr.next(), slot, false);
                        }
                    }

                    if (match) {
                        inRecipe = true;
                        required.remove(next);
                        break;
                    }
                }

                if (!inRecipe)
                    return false;
            }
        }

        return required.isEmpty();
    }

    public ArrayList<Object> getInput() {
        return this.input;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
