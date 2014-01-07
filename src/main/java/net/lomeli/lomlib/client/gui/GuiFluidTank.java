package net.lomeli.lomlib.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.inventory.Container;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

@SideOnly(Side.CLIENT)
public class GuiFluidTank extends GuiContainerPlus {

    public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");

    public GuiFluidTank(Container par1Container) {
        super(par1Container);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) {
    }

    public void drawFluid(int x, int y, Fluid fluid, int width, int height) {
        if(fluid == null)
            return;
        field_146297_k.renderEngine.bindTexture(MC_BLOCK_SHEET);
        drawTiledTexture(x, y, fluid.getIcon(), width, height);
    }

    public void drawTiledTexture(int x, int y, IIcon icon, int width, int height) {

        int i = 0;
        int j = 0;

        int drawHeight = 0;
        int drawWidth = 0;

        for(i = 0; i < width; i += 16) {
            for(j = 0; j < height; j += 16) {
                drawWidth = (width - i) < 16 ? (width - i) : 16;
                drawHeight = (height - j) < 16 ? (height - j) : 16;
                drawScaledTexturedModelRectFromIcon(x + i, y + j, icon, drawWidth, drawHeight);
            }
        }
        GL11.glColor4f(1f, 1f, 1f, 1F);
    }

    public void drawScaledTexturedModelRectFromIcon(int x, int y, IIcon icon, int width, int height) {

        if(icon == null) {
            return;
        }
        double minU = icon.getMinU();
        double maxU = icon.getMaxU();
        double minV = icon.getMinV();
        double maxV = icon.getMaxV();

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x + 0, y + height, this.zLevel, minU, minV + (maxV - minV) * height / 16F);
        tessellator.addVertexWithUV(x + width, y + height, this.zLevel, minU + (maxU - minU) * width / 16F, minV + (maxV - minV)
                * height / 16F);
        tessellator.addVertexWithUV(x + width, y + 0, this.zLevel, minU + (maxU - minU) * width / 16F, minV);
        tessellator.addVertexWithUV(x + 0, y + 0, this.zLevel, minU, minV);
        tessellator.draw();
    }
}
