package net.lomeli.lomlib.core.recipes;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

public class FluidAnvilRecipe implements IAnvilRecipe {
    private final ItemStack output;
    private Object[] inputs = new Object[2];
    private int expLvlCost = 0;
    private boolean reverse;

    /**
     * ATM, right input cannot be null due to how the AnvilUpdateEvent works.
     * Not much I can do about that
     *
     * @param output
     * @param leftInput
     * @param rightInput
     * @param expCost
     */
    public FluidAnvilRecipe(ItemStack output, Object leftInput, Object rightInput, int expCost, boolean reverseable) {
        this.output = output;
        addInput(leftInput, 0);
        addInput(rightInput, 1);
        setXPCost(expCost);
        if (leftInput == null && rightInput == null)
            throw new RuntimeException("Invalid Anvil Recipe: Both inputs cannot be null!");
        reverse = reverseable;
    }

    public FluidAnvilRecipe(ItemStack output, Object leftInput, Object rightInput, int expCost) {
        this(output, leftInput, rightInput, expCost, false);
    }

    public FluidAnvilRecipe(ItemStack output, Object input, int expCost) {
        this(output, input, null, expCost, false);
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
                    } else
                        inputs[slot] = OreDictionary.getOres(in);
                } else
                    throw new RuntimeException("Invalid Anvil Recipe: " + input);
            }
        }
    }

    @Override
    public boolean matches(ItemStack left, ItemStack right) {
        boolean flag = doItemsMatch(left, 0) && doItemsMatch(right, 1);
        if (!flag && reverse)
            flag = doItemsMatch(left, 1) && doItemsMatch(right, 0);
        return flag;
    }

    @SuppressWarnings("unchecked")
    public boolean doItemsMatch(ItemStack itemStack, int slot) {
        if (slot == 0 || slot == 1) {
            Object target = inputs[slot];
            if (target == null)
                return itemStack == null;
            else if (target instanceof ItemStack) {
                if (!OreDictionary.itemMatches((ItemStack) target, itemStack, false))
                    return false;
            } else if (target instanceof List) {
                boolean matched = false;
                for (ItemStack item : (List<ItemStack>) target) {
                    matched = matched || OreDictionary.itemMatches(item, itemStack, false);
                }
                if (!matched)
                    return false;
            }
            return true;
        }
        return false;
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
