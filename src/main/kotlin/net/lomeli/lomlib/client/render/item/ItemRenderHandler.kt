package net.lomeli.lomlib.client.render.item

import net.lomeli.lomlib.util.MathUtil
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraft.util.math.MathHelper
import net.minecraftforge.client.event.RenderSpecificHandEvent
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
object ItemRenderHandler {

    /**
     * Handles rendering items in hand in first person
     */
    @SubscribeEvent
    fun renderHandEvent(event : RenderSpecificHandEvent) {
        val player = FMLClientHandler.instance().clientPlayerEntity
        if (event.itemStack != null && event.itemStack?.item is ISpecialRender) {
            val special = event.itemStack?.item as ISpecialRender
            if (special.hasSpecialRenderer(event.itemStack)) {
                val renderer = special.getSpecialRenderer(event.itemStack)
                if (renderer.useRenderer(RenderType.FIRST_PERSON, event.hand, event.itemStack)) {
                    event.isCanceled = true
                    val enumhandside = if (event.hand === EnumHand.MAIN_HAND) player.primaryHand else player.primaryHand.opposite()
                    renderer.renderFirstPerson(event.hand, enumhandside, event.partialTicks, event.swingProgress, event.equipProgress, event.itemStack)
                }
            }
        }
    }

    /**
     *  The following is copied from williewillus fork of Botania you can find it here:
     *  https://github.com/williewillus/Botania/blob/master/src/main/java/vazkii/botania/client/core/handler/RenderLexicon.java
     **/
    // Copy - ItemRenderer.transformSideFirstPerson
    // Arg - Side, EquipProgress
    fun transformEquipProgressFirstPerson(handSide : EnumHandSide, equipProgress : Float) {
        val i = if (handSide === EnumHandSide.RIGHT) 1 else -1
        GlStateManager.translate(i.toFloat() * 0.56f, -0.52f + equipProgress * -0.6f, -0.72f)
    }

    // Copy with modification - ItemRenderer.transformFirstPerson
    // Arg - Side, SwingProgress
    fun transformSwingProgressFirstPerson(handSide : EnumHandSide, swingProgress : Float) {
        val i = if (handSide === EnumHandSide.RIGHT) 1 else -1
        GlStateManager.translate(if (handSide === EnumHandSide.RIGHT) 0.5f else 0.3f, -0.25f, 0.2f)
        GlStateManager.rotate(90f, 0f, 1f, 0f)
        GlStateManager.rotate(12f, 0f, 0f, -1f)
        val f = MathUtil.sin(swingProgress * swingProgress * Math.PI).toFloat()
        GlStateManager.rotate(i.toFloat() * (45.0f + f * -20.0f), 0.0f, 1.0f, 0.0f)
        val f1 = MathUtil.sin(MathHelper.sqrt_float(swingProgress) * Math.PI).toFloat()
        GlStateManager.rotate(i.toFloat() * f1 * -20.0f, 0.0f, 0.0f, 1.0f)
        GlStateManager.rotate(f1 * -80.0f, 1.0f, 0.0f, 0.0f)
        GlStateManager.rotate(i.toFloat() * -45.0f, 0.0f, 1.0f, 0.0f)
    }
}