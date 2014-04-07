package net.lomeli.lomlib.client.gui.element;

import org.lwjgl.opengl.GL11;

import java.util.List;

import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.client.gui.GuiContainerPlus;
import net.lomeli.lomlib.util.ToolTipUtil;

public class TabInfo extends TabBase {

    int headerColor = 14797103;
    int subheaderColor = 11186104;
    int textColor = 16777215;
    String myInfo;

    public TabInfo(GuiContainerPlus gui, String info) {
        super(gui, 0);
        this.backgroundColor = 5592405;
        this.maxHeight += 4 + ToolTipUtil.getSplitStringHeight(elementFontRenderer, info, this.maxWidth);
        this.myInfo = info;
    }

    public TabInfo(GuiContainerPlus gui, String info, int extraLines) {
        super(gui, 0);
        this.backgroundColor = 5592405;
        this.maxHeight += 4 + elementFontRenderer.FONT_HEIGHT * extraLines
                + ToolTipUtil.getSplitStringHeight(elementFontRenderer, info, this.maxWidth);
        this.myInfo = info;
    }

    @Override
    public void draw() {
        drawBackground();
        drawTabIcon("Icon_Info");
        if (!isFullyOpened())
            return;

        int xPos1 = 0, xPos2;

        if (side == 0) {
            xPos1 = this.posX - this.currentWidth + 22;
            xPos2 = this.posX + 8 - this.currentWidth;
        } else {
            xPos1 = this.posX + this.currentWidth - 105;
            xPos2 = this.posX + 128 - this.currentWidth;
        }

        elementFontRenderer.drawStringWithShadow(StatCollector.translateToLocal("tab.lomlib.info"), xPos1, this.posY + 6,
                this.headerColor);
        elementFontRenderer.drawSplitString(this.myInfo, xPos2, this.posY + 20, this.maxWidth - 8, this.textColor);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void addTooltip(List<String> list) {
        if (!isFullyOpened())
            list.add(StatCollector.translateToLocal("tab.lomlib.info"));
    }
}
