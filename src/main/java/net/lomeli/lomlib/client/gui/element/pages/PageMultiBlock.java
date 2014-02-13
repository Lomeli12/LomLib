package net.lomeli.lomlib.client.gui.element.pages;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.lomeli.lomlib.libs.Strings;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class PageMultiBlock extends PageBase {
    private ItemStack[] blocks;
    private String name;
    private int xPos;
    private ResourceLocation grid = new ResourceLocation(Strings.MOD_ID.toLowerCase(), "textures/gui/layerDown.png");

    public PageMultiBlock(GuiScreen gui, String multiBlockName, ItemStack... items) {
        super(gui);
        blocks = items;
        name = multiBlockName;
    }

    @Override
    public void draw() {
        super.draw();
        String translated = StatCollector.translateToLocal(name);
        smallFontRenderer.drawStringWithShadow(translated, x + (width / 2) - (smallFontRenderer.getStringWidth(translated) / 2),
                y + 5, Color.YELLOW.getRGB());
        if(blocks != null && blocks.length >= 27) {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            bindTexture(grid);
            gui.drawTexturedModalRect(x + 25, y + 50, 40, 63, width, height);
            xPos = x + 15;
            layerOne();
            layerTwo();
            layerThree();
        }
    }

    private void layerOne() {
        if(blocks[0] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[0], xPos, y + 140);
        if(blocks[1] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[1], xPos + 16, y + 148);
        if(blocks[2] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[2], xPos + 32, y + 156);

        if(blocks[3] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[3], xPos + 16, y + 132);
        if(blocks[4] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[4], xPos + 32, y + 140);
        if(blocks[5] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[5], xPos + 48, y + 148);

        if(blocks[6] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[6], xPos + 32, y + 124);
        if(blocks[7] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[7], xPos + 48, y + 132);
        if(blocks[8] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[8], xPos + 64, y + 140);
    }

    private void layerTwo() {
        if(blocks[9] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[9], xPos, y + 90);
        if(blocks[10] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[10], xPos + 16, y + 98);
        if(blocks[11] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[11], xPos + 32, y + 106);

        if(blocks[12] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[12], xPos + 16, y + 82);
        if(blocks[13] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[13], xPos + 32, y + 90);
        if(blocks[14] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[14], xPos + 48, y + 98);

        if(blocks[15] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[15], xPos + 32, y + 74);
        if(blocks[16] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[16], xPos + 48, y + 82);
        if(blocks[17] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[17], xPos + 64, y + 90);
    }

    private void layerThree() {
        if(blocks[18] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[18], xPos, y + 40);
        if(blocks[19] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[19], xPos + 16, y + 48);
        if(blocks[20] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[20], xPos + 32, y + 56);

        if(blocks[21] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[21], xPos + 16, y + 32);
        if(blocks[22] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[22], xPos + 32, y + 40);
        if(blocks[23] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[23], xPos + 48, y + 48);

        if(blocks[24] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[24], xPos + 32, y + 24);
        if(blocks[25] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[25], xPos + 48, y + 32);
        if(blocks[26] != null)
            itemRenderer.renderItemAndEffectIntoGUI(largeFontRenderer, mc.renderEngine, blocks[26], xPos + 64, y + 40);
    }

}
