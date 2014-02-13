package net.lomeli.lomlib.client.capes;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.IImageBuffer;

/**
 * This class is used by DevCapes as an implementation of {@link IImageBuffer}
 * that allows capes to be HD
 * 
 * @author Jadar
 */
@SideOnly(Side.CLIENT)
public class HDImageBuffer implements IImageBuffer {
    @Override
    public BufferedImage parseUserSkin(final BufferedImage texture) {
        if(texture == null)
            return null;
        int imageWidth = texture.getWidth(null) <= 64 ? 64 : texture.getWidth(null);
        int imageHeight = texture.getHeight(null) <= 32 ? 32 : texture.getHeight(null);

        BufferedImage capeImage = new BufferedImage(imageWidth, imageHeight, 2);

        Graphics graphics = capeImage.getGraphics();
        graphics.drawImage(texture, 0, 0, null);
        graphics.dispose();

        return capeImage;
    }
}