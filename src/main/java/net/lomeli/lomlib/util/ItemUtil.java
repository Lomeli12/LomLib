package net.lomeli.lomlib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.oredict.OreDictionary;

public class ItemUtil {
    public static ItemStack consumeItem(ItemStack stack) {
        if (stack.stackSize == 1) {
            if (stack.getItem().hasContainerItem(stack))
                return stack.getItem().getContainerItem(stack);
            else
                return null;
        } else {
            stack.splitStack(1);

            return stack;
        }
    }

    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB) {
        return stackA == null ? stackB == null ? true : false : stackB != null && areItemsTheSame(stackA, stackB) && (stackA.getHasSubtypes() == false || stackA.getItemDamage() == stackB.getItemDamage());
    }

    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {
        return stackA == null ? stackB == null ? true : false : stackB != null && areItemsTheSame(stackA, stackB) && stackA.getItemDamage() == stackB.getItemDamage()
                && (!checkNBT || NBTUtil.doNBTsMatch(stackA.getTagCompound(), stackB.getTagCompound()));
    }

    public static boolean areItemsTheSame(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage();
    }

    public static ItemStack cloneStack(Item item, int stackSize) {
        if (item == null)
            return null;

        ItemStack stack = new ItemStack(item, stackSize);

        return stack;
    }

    public static ItemStack cloneStack(ItemStack stack, int stackSize) {
        if (stack == null)
            return null;

        ItemStack retStack = stack.copy();
        retStack.stackSize = stackSize;

        return retStack;
    }

    public static void dropItemStackIntoWorld(ItemStack stack, World world, double x, double y, double z, boolean velocity) {
        if (stack != null) {
            float x2 = 0.5F;
            float y2 = 0.0F;
            float z2 = 0.5F;

            if (velocity) {
                x2 = world.rand.nextFloat() * 0.8F + 0.1F;
                y2 = world.rand.nextFloat() * 0.8F + 0.1F;
                z2 = world.rand.nextFloat() * 0.8F + 0.1F;
            }
            EntityItem entity = new EntityItem(world, x + x2, y + y2, z + z2, stack.copy());

            if (velocity) {
                entity.motionX = ((float) world.rand.nextGaussian() * 0.05F);
                entity.motionY = ((float) world.rand.nextGaussian() * 0.05F + 0.2F);
                entity.motionZ = ((float) world.rand.nextGaussian() * 0.05F);
            } else {
                entity.motionY = -0.0500000007450581D;
                entity.motionX = 0.0D;
                entity.motionZ = 0.0D;
            }

            world.spawnEntityInWorld(entity);
        }
    }

    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null)
            return false;
        if (!stack1.isItemEqual(stack2))
            return false;
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2))
            return false;
        return true;
    }

    public static int searchForPossibleSlot(ItemStack stack, IInventory inventory) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if (item == null || item.getItem() == null)
                return i;
            else if (canStacksMerge(stack, item) && (item.stackSize + stack.stackSize) < item.getMaxStackSize())
                return i;
        }
        return -1;
    }

    /**
     * Attempts to put an item into the given inventory. Returns false if it fails.
     *
     * @param inventory
     * @param stack
     * @param doMove
     * @return
     */
    public static boolean insertIntoInventory(IInventory inventory, ItemStack stack, boolean doMove) {
        if (stack == null) return false;
        if (inventory == null) return false;

        int slot = searchForPossibleSlot(stack, inventory);

        if (doMove && slot > -1 && slot < inventory.getSizeInventory())
            return tryInsertStack(inventory, slot, stack, true);

        return slot != -1;
    }

    public static boolean tryInsertStack(IInventory targetInventory, int slot, ItemStack stack, boolean canMerge) {
        if (targetInventory.isItemValidForSlot(slot, stack)) {
            ItemStack targetStack = targetInventory.getStackInSlot(slot);
            if (targetStack == null) {
                int limit = targetInventory.getInventoryStackLimit();
                if (limit < stack.stackSize)
                    targetInventory.setInventorySlotContents(slot, stack.splitStack(limit));
                else {
                    targetInventory.setInventorySlotContents(slot, stack.copy());
                    stack.stackSize = 0;
                }
                return true;
            } else if (canMerge) {
                if (targetInventory.isItemValidForSlot(slot, stack) &&
                        canStacksMerge(stack, targetStack)) {
                    int space = targetStack.getMaxStackSize() - targetStack.stackSize;
                    int mergeAmount = Math.min(space, stack.stackSize);
                    ItemStack copy = targetStack.copy();
                    copy.stackSize += mergeAmount;
                    targetInventory.setInventorySlotContents(slot, copy);
                    stack.stackSize -= mergeAmount;
                    return true;
                }
            }
        }
        return false;
    }

    public static int getSlotWithStack(IInventory inventory, ItemStack stack) {
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack item = inventory.getStackInSlot(i);
            if (item != null && OreDictionary.itemMatches(item, stack, false))
                return i;
        }
        return -1;
    }

    public static boolean hasItemStack(IInventory inventory, ItemStack stack) {
        return getSlotWithStack(inventory, stack) != -1;
    }

    public static void setEntityItemAge(EntityItem item, int age) {
        item.age = age;
    }
}
