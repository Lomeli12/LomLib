package net.lomeli.lomlib.client.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.ResourceUtil;
import net.lomeli.lomlib.client.gui.element.ElementBase;
import net.lomeli.lomlib.client.gui.element.IconRegistry;
import net.lomeli.lomlib.client.gui.element.TabBase;
import net.lomeli.lomlib.client.gui.element.TabTracker;

@SideOnly(Side.CLIENT)
public class GuiLomLib extends GuiContainer {

    protected ArrayList<TabBase> tabs = new ArrayList<TabBase>();
    protected ArrayList<ElementBase> elements = new ArrayList<ElementBase>();
    protected List<String> tooltip = new LinkedList<String>();
    protected ResourceLocation texture;

    protected int mouseX = 0, mouseY = 0;

    public GuiLomLib(Container par1Container) {
        super(par1Container);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.tabs.clear();
        this.elements.clear();
    }

    protected void drawGui() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceUtil.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
        drawElements();
        drawTabs();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int x, int y) {
        this.addTooltips(tooltip);
        this.drawTooltip(tooltip);
    }

    @SuppressWarnings("rawtypes")
    public void drawToolTipOverArea(int mouseX, int mouseY, int minX, int minY, int maxX, int maxY, List list, FontRenderer font) {
        if (list != null && font != null) {
            if ((mouseX >= minX && mouseX <= maxX) && (mouseY >= minY && mouseY <= maxY))
                this.drawHoveringText(list, mouseX, mouseY, font);
        }
    }

    public void drawTiledTexture(int x, int y, IIcon icon, int width, int height) {
        int i = 0;
        int j = 0;

        int drawHeight = 0;
        int drawWidth = 0;

        for (i = 0; i < width; i += 16) {
            for (j = 0; j < height; j += 16) {
                drawWidth = (width - i) < 16 ? (width - i) : 16;
                drawHeight = (height - j) < 16 ? (height - j) : 16;
                drawScaledTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
            }
        }
        GL11.glColor4f(1f, 1f, 1f, 1F);
    }

    public void drawSizedTexturedModalRect(int x, int y, int u, int v, int width, int height, float texW, float texH) {
        float texU = 1 / texW;
        float texV = 1 / texH;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, (u + 0) * texU, (v + height) * texV);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, (u + width) * texU, (v + height) * texV);
        tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, (u + width) * texU, (v + 0) * texV);
        tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, (u + 0) * texU, (v + 0) * texV);
        tessellator.draw();
    }

    public void drawIcon(IIcon icon, int x, int y, int spriteSheet) {
        if (spriteSheet == 0)
            ResourceUtil.setBlockTextureSheet();
        else
            ResourceUtil.setItemTextureSheet();

        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
        drawTexturedModelRectFromIcon(x, y, icon, 16, 16);
    }

    @Override
    protected void mouseClicked(int x, int y, int mouseButton) {
        super.mouseClicked(x, y, mouseButton);

        TabBase tab = getTabAtPosition(mouseX, mouseY);

        if (tab != null && !tab.handleMouseClicked(mouseX, mouseY, mouseButton)) {
            for (TabBase other : tabs) {
                if (other != tab && other.open && other.side == tab.side) {
                    other.toggleOpen();
                }
            }
            tab.toggleOpen();
        }
        ElementBase element = getElementAtPosition(mouseX, mouseY);

        if (element != null) {
            element.handleMouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void handleMouseInput() {
        int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        mouseX = x - guiLeft;
        mouseY = y - guiTop;

        super.handleMouseInput();
    }

    public void drawIcon(String iconName, int x, int y, int spriteSheet) {
        drawIcon(IconRegistry.getIcon(iconName), x, y, spriteSheet);
    }

    public void drawFluid(int x, int y, FluidStack fluid, int width, int height) {
        if (fluid == null || fluid.getFluid() == null)
            return;
        ResourceUtil.bindTexture(ResourceUtil.MC_BLOCK_SHEET);
        GL11.glColor3ub((byte) (fluid.getFluid().getColor() >> 16 & 0xFF), (byte) (fluid.getFluid().getColor() >> 8 & 0xFF), (byte) (fluid.getFluid().getColor() & 0xFF));
        drawTiledTexture(x, y, fluid.getFluid().getIcon(fluid), width, height);
    }

    public void drawTooltip(List<String> list) {
        drawTooltipHoveringText(list, mouseX, mouseY, fontRendererObj);
        tooltip.clear();
    }

    @SuppressWarnings("rawtypes")
    protected void drawTooltipHoveringText(List list, int x, int y, FontRenderer font) {
        if (list == null || list.isEmpty())
            return;

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        int k = 0;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();
            int l = font.getStringWidth(s);

            if (l > k) {
                k = l;
            }
        }
        int i1 = x + 12;
        int j1 = y - 12;
        int k1 = 8;

        if (list.size() > 1)
            k1 += 2 + (list.size() - 1) * 10;

        if (i1 + k > this.width)
            i1 -= 28 + k;

        if (j1 + k1 + 6 > this.height)
            j1 = this.height - k1 - 6;

        this.zLevel = 300.0F;
        itemRender.zLevel = 300.0F;
        int l1 = -267386864;
        this.drawGradientRect(i1 - 3, j1 - 4, i1 + k + 3, j1 - 3, l1, l1);
        this.drawGradientRect(i1 - 3, j1 + k1 + 3, i1 + k + 3, j1 + k1 + 4, l1, l1);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 - 4, j1 - 3, i1 - 3, j1 + k1 + 3, l1, l1);
        this.drawGradientRect(i1 + k + 3, j1 - 3, i1 + k + 4, j1 + k1 + 3, l1, l1);
        int i2 = 1347420415;
        int j2 = (i2 & 16711422) >> 1 | i2 & -16777216;
        this.drawGradientRect(i1 - 3, j1 - 3 + 1, i1 - 3 + 1, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 + k + 2, j1 - 3 + 1, i1 + k + 3, j1 + k1 + 3 - 1, i2, j2);
        this.drawGradientRect(i1 - 3, j1 - 3, i1 + k + 3, j1 - 3 + 1, i2, i2);
        this.drawGradientRect(i1 - 3, j1 + k1 + 2, i1 + k + 3, j1 + k1 + 3, j2, j2);

        for (int k2 = 0; k2 < list.size(); ++k2) {
            String s1 = (String) list.get(k2);
            font.drawStringWithShadow(s1, i1, j1, -1);

            if (k2 == 0)
                j1 += 2;

            j1 += 10;
        }
        this.zLevel = 0.0F;
        itemRender.zLevel = 0.0F;
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    public void drawScaledTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height) {
        if (icon == null)
            return;

        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, minU, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, minU + (maxU - minU) * width / 16F, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, minU + (maxU - minU) * width / 16F, minV);
        tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, minU, minV);
        tessellator.draw();
    }

    protected void drawElements() {
        for (ElementBase element : elements) {
            element.draw();
        }
    }

    protected void drawTabs() {
        int yPosRight = 4;
        int yPosLeft = 4;

        for (TabBase tab : tabs) {
            tab.update();
            if (!tab.isVisible())
                continue;

            if (tab.side == 0) {
                tab.draw(guiLeft, guiTop + yPosLeft);
                yPosLeft += tab.currentHeight;
            }else {
                tab.draw(guiLeft + xSize, guiTop + yPosRight);
                yPosRight += tab.currentHeight;
            }

        }
    }

    public TabBase addTab(TabBase tab) {
        tabs.add(tab);
        if (TabTracker.getOpenedLeftTab() != null && tab.getClass().equals(TabTracker.getOpenedLeftTab()))
            tab.setFullyOpen();
        else if (TabTracker.getOpenedRightTab() != null && tab.getClass().equals(TabTracker.getOpenedRightTab()))
            tab.setFullyOpen();

        return tab;
    }

    public ElementBase addElement(ElementBase element) {
        elements.add(element);
        return element;
    }

    public void addTooltips(List<String> tooltip) {
        TabBase tab = getTabAtPosition(mouseX, mouseY);

        if (tab != null)
            tab.addTooltip(tooltip);

        ElementBase element = getElementAtPosition(mouseX, mouseY);

        if (element != null)
            element.addTooltip(tooltip);
    }

    protected ElementBase getElementAtPosition(int mX, int mY) {
        for (ElementBase element : elements) {
            if (element.intersectsWith(mX, mY)) {
                return element;
            }
        }
        return null;
    }

    protected TabBase getTabAtPosition(int mX, int mY) {
        int xShift = 0;
        int yShift = 4;

        for (TabBase tab : tabs) {
            if (!tab.isVisible() || tab.side == 1)
                continue;

            tab.currentShiftX = xShift;
            tab.currentShiftY = yShift;
            if (tab.intersectsWith(mX, mY, xShift, yShift))
                return tab;

            yShift += tab.currentHeight;
        }

        xShift = xSize;
        yShift = 4;

        for (TabBase tab : tabs) {
            if (!tab.isVisible() || tab.side == 0)
                continue;

            tab.currentShiftX = xShift;
            tab.currentShiftY = yShift;
            if (tab.intersectsWith(mX, mY, xShift, yShift))
                return tab;

            yShift += tab.currentHeight;
        }
        return null;
    }

    public int getGuiLeft() {
        return guiLeft;
    }

    public int getGuiTop() {
        return guiTop;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public float getZLevel() {
        return zLevel;
    }
}
