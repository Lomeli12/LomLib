package net.lomeli.lomlib.block;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Meant to be a very generic block that is already ready for custom mod
 * textures and not require a generic block class
 * 
 * @author Lomeli12
 */
public class BlockGeneric extends Block {
    protected String modID;
    protected String blockTexture;

    /**
     * 
     * @param id
     *            Block Id
     * @param material
     *            Block material
     * @param mod
     *            The mod id
     * @param texture
     *            The block's texture file (without .png)
     */
    public BlockGeneric(Material material, String mod, String texture) {
        super(material);
        this.modID = mod;
        this.blockTexture = texture;
    }

    public String getBlockTexture() {
        return this.blockTexture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_149651_a(IIconRegister iconRegister) {
        field_149761_L = iconRegister.registerIcon(modID + ":" + blockTexture);
    }
}
