package net.lomeli.lomlib.client.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiButtonNextPage extends GuiButton {
    private final boolean nextPage;

    public GuiButtonNextPage(int par1, int par2, int par3, boolean par4) {
        super(par1, par2, par3, "");
        this.nextPage = par4;
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (this.enabled) {
            boolean flag = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/book.png"));

            int k = 0;
            int l = 192;

            if (flag)
                k += 23;

            if (!this.nextPage)
                l += 13;

            this.drawTexturedModalRect(this.xPosition, this.yPosition, k, l, 23, 13);
        }
    }
}
