package net.lomeli.lomlib.util;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeDirection;

public class RotationHelper {

    public static enum BlockType {
        LOG, DISPENSER, BED, RAIL, RAIL_POWERED, RAIL_ASCENDING, RAIL_CORNER, TORCH, STAIR, CHEST, SIGNPOST, DOOR, LEVER, BUTTON, REDSTONE_REPEATER, TRAPDOOR, MUSHROOM_CAP, MUSHROOM_CAP_CORNER, MUSHROOM_CAP_SIDE, VINE, SKULL, ANVIL
    }

    private static final Map<BlockType, BiMap<Integer, ForgeDirection>> MAPPINGS = new HashMap<BlockType, BiMap<Integer, ForgeDirection>>();

    public static boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis, int mask, BlockType blockType) {
        int rotMeta = worldObj.getBlockMetadata(x, y, z);
        if(blockType == BlockType.DOOR && (rotMeta & 0x8) == 0x8)
            return false;
        
        int masked = rotMeta & ~mask;
        int meta = rotateMetadata(axis, blockType, rotMeta & mask);
        if(meta == -1)
            return false;
        
        worldObj.setBlockMetadataWithNotify(x, y, z, meta & mask | masked, 3);
        return true;
    }

    public static int rotateMetadata(ForgeDirection axis, BlockType blockType, int meta) {
        ForgeDirection orientation = metadataToDirection(blockType, meta);
        ForgeDirection rotated = orientation.getRotation(axis);
        return directionToMetadata(blockType, rotated);
    }

    public static ForgeDirection metadataToDirection(BlockType blockType, int meta) {
        if(blockType == BlockType.LEVER) {
            if(meta == 0x6)
                meta = 0x5;
            else if(meta == 0x0) 
                meta = 0x7;
        }

        if(MAPPINGS.containsKey(blockType)) {
            BiMap<Integer, ForgeDirection> biMap = MAPPINGS.get(blockType);
            if(biMap.containsKey(meta))
                return biMap.get(meta);
        }

        if(blockType == BlockType.TORCH)
            return ForgeDirection.getOrientation(6 - meta);
        if(blockType == BlockType.STAIR) 
            return ForgeDirection.getOrientation(5 - meta);
        if(blockType == BlockType.CHEST || blockType == BlockType.DISPENSER || blockType == BlockType.SKULL)
            return ForgeDirection.getOrientation(meta);
        if(blockType == BlockType.BUTTON) 
            return ForgeDirection.getOrientation(6 - meta);
        if(blockType == BlockType.TRAPDOOR)
            return ForgeDirection.getOrientation(meta + 2).getOpposite();

        return ForgeDirection.UNKNOWN;
    }

    public static int directionToMetadata(BlockType blockType, ForgeDirection direction) {
        if((blockType == BlockType.LOG || blockType == BlockType.ANVIL)
                && (direction.offsetX + direction.offsetY + direction.offsetZ) < 0)
            direction = direction.getOpposite();

        if(MAPPINGS.containsKey(blockType)) {
            BiMap<ForgeDirection, Integer> biMap = MAPPINGS.get(blockType).inverse();
            if(biMap.containsKey(direction)) {
                return biMap.get(direction);
            }
        }

        if(blockType == BlockType.TORCH) {
            if(direction.ordinal() >= 1)
                return 6 - direction.ordinal();
        }
        if(blockType == BlockType.STAIR) 
            return 5 - direction.ordinal();
        if(blockType == BlockType.CHEST || blockType == BlockType.DISPENSER || blockType == BlockType.SKULL)
            return direction.ordinal();
        if(blockType == BlockType.BUTTON) {
            if(direction.ordinal() >= 2)
                return 6 - direction.ordinal();
        }
        if(blockType == BlockType.TRAPDOOR)
            return direction.getOpposite().ordinal() - 2;

        return -1;
    }
    
    public static ForgeDirection getOrientationFromTile(TileEntity baseTile, TileEntity targetTile){
        int x = (targetTile.xCoord - baseTile.xCoord), y = (targetTile.yCoord - baseTile.yCoord), z = (targetTile.zCoord - baseTile.zCoord);
        if(x != 0){
            if(x == 1)
                return ForgeDirection.EAST;
            else
                return ForgeDirection.WEST;
        }else if(y != 0){
            if(y == 1)
                return ForgeDirection.UP;
            else
                return ForgeDirection.DOWN;
        }else if(z != 0){
            if(z == 1)
                return ForgeDirection.SOUTH;
            else
                return ForgeDirection.NORTH;
        }
        return ForgeDirection.UNKNOWN;
    }
}
