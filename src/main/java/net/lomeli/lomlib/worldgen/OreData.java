package net.lomeli.lomlib.worldgen;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class OreData {
    public int maxHeight, minHeight, maxCluster, minCluster, clusterPerChunk, meta;
    public Block oreType;

    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, Block block) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = block;
        this.meta = 0;
    }
    
    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, Block block, int meta) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = block;
        this.meta = meta;
    }
}
