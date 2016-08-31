package net.lomeli.lomlib.client.models

import net.minecraft.client.renderer.ItemMeshDefinition
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

interface IMeshVariant : IModelHolder{
    @SideOnly(Side.CLIENT) fun getCustomMesh(): ItemMeshDefinition
}