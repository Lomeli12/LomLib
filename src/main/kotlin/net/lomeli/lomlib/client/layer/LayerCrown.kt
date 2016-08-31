package net.lomeli.lomlib.client.layer

import net.lomeli.lomlib.util.client.RenderUtil
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EntityPlayer

import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.relauncher.Side

import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.client.layer.patreon.PatreonCrown
import net.lomeli.lomlib.lib.Config
import net.lomeli.lomlib.lib.ModLibs
import net.lomeli.lomlib.util.ObfUtil
import java.util.*

class LayerCrown : LayerRenderer<EntityPlayer> {
    var model: PatreonCrown

    init {
        model = PatreonCrown()
    }

    override fun doRenderLayer(player: EntityPlayer, f: Float, f1: Float, partialTicks: Float, f2: Float, f3: Float, f4: Float, f5: Float) {
        if (Config.crown && LomLib.proxy!!.list.isPatreon(player) && !player.isInvisible) {
            if (Loader.isModLoaded("morph")) {
                try {
                    val morphAPI = ObfUtil.invokeMethod<Any, Any?>(Class.forName("me.ichun.mods.morph.api.MorphApi"), null, arrayOf("getApiImpl"))
                    if (morphAPI != null) {
                        val isMorphed = (ObfUtil.invokeMethod<Any, Any>(Class.forName("me.ichun.mods.morph.api.IApi"), morphAPI, arrayOf("hasMorph"), player.name, Side.CLIENT) as Boolean?)!!
                        if (isMorphed)
                            return
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            val yaw = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partialTicks
            val yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partialTicks
            val pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partialTicks

            GlStateManager.pushMatrix()
            GlStateManager.disableCull()
            GlStateManager.rotate(yawOffset, 0f, -1f, 0f)
            GlStateManager.rotate(yaw - 270, 0f, 1f, 0f)
            GlStateManager.rotate(pitch, 0f, 0f, 1f)
            RenderUtil.translateToHeadLevel(player)

            RenderUtil.bindTexture(ModLibs.MOD_ID.toLowerCase(), "textures/Crown.png")
            RenderUtil.resetColor()
            model.render(0.0625f)
            RenderUtil.resetColor()
            GlStateManager.alphaFunc(0x204, 0.1f)
            GlStateManager.enableCull()
            GlStateManager.popMatrix()
        }
    }

    override fun shouldCombineTextures(): Boolean = false
}