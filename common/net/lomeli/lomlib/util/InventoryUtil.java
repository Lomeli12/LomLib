package net.lomeli.lomlib.util;

import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;

public class InventoryUtil
{
	/**
	 * Meant to allow you to make a custom crafting grid on the fly. A 
	 * bit bugged at the moment (DO NOT USE).
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
    public static void createCraftMatrix(Container container, IInventory inventory, 
		int craftWidth, int craftHeight, int firstSlot, int x, int y)
	{
		int slotNum = firstSlot;
		for(int i = 0; i < craftWidth; i++)
		{
			for(int j = 0; j < craftHeight; j++)
			{
				container.inventorySlots.add(new Slot(inventory, slotNum, x + i * 18, y +  j * 18));
				container.inventoryItemStacks.add((Object)null);
				slotNum++;
			}
		}
	}
}
