package net.lomeli.lomlib.client.render;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.util.Icon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IconConnected implements Icon {

    private int n;

    public final Icon[] icons = new Icon[5];

    public IconConnected(IconRegister register, String iconName, String modid) {
        this(register.registerIcon(modid + ":" + iconName + "_corners"), register.registerIcon(modid + ":" + iconName + "_vertical"), register.registerIcon(modid + ":" + iconName
                + "_horizontal"), register.registerIcon(modid + ":" + iconName), register.registerIcon(modid + ":" + iconName + "_anticorners"));
    }

    public IconConnected(Icon... icon) {
        for (int i = 0; i < icon.length; i++) {
            this.icons[i] = icon[i];
        }
    }

    public void setType(int i) {
        this.n = i;
    }

    public void resetType() {
        setType(0);
    }

    @Override
    public int getIconWidth() {
        return this.icons[this.n].getIconWidth();
    }

    @Override
    public int getIconHeight() {
        return this.icons[this.n].getIconHeight();
    }

    @Override
    public float getMinU() {
        return this.icons[this.n].getMinU();
    }

    @Override
    public float getMaxU() {
        return this.icons[this.n].getMaxU();
    }

    @Override
    public float getInterpolatedU(double d0) {
        float f = this.getMaxU() - this.getMinU();
        return this.getMinU() + f * ((float) d0 / 16F);
    }

    @Override
    public float getMinV() {
        return this.icons[this.n].getMinV();
    }

    @Override
    public float getMaxV() {
        return this.icons[this.n].getMaxV();
    }

    @Override
    public float getInterpolatedV(double d0) {
        float f = this.getMaxV() - this.getMinV();
        return this.getMinV() + f * ((float) d0 / 16F);
    }

    @Override
    public String getIconName() {
        return this.icons[this.n].getIconName();
    }

}
