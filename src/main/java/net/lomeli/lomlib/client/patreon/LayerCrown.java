package net.lomeli.lomlib.client.patreon;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.lib.ModLibs;
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.RenderUtils;

public class LayerCrown implements LayerRenderer {
    public PatreonCrown model;

    public LayerCrown() {
        model = new PatreonCrown();
    }

    public void doRender(EntityPlayer player, float renderTick) {
        if (LomLib.crown && LomLib.proxy.list.isPatreon(player) && !player.isInvisible()) {
            if (Loader.isModLoaded("morph")) {
                try {
                    Object morphAPI = ObfUtil.invokeMethod(Class.forName("me.ichun.mods.morph.api.MorphApi"), null, new String[]{"getApiImpl"});
                    if (morphAPI != null) {
                        boolean isMorphed = (Boolean) ObfUtil.invokeMethod(Class.forName("me.ichun.mods.morph.api.IApi"), morphAPI, new String[]{"hasMorph"}, player.getName(), Side.CLIENT);
                        if (isMorphed)
                            return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            float renderYaw = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * renderTick - (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * renderTick);
            float renderPitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * renderTick;
            GlStateManager.pushMatrix();
            GlStateManager.disableCull();
            GlStateManager.rotate(renderYaw, 0f, 1f, 0f);
            GlStateManager.rotate(renderPitch, 1f, 0f, 0f);
            RenderUtils.translateToHeadLevel(player);

            RenderUtils.bindTexture(ModLibs.MOD_ID.toLowerCase(), "textures/Crown.png");
            RenderUtils.resetColor();
            model.render(0.0625f);
            RenderUtils.resetColor();
            GlStateManager.alphaFunc(0x204, 0.1F);
            GlStateManager.enableCull();
            GlStateManager.popMatrix();
        }
    }

    @Override
    public void doRenderLayer(EntityLivingBase entity, float f, float f1, float renderTick, float f2, float f3, float f4, float f5) {
        if (entity != null && entity instanceof EntityPlayer)
            doRender((EntityPlayer) entity, renderTick);
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
