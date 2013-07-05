package net.lomeli.lomlib.block;

import cpw.mods.fml.common.FMLLog;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BlockUtil
{
    public static Block getBlock(World world, int x, int y, int z)
    {
        return Block.blocksList[world.getBlockId(x, y, z)];
    }
    /**
     * Allows you to get a block from practically any other mod.
     * @param itemString name of block instance
     * @param meta Metadata number for the block
     * @param BlockClass Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromModWithMeta(String itemString, int meta, String BlockClass)
    {
    	ItemStack item = null;
        try
        {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString)
                    .get(null);
            if (obj instanceof Block)
                item = new ItemStack((Block) obj, 1, meta);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            
        } catch (Exception ex)
        {
            FMLLog.warning("Could not retrieve block identified by: "
            	+ itemString);
        }
        return item;
    }
    /**
     * Allows you to get a block from practically any other mod.
     * @param itemString name of block instance
     * @param BlockClass Class where the blocks are declared
     * @author Lomeli12
     */
    public static ItemStack getBlockFromMod(String itemString, String BlockClass)
    {
    	ItemStack item = null;
        try
        {
            String itemClass = BlockClass;
            Object obj = Class.forName(itemClass).getField(itemString)
                    .get(null);
            if (obj instanceof Block)
                item = new ItemStack((Block) obj);
            else if (obj instanceof ItemStack)
                item = (ItemStack) obj;
            
        } catch (Exception ex)
        {
            FMLLog.warning("Could not retrieve block identified by: "
            	+ itemString);
        }
        return item;
    }
    
    private static boolean land = false;
    
	/**
	 * Checks if a block is water and if it's stationary
	 * <p>From q3hardcore's Coral Reef Mod
	 * @author q3hardcore
	 */
	public static boolean checkWater(World world, int x, int y, int z, boolean stationary, Block block) {
		if(checkWater(world, x, y, z, block)) {
			int blockID = world.getBlockId(x, y, z);
			if(blockID > 0 && blockID < Block.blocksList.length) {
				Block waterBlock = Block.blocksList[blockID];
				if(waterBlock != null) {
					boolean waterStationary = waterBlock.func_82506_l();
					return waterStationary == stationary;
				}
			}
			return false;
		} else {
			return false;
		}
	}

	/**
	 * Checks if a block is water.
	 * <p>From q3hardcore's Coral Reef Mod
	 * @author q3hardcore
	 */
	public static boolean checkWater(World world, int x, int y, int z, Block block) {
		int blockID = world.getBlockId(x, y, z);

		// if the block is any type of coral, it's not water
		if(blockID == block.blockID) {
			return false;
		}

		if(land) {
			return true;
		} else {
			if(world.getBlockMaterial(x, y, z) == Material.water) {
				return true;
			}
		}
		return false;
	}
}
