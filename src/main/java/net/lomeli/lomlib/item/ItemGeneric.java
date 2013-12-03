package net.lomeli.lomlib.item;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.Item;

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
     * @param id
     *            Item id
     * @param mod
     *            The mod id
     * @param Texture
     *            The item's texture file (without .png)
     */
    public ItemGeneric(int id, String mod, String Texture) {
        super(id);
        this.modID = mod.toLowerCase();
        this.itemTexture = Texture;
    }

    @Override
    public void registerIcons(IconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(this.modID + ":" + this.itemTexture);
    }
}
