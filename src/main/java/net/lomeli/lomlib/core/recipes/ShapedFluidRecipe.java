package net.lomeli.lomlib.core.recipes;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.oredict.OreDictionary;

import net.lomeli.lomlib.util.FluidUtil;

/**
 * Based off of OreDict's ShapedOreRecipe
 *
 * @author Lomeli12
 */
public class ShapedFluidRecipe implements IRecipe {

    private static final int MAX_CRAFT_GRID_WIDTH = 3;
    private static final int MAX_CRAFT_GRID_HEIGHT = 3;
    public Object[] input = null;
    public int width = 0;
    public int height = 0;
    private ItemStack output = null;
    private boolean mirrored = true;

    public ShapedFluidRecipe(ItemStack result, Object... recipe) {
        output = result.copy();

        String shape = "";
        int idx = 0;

        if (recipe[idx] instanceof Boolean) {
            mirrored = (Boolean) recipe[idx];
            if (recipe[idx + 1] instanceof Object[])
                recipe = (Object[]) recipe[idx + 1];
            else
                idx = 1;
        }

        if (recipe[idx] instanceof String[]) {
            String[] parts = ((String[]) recipe[idx++]);

            for (String s : parts) {
                width = s.length();
                shape += s;
            }

            height = parts.length;
        } else {
            while (recipe[idx] instanceof String) {
                String s = (String) recipe[idx++];
                shape += s;
                width = s.length();
                height++;
            }
        }

        if (width * height != shape.length()) {
            String ret = "Invalid shaped ore recipe: ";
            for (Object tmp : recipe) {
                ret += tmp + ", ";
            }
            ret += output;
            throw new RuntimeException(ret);
        }

        HashMap<Character, Object> itemMap = new HashMap<Character, Object>();

        for (; idx < recipe.length; idx += 2) {
            Character chr = (Character) recipe[idx];
            Object in = recipe[idx + 1];

            if (in instanceof ItemStack) {
                if (FluidUtil.isFilledContainer((ItemStack) in))
                    itemMap.put(chr, FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid((ItemStack) in)));
                else
                    itemMap.put(chr, ((ItemStack) in).copy());
            } else if (in instanceof Item) {
                if (FluidUtil.isFilledContainer(new ItemStack((Item) in)))
                    itemMap.put(chr, FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(new ItemStack((Item) in))));
                else
                    itemMap.put(chr, new ItemStack((Item) in));
            } else if (in instanceof Block)
                itemMap.put(chr, new ItemStack((Block) in, 1, OreDictionary.WILDCARD_VALUE));
            else if (in instanceof String) {
                if (((String) in).startsWith("fluid$")) {
                    String fluidName = ((String) in).substring(6);
                    if (FluidRegistry.isFluidRegistered(fluidName))
                        itemMap.put(chr, FluidUtil.getContainersForFluid(FluidRegistry.getFluid(fluidName)));
                } else
                    itemMap.put(chr, OreDictionary.getOres((String) in));
            } else {
                String ret = "Invalid shaped ore recipe: ";
                for (Object tmp : recipe) {
                    ret += tmp + ", ";
                }
                ret += output;
                throw new RuntimeException(ret);
            }
        }

        input = new Object[width * height];
        int x = 0;
        for (char chr : shape.toCharArray()) {
            input[x++] = itemMap.get(chr);
        }
    }

    public ShapedFluidRecipe(Block result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    public ShapedFluidRecipe(Item result, Object... recipe) {
        this(new ItemStack(result), recipe);
    }

    ShapedFluidRecipe(ShapedRecipes recipe, Map<ItemStack, String> replacements) {
        output = recipe.getRecipeOutput();
        width = recipe.recipeWidth;
        height = recipe.recipeHeight;

        input = new Object[recipe.recipeItems.length];

        for (int i = 0; i < input.length; i++) {
            ItemStack ingred = recipe.recipeItems[i];

            if (ingred == null)
                continue;

            input[i] = recipe.recipeItems[i];

            for (Entry<ItemStack, String> replace : replacements.entrySet()) {
                if (OreDictionary.itemMatches(replace.getKey(), ingred, true)) {
                    input[i] = OreDictionary.getOres(replace.getValue());
                    break;
                } else if (FluidUtil.isFilledContainer(replace.getKey())) {
                    input[i] = FluidUtil.getContainersForFluid(FluidUtil.getContainerFluid(replace.getKey()));
                    break;
                }
            }
        }
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting var1) {
        return output.copy();
    }

    @Override
    public int getRecipeSize() {
        return input.length;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return output;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        for (int x = 0; x <= MAX_CRAFT_GRID_WIDTH - width; x++) {
            for (int y = 0; y <= MAX_CRAFT_GRID_HEIGHT - height; ++y) {
                if (checkMatch(inv, x, y, false))
                    return true;

                if (mirrored && checkMatch(inv, x, y, true))
                    return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    private boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror) {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++) {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++) {
                int subX = x - startX;
                int subY = y - startY;
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height) {
                    if (mirror) {
                        target = input[width - subX - 1 + subY * width];
                    } else {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack) {
                    if (!OreDictionary.itemMatches((ItemStack) target, slot, false))
                        return false;
                } else if (target instanceof List) {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>) target).iterator();
                    while (itr.hasNext() && !matched)
                        matched = OreDictionary.itemMatches(itr.next(), slot, false);

                    if (!matched)
                        return false;
                } else if (target == null && slot != null)
                    return false;
            }
        }

        return true;
    }

    public Object[] getInput() {
        return this.input;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
