package net.lomeli.lomlib.client.gui.element;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.client.gui.GuiLomLib;
import net.lomeli.lomlib.codechicken.lib.vec.Rectangle4i;

public class TabBase extends ElementBase {

    public static final ResourceLocation DEFAULT_TEXTURE_LEFT = new ResourceLocation("lomlib:textures/elements/Tab_Left.png");
    public static final ResourceLocation DEFAULT_TEXTURE_RIGHT = new ResourceLocation("lomlib:textures/elements/Tab_Right.png");
    public static int tabExpandSpeed = 8;
    public boolean open;
    public int side = 1;
    public int backgroundColor = 0xffffff;
    public int currentShiftX = 0;
    public int currentShiftY = 0;
    public int minWidth = 22;
    public int currentWidth = minWidth;
    public int maxWidth = 124;
    public int minHeight = 22;
    public int currentHeight = minHeight;
    public int maxHeight = 22;

    public TabBase(GuiLomLib gui) {
        super(gui, 0, 0);
        texture = DEFAULT_TEXTURE_RIGHT;
    }

    public TabBase(GuiLomLib gui, int side) {
        super(gui, 0, 0);
        this.side = side;

        if (side == 0)
            texture = DEFAULT_TEXTURE_LEFT;
        else
            texture = DEFAULT_TEXTURE_RIGHT;
    }

    public TabBase setSide(int side) {
        this.side = side;

        if (side == 0)
            texture = DEFAULT_TEXTURE_LEFT;
        else
            texture = DEFAULT_TEXTURE_RIGHT;
        return this;
    }

    @Override
    public void update() {
        if (open && currentWidth < maxWidth)
            currentWidth += tabExpandSpeed;
        else if (!open && currentWidth > minWidth)
            currentWidth -= tabExpandSpeed;

        if (currentWidth > maxWidth)
            currentWidth = maxWidth;
        else if (currentWidth < minWidth)
            currentWidth = minWidth;

        if (open && currentHeight < maxHeight)
            currentHeight += tabExpandSpeed;
        else if (!open && currentHeight > minHeight)
            currentHeight -= tabExpandSpeed;

        if (currentHeight > maxHeight)
            currentHeight = maxHeight;
        else if (currentHeight < minHeight)
            currentHeight = minHeight;

        if (open && currentWidth == maxWidth && currentHeight == maxHeight)
            setFullyOpen();
    }

    public boolean intersectsWith(int mouseX, int mouseY, int shiftX, int shiftY) {
        if (side == 0) {
            if (mouseX <= shiftX && mouseX >= shiftX - currentWidth && mouseY >= shiftY && mouseY <= shiftY + currentHeight)
                return true;
        } else if (mouseX >= shiftX && mouseX <= shiftX + currentWidth && mouseY >= shiftY && mouseY <= shiftY + currentHeight)
            return true;

        return false;
    }

    protected void drawBackground() {
        float colorR = (backgroundColor >> 16 & 255) / 255.0F;
        float colorG = (backgroundColor >> 8 & 255) / 255.0F;
        float colorB = (backgroundColor & 255) / 255.0F;

        GL11.glColor4f(colorR, colorG, colorB, 1.0F);

        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        if (side == 0) {
            gui.drawTexturedModalRect(posX - currentWidth, posY + 4, 0, 256 - currentHeight + 4, 4, currentHeight - 4);
            gui.drawTexturedModalRect(posX - currentWidth + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            gui.drawTexturedModalRect(posX - currentWidth, posY, 0, 0, 4, 4);
            gui.drawTexturedModalRect(posX - currentWidth + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        } else {
            gui.drawTexturedModalRect(posX, posY, 0, 256 - currentHeight, 4, currentHeight);
            gui.drawTexturedModalRect(posX + 4, posY, 256 - currentWidth + 4, 0, currentWidth - 4, 4);
            gui.drawTexturedModalRect(posX, posY, 0, 0, 4, 4);
            gui.drawTexturedModalRect(posX + 4, posY + 4, 256 - currentWidth + 4, 256 - currentHeight + 4, currentWidth - 4, currentHeight - 4);
        }
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0F);
    }

    protected void drawTabIcon(String iconName) {
        int offsetX = 2;
        if (side == 0)
            offsetX = 4 - currentWidth;

        gui.drawIcon(iconName, posX + offsetX, posY + 3, 1);
    }

    public boolean isFullyOpened() {
        return currentWidth >= maxWidth;
    }

    public void setFullyOpen() {
        open = true;
        currentWidth = maxWidth;
        currentHeight = maxHeight;
    }

    @Override
    public void draw() {
    }

    public void toggleOpen() {
        if (open) {
            open = false;
            if (side == 0)
                TabTracker.setOpenedLeftTab(null);
            else
                TabTracker.setOpenedRightTab(null);
        } else {
            open = true;
            if (side == 0)
                TabTracker.setOpenedLeftTab(this.getClass());
            else
                TabTracker.setOpenedRightTab(this.getClass());
        }
    }

    public Rectangle4i getBounds() {
        if (side == 0)
            return new Rectangle4i(posX - currentWidth, posY, currentWidth, currentHeight);
        else
            return new Rectangle4i(posX, posY, currentWidth, currentHeight);
    }

}
