package net.lomeli.lomlib.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
     * @param id       Block Id
     * @param material Block material
     * @param mod      The mod id
     * @param texture  The block's texture file (without .png)
     */
    public BlockGeneric(Material material, String mod, String texture) {
        super(material);
        this.modID = mod;
        this.blockTexture = texture;
        this.setBlockTextureName(this.modID + ":" + this.blockTexture);
    }
    
    @Override
    public BlockGeneric setBlockName(String name) {
    	super.setBlockName(this.modID.toLowerCase() + ":" + name);
    	return this;
    }
}
