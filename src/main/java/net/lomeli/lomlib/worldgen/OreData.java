package net.lomeli.lomlib.worldgen;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class OreData {
    public int maxHeight, minHeight, maxCluster, minCluster, clusterPerChunk, oreType, meta;

    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, int oreType, int meta) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = oreType;
        this.meta = meta;
    }

    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, int oreType) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = oreType;
        this.meta = 0;
    }

    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, ItemStack block) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = block.itemID;
        this.meta = block.getItemDamage();
    }

    public OreData(int maxHeight, int minHeight, int maxCluster, int minCluster, int clusterPerChunk, Block block) {
        this.maxHeight = maxHeight;
        this.minHeight = minHeight;
        this.maxCluster = maxCluster;
        this.minCluster = minCluster;
        this.clusterPerChunk = clusterPerChunk;
        this.oreType = block.blockID;
        this.meta = 0;
    }
}
