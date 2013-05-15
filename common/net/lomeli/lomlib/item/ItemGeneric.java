package net.lomeli.lomlib.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

/**
 * Meant to be a very generic item that is already ready for 
 * custom mod textures and not require a generic item class
 * @author Lomeli12
 */
public class ItemGeneric extends Item
{
	private String modID;
	private String itemTexture;
	
	/**
	 * @param id Item id
	 * @param Texture The mod id
	 * @param mod The item's texture file (without .png)
	 */
	public ItemGeneric(int id, String Texture, String mod)
	{
		super(id);
		this.modID = mod;
		this.itemTexture = Texture;
	}

	@Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        itemIcon = iconRegister.registerIcon(this.modID + ":" + this.itemTexture);
    }
}
