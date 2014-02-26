package net.lomeli.lomlib.client.gui.element.pages;

import java.awt.Color;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.libs.Strings;

public class PageSmelting extends PageBase {
    private ItemStack input;

    public PageSmelting(GuiScreen gui, ItemStack input) {
        super(gui);
        this.input = input;
    }

    @Override
    public void draw() {
        super.draw();
        if (input != null) {
            smallFontRenderer.drawStringWithShadow(input.getDisplayName(),
                    x + (width / 2) - (smallFontRenderer.getStringWidth(input.getDisplayName()) / 2), y + 2,
                    Color.YELLOW.getRGB());
            super.draw();

            bindTexture(prop);
            gui.drawTexturedModalRect(x + 13, y + 38, 35, 236, 20, 20);

            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, input, x + 15, y + 40);

            ItemStack output = FurnaceRecipes.smelting().getSmeltingResult(input);
            if (output != null) {
                bindTexture(prop);
                gui.drawTexturedModalRect(x + 16, y + 60, 0, 243, 13, 13);
                gui.drawTexturedModalRect(x + 35, y + 40, 13, 240, 22, 16);
                gui.drawTexturedModalRect(x + 58, y + 38, 35, 236, 20, 20);
                itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, output, x + 60, y + 40);
            }else
                smallFontRenderer.drawSplitString(StatCollector.translateToLocal(Strings.INVALID_RECIPE), x, y + 95, width,
                        Color.BLACK.getRGB());
        }
    }
}
