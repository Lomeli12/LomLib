package net.lomeli.lomlib.render;

import net.lomeli.lomlib.libs.Incomplete;

import net.minecraft.util.Icon;
import net.minecraft.world.World;

/**
 * My implementation of Connected Textures. Not the best implementation (in
 * fact, probably the worst), but it works for me.
 * 
 * @author Lomeli12
 * 
 */
public class ConnectedTexturesHelper {
    /**
     * This is just a proof of concept function for setting the metadata. Is
     * useful for vertical connected textures.
     * <p>
     * Meta = 1 : middle blocks <br>
     * Meta = 2 : bottom most block <br>
     * Meta = 3 : top most block
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @param blockID
     * @param iconArray
     */
    public static void setBlockMetaForTextures(World world, int x, int y, int z, int blockID) {
        int meta = 0;

        if(world.getBlockId(x, y + 1, z) == blockID) {
            if(world.getBlockId(x, y - 1, z) == blockID)
                meta = 1;
            else
                meta = 3;
        }else if(world.getBlockId(x, y - 1, z) == blockID) {
            if(world.getBlockId(x, y + 1, z) == blockID)
                meta = 1;
            else
                meta = 2;
        }
        world.setBlockMetadataWithNotify(x, y, z, meta, 1);
        world.markBlockForUpdate(x, y, z);
        world.markBlockForRenderUpdate(x, y, z);
    }

    /**
     * Gets Block texture depending on side. Make sure to have an instance of
     * World for your block (see BlockGeneric for example) and to have a check
     * for null world objects. DOES NOT USE METADATA! Though I could use
     * metadata for this (add each digit of metadata to array, each int in array
     * corresponds to icon for side, etc), but I really don't want to do the
     * math for that, and while this can cause null pointers, in my opinion it's
     * much cleaner
     * 
     * @param world
     * @param iconArray
     * @param x
     * @param y
     * @param z
     * @param side
     * @param blockID
     * @return Corresponding icon
     */
    @Incomplete
    public static Icon getBlockTexture(World world, Icon[] iconArray, int x, int y, int z, int side, int blockID) {
        Icon faceIcon = null;
        if(iconArray.length != 16)
            return faceIcon;

        boolean up = false, down = false, left = false, right = false;
        int type = 0;

        if(side == 2) {
            if(world.getBlockId(x - 1, y, z) == blockID)
                left = true;
            if(world.getBlockId(x + 1, y, z) == blockID)
                right = true;
            if(world.getBlockId(x, y + 1, z) == blockID)
                up = true;
            if(world.getBlockId(x, y - 1, z) == blockID)
                down = true;
        }
        if(side == 3) {
            if(world.getBlockId(x + 1, y, z) == blockID)
                left = true;
            if(world.getBlockId(x - 1, y, z) == blockID)
                right = true;
            if(world.getBlockId(x, y - 1, z) == blockID)
                up = true;
            if(world.getBlockId(x, y + 1, z) == blockID)
                down = true;
        }
        if(side == 5) {
            if(world.getBlockId(x, y, z + 1) == blockID)
                left = true;
            if(world.getBlockId(x, y, z - 1) == blockID)
                right = true;
            if(world.getBlockId(x, y - 1, z) == blockID)
                up = true;
            if(world.getBlockId(x, y + 1, z) == blockID)
                down = true;
        }
        if(side == 6) {
            if(world.getBlockId(x, y, z + 1) == blockID)
                left = true;
            if(world.getBlockId(x, y, z - 1) == blockID)
                right = true;
            if(world.getBlockId(x, y - 1, z) == blockID)
                up = true;
            if(world.getBlockId(x, y + 1, z) == blockID)
                down = true;
        }

        if(!left && !right && !up && !down)
            type = 0;
        else
            type = 15;
        if(left) {
            type = 1;
            if(right) {
                type = 2;
                if(down)
                    type = 3;
            }else if(up) {
                type = 3;
            }else if(down) {

            }
        }
        faceIcon = iconArray[type];
        return faceIcon;
    }
    /*
     * public static void setMetaForTextures(World world, int x, int y, int z,
     * int blockID) { int meta = 0; int[] metaArray = new int[6];
     * 
     * for (int i = -1; i < 2; i++) { for (int j = -1; j < 2; i++) { for (int k
     * = -1; k < 2; i++) { if (i != 0 && j != 0 && k != 0) { if
     * (world.getBlockId(x + 1, y + j, z + k) == blockID) {
     * metaArray[getMetaArrayIndex(i, j, k)] = 1; } } } } }
     * 
     * meta += metaArray[0] + (metaArray[1] * 10) + (metaArray[2] * 100) +
     * (metaArray[3] * 1000) + (metaArray[4] * 10000) + (metaArray[5] * 100000);
     * 
     * world.setBlockMetadataWithNotify(x, y, z, meta, 1);
     * world.markBlockForUpdate(x, y, z); world.markBlockForRenderUpdate(x, y,
     * z); }
     * 
     * public static void setMetaForPaneTextures(World world, int x, int y, int
     * z, int blockID) {
     * 
     * }
     * 
     * public static Icon getSideIcon(Icon[] iconArray, int metaData) { Icon
     * sideIcon = null;
     * 
     * ArrayList<Integer> metaArray = new ArrayList<Integer>(); String str =
     * String.valueOf(metaData); for (int j = 0; j < str.length(); j++) {
     * metaArray.add((int) (str.charAt(j))); }
     * 
     * return sideIcon; }
     * 
     * public static int getMetaArrayIndex(int i, int j, int k) { int meta = 0;
     * int[] integerArray = new int[] { i, j, k };
     * 
     * for (int p = 0; p < integerArray.length; p++) { switch (integerArray[p])
     * { case -1: meta += 1; break; case 1: meta += 2; break; default: break; }
     * } meta--; return meta >= 0 ? meta : 0; }
     */
}
