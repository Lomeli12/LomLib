package net.lomeli.lomlib.util;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BlockUtil {
    public static void registerTile(Class<? extends TileEntity> tile) {
        GameRegistry.registerTileEntity(tile, tile.getName().toLowerCase());
    }

    public static Block getBlock(IBlockAccess world, BlockPos blockPos) {
        return world.getBlockState(blockPos).getBlock();
    }

    public static Block getBlock(IBlockAccess world, int x, int y, int z) {
        return getBlock(world, new BlockPos(x, y, z));
    }

    public static boolean posEqual(BlockPos pos1, BlockPos pos2) {
        return pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ();
    }

    public static float getDistance(BlockPos pos1, BlockPos pos2) {
        return net.minecraft.util.math.MathHelper.sqrt_double(pos1.distanceSq(pos2.getX(), pos2.getY(), pos2.getZ()));
    }

    public static EnumFacing getFaceFromDif(BlockPos base, BlockPos target) {
        int xDif = target.getX() - base.getX();
        int yDif = target.getY() - base.getY();
        int zDif = target.getZ() - base.getZ();
        if (xDif == 1)
            return EnumFacing.EAST;
        if (xDif == -1)
            return EnumFacing.WEST;
        if (zDif == 1)
            return EnumFacing.SOUTH;
        if (zDif == -1)
            return EnumFacing.NORTH;
        if (yDif == 1)
            return EnumFacing.UP;
        if (yDif == -1)
            return EnumFacing.DOWN;
        return EnumFacing.DOWN;
    }
}
