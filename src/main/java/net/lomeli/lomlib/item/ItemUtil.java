package net.lomeli.lomlib.item;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil {
    /**
     * Allows you to get a item from practically any other mod.
     * 
     * @param itemString
     *            name of item instance
     * @param meta
     *            Metadata number for the item
     * @param ItemCassLoc
     *            Class where the items are declared
     * @author Lomeli12
     */

    public static ItemStack getItem(String itemString, int meta, String itemClassLoc) {
        ItemStack item = null;

        try {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if(obj instanceof Item)
                item = new ItemStack((Item) obj, 1, meta);
            else if(obj instanceof ItemStack)
                item = (ItemStack) obj;

        }catch(Exception ex) {
            FMLLog.warning("Could not retrieve item identified by: " + itemString);
        }
        return item;
    }

    /**
     * Allows you to get a item from practically any other mod.
     * 
     * @param itemString
     *            name of item instance
     * @param ItemCassLoc
     *            Class where the items are declared
     * @author Lomeli12
     */
    public static ItemStack getItem(String itemString, String itemClassLoc) {
        ItemStack item = null;

        try {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if(obj instanceof Item)
                item = new ItemStack((Item) obj);
            else if(obj instanceof ItemStack)
                item = (ItemStack) obj;

        }catch(Exception ex) {
            FMLLog.warning("Could not retrieve item identified by: " + itemString);
        }
        return item;
    }

    public static ItemStack consumeItem(ItemStack stack) {
        if(stack.stackSize == 1) {
            if(stack.getItem().hasContainerItem()) {
                return stack.getItem().getContainerItem(stack);
            }else {
                return null;
            }
        }else {
            stack.splitStack(1);

            return stack;
        }
    }
}
