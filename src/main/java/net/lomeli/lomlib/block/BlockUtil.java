package net.lomeli.lomlib.block;

import java.util.logging.Level;

import cpw.mods.fml.common.FMLLog;

import net.lomeli.lomlib.LomLib;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class BlockUtil {

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
            if (obj instanceof Block)
                item = new ItemStack((Block) obj, 1, meta);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            if (LomLib.debug)
                LomLib.logger.log(Level.INFO, obj.toString());

        } catch (Exception ex) {
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
            if (obj instanceof Block)
                item = new ItemStack((Block) obj);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;

            if (LomLib.debug)
                LomLib.logger.log(Level.INFO, obj.toString());

        } catch (Exception ex) {
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
        /*if (world.getBlockId(x, y + 1, z) == Block.waterStill.blockID || world.getBlockId(x, y - 1, z) == Block.waterStill.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterStill.blockID || world.getBlockId(x - 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterStill.blockID || world.getBlockId(x, y, z - 1) == Block.waterStill.blockID
                || world.getBlockId(x, y + 1, z) == Block.waterMoving.blockID || world.getBlockId(x, y - 1, z) == Block.waterMoving.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterMoving.blockID || world.getBlockId(x - 1, y, z) == Block.waterMoving.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterMoving.blockID || world.getBlockId(x, y, z - 1) == Block.waterMoving.blockID)
            return true;
        else
            */return false;
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
        /*if (world.getBlockId(x, y + 1, z) == Block.waterStill.blockID || world.getBlockId(x, y - 1, z) == Block.waterStill.blockID
                || world.getBlockId(x + 1, y, z) == Block.waterStill.blockID || world.getBlockId(x - 1, y, z) == Block.waterStill.blockID
                || world.getBlockId(x, y, z + 1) == Block.waterStill.blockID || world.getBlockId(x, y, z - 1) == Block.waterStill.blockID)
            return true;
        else
            */return false;
    }

    public static boolean isAboveBlock(Entity entity, int x, int y, int z) {
        return ((entity.posX < x + 1.4D && entity.posX >= x) && (entity.posY < y + 1.5D && entity.posY >= y) && (entity.posZ < z + 1.4D && entity.posZ >= z));
    }

    /**
     * Use to check if double chest or not.
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return
     */
    public static boolean isThereANeighborChest(World world, int x, int y, int z) {
        boolean yesThereIs = false;
        /*if (world.getBlockId(x, y, z) == Block.chest.blockID) {
            if (world.getBlockId(x + 1, y, z) == Block.chest.blockID)
                yesThereIs = true;
            if (world.getBlockId(x - 1, y, z) == Block.chest.blockID)
                yesThereIs = true;
            if (world.getBlockId(x, y, z + 1) == Block.chest.blockID)
                yesThereIs = true;
            if (world.getBlockId(x, y, z - 1) == Block.chest.blockID)
                yesThereIs = true;
        } else if (world.getBlockId(x, y, z) == Block.chestTrapped.blockID) {
            if (world.getBlockId(x + 1, y, z) == Block.chestTrapped.blockID)
                yesThereIs = true;
            if (world.getBlockId(x - 1, y, z) == Block.chestTrapped.blockID)
                yesThereIs = true;
            if (world.getBlockId(x, y, z + 1) == Block.chestTrapped.blockID)
                yesThereIs = true;
            if (world.getBlockId(x, y, z - 1) == Block.chestTrapped.blockID)
                yesThereIs = true;
        }*/

        return yesThereIs;
    }

    public static int determineOrientation(World world, int x, int y, int z, EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - (float) x) < 2.0F && MathHelper.abs((float) entity.posZ - (float) z) < 2.0F) {
            double d0 = entity.posY + 1.82D - (double) entity.yOffset;

            if (d0 - (double) y > 2.0D)
                return 1;

            if ((double) y - d0 > 0.0D)
                return 0;
        }

        int l = MathHelper.floor_double((double) (entity.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }
}
