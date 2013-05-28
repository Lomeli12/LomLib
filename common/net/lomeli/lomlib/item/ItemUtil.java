package net.lomeli.lomlib.item;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil
{
	/**
	 * Checks an entire inventory for the first 
	 * instance of a certain item
	 * @param itemID Id for the item or block you're looking for
	 * @param inventory Inventory you are searching
	 * @author Lomeli12
	 */
    public static int getSlotContainingItem(int itemID, ItemStack[] inventory)
    {
        for (int j = 0; j < inventory.length; j++)
        {
            if (inventory[j] != null && inventory[j].itemID == itemID)
                return j;
        }
        return -1;
    }
    
    /**
     * Allows you to get a item from practically any other mod.
     * @param itemString name of item instance
     * @param meta Metadata number for the item
     * @param ItemCassLoc Class where the items are declared
     * @author Lomeli12
     */
    
    public static ItemStack getItem(String itemString, int meta, String itemClassLoc)
    {
        ItemStack item = null;

        try
        {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString)
                    .get(null);
            if (obj instanceof Item)
                item = new ItemStack((Item) obj, 1, meta);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            
        } catch (Exception ex)
        {
            FMLLog.warning("Could not retrieve item identified by: "
                    + itemString);
        }
        return item;
    }

    /**
     * Allows you to get a item from practically any other mod.
     * @param itemString name of item instance
     * @param ItemCassLoc Class where the items are declared
     * @author Lomeli12
     */
    public static ItemStack getItem(String itemString, String itemClassLoc)
    {
        ItemStack item = null;

        try
        {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString)
                    .get(null);
            if (obj instanceof Item)
                item = new ItemStack((Item) obj);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            
        } catch (Exception ex)
        {
            FMLLog.warning("Could not retrieve item identified by: "
                    + itemString);
        }
        return item;
    }
}
