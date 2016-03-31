package net.lomeli.lomlib.client.models;

import net.minecraft.client.renderer.ItemMeshDefinition;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IMeshVariant extends IModelHolder {
    @SideOnly(Side.CLIENT)
    ItemMeshDefinition getCustomMesh();
}
