package net.lomeli.lomlib.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;

/**
 * Meant to be a very generic block that is already ready for 
 * custom mod textures and not require a generic block class
 * @author Lomeli12
 */
public class BlockGeneric extends Block
{
	private String modID;
	private String blockTexture;
	
	/**
	 * 
	 * @param id Block Id
	 * @param material Block material
	 * @param mod The mod id
	 * @param texture The block's texture file (without .png)
	 */
	public BlockGeneric(int id, Material material, 
		String mod, String texture)
    {
	    super(id, material);
	    this.modID = mod;
	    this.blockTexture = texture;
    }
	
	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(modID + ":" + blockTexture);
    }

}
