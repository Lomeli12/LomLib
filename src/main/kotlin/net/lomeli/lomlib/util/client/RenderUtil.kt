package net.lomeli.lomlib.util.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.*
import net.minecraft.client.renderer.block.model.ModelManager
import net.minecraft.client.renderer.entity.RenderLivingBase
import net.minecraft.client.renderer.entity.RenderPlayer
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.lwjgl.opengl.GL11
import java.awt.Color
import java.util.*

/**
 * Lots of stuff copied from Tinker's
 */
@SideOnly(Side.CLIENT) object RenderUtil {
    val TEXTURE_MAP = TextureMap.LOCATION_BLOCKS_TEXTURE
    val texEnchant = ResourceLocation("textures/misc/enchanted_item_glint.png")
    val magicNum = 0.0625f
    val mc: Minecraft

    init {
        mc = FMLClientHandler.instance().client
    }

    /**
     * Allows adding custom layers to a entity renderer

     * @param renderer
     * *
     * @param layer
     */
    fun addLayerToRenderer(renderer: RenderLivingBase<out EntityLivingBase>, layer: LayerRenderer<out EntityLivingBase>) {
        if (renderer != null && layer != null)
            renderer.addLayer(layer as LayerRenderer<EntityLivingBase>)
    }

    /**
     * Get an entity's renderer

     * @param clazz
     * *
     * @return
     */

    fun getEntityRenderer(clazz: Class<out EntityLivingBase>): RenderLivingBase<EntityLivingBase> = mc.renderManager.entityRenderMap[clazz] as RenderLivingBase<EntityLivingBase>

    fun getPlayerRenderer(type: String): RenderPlayer? = mc.renderManager.skinMap[type]

    fun setPlayerUseCount(player: EntityPlayer?, count: Int) {
        if (player != null) player.activeItemStackUseCount = count
    }

    fun getNewRenderItems(): RenderItem = RenderItem(getTextureManager(), ModelManager(mc.textureMapBlocks), mc.itemColors)

