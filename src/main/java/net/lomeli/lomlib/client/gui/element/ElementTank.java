package net.lomeli.lomlib.client.gui.element;

import java.util.List;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import net.minecraftforge.fluids.FluidTank;

import net.lomeli.lomlib.client.ResourceUtil;
import net.lomeli.lomlib.client.gui.GuiLomLib;

public class ElementTank extends ElementBase {

    private static final ResourceLocation defaultTexture = new ResourceLocation("lomlib:textures/elements/FluidTank.png");
    protected FluidTank tank;
    protected int gaugeType;

    public ElementTank(GuiLomLib gui, int posX, int posY, FluidTank tank) {
        super(gui, posX, posY);
        this.setSize(16, 60);
        this.texture = defaultTexture;
        this.texW = 64;
        this.texH = 64;
        this.tank = tank;
    }

    public ElementTank(GuiLomLib gui, int posX, int posY, FluidTank tank, String texture) {
        super(gui, posX, posY);
        this.setSize(16, 60);
        this.texture = new ResourceLocation(texture);
        this.texW = 64;
        this.texH = 64;
        this.tank = tank;
    }

    public ElementTank setGauge(int gaugeType) {
        this.gaugeType = gaugeType;
        return this;
    }

    @Override
    public boolean handleMouseClicked(int x, int y, int mouseButton) {
        return false;
    }

    @Override
    public void draw() {
        ResourceUtil.bindTexture(this.texture);
        drawTexturedModalRect(posX, posY, 0, 0, sizeX + 2, sizeY + 2);
        int amount = ((tank.getFluidAmount() * this.sizeY) / tank.getCapacity());
        this.gui.drawFluid(this.posX + 1, this.posY + 1 + this.sizeY - amount, this.tank.getFluid(), this.sizeX, amount);
        ResourceUtil.bindTexture(this.texture);
        drawTexturedModalRect(posX, posY, 32 + gaugeType * 16, 1, sizeX, sizeY);
    }

    @Override
    public void addTooltip(List<String> list) {
        if (tank.getFluid() != null && tank.getFluidAmount() > 0) {
            list.add(StatCollector.translateToLocal(tank.getFluid().getFluid().getUnlocalizedName()));
            list.add("" + tank.getFluidAmount() + " / " + tank.getCapacity() + " mB");
        } else
            list.add(StatCollector.translateToLocal("element.lomlib.tankEmpty"));
    }
}
