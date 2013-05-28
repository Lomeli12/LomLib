package net.lomeli.lomlib.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * This worldgen is based off of that of Vanilla
 * clay, allowing you to generate blocks in the water,
 * such in ocean biomes, swamps, and rivers
 * @author Lomeli12
 */
public class WorldGenSubmergedOre extends WorldGenerator
{
    /** The block ID for whatever block you want to generate. */
    private int blockID;

    /** The number of blocks to generate. */
    private int numberOfBlocks;
    
    /** The block's metadata. */
    private int blockMetaData;

    public WorldGenSubmergedOre(int itemID, int amount)
    {
    	blockID = itemID;
        numberOfBlocks = amount;
        blockMetaData = 0;
    }
    
    public WorldGenSubmergedOre(int itemID, int metadata, int amount)
    {
    	blockID = itemID;
        numberOfBlocks = amount;
        blockMetaData = metadata;
    }


    public WorldGenSubmergedOre(Block generatedBlock, int amount)
    {
    	blockID = generatedBlock.blockID;
        numberOfBlocks = amount;
        blockMetaData = 0;
    }
    
    public WorldGenSubmergedOre(Block generatedBlock, int meta, int amount)
    {
    	blockID = generatedBlock.blockID;
        numberOfBlocks = amount;
        blockMetaData = meta;
    }
    
    @Override
    public boolean generate(World par1World, Random par2Random, int par3,
            int par4, int par5)
    {
        if (par1World.getBlockMaterial(par3, par4, par5) != Material.water)
            return false;
        else
        {
            int var6 = par2Random.nextInt(numberOfBlocks) + 2;
            byte var7 = 1;

            for (int var8 = par3 - var6; var8 <= par3 + var6; ++var8)
            {
                for (int var9 = par5 - var6; var9 <= par5 + var6; ++var9)
                {
                    int var10 = var8 - par3;
                    int var11 = var9 - par5;

                    if (var10 * var10 + var11 * var11 <= var6 * var6)
                    {
                        for (int var12 = par4 - var7; var12 <= par4 + var7; ++var12)
                        {
                            int var13 = par1World.getBlockId(var8, var12, var9);

                            if (var13 == Block.dirt.blockID
                                    || var13 == Block.sand.blockID
                                    || var13 == Block.blockClay.blockID
                                    || var13 == Block.stone.blockID)
                            {
                                par1World.setBlock(var8, var12, var9, blockID, blockMetaData, 0);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}