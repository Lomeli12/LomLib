package net.lomeli.lomlib.util

import net.minecraft.block.Block
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import net.minecraft.world.IBlockAccess
import net.minecraftforge.fml.common.registry.GameRegistry

object BlockUtil {
    fun registerTile(tile: Class<out TileEntity>) {
        GameRegistry.registerTileEntity(tile, tile.name.toLowerCase())
    }

    fun getBlock(world: IBlockAccess, blockPos: BlockPos): Block = world.getBlockState(blockPos).getBlock()

    fun getBlock(world: IBlockAccess, x: Int, y: Int, z: Int): Block = getBlock(world, BlockPos(x, y, z))

    fun posEqual(pos1: BlockPos, pos2: BlockPos): Boolean = pos1.x === pos2.x && pos1.y === pos2.y && pos1.z === pos2.z

    fun getDistance(pos1: BlockPos, pos2: BlockPos): Float = MathHelper.sqrt_double(pos1.distanceSq(pos2.x as Double, pos2.y as Double, pos2.z as Double))

    fun getFaceFromDif(base: BlockPos, target: BlockPos): EnumFacing {
        val xDif = target.x - base.x
        val yDif = target.y - base.y
        val zDif = target.z - base.z
        if (xDif == 1)
            return EnumFacing.EAST
        if (xDif == -1)
            return EnumFacing.WEST
        if (zDif == 1)
            return EnumFacing.SOUTH
        if (zDif == -1)
            return EnumFacing.NORTH
        if (yDif == 1)
            return EnumFacing.UP
        if (yDif == -1)
            return EnumFacing.DOWN
        return EnumFacing.DOWN
    }
}