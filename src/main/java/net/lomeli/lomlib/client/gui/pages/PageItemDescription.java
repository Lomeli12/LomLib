package net.lomeli.lomlib.client.gui.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class PageItemDescription extends PageBase {
    private ItemStack stack;
    private String[] pageText;

    public PageItemDescription(int x, int y, GuiScreen gui, ItemStack item, String... description) {
        super(x, y, gui);
        stack = item;
        pageText = description;
    }

    public PageItemDescription(GuiScreen gui, ItemStack item, String... description) {
        super(gui);
        stack = item;
        pageText = description;
    }

    @Override
    public void draw() {
        super.draw();
        bindTexture(prop);
        gui.drawTexturedModalRect(x - 2, y - 2, 35, 236, 20, 20);
        itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, stack, x, y);
        smallFontRenderer.drawStringWithShadow(stack.getDisplayName(), x + 20, y + 2, Color.YELLOW.getRGB());
        int textHeight = y + 20;

        for (int i = 0; i < pageText.length; i++) {
            smallFontRenderer.drawSplitString(StatCollector.translateToLocal(pageText[i]), x, textHeight, width, Color.BLACK.getRGB());
            textHeight += smallFontRenderer.getStringHeight(StatCollector.translateToLocal(pageText[i]), width) + 20;
        }
    }
}
