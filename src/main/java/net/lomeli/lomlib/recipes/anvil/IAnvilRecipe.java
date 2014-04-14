package net.lomeli.lomlib.recipes.anvil;

import net.minecraft.item.ItemStack;

public interface IAnvilRecipe {
    /**
     * Checks to see if the left and right stack match the recipe
     * @param left
     * @param right
     * @return
     */
    boolean matches(ItemStack left, ItemStack right);
    
    /**
     * Get a copy of the result
     * @param left
     * @param right
     * @return
     */
    ItemStack getCraftingResult(ItemStack left, ItemStack right);
    
    /**
     * Get the recipe result
     * @return
     */
    ItemStack recipeOutput();
    
    /**
     * Get the inputs for the recipe
     * @return
     */
    Object[] recipeInputs();
    
    /**
     * Cost (in Experience levels) of the recipe
     * @return
     */
    int recipeCost();
}
