package net.lomeli.lomlib.util;

import net.lomeli.lomlib.libs.Incomplete;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class InventoryUtil {
    /**
     * Meant to allow you to make a custom crafting grid on the fly. A bit
     * bugged at the moment (DO NOT USE).
     * 
     * @param container
     * @param inventory
     * @param craftWidth
     * @param craftHeight
     * @param firstSlot
     * @param x
     * @param y
     * @author Lomeli12
     */
    @SuppressWarnings("unchecked")
    @Incomplete
    public static void createCraftMatrix(Container container,
            IInventory inventory, int craftWidth, int craftHeight,
            int firstSlot, int x, int y) {
        int slotNum = firstSlot;
        for(int i = 0; i < craftWidth; i++) {
            for(int j = 0; j < craftHeight; j++) {
                container.inventorySlots.add(new Slot(inventory, slotNum, x + i
                        * 18, y + j * 18));
                container.inventoryItemStacks.add((Object) null);
                slotNum++;
            }
        }
    }

    /**
     * Checks an entire inventory for the first instance of a certain item
     * 
     * @param itemID
     *            Id for the item or block you're looking for
     * @param inventory
     *            Inventory you are searching
     * @author Lomeli12
     */
    public static int getSlotContainingItem(int itemID, int meta,
            ItemStack[] inventory) {
        for(int j = 0; j < inventory.length; j++) {
            if(inventory[j] != null && inventory[j].itemID == itemID
                    && inventory[j].getItemDamage() == meta)
                return j;
        }
        return -1;
    }

    public static int getSlotContainingItem(int itemID, ItemStack[] inventory) {
        return getSlotContainingItem(itemID, 0, inventory);
    }

    public static int getSlotContainingStack(ItemStack stack,
            ItemStack[] inventory) {
        return getSlotContainingItem(stack.itemID, stack.getItemDamage(),
                inventory);
    }

    public static int getPlayerInvSlotContainingItem(int itemID, int meta,
            InventoryPlayer inventory) {
        for(int j = 0; j < inventory.getSizeInventory(); j++) {
            if(inventory.getStackInSlot(j) != null
                    && inventory.getStackInSlot(j).itemID == itemID
                    && inventory.getStackInSlot(j).getItemDamage() == meta)
                return j;
        }
        return -1;
    }

    public static int getPlayerInvSlotContainingItem(int itemID,
            InventoryPlayer inventory) {
        return getPlayerInvSlotContainingItem(itemID, 0, inventory);
    }

    public static int getPlayerInvSlotContainingStack(ItemStack stack,
            InventoryPlayer inventory) {
        return getPlayerInvSlotContainingItem(stack.itemID,
                stack.getItemDamage(), inventory);
    }
}
