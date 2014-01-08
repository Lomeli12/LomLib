package net.lomeli.lomlib.client.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiContainerPlus extends GuiContainer {

    public GuiContainerPlus(Container par1Container) {
        super(par1Container);
    }

    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
    }

    @SuppressWarnings("rawtypes")
    public void drawToolTipOverArea(int mouseX, int mouseY, int minX, int minY, int maxX, int maxY, List list, FontRenderer font) {
        if(list != null && font != null) {
            if((mouseX >= minX && mouseX <= maxX) && (mouseY >= minY && mouseY <= maxY))
                this.drawHoveringText(list, mouseX, mouseY, font);
        }
    }

    @Override
    protected void func_146976_a(float f, int mouseX, int mouseY) {
        // TODO Auto-generated method stub
        
    }
}
