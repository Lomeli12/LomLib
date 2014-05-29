package net.lomeli.lomlib.client.gui.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class PageText extends PageBase {

    private String[] pageText;

    public PageText(int x, int y, GuiScreen gui, String... text) {
        super(x, y, gui);
        this.pageText = text;
    }

    public PageText(GuiScreen gui, String... text) {
        super(gui);
        this.pageText = text;
    }

    @Override
    public void draw() {
        super.draw();
        int textHeight = y + 5;

        for (int i = 0; i < pageText.length; i++) {
            smallFontRenderer.drawSplitString(StatCollector.translateToLocal(pageText[i]), x, textHeight, width, Color.BLACK.getRGB());
            textHeight += smallFontRenderer.getStringHeight(StatCollector.translateToLocal(pageText[i]), width) + 20;
        }
    }

}
