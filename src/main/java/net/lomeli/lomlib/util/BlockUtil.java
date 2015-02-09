package net.lomeli.lomlib.util;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.lomeli.lomlib.LomLib;

public class BlockUtil {
    /**
     * Allows you to get a block from practically any other mod.
     *
     * @param itemString name of block instance
     * @param meta       Metadata number for the block
     * @param BlockClass Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromModWithMeta(String itemString, int meta, String BlockClass) {
        ItemStack item = null;
        try {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if (obj instanceof Block)
                item = new ItemStack((Block) obj, 1, meta);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            if (LomLib.debug)
                LomLib.logger.logBasic(obj.toString());

        } catch (Exception ex) {
            FMLLog.warning("Could not retrieve block identified by: " + itemString);
        }
        return item;
    }

    /**
     * Allows you to get a block from practically any other mod.
     *
     * @param itemString name of block instance
     * @param BlockClass Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromMod(String itemString, String BlockClass) {
        ItemStack item = null;
        try {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if (obj instanceof Block)
                item = new ItemStack((Block) obj);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;

            if (LomLib.debug)
                LomLib.logger.logBasic(obj.toString());

        } catch (Exception ex) {
            FMLLog.warning("Could not retrieve block identified by: " + itemString);
        }
        return item;
    }

    public static void registerTile(Class<? extends TileEntity> tile) {
        GameRegistry.registerTileEntity(tile, tile.getName().toLowerCase());
    }

    public static Block getBlock(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock();
    }

    public static Block getBlock(IBlockAccess world, int x, int y, int z) {
        return getBlock(world, new BlockPos(x, y, z));
    }
}
