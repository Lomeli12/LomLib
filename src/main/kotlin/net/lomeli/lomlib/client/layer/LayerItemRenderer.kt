package net.lomeli.lomlib.client.layer

import net.lomeli.lomlib.client.render.item.IItemRenderer
import net.lomeli.lomlib.client.render.item.ISpecialRender
import net.lomeli.lomlib.client.render.item.RenderType
import net.minecraft.client.model.ModelBiped
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
class LayerItemRenderer(val entityRenderer : RenderPlayer) : LayerRenderer<EntityPlayer> {

    override fun shouldCombineTextures() : Boolean = true

    override fun doRenderLayer(player : EntityPlayer?, limbSwing : Float, limbSwingAmount : Float, partialTicks : Float, ageInTicks : Float, netHeadYaw : Float, headPitch : Float, scale : Float) {
        if (player != null) {
            val mainStack = player?.heldItemMainhand
            val offStack = player?.heldItemOffhand
            val mainSide = player?.primaryHand
            val offSide = player?.primaryHand?.opposite()
            if (mainStack != null || offStack != null) {
                GlStateManager.pushMatrix()

                if (this.entityRenderer.mainModel.isChild) {
                    GlStateManager.translate(0.0f, 0.625f, 0.0f)
                    GlStateManager.rotate(-20.0f, -1.0f, 0.0f, 0.0f)
                    GlStateManager.scale(0.5f, 0.5f, 0.5f)
                }
                val mainRenderer = getRenderer(mainStack)
                if (mainRenderer != null && mainRenderer.useRenderer(RenderType.THIRD_PERSON, EnumHand.MAIN_HAND, mainStack))
                    renderItem(player, mainSide!!, mainStack, mainRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale)
                val offRenderer = getRenderer(offStack)
                if (offRenderer != null && offRenderer.useRenderer(RenderType.THIRD_PERSON, EnumHand.OFF_HAND, offStack))
                    renderItem(player, offSide!!, offStack, offRenderer, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale)
                GlStateManager.popMatrix()
            }
        }
    }

    private fun renderItem(player : EntityPlayer, handSide : EnumHandSide, stack : ItemStack?, renderer : IItemRenderer, limbSwing : Float, limbSwingAmount : Float, partialTicks : Float, ageInTicks : Float, netHeadYaw : Float, headPitch : Float, scale : Float) {
        if (stack != null) {
            GlStateManager.pushMatrix()

            if (player.isSneaking) GlStateManager.translate(0.0f, 0.2f, 0.0f)

            ((entityRenderer.mainModel) as ModelBiped).postRenderArm(0.0625f, handSide)
            GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f)
            GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f)
            val flag = handSide == EnumHandSide.LEFT
            GlStateManager.translate((if (flag) -1 else 1).toFloat() / 16.0f, 0.125f, -0.625f)
            renderer.renderThirdPerson(player, handSide, stack, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale)
            GlStateManager.popMatrix()
        }

    }

    private fun getRenderer(item : ItemStack?) : IItemRenderer? {
        if (item != null && item.item is ISpecialRender) {
            val special = item.item as ISpecialRender
            if (special.hasSpecialRenderer(item))
                return special.getSpecialRenderer(item)
        }
        return null
    }
}