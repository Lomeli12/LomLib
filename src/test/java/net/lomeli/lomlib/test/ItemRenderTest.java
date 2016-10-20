package net.lomeli.lomlib.test;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.render.item.IItemRenderer;
import net.lomeli.lomlib.client.render.item.ISpecialRender;
import net.lomeli.lomlib.test.client.RenderTest;

public class ItemRenderTest extends Item implements ISpecialRender{

    @SideOnly(Side.CLIENT)
    private IItemRenderer renderer;

    public ItemRenderTest() {
        super();
        this.setCreativeTab(CreativeTabs.MISC);
        this.setUnlocalizedName("renderTester");
    }

    @Override
    public Item setUnlocalizedName(String unlocalizedName) {
        return super.setUnlocalizedName("lomlibtest." + unlocalizedName);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean hasSpecialRenderer(@Nullable ItemStack stack) {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @NotNull
    @Override
    public IItemRenderer getSpecialRenderer(@Nullable ItemStack stack) {
        if(renderer == null) renderer = new RenderTest();
        return renderer;
    }
}
