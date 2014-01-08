package net.lomeli.lomlib.worldgen;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

public class WorldGenSubmergedOre {

    public WorldGenSubmergedOre(OreData data) {
        this.data = data;
    }

    private OreData data;

    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator, IChunkProvider chunkProvider) {
        int x, y, z;
        int numOre;
        int numCluster;
        numCluster = random.nextInt(data.clusterPerChunk);
        if (numCluster == 0 && data.clusterPerChunk != 0)
            numCluster = 1;

        for (int count = 0; count < numCluster; count++) {
            x = random.nextInt(16);
            z = random.nextInt(16);
            y = random.nextInt(data.maxHeight - data.minHeight);
            x = x + (16 * chunkX);
            z = z + (16 * chunkZ);
            y = y + data.minHeight;
            numOre = MathHelper.clamp_int(random.nextInt(data.maxCluster), data.minCluster, data.maxCluster);

            generateOre(world, random, x, y, z, data.oreType, data.meta, numOre);
        }
    }

    private void generateOre(World world, Random rand, int x, int y, int z, int blockID, int meta, int ntg) {
        int lx, ly, lz;
        lx = x;
        ly = y;
        lz = z;
        int id;
        id = world.getBlockId(lx, ly, lz);
        if (id != Block.sand.blockID || id != Block.dirt.blockID || id != Block.blockClay.blockID)
            return;

        for (int i = 0; i < ntg; i++) {

            id = world.getBlockId(lx, ly, lz);

            if (world.getBlockId(lx, ly + 1, lz) == Block.waterMoving.blockID || world.getBlockId(lx, ly + 1, lz) == Block.waterStill.blockID) {

                world.setBlock(lx, ly, lz, blockID, meta, 2);
                switch (rand.nextInt(3)) {
                case 0:
                    lx = lx + (rand.nextInt(4) - 2);
                    break;
                case 1:
                    ly = ly + (rand.nextInt(4) - 2);
                    break;
                case 2:
                    lz = lz + (rand.nextInt(4) - 2);
                    break;
                }
            }
        }
    }
}