    /**
     * Translate to player head

     * @param player
     */
    fun translateToHeadLevel(player: EntityPlayer) {
        var y = 0.08f - player.getEyeHeight()
        if (player.isSneaking)
            y += 0.1725f
        if (player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) != null)
            y -= 0.045f
        GlStateManager.translate(0.0f, y, 0.0f)
    }

    fun bindTexture(modid: String, texture: String) {
        val rl = ResourceLocation(modid + ":" + texture)
        bindTexture(rl)
    }

    fun bindTexture(rl: ResourceLocation) {
        mc.renderEngine.bindTexture(rl)
    }

    fun resetColor() {
        GlStateManager.disableBlend()
        GlStateManager.resetColor()
    }

    fun applyColor(color: Color) {
        applyColor(color, 1f)
    }

    fun applyColor(color: Color, alpha: Float) {
        applyColor(color.rgb, alpha)
    }

    fun applyColor(rgb: Int) {
        applyColor(rgb, 1f)
    }

    fun applyColor(r: Float, g: Float, b: Float) {
        applyColor(r, g, b, 1f)
    }

    fun applyColor(rgb: Int, alpha: Float) {
        val r = (rgb shr 16 and 255) / 255.0f
        val g = (rgb shr 8 and 255) / 255.0f
        val b = (rgb and 255) / 255.0f
        applyColor(r, g, b, alpha)
    }

    fun applyColor(r: Float, g: Float, b: Float, alpha: Float) {
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(0x302, 0x303)
        GlStateManager.color(r, g, b, alpha)
    }

    fun setColorForFluidStack(fluidstack: FluidStack?) {
        if (fluidstack != null)
            applyColor(fluidstack.fluid.getColor(fluidstack))
    }

    fun getTextureManager(): TextureManager = mc.textureManager

    /**
     * Renders the standard item tooltip

     * @param x
     * *
     * @param y
     * *
     * @param stack
     */
    fun renderItemToolTip(x: Int, y: Int, stack: ItemStack) {
        val color = 0x505000ff
        val color2 = 0xf0100010.toInt()
        val toolTipData = stack.getTooltip(mc.thePlayer, false)
        val parsedTooltip = ArrayList<String>()
        var first = true

        for (s in toolTipData) {
            var s_ = s
            if (!first)
                s_ = "${TextFormatting.GRAY}$s"
            parsedTooltip.add(s_)
            first = false
        }
        renderTooltip(x, y, parsedTooltip, color, color2)
    }

    /**
     * Render a tooltip

     * @param x
     * *
     * @param y
     * *
     * @param tooltipData
     * *
     * @param color
     * *
     * @param color2
     */
    fun renderTooltip(x: Int, y: Int, tooltipData: List<String>, color: Int, color2: Int) {
        val lighting = GL11.glGetBoolean(GL11.GL_LIGHTING)
        if (lighting)
            RenderHelper.disableStandardItemLighting()
        if (!tooltipData.isEmpty()) {
            var var5 = 0
            var var6: Int
            var var7: Int
            val fontRenderer = mc.fontRendererObj
            var6 = 0
            while (var6 < tooltipData.size) {
                var7 = fontRenderer.getStringWidth(tooltipData[var6])
                if (var7 > var5)
                    var5 = var7
                ++var6
            }
            var6 = x + 12
            var7 = y - 12
            var var9 = 8
            if (tooltipData.size > 1)
                var9 += 2 + (tooltipData.size - 1) * 10
            val z = 300f
            drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2)
            drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2)
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2)
            drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2)
            drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2)
            val var12 = color and 0xFFFFFF shr 1 or (color and -16777216)
            drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12)
            drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12)
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color)
            drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12)

            GlStateManager.disableDepth()
            for (var13 in 0..tooltipData.size - 1) {
                val var14 = tooltipData[var13]
                fontRenderer.drawString(var14, var6 + 1, var7 + 1, 0)
                fontRenderer.drawString(var14, var6, var7, -1)
                if (var13 == 0)
                    var7 += 2
                var7 += 10
            }
            GlStateManager.enableDepth()
        }
        if (!lighting)
            RenderHelper.disableStandardItemLighting()
        GlStateManager.resetColor()
    }

    fun drawGradientRect(par1: Int, par2: Int, z: Float, par3: Int, par4: Int, par5: Int, par6: Int) {
        val var7 = (par5 shr 24 and 255) / 255f
        val var8 = (par5 shr 16 and 255) / 255f
        val var9 = (par5 shr 8 and 255) / 255f
        val var10 = (par5 and 255) / 255f
        val var11 = (par6 shr 24 and 255) / 255f
        val var12 = (par6 shr 16 and 255) / 255f
        val var13 = (par6 shr 8 and 255) / 255f
        val var14 = (par6 and 255) / 255f
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.blendFunc(0x302, 0x303)
        GlStateManager.shadeModel(0x1D01)
        val vertexBuffer = Tessellator.getInstance().buffer
        vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX)
        vertexBuffer.color(var8, var9, var10, var7)
        vertexBuffer.pos(par3.toDouble(), par2.toDouble(), z.toDouble())
        vertexBuffer.pos(par1.toDouble(), par2.toDouble(), z.toDouble())
        vertexBuffer.color(var12, var13, var14, var11)
        vertexBuffer.pos(par1.toDouble(), par4.toDouble(), z.toDouble())
        vertexBuffer.pos(par3.toDouble(), par4.toDouble(), z.toDouble())
        Tessellator.getInstance().draw()
        GlStateManager.shadeModel(0x1D00)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
    }

    /**
     * Renders the given texture tiled into a GUI
     */
    fun renderTiledTextureAtlas(x: Int, y: Int, width: Int, height: Int, depth: Float, sprite: TextureAtlasSprite) {
        val tessellator = Tessellator.getInstance()
        val vertexBuffer = tessellator.buffer
        vertexBuffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        mc.renderEngine.bindTexture(TEXTURE_MAP)

        putTiledTextureQuads(vertexBuffer, x, y, width, height, depth, sprite)

        tessellator.draw()
    }

    fun drawCutIcon(sprite: TextureAtlasSprite, x: Int, y: Int, zLevel: Float, width: Int, height: Int, cut: Int) {
        val tess = Tessellator.getInstance()
        val renderer = tess.buffer
        renderer.begin(7, renderer.vertexFormat)
        renderer.pos(x.toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(sprite.minU.toDouble(), sprite.getInterpolatedV(height.toDouble()).toDouble()).endVertex()
        renderer.pos((x + width).toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(sprite.getInterpolatedU(width.toDouble()).toDouble(), sprite.getInterpolatedV(height.toDouble()).toDouble()).endVertex()
        renderer.pos((x + width).toDouble(), (y + cut).toDouble(), zLevel.toDouble()).tex(sprite.getInterpolatedU(width.toDouble()).toDouble(), sprite.getInterpolatedV(cut.toDouble()).toDouble()).endVertex()
        renderer.pos(x.toDouble(), (y + cut).toDouble(), zLevel.toDouble()).tex(sprite.minU.toDouble(), sprite.getInterpolatedV(cut.toDouble()).toDouble()).endVertex()
        tess.draw()
    }

    fun drawTexturedModalRect(x: Int, y: Int, zLevel: Float, textureX: Int, textureY: Int, width: Int, height: Int) {
        val f = 0.00390625f
        val f1 = 0.00390625f
        val tessellator = Tessellator.getInstance()
        val renderer = tessellator.buffer
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX)
        renderer.pos((x + 0).toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(((textureX + 0).toFloat() * f).toDouble(), ((textureY + height).toFloat() * f1).toDouble()).endVertex()
        renderer.pos((x + width).toDouble(), (y + height).toDouble(), zLevel.toDouble()).tex(((textureX + width).toFloat() * f).toDouble(), ((textureY + height).toFloat() * f1).toDouble()).endVertex()
        renderer.pos((x + width).toDouble(), (y + 0).toDouble(), zLevel.toDouble()).tex(((textureX + width).toFloat() * f).toDouble(), ((textureY + 0).toFloat() * f1).toDouble()).endVertex()
        renderer.pos((x + 0).toDouble(), (y + 0).toDouble(), zLevel.toDouble()).tex(((textureX + 0).toFloat() * f).toDouble(), ((textureY + 0).toFloat() * f1).toDouble()).endVertex()
        tessellator.draw()
    }

    /**
     * Renders a fluid block, call from TESR. x/y/z is the rendering offset.

     * @param fluid Fluid to render
     * *
     * @param pos   BlockPos where the Block is rendered. Used for brightness.
     * *
     * @param x     Rendering offset. TESR x parameter.
     * *
     * @param y     Rendering offset. TESR x parameter.
     * *
     * @param z     Rendering offset. TESR x parameter.
     * *
     * @param w     Width. 1 = full X-Width
     * *
     * @param h     Height. 1 = full Y-Height
     * *
     * @param d     Depth. 1 = full Z-Depth
     */
    fun renderFluidCuboid(fluid: FluidStack, pos: BlockPos, x: Double, y: Double, z: Double, w: Double, h: Double, d: Double) {
        val wd = (1.0 - w) / 2.0
        val hd = (1.0 - h) / 2.0
        val dd = (1.0 - d) / 2.0

        renderFluidCuboid(fluid, pos, x, y, z, wd, hd, dd, 1.0 - wd, 1.0 - hd, 1.0 - dd)
    }

    fun renderFluidCuboid(fluid: FluidStack, pos: BlockPos, x: Double, y: Double, z: Double, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double) {
        val color = fluid.fluid.getColor(fluid)
        renderFluidCuboid(fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color)
    }

    /**
     * Renders block with offset x/y/z from x1/y1/z1 to x2/y2/z2 inside the block local coordinates, so from 0-1
     */
    fun renderFluidCuboid(fluid: FluidStack, pos: BlockPos, x: Double, y: Double, z: Double, x1: Double, y1: Double, z1: Double, x2: Double, y2: Double, z2: Double, color: Int) {
        val tessellator = Tessellator.getInstance()
        val renderer = tessellator.buffer
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK)
        bindTexture(TEXTURE_MAP)
        //setColorRGBA(color);
        val brightness = mc.theWorld.getCombinedLight(pos, fluid.fluid.luminosity)

        pre(x, y, z)

        val still = mc.textureMapBlocks.getTextureExtry(fluid.fluid.getStill(fluid).toString())
        val flowing = mc.textureMapBlocks.getTextureExtry(fluid.fluid.getFlowing(fluid).toString())

        // x/y/z2 - x/y/z1 is because we need the width/height/depth
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.DOWN, color, brightness, false)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.NORTH, color, brightness, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.EAST, color, brightness, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.SOUTH, color, brightness, true)
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.WEST, color, brightness, true)
        putTexturedQuad(renderer, still, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, EnumFacing.UP, color, brightness, false)

        tessellator.draw()

        post()
    }

    fun pre(x: Double, y: Double, z: Double) {
        GlStateManager.pushMatrix()

        RenderHelper.disableStandardItemLighting()
        GlStateManager.enableBlend()
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)

        if (Minecraft.isAmbientOcclusionEnabled())
            GL11.glShadeModel(GL11.GL_SMOOTH)
        else
            GL11.glShadeModel(GL11.GL_FLAT)

        GlStateManager.translate(x, y, z)
    }

    fun post() {
        GlStateManager.disableBlend()
        RenderHelper.enableStandardItemLighting()
        GlStateManager.popMatrix()
    }

    fun putTexturedQuad(renderer: VertexBuffer, sprite: TextureAtlasSprite, x: Double, y: Double, z: Double, w: Double, h: Double, d: Double, face: EnumFacing,
                        color: Int, brightness: Int, flowing: Boolean) {
        val l1 = brightness shr 0x10 and 0xFFFF
        val l2 = brightness and 0xFFFF

        val a = color shr 24 and 0xFF
        val r = color shr 16 and 0xFF
        val g = color shr 8 and 0xFF
        val b = color and 0xFF

        putTexturedQuad(renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, l1, l2, flowing)
    }

    // x and x+w has to be within [0,1], same for y/h and z/d
    fun putTexturedQuad(renderer: VertexBuffer, sprite: TextureAtlasSprite?, x: Double, y: Double, z: Double, w: Double, h: Double, d: Double, face: EnumFacing,
                        r: Int, g: Int, b: Int, a: Int, light1: Int, light2: Int, flowing: Boolean) {
        if (sprite == null)
            return
        val minU: Double
        val maxU: Double
        val minV: Double
        val maxV: Double

        var size = 16.0
        if (flowing) size = 8.0

        val x1 = x
        val x2 = x + w
        val y1 = y
        val y2 = y + h
        val z1 = z
        val z2 = z + d

        val xt1 = x1 % 1.0
        var xt2 = xt1 + w
        while (xt2 > 1f) xt2 -= 1.0
        var yt1 = y1 % 1.0
        var yt2 = yt1 + h
        while (yt2 > 1f) yt2 -= 1.0
        val zt1 = z1 % 1.0
        var zt2 = zt1 + d
        while (zt2 > 1f) zt2 -= 1.0

        // flowing stuff should start from the bottom, not from the start
        if (flowing) {
            val tmp = 1.0 - yt1
            yt1 = 1.0 - yt2
            yt2 = tmp
        }

        when (face) {
            EnumFacing.DOWN, EnumFacing.UP -> {
                minU = sprite.getInterpolatedU(xt1 * size).toDouble()
                maxU = sprite.getInterpolatedU(xt2 * size).toDouble()
                minV = sprite.getInterpolatedV(zt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(zt2 * size).toDouble()
            }
            EnumFacing.NORTH, EnumFacing.SOUTH -> {
                minU = sprite.getInterpolatedU(xt2 * size).toDouble()
                maxU = sprite.getInterpolatedU(xt1 * size).toDouble()
                minV = sprite.getInterpolatedV(yt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(yt2 * size).toDouble()
            }
            EnumFacing.WEST, EnumFacing.EAST -> {
                minU = sprite.getInterpolatedU(zt2 * size).toDouble()
                maxU = sprite.getInterpolatedU(zt1 * size).toDouble()
                minV = sprite.getInterpolatedV(yt1 * size).toDouble()
                maxV = sprite.getInterpolatedV(yt2 * size).toDouble()
            }
            else -> {
                minU = sprite.minU.toDouble()
                maxU = sprite.maxU.toDouble()
                minV = sprite.minV.toDouble()
                maxV = sprite.maxV.toDouble()
            }
        }

        when (face) {
            EnumFacing.DOWN -> {
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
            }
            EnumFacing.UP -> {
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
            }
            EnumFacing.NORTH -> {
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
            }
            EnumFacing.SOUTH -> {
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
            }
            EnumFacing.WEST -> {
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
            }
            EnumFacing.EAST -> {
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex()
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex()
            }
        }
    }

    fun drawFluid(x: Int, y: Int, width: Int, height: Int, depth: Float, fluidStack: FluidStack?) {
        if (fluidStack == null || fluidStack.fluid == null)
            return
        val fluidSprite = mc.textureMapBlocks.getAtlasSprite(fluidStack.fluid.getStill(fluidStack).toString())
        applyColor(fluidStack.fluid.getColor(fluidStack))
        renderTiledTextureAtlas(x, y, width, height, depth, fluidSprite)
    }

    /**
     * Adds a quad to the rendering pipeline. Call startDrawingQuads beforehand. You need to call draw() yourself.
     */
    fun putTiledTextureQuads(renderer: VertexBuffer, x: Int, y: Int, width: Int, height: Int, depth: Float, sprite: TextureAtlasSprite) {
        var y = y
        var height = height
        val u1 = sprite.minU
        val v1 = sprite.minV

        // tile vertically
        do {
            val renderHeight = Math.min(sprite.iconHeight, height)
            height -= renderHeight

            val v2 = sprite.getInterpolatedV((16f * renderHeight / sprite.iconHeight.toFloat()).toDouble())

            // we need to draw the quads per width too
            var x2 = x
            var width2 = width
            // tile horizontally
            do {
                val renderWidth = Math.min(sprite.iconWidth, width2)
                width2 -= renderWidth

                val u2 = sprite.getInterpolatedU((16f * renderWidth / sprite.iconWidth.toFloat()).toDouble())

                renderer.pos(x2.toDouble(), y.toDouble(), depth.toDouble()).tex(u1.toDouble(), v1.toDouble()).endVertex()
                renderer.pos(x2.toDouble(), (y + renderHeight).toDouble(), depth.toDouble()).tex(u1.toDouble(), v2.toDouble()).endVertex()
                renderer.pos((x2 + renderWidth).toDouble(), (y + renderHeight).toDouble(), depth.toDouble()).tex(u2.toDouble(), v2.toDouble()).endVertex()
                renderer.pos((x2 + renderWidth).toDouble(), y.toDouble(), depth.toDouble()).tex(u2.toDouble(), v1.toDouble()).endVertex()

                x2 += renderWidth
            } while (width2 > 0)

            y += renderHeight
        } while (height > 0)
    }

    fun startGLScissor(x: Int, y: Int, width: Int, height: Int) {
        val res = ScaledResolution(mc)
        val scaleW = (mc.displayWidth / res.scaledWidth_double)
        val scaleH = (mc.displayHeight / res.scaledHeight_double)

        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(Math.floor(x as Double * scaleW).toInt(), Math.floor(mc.displayHeight as Double - (y as Double + height) * scaleH).toInt(), Math.floor((x as Double + width) * scaleW).toInt() - Math.floor(x as Double * scaleW).toInt(), Math.floor(mc.displayHeight as Double - y as Double * scaleH).toInt() - Math.floor(mc.displayHeight as Double - (y as Double + height) * scaleH).toInt()) //starts from lower left corner (minecraft starts from upper left)
    }

    fun stopGLScissor() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
    }
}