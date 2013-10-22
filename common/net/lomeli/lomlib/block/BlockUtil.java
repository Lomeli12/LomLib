package net.lomeli.lomlib.block;

import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import net.lomeli.lomlib.LomLib;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockUtil {
    public static Block getBlock(World world, int x, int y, int z) {
        return Block.blocksList[world.getBlockId(x, y, z)];
    }

    /**
     * Allows you to get a block from practically any other mod.
     * 
     * @param itemString
     *            name of block instance
     * @param meta
     *            Metadata number for the block
     * @param BlockClass
     *            Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromModWithMeta(String itemString, int meta, String BlockClass) {
        ItemStack item = null;
        try {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if(obj instanceof Block)
                item = new ItemStack((Block) obj, 1, meta);
            else if(obj instanceof ItemStack)
                item = (ItemStack) obj;
            if(LomLib.debug)
                LomLib.logger.log(Level.INFO, obj.toString());

        }catch(Exception ex) {
            FMLLog.warning("Could not retrieve block identified by: " + itemString);
        }
        return item;
    }

    /**
     * Allows you to get a block from practically any other mod.
     * 
     * @param itemString
     *            name of block instance
     * @param BlockClass
     *            Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromMod(String itemString, String BlockClass) {
        ItemStack item = null;
        try {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString).get(null);
            if(obj instanceof Block)
                item = new ItemStack((Block) obj);
            else if(obj instanceof ItemStack)
                item = (ItemStack) obj;

            if(LomLib.debug)
                LomLib.logger.log(Level.INFO, obj.toString());

        }catch(Exception ex) {
            FMLLog.warning("Could not retrieve block identified by: " + itemString);
        }
        return item;
    }

    /**
     * Checks if the block is adjacent to a water block.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return True if next to water block, otherwise false
     * @author Lomeli12
     */
    public static boolean isBlockAdjacentToWater(World world, int x, int y, int z) {
        if(world.getBlockId(x, y + 1, z) == Block.waterStill.blockID || world.getBlockId(x, y - 1, z) == Block.waterStill.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x - 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterStill.blockID
                || world.getBlockId(x, y, z - 1) == Block.waterStill.blockID
                || world.getBlockId(x, y + 1, z) == Block.waterMoving.blockID
                || world.getBlockId(x, y - 1, z) == Block.waterMoving.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterMoving.blockID
                || world.getBlockId(x - 1, y, z) == Block.waterMoving.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterMoving.blockID
                || world.getBlockId(x, y, z - 1) == Block.waterMoving.blockID)
            return true;
        else
            return false;
    }

    /**
     * Similar to isBlockAdjacentToWater(), but restricted to water source
     * blocks
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return True if next to water block, otherwise false
     * @author Lomeli12
     */
    public static boolean isBlockAdjacentToWaterSource(World world, int x, int y, int z) {
        if(world.getBlockId(x, y + 1, z) == Block.waterStill.blockID || world.getBlockId(x, y - 1, z) == Block.waterStill.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x - 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterStill.blockID
                || world.getBlockId(x, y, z - 1) == Block.waterStill.blockID)
            return true;
        else
            return false;
    }

    public static boolean isAboveBlock(Entity entity, int x, int y, int z) {
        return ((entity.posX < x + 1.4D && entity.posX >= x) && (entity.posY < y + 1.4D && entity.posY >= y) && (entity.posZ < z + 1.4D && entity.posZ >= z));
    }
}
