package net.lomeli.lomlib.fluid;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.Container;
import net.minecraft.util.Icon;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class GuiFluidTank extends GuiContainer {

    public GuiFluidTank(Container par1Container) {
        super(par1Container);
    }

    protected void displayGauge(int j, int k, int u, int v, int squaled, FluidStack liquid, ResourceLocation TEXTURE) {
        if (liquid == null) {
            return;
        }
        int start = 0;

        Icon liquidIcon = null;
        Fluid fluid = liquid.getFluid();
        if (fluid != null && fluid.getStillIcon() != null) {
            liquidIcon = FluidRender.getFluidTexture(fluid, false);
        }
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        if (liquidIcon != null) {
            while (true) {
                int x;

                if (squaled > 16) {
                    x = 16;
                    squaled -= 16;
                } else {
                    x = squaled;
                    squaled = 0;
                }

                drawTexturedModelRectFromIcon(j + u, k + v + 58 - x - start, liquidIcon, 16, 16 - (16 - x));
                start = start + 16;

                if (x == 0 || squaled == 0) {
                    break;
                }
            }
        }

        mc.renderEngine.bindTexture(TEXTURE);
        drawTexturedModalRect(j, k, u, v, 16, 60);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
    }
}
