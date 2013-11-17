package net.lomeli.lomlib.worldgen;

import java.util.Random;
import java.util.logging.Level;

import net.lomeli.lomlib.LomLib;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;

/**
 * This worldgen is based off of that of Vanilla clay, allowing you to generate
 * blocks in the water, such in ocean biomes, swamps, and rivers
 * 
 * @author Lomeli12
 */
public class WorldGenSubmergedOre extends WorldGenerator {
    /** The block ID for whatever block you want to generate. */
    private int blockID;

    /** The number of blocks to generate. */
    private int numberOfBlocks;

    /** The block's metadata. */
    private int blockMetaData;

    public WorldGenSubmergedOre(int itemID, int amount) {
        blockID = itemID;
        numberOfBlocks = amount;
        blockMetaData = 0;
    }

    public WorldGenSubmergedOre(int itemID, int metadata, int amount) {
        blockID = itemID;
        numberOfBlocks = amount;
        blockMetaData = metadata;
    }

    public WorldGenSubmergedOre(Block generatedBlock, int amount) {
        blockID = generatedBlock.blockID;
        numberOfBlocks = amount;
        blockMetaData = 0;
    }

    public WorldGenSubmergedOre(Block generatedBlock, int meta, int amount) {
        blockID = generatedBlock.blockID;
        numberOfBlocks = amount;
        blockMetaData = meta;
    }

    @Override
    public boolean generate(World par1World, Random par2Random, int par3, int par4, int par5) {
        if(par1World.getBlockMaterial(par3, par4, par5) != Material.water) {
            return false;
        }else {
            int l = par2Random.nextInt(this.numberOfBlocks) + 2;
            byte b0 = 1;

            for(int i1 = par3 - l; i1 <= par3 + l; ++i1) {
                for(int j1 = par5 - l; j1 <= par5 + l; ++j1) {
                    int k1 = i1 - par3;
                    int l1 = j1 - par5;

                    if(k1 * k1 + l1 * l1 <= l * l) {
                        for(int i2 = par4 - b0; i2 <= par4 + b0; ++i2) {
                            int j2 = par1World.getBlockId(i1, i2, j1);

                            if(j2 == Block.dirt.blockID || j2 == Block.sand.blockID || j2 == Block.blockClay.blockID
                                    || j2 == Block.stone.blockID) {
                                if(LomLib.debug)
                                    LomLib.logger.log(Level.INFO, "Generating block " + blockID + "at " + i1 + "," + i2 + ","
                                            + j1);
                                par1World.setBlock(i1, i2, j1, blockID, blockMetaData, 1);
                            }
                        }
                    }
                }
            }
            return true;
        }
    }
}