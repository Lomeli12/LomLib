package net.lomeli.lomlib.client.gui.element.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.StatCollector;

public class PageTitle extends PageBase {
    public String author, title;

    public PageTitle(GuiScreen gui, String author, String title) {
        super(gui);
        this.author = author;
        this.title = title;
    }

    public PageTitle(int x, int y, GuiScreen gui, String author, String title) {
        super(x, y, gui);
        this.author = author;
        this.title = title;
    }

    @Override
    public void draw() {
        super.draw();
        String translated = StatCollector.translateToLocal(title);
        smallFontRenderer.drawStringWithShadow(translated, x + (width / 2) - (smallFontRenderer.getStringWidth(translated) / 2),
                y + 35, Color.YELLOW.getRGB());
        smallFontRenderer.drawSplitString(author, x + (width / 2) - (smallFontRenderer.getStringWidth(author) / 2), y + 60,
                width, Color.BLACK.getRGB());
    }
}
