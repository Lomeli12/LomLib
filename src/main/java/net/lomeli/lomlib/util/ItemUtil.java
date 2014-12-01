package net.lomeli.lomlib.util;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLLog;

public class ItemUtil {
    /**
     * Allows you to get a item from practically any other mod.
     *
     * @param itemString   name of item instance
     * @param meta         Metadata number for the item
     * @param itemClassLoc Class where the items are declared
     * @author Lomeli12
     */

    public static ItemStack getItem(String itemString, int meta, String itemClassLoc) {
        ItemStack item = null;

        try {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if (obj instanceof Item)
                item = new ItemStack((Item) obj, 1, meta);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;

        } catch (Exception ex) {
            FMLLog.warning("Could not retrieve item identified by: " + itemString);
        }
        return item;
    }

    /**
     * Allows you to get a item from practically any other mod.
     *
     * @param itemString   name of item instance
     * @param itemClassLoc Class where the items are declared
     * @author Lomeli12
     */
    public static ItemStack getItem(String itemString, String itemClassLoc) {
        ItemStack item = null;

        try {
            String itemClass = itemClassLoc;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if (obj instanceof Item)
                item = new ItemStack((Item) obj);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;

        } catch (Exception ex) {
            FMLLog.warning("Could not retrieve item identified by: " + itemString);
        }
        return item;
    }

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

    public static void setBlock(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block block, int metaData) {
        Block i1 = world.getBlock(x, y, z);

        if (i1.equals(Blocks.snow) && (world.getBlockMetadata(x, y, z) & 7) < 1)
            side = 1;
        else if (i1 != Blocks.vine && i1 != Blocks.tallgrass && i1 != Blocks.deadbush && (i1 == null || !i1.isReplaceable(world, x, y, z))) {
            if (side == 0)
                --y;

            if (side == 1)
                ++y;

            if (side == 2)
                --z;

            if (side == 3)
                ++z;

            if (side == 4)
                --x;

            if (side == 5)
                ++x;
        }

        if (stack.stackSize == 0)
            return;
        else if (!player.canPlayerEdit(x, y, z, side, stack))
            return;
        else if (y == 255 && block.getMaterial().isSolid())
            return;
        else if (world.canPlaceEntityOnSide(block, x, y, z, false, side, player, stack)) {
            int j1 = metaData;
            int k1 = block.onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, j1);

            if (placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, block, k1)) {
                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.soundTypeCloth.getBreakSound(), (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }
        }
    }

    public static boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, Block block, int metadata) {
        if (!world.setBlock(x, y, z, block, metadata, 3))
            return false;

        if (world.getBlock(x, y, z).equals(block)) {
            block.onBlockPlacedBy(world, x, y, z, player, stack);
            block.onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }

    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB) {
        return stackA == null ? stackB == null ? true : false : stackB != null && areItemsTheSame(stackA, stackB) && (stackA.getHasSubtypes() == false || stackA.getItemDamage() == stackB.getItemDamage());
    }

    public static boolean itemsEqualWithMetadata(ItemStack stackA, ItemStack stackB, boolean checkNBT) {
        return stackA == null ? stackB == null ? true : false : stackB != null && areItemsTheSame(stackA, stackB) && stackA.getItemDamage() == stackB.getItemDamage()
                && (!checkNBT || NBTUtil.doNBTsMatch(stackA.stackTagCompound, stackB.stackTagCompound));
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

    public static ItemStack createNewBook(String author, String title, String[] pageText) {
        ItemStack newBook = new ItemStack(Items.written_book);

        if (!newBook.hasTagCompound())
            newBook.stackTagCompound = new NBTTagCompound();

        NBTUtil.setString(newBook, "author", author);
        NBTUtil.setString(newBook, "title", title);
        NBTTagList pages = new NBTTagList();
        for (int i = 0; i < pageText.length; i++) {
            pages.appendTag(new NBTTagString("" + (i + 1)));
        }
        newBook.setTagInfo("pages", pages);
        return newBook;
    }
}
