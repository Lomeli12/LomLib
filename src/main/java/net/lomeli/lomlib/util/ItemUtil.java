package net.lomeli.lomlib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
}
