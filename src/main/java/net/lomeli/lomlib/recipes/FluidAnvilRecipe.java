package net.lomeli.lomlib.recipes;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

public class FluidAnvilRecipe implements IAnvilRecipe {
    private ItemStack output = null;
    private Object[] inputs = new Object[2];
    private int expLvlCost = 0;

    /**
     * ATM, right input cannot be null due to how the AnvilUpdateEvent works.
     * Not much I can do about that
     * 
     * @param output
     * @param leftInput
     * @param rightInput
     * @param expCost
     */
    public FluidAnvilRecipe(ItemStack output, Object leftInput, Object rightInput, int expCost) {
        this.output = output;
        addInput(leftInput, 0);
        addInput(rightInput, 1);
        setXPCost(expCost);
        if (leftInput == null && rightInput == null)
            throw new RuntimeException("Invalid Anvil Recipe: Both inputs cannot be null!");
    }

    public FluidAnvilRecipe(ItemStack output, Object input, int expCost) {
        this(output, input, null, expCost);
    }

    private void setXPCost(int exp) {
        if (exp < 0)
            throw new RuntimeException("Invalid Anvil Recipe: Recipe cost less than zero!");
        this.expLvlCost = exp;
    }

    private void addInput(Object input, int slot) {
        if (slot == 0 || slot == 1) {
            if (input != null) {
                if (input instanceof ItemStack)
                    inputs[slot] = input;
                else if (input instanceof Item)
                    inputs[slot] = new ItemStack((Item) input, 1);
                else if (input instanceof Block)
                    inputs[slot] = new ItemStack((Block) input, 1);
                else if (input instanceof String) {
                    String in = (String) input;
                    if (in.startsWith("fluid$")) {
                        String fluidName = in.substring(6);
                        if (FluidRegistry.isFluidRegistered(fluidName))
                            inputs[slot] = FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName));
                    }else
                        inputs[slot] = OreDictionary.getOres(in);
                }else
                    throw new RuntimeException("Invalid Anvil Recipe: " + input);
            }
        }
    }

    @Override
    public boolean matches(ItemStack left, ItemStack right) {
        return doItemsMatch(left, 0) && doItemsMatch(right, 1);
    }

    @SuppressWarnings("unchecked")
    public boolean doItemsMatch(ItemStack itemStack, int slot) {
        if (slot == 0 || slot == 1) {
            Object target = inputs[slot];
            if (target == null)
                return itemStack == null;
            else if (target instanceof ItemStack) {
                if (!checkItemEquals((ItemStack) target, itemStack))
                    return false;
            }else if (target instanceof ArrayList) {
                boolean matched = false;
                for (ItemStack item : (ArrayList<ItemStack>) target) {
                    matched = matched || checkItemEquals(item, itemStack);
                }
                if (!matched)
                    return false;
            }
            return true;
        }
        return false;
    }

    private boolean checkItemEquals(ItemStack target, ItemStack input) {
        if (input == null && target != null || input != null && target == null)
            return false;
        return (target.getItem() == input.getItem() && (target.getItemDamage() == OreDictionary.WILDCARD_VALUE || target.getItemDamage() == input.getItemDamage()));
    }

    @Override
    public ItemStack getCraftingResult(ItemStack left, ItemStack right) {
        return output.copy();
    }

    @Override
    public ItemStack recipeOutput() {
        return output;
    }

    @Override
    public Object[] recipeInputs() {
        return inputs;
    }

    @Override
    public int recipeCost() {
        return expLvlCost;
    }

}
