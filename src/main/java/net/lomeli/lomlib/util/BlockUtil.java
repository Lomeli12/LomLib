package net.lomeli.lomlib.util;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLLog;
import cpw.mods.fml.common.registry.GameRegistry;

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
        Block water = Blocks.water, flowing = Blocks.flowing_water;
        if (world.getBlock(x, y + 1, z).equals(water) || world.getBlock(x, y - 1, z).equals(water) || world.getBlock(x + 1, y, z).equals(water) || world.getBlock(x - 1, y, z).equals(water)
                || world.getBlock(x, y, z + 1).equals(water) || world.getBlock(x, y, z - 1).equals(water) || world.getBlock(x, y + 1, z).equals(flowing) || world.getBlock(x, y - 1, z).equals(flowing)
                || world.getBlock(x + 1, y, z).equals(flowing) || world.getBlock(x - 1, y, z).equals(flowing) || world.getBlock(x, y, z + 1).equals(flowing) || world.getBlock(x, y, z - 1).equals(flowing))
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
        if (world.getBlockMetadata(x - 1, y, z) == 0 && isWaterSource(world, x - 1, y, z))
            j++;
        if (world.getBlockMetadata(x + 1, y, z) == 0 && isWaterSource(world, x + 1, y, z))
            j++;
        if (world.getBlockMetadata(x, y, z - 1) == 0 && isWaterSource(world, x, y, z - 1))
            j++;
        if (world.getBlockMetadata(x, y, z + 1) == 0 && isWaterSource(world, x, y, z + 1))
            j++;
        return j;
    }

    public static boolean isWaterSource(World world, int x, int y, int z) {
        return world.getBlock(x, y, z).equals(Blocks.water);
    }

    public static boolean isBlockWater(World world, int x, int y, int z) {
        return (world.getBlock(x, y, z).equals(Blocks.water)) || (world.getBlock(x, y, z).equals(Blocks.flowing_water));
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
        Block chest = Blocks.chest, trap = Blocks.trapped_chest;
        boolean yesThereIs = false;
        if (world.getBlock(x, y, z).equals(chest)) {
            if (world.getBlock(x + 1, y, z).equals(chest))
                yesThereIs = true;
            if (world.getBlock(x - 1, y, z).equals(chest))
                yesThereIs = true;
            if (world.getBlock(x, y, z + 1).equals(chest))
                yesThereIs = true;
            if (world.getBlock(x, y, z - 1).equals(chest))
                yesThereIs = true;
        } else if (world.getBlock(x, y, z).equals(trap)) {
            if (world.getBlock(x + 1, y, z).equals(trap))
                yesThereIs = true;
            if (world.getBlock(x - 1, y, z).equals(trap))
                yesThereIs = true;
            if (world.getBlock(x, y, z + 1).equals(trap))
                yesThereIs = true;
            if (world.getBlock(x, y, z - 1).equals(trap))
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

    public static void registerTile(Class<? extends TileEntity> tile) {
        GameRegistry.registerTileEntity(tile, tile.getName().toLowerCase());
    }

    public static boolean isTransparent(Block block) {
        return block.getLightOpacity() != 0xff;
    }
}
