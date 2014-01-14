package net.lomeli.lomlib.util;

import net.lomeli.lomlib.libs.Incomplete;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ReportedException;

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
    public static void createCraftMatrix(Container container, IInventory inventory, int craftWidth, int craftHeight, int firstSlot, int x, int y) {
        int slotNum = firstSlot;
        for (int i = 0; i < craftWidth; i++) {
            for (int j = 0; j < craftHeight; j++) {
                container.inventorySlots.add(new Slot(inventory, slotNum, x + i * 18, y + j * 18));
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
    public static int getSlotContainingItem(int itemID, int meta, ItemStack[] inventory) {
        for (int j = 0; j < inventory.length; j++) {
            if (inventory[j] != null && inventory[j].itemID == itemID && inventory[j].getItemDamage() == meta)
                return j;
        }
        return -1;
    }

    public static int getSlotContainingItem(int itemID, ItemStack[] inventory) {
        return getSlotContainingItem(itemID, 0, inventory);
    }

    public static int getSlotContainingStack(ItemStack stack, ItemStack[] inventory) {
        return getSlotContainingItem(stack.itemID, stack.getItemDamage(), inventory);
    }

    public static int getPlayerInvSlotContainingItem(int itemID, int meta, InventoryPlayer inventory) {
        for (int j = 0; j < inventory.getSizeInventory(); j++) {
            if (inventory.getStackInSlot(j) != null && inventory.getStackInSlot(j).itemID == itemID && inventory.getStackInSlot(j).getItemDamage() == meta)
                return j;
        }
        return -1;
    }

    public static int getPlayerInvSlotContainingItem(int itemID, InventoryPlayer inventory) {
        return getPlayerInvSlotContainingItem(itemID, 0, inventory);
    }

    public static int getPlayerInvSlotContainingStack(ItemStack stack, InventoryPlayer inventory) {
        return getPlayerInvSlotContainingItem(stack.itemID, stack.getItemDamage(), inventory);
    }

    public static int getFirstEmptyStack(IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (inventory.getStackInSlot(i) == null) {
                return i;
            }
        }

        return -1;
    }

    public static void addItemsToInventory(ItemStack stack, IInventory inventory) {
        if (stack == null) {
            return;
        } else if (stack.stackSize == 0) {
            return;
        } else {
            try {
                int i;

                if (stack != null && stack.isItemDamaged()) {
                    i = getFirstEmptyStack(inventory);

                    if (i >= 0) {
                        inventory.setInventorySlotContents(i, ItemStack.copyItemStack(stack));
                        inventory.getStackInSlot(i).animationsToGo = 5;
                        stack.stackSize = 0;
                        return;
                    } else {
                        return;
                    }
                } else {
                    do {
                        i = stack.stackSize;
                        stack.stackSize = storePartialItemStack(stack, inventory);
                    } while (stack.stackSize > 0 && stack.stackSize < i);

                    if (stack.stackSize == i) {
                        stack.stackSize = 0;
                        return;
                    }
                    return;
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "[LomLib]: Failed to add item to inventory");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Item being added");
                crashreportcategory.addCrashSection("Item ID", Integer.valueOf(stack.itemID));
                crashreportcategory.addCrashSection("Item data", Integer.valueOf(stack.getItemDamage()));
                throw new ReportedException(crashreport);
            }
        }
    }

    @SuppressWarnings("unused")
    private static int storePartialItemStack(ItemStack par1ItemStack, IInventory inventory) {
        int i = par1ItemStack.itemID;
        int j = par1ItemStack.stackSize;
        int k;
        if (par1ItemStack != null) {

            if (par1ItemStack.getMaxStackSize() == 1) {
                k = getFirstEmptyStack(inventory);

                if (k < 0) {
                    return j;
                } else {
                    if (inventory.getStackInSlot(k) == null) {
                        inventory.setInventorySlotContents(k, ItemStack.copyItemStack(par1ItemStack));
                    }

                    return 0;
                }
            } else {
                k = storeItemStack(par1ItemStack, inventory);

                if (k < 0) {
                    k = getFirstEmptyStack(inventory);
                }

                if (k < 0) {
                    return j;
                } else {
                    if (inventory.getStackInSlot(k) == null) {
                        inventory.setInventorySlotContents(k, new ItemStack(i, 0, par1ItemStack.getItemDamage()));

                        if (par1ItemStack.hasTagCompound()) {
                            inventory.getStackInSlot(k).setTagCompound((NBTTagCompound) par1ItemStack.getTagCompound().copy());
                        }
                    }

                    int l = j;
                    if (inventory.getStackInSlot(k) != null) {
                        if (j > inventory.getStackInSlot(k).getMaxStackSize() - inventory.getStackInSlot(k).stackSize) {
                            l = inventory.getStackInSlot(k).getMaxStackSize() - inventory.getStackInSlot(k).stackSize;
                        }

                        if (l > inventory.getInventoryStackLimit() - inventory.getStackInSlot(k).stackSize) {
                            l = inventory.getInventoryStackLimit() - inventory.getStackInSlot(k).stackSize;
                        }

                        if (l == 0) {
                            return j;
                        } else {
                            j -= l;
                            inventory.getStackInSlot(k).stackSize += l;
                            inventory.getStackInSlot(k).animationsToGo = 5;
                            return j;
                        }
                    } else
                        return 0;
                }
            }
        }
        return 0;
    }

    private static int storeItemStack(ItemStack par1ItemStack, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            if (inventory.getStackInSlot(i) != null && inventory.getStackInSlot(i).itemID == par1ItemStack.itemID && inventory.getStackInSlot(i).isStackable()
                    && inventory.getStackInSlot(i).stackSize < inventory.getStackInSlot(i).getMaxStackSize()
                    && inventory.getStackInSlot(i).stackSize < inventory.getInventoryStackLimit()
                    && (!inventory.getStackInSlot(i).getHasSubtypes() || inventory.getStackInSlot(i).getItemDamage() == par1ItemStack.getItemDamage())
                    && ItemStack.areItemStackTagsEqual(inventory.getStackInSlot(i), par1ItemStack)) {
                return i;
            }
        }

        return -1;
    }
}
