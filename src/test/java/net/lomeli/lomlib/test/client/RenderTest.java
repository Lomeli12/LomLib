package net.lomeli.lomlib.test.client;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.render.item.IItemRenderer;
import net.lomeli.lomlib.client.render.item.ItemRenderHandler;
import net.lomeli.lomlib.client.render.item.RenderType;
import net.lomeli.lomlib.util.client.RenderUtil;

/**
 * Testing using the gun model from Turtle Gun
 */
@SideOnly(Side.CLIENT)
public class RenderTest implements IItemRenderer {
    private ModelGun model = new ModelGun();

    @Override
    public void renderFirstPerson(EnumHand hand, EnumHandSide handSide, float partialTicks, float swingProgress, float equipProgress, ItemStack stack) {
        GlStateManager.pushMatrix();
        ItemRenderHandler.INSTANCE.transformEquipProgressFirstPerson(handSide, equipProgress);

        GlStateManager.pushMatrix();
        RenderUtil.INSTANCE.bindTexture(new ResourceLocation("lomlibtest:textures/guntexture.png"));

        GlStateManager.color(1f, 1f, 1f, 1f);
        if (handSide == EnumHandSide.RIGHT) {
            GlStateManager.translate(0.5f, 0.25f, -0.2f);
            GlStateManager.rotate(87.5F * 5, 0F, 1F, 0F);
            GlStateManager.rotate(90f, 0f, 0.5f, 0f);
            GlStateManager.rotate(180f, 1f, 0f, 0f);
        } else {
            GlStateManager.translate(-0.5f, 0.25f, -0.3f);
            GlStateManager.rotate(180f, 0f, 0f, 1f);
        }

        model.render(RenderUtil.INSTANCE.getMagicNum());
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    public void renderThirdPerson(EntityPlayer player, EnumHandSide side, ItemStack stack, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
    }

    @Override
    public boolean useRenderer(RenderType type, EnumHand hand, ItemStack stack) {
        return type == RenderType.FIRST_PERSON;
    }
}
