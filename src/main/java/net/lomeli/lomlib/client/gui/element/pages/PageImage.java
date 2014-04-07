package net.lomeli.lomlib.client.gui.element.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PageImage extends PageBase {
    private ResourceLocation image;
    private String[] pageText;
    private int imaageWidth, imageHeight, imageX, imageY;

    public PageImage(GuiScreen gui, ResourceLocation image, int imageX, int imageY, int imageWidth, int imageHeight,
                     String... imageDescription) {
        super(gui);
        this.image = image;
        pageText = imageDescription;
        this.imageHeight = imageHeight;
        this.imaageWidth = imageWidth;
        this.imageX = imageX;
        this.imageY = imageY;
    }

    @Override
    public void draw() {
        super.draw();
        if (image != null) {
            bindTexture(image);
            gui.drawTexturedModalRect(x, y, imageX, imageY, imaageWidth, imageHeight);
            int textHeight = y + imageHeight;
            if (pageText != null) {
                for (int i = 0; i < pageText.length; i++) {
                    if (pageText[i] != "") {
                        smallFontRenderer.drawSplitString(StatCollector.translateToLocal(pageText[i]), x, textHeight, width,
                                Color.BLACK.getRGB());
                        textHeight += smallFontRenderer.getStringHeight(StatCollector.translateToLocal(pageText[i]), width) + 20;
                    }
                }
            }
        }
    }
}
