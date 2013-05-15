package net.lomeli.lomlib.block;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.block.Block;
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
            {
                item = new ItemStack((Block) obj, 1, meta);
            } else if (obj instanceof ItemStack)
            {
                item = (ItemStack) obj;
            }
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
            {
                item = new ItemStack((Block) obj);
            } else if (obj instanceof ItemStack)
            {
                item = (ItemStack) obj;
            }
        } catch (Exception ex)
        {
            FMLLog.warning("Could not retrieve block identified by: "
            	+ itemString);
        }
        return item;
    }
}
