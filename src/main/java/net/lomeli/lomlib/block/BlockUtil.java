package net.lomeli.lomlib.block;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import net.lomeli.lomlib.LomLibCore;

import cpw.mods.fml.common.FMLLog;

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
            if (LomLibCore.debug)
                LomLibCore.logger.logBasic(obj.toString());

        }catch (Exception ex) {
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

            if (LomLibCore.debug)
                LomLibCore.logger.logBasic(obj.toString());

        }catch (Exception ex) {
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
        String water = Blocks.water.getUnlocalizedName(), flowing = Blocks.flowing_water.getUnlocalizedName();
        if (world.getBlock(x, y + 1, z).getUnlocalizedName().equals(water)
                || world.getBlock(x, y - 1, z).getUnlocalizedName().equals(water)
                || world.getBlock(x + 1, y, z).getUnlocalizedName().equals(water)
                || world.getBlock(x - 1, y, z).getUnlocalizedName().equals(water)
                || world.getBlock(x, y, z + 1).getUnlocalizedName().equals(water)
                || world.getBlock(x, y, z - 1).getUnlocalizedName().equals(water)
                || world.getBlock(x, y + 1, z).getUnlocalizedName().equals(flowing)
                || world.getBlock(x, y - 1, z).getUnlocalizedName().equals(flowing)
                || world.getBlock(x + 1, y, z).getUnlocalizedName().equals(flowing)
                || world.getBlock(x - 1, y, z).getUnlocalizedName().equals(flowing)
                || world.getBlock(x, y, z + 1).getUnlocalizedName().equals(flowing)
                || world.getBlock(x, y, z - 1).getUnlocalizedName().equals(flowing))
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
     * @return number of water source blocks the block is next to
     * @author Lomeli12
     */
    public static int isBlockAdjacentToWaterSource(World world, int x, int y, int z) {
        int j = 0;
        if (world.getBlockMetadata(x - 1, y, z) == 0 && isBlockWater(world, x - 1, y, z))
            j++;
        if (world.getBlockMetadata(x + 1, y, z) == 0 && isBlockWater(world, x + 1, y, z))
            j++;
        if (world.getBlockMetadata(x, y, z - 1) == 0 && isBlockWater(world, x, y, z - 1))
            j++;
        if (world.getBlockMetadata(x, y, z + 1) == 0 && isBlockWater(world, x, y, z + 1))
            j++;
        return j;
    }

    public static boolean isBlockWater(World world, int x, int y, int z) {
        return (world.getBlock(x, y, z).getUnlocalizedName().equals(Blocks.water.getUnlocalizedName()))
                || (world.getBlock(x, y, z).getUnlocalizedName().equals(Blocks.flowing_water.getUnlocalizedName()));
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
        String chest = Blocks.chest.getUnlocalizedName(), trap = Blocks.trapped_chest.getUnlocalizedName();
        boolean yesThereIs = false;
        if (world.getBlock(x, y, z).getUnlocalizedName().equals(chest)) {
            if (world.getBlock(x + 1, y, z).getUnlocalizedName().equals(chest))
                yesThereIs = true;
            if (world.getBlock(x - 1, y, z).getUnlocalizedName().equals(chest))
                yesThereIs = true;
            if (world.getBlock(x, y, z + 1).getUnlocalizedName().equals(chest))
                yesThereIs = true;
            if (world.getBlock(x, y, z - 1).getUnlocalizedName().equals(chest))
                yesThereIs = true;
        }else if (world.getBlock(x, y, z).getUnlocalizedName().equals(trap)) {
            if (world.getBlock(x + 1, y, z).getUnlocalizedName().equals(trap))
                yesThereIs = true;
            if (world.getBlock(x - 1, y, z).getUnlocalizedName().equals(trap))
                yesThereIs = true;
            if (world.getBlock(x, y, z + 1).getUnlocalizedName().equals(trap))
                yesThereIs = true;
            if (world.getBlock(x, y, z - 1).getUnlocalizedName().equals(trap))
                yesThereIs = true;
        }

        return yesThereIs;
    }

    public static int determineOrientation(World world, int x, int y, int z, EntityLivingBase entity) {
        if (MathHelper.abs((float) entity.posX - x) < 2.0F && MathHelper.abs((float) entity.posZ - z) < 2.0F) {
            double d0 = entity.posY + 1.82D - entity.yOffset;

            if (d0 - y > 2.0D)
                return 1;

            if (y - d0 > 0.0D)
                return 0;
        }

        int l = MathHelper.floor_double(entity.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
        return l == 0 ? 2 : (l == 1 ? 5 : (l == 2 ? 3 : (l == 3 ? 4 : 0)));
    }
}
