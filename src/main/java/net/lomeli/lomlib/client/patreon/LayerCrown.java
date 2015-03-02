package net.lomeli.lomlib.client.patreon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.Loader;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.core.Strings;
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.RenderUtils;

public class LayerCrown implements LayerRenderer {
    public PatreonCrown model;

    public LayerCrown() {
        model = new PatreonCrown();
    }

    public void doRender(EntityPlayer player, float f, float f1, float renderTick, float f2, float f3, float f4, float f5) {
        if (LomLib.proxy.list.isPatreon(player)) {
            if (Loader.isModLoaded("morph")) {
                try {
                    Object obj = ObfUtil.invokeMethod(Class.forName("morph.api.Api"), null, new String[]{"getMorphEntity"}, player.getName(), true);
                    if (obj != null && !(obj instanceof EntityPlayer))
                        return;
                } catch (Exception e) {
                }
            }
            float renderYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * renderTick - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * renderTick);
            float renderPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * renderTick;
            GlStateManager.pushMatrix();
            GlStateManager.rotate(renderYaw, 0f, 1f, 0f);
            GlStateManager.rotate(renderPitch, 1f, 0f, 0f);
            RenderUtils.translateToHeadLevel(player);

            RenderUtils.bindTexture(Strings.MOD_ID.toLowerCase(), "textures/Crown.png");
            GlStateManager.resetColor();
            model.render(0.0625f);
            GlStateManager.resetColor();
            GlStateManager.alphaFunc(0x204, 0.1F);
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float f, float f1, float renderTick, float f2, float f3, float f4, float f5) {
        if (entity != null && entity instanceof EntityPlayer)
            doRender((EntityPlayer) entity, f, f1, renderTick, f2, f3, f4, f5);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
