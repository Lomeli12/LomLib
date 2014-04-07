package net.lomeli.lomlib.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * Meant to be a very generic item that is already ready for custom mod textures
 * and not require a generic item class
 *
 * @author Lomeli12
 */
public class ItemGeneric extends Item {
    protected String modID;
    protected String itemTexture;

    /**
     * @param id      Item id
     * @param mod     The mod id
     * @param Texture The item's texture file (without .png)
     */
    public ItemGeneric(int id, String mod, String Texture) {
        super();
        this.modID = mod.toLowerCase();
        this.itemTexture = Texture;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(this.modID + ":" + this.itemTexture);
    }
}
