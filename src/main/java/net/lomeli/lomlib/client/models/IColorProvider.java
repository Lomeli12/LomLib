package net.lomeli.lomlib.client.models;

import net.minecraft.client.renderer.color.IItemColor;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IColorProvider {
    @SideOnly(Side.CLIENT)
    IItemColor getColor();
}
