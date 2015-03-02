package net.lomeli.lomlib.client.models;

import net.minecraft.block.Block;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class LayerItemModel implements LayerRenderer {
    private final RendererLivingEntity entityRenderer;

    public LayerItemModel(RendererLivingEntity entityRenderer) {
        this.entityRenderer = entityRenderer;
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float f, float f1, float renderTick, float f2, float f3, float f4, float f5) {
        ItemStack stack = entity.getHeldItem();
        if (stack != null && stack.getItem() != null && RendererRegistry.instance().stackHasRenderer(stack)) {
            
            GlStateManager.pushMatrix();

            if (this.entityRenderer.getMainModel().isChild) {
                float f7 = 0.5F;
                GlStateManager.translate(0.0F, 0.625F, 0.0F);
                GlStateManager.rotate(-20.0F, -1.0F, 0.0F, 0.0F);
                GlStateManager.scale(f7, f7, f7);
            }

            ((ModelBiped) this.entityRenderer.getMainModel()).postRenderArm(0.0625F);
            GlStateManager.translate(-0.0625F, 0.4375F, 0.0625F);

            if (entity instanceof EntityPlayer && ((EntityPlayer) entity).fishEntity != null)
                stack = new ItemStack(Items.fishing_rod, 0);

            Item item = stack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item).getRenderType() == 2) {
                GlStateManager.translate(0.0F, 0.1875F, -0.3125F);
                GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
                GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
                float f8 = 0.375F;
                GlStateManager.scale(-f8, -f8, f8);
            }

            IItemRenderer renderer = RendererRegistry.instance().getRenderer(item);
            renderer.renderItem(RenderType.THIRD_PERSON, entity, stack, renderTick);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
