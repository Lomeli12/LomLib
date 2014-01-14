package net.lomeli.lomlib.item;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
            if (stack.getItem().hasContainerItem()) {
                return stack.getItem().getContainerItemStack(stack);
            } else {
                return null;
            }
        } else {
            stack.splitStack(1);

            return stack;
        }
    }

    public static void setBlock(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int blockID, int metaData) {
        int i1 = world.getBlockId(x, y, z);

        if (i1 == Block.snow.blockID && (world.getBlockMetadata(x, y, z) & 7) < 1) {
            side = 1;
        } else if (i1 != Block.vine.blockID && i1 != Block.tallGrass.blockID && i1 != Block.deadBush.blockID
                && (Block.blocksList[i1] == null || !Block.blocksList[i1].isBlockReplaceable(world, x, y, z))) {
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
        else if (y == 255 && Block.blocksList[blockID].blockMaterial.isSolid())
            return;
        else if (world.canPlaceEntityOnSide(blockID, x, y, z, false, side, player, stack)) {
            Block block = Block.blocksList[blockID];
            int j1 = metaData;
            int k1 = Block.blocksList[blockID].onBlockPlaced(world, x, y, z, side, hitX, hitY, hitZ, j1);

            if (placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, blockID, k1)) {
                world.playSoundEffect(x + 0.5F, y + 0.5F, z + 0.5F, Block.soundClothFootstep.getPlaceSound(),
                        (block.stepSound.getVolume() + 1.0F) / 2.0F, block.stepSound.getPitch() * 0.8F);
                --stack.stackSize;
            }
        }
    }

    public static boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int blockID,
            int metadata) {
        if (!world.setBlock(x, y, z, blockID, metadata, 3))
            return false;

        if (world.getBlockId(x, y, z) == blockID) {
            Block.blocksList[blockID].onBlockPlacedBy(world, x, y, z, player, stack);
            Block.blocksList[blockID].onPostBlockPlaced(world, x, y, z, metadata);
        }

        return true;
    }
}
