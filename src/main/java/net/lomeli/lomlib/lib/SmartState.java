package net.lomeli.lomlib.lib;

import com.google.common.collect.ImmutableMap;

import java.util.Collection;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;

public class SmartState implements IBlockState {
    private IBlockAccess blockAccess;
    private BlockPos pos;
    private Block block;

    public SmartState(IBlockAccess blockAccess, BlockPos pos, Block block) {
        this.blockAccess = blockAccess;
        this.pos = pos;
        this.block = block;
    }

    @Override
    public Collection getPropertyNames() {
        return null;
    }

    @Override
    public Comparable getValue(IProperty property) {
        return null;
    }

    @Override
    public IBlockState withProperty(IProperty property, Comparable value) {
        return null;
    }

    @Override
    public IBlockState cycleProperty(IProperty property) {
        return null;
    }

    @Override
    public ImmutableMap getProperties() {
        return null;
    }

    @Override
    public Block getBlock() {
        return block;
    }

    public BlockPos getPos() {
        return pos;
    }

    public IBlockAccess getBlockAccess() {
        return blockAccess;
    }
}
