package net.lomeli.lomlib.util;

import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    public static final ResourceLocation TEXTURE_MAP = TextureMap.locationBlocksTexture;
    public static final ResourceLocation texEnchant = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static final float magicNum = 0.0625F;

    /**
     * Allows adding custom layers to a entity renderer
     * @param renderer
     * @param layer
     */
    public static void addLayerToRenderer(RendererLivingEntity renderer, LayerRenderer layer) {
        if (renderer != null && layer != null)
            renderer.addLayer(layer);
    }

    /**
     * Get an entity's renderer
     * @param clazz
     * @return
     */
    public static RendererLivingEntity getEntityRenderer(Class<?> clazz) {
        return (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(clazz);
    }

    public static void setPlayerUseCount(EntityPlayer player, int count) {
        if (player != null) player.itemInUseCount = count;
    }

    public static RenderItem getNewRenderItems() {
        return new RenderItem(getTextureManager(), new ModelManager(Minecraft.getMinecraft().getTextureMapBlocks()));
    }

    /**
     * Translate to player head
     * @param player
     */
    public static void translateToHeadLevel(EntityPlayer player) {
        GlStateManager.translate(0, 0.08f - player.getEyeHeight() + (player.isSneaking() ? 0.1725 : 0) - (player.getCurrentArmor(3) != null ? 0.045f : 0f), 0);
    }

    public static void bindTexture(String modid, String texture) {
        ResourceLocation rl = new ResourceLocation(modid + ":" + texture);
        bindTexture(rl);
    }

    public static void bindTexture(ResourceLocation rl) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
    }

    public static void resetColor() {
        GlStateManager.disableBlend();
        GlStateManager.resetColor();
    }

    public static void applyColor(Color color) {
        applyColor(color, 1);
    }

    public static void applyColor(Color color, float alpha) {
        applyColor(color.getRGB(), alpha);
    }

    public static void applyColor(int rgb) {
        applyColor(rgb, 1);
    }

    public static void applyColor(float r, float g, float b) {
        applyColor(r, g, b, 1);
    }

    public static void applyColor(int rgb, float alpha) {
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;
        applyColor(r, g, b, alpha);
    }

    public static void applyColor(float r, float g, float b, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(0x302, 0x303);
        GlStateManager.color(r, g, b, alpha);
    }

    public static void setColorForFluidStack(FluidStack fluidstack) {
        if (fluidstack != null)
            applyColor(fluidstack.getFluid().getColor(fluidstack));
    }

    public static TextureManager getTextureManager() {
        return Minecraft.getMinecraft().getTextureManager();
    }

    /**
     * Renders the standard item tooltip
     * @param x
     * @param y
     * @param stack
     */
    public static void renderItemToolTip(int x, int y, ItemStack stack) {
        int color = 0x505000ff;
        int color2 = 0xf0100010;
        List<String> toolTipData = stack.getTooltip(Minecraft.getMinecraft().thePlayer, false);
        List<String> parsedTooltip = new ArrayList();
        boolean first = true;

        for (String s : toolTipData) {
            String s_ = s;
            if (!first)
                s_ = EnumChatFormatting.GRAY + s;
            parsedTooltip.add(s_);
            first = false;
        }
        renderTooltip(x, y, parsedTooltip, color, color2);
    }

    /**
     * Render a tooltip
     * @param x
     * @param y
     * @param tooltipData
     * @param color
     * @param color2
     */
    public static void renderTooltip(int x, int y, List<String> tooltipData, int color, int color2) {
        boolean lighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
        if (lighting)
            RenderHelper.disableStandardItemLighting();
        if (!tooltipData.isEmpty()) {
            int var5 = 0;
            int var6;
            int var7;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;
            for (var6 = 0; var6 < tooltipData.size(); ++var6) {
                var7 = fontRenderer.getStringWidth(tooltipData.get(var6));
                if (var7 > var5)
                    var5 = var7;
            }
            var6 = x + 12;
            var7 = y - 12;
            int var9 = 8;
            if (tooltipData.size() > 1)
                var9 += 2 + (tooltipData.size() - 1) * 10;
            float z = 300F;
            drawGradientRect(var6 - 3, var7 - 4, z, var6 + var5 + 3, var7 - 3, color2, color2);
            drawGradientRect(var6 - 3, var7 + var9 + 3, z, var6 + var5 + 3, var7 + var9 + 4, color2, color2);
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 + var9 + 3, color2, color2);
            drawGradientRect(var6 - 4, var7 - 3, z, var6 - 3, var7 + var9 + 3, color2, color2);
            drawGradientRect(var6 + var5 + 3, var7 - 3, z, var6 + var5 + 4, var7 + var9 + 3, color2, color2);
            int var12 = (color & 0xFFFFFF) >> 1 | color & -16777216;
            drawGradientRect(var6 - 3, var7 - 3 + 1, z, var6 - 3 + 1, var7 + var9 + 3 - 1, color, var12);
            drawGradientRect(var6 + var5 + 2, var7 - 3 + 1, z, var6 + var5 + 3, var7 + var9 + 3 - 1, color, var12);
            drawGradientRect(var6 - 3, var7 - 3, z, var6 + var5 + 3, var7 - 3 + 1, color, color);
            drawGradientRect(var6 - 3, var7 + var9 + 2, z, var6 + var5 + 3, var7 + var9 + 3, var12, var12);

            GlStateManager.disableDepth();
            for (int var13 = 0; var13 < tooltipData.size(); ++var13) {
                String var14 = tooltipData.get(var13);
                fontRenderer.drawString(var14, var6 + 1, var7 + 1, 0);
                fontRenderer.drawString(var14, var6, var7, -1);
                if (var13 == 0)
                    var7 += 2;
                var7 += 10;
            }
            GlStateManager.enableDepth();
        }
        if (!lighting)
            RenderHelper.disableStandardItemLighting();
        GlStateManager.resetColor();
    }

    public static void drawGradientRect(int par1, int par2, float z, int par3, int par4, int par5, int par6) {
        float var7 = (par5 >> 24 & 255) / 255F;
        float var8 = (par5 >> 16 & 255) / 255F;
        float var9 = (par5 >> 8 & 255) / 255F;
        float var10 = (par5 & 255) / 255F;
        float var11 = (par6 >> 24 & 255) / 255F;
        float var12 = (par6 >> 16 & 255) / 255F;
        float var13 = (par6 >> 8 & 255) / 255F;
        float var14 = (par6 & 255) / 255F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(0x302, 0x303);
        GlStateManager.shadeModel(0x1D01);
        WorldRenderer var15 = Tessellator.getInstance().getWorldRenderer();
        var15.startDrawingQuads();
        var15.setColorRGBA_F(var8, var9, var10, var7);
        var15.addVertex(par3, par2, z);
        var15.addVertex(par1, par2, z);
        var15.setColorRGBA_F(var12, var13, var14, var11);
        var15.addVertex(par1, par4, z);
        var15.addVertex(par3, par4, z);
        Tessellator.getInstance().draw();
        GlStateManager.shadeModel(0x1D00);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawFluid(Minecraft mc, FluidStack fluid, int x, int y, float zLevel, int width, int height, int maxCapacity) {
        if (fluid == null || fluid.getFluid() == null)
            return;
        TextureAtlasSprite sprite = fluid.getFluid().getIcon(fluid);
        mc.renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        RenderUtils.applyColor(fluid.getFluid().getColor(fluid));
        int fullX = width / 16;
        int fullY = height / 16;
        int lastX = width - fullX * 16;
        int lastY = height - fullY * 16;
        int level = fluid.amount * height / maxCapacity;
        int fullLvl = (height - level) / 16;
        int lastLvl = (height - level) - fullLvl * 16;
        for (int i = 0; i < fullX; i++) {
            for (int j = 0; j < fullY; j++) {
                if (j >= fullLvl)
                    drawCutIcon(sprite, x + i * 16, y + j * 16, zLevel, 16, 16, j == fullLvl ? lastLvl : 0);
            }
        }
        for (int i = 0; i < fullX; i++) {
            drawCutIcon(sprite, x + i * 16, y + fullY * 16, zLevel, 16, lastY, fullLvl == fullY ? lastLvl : 0);
        }
        for (int i = 0; i < fullY; i++) {
            if (i >= fullLvl)
                drawCutIcon(sprite, x + fullX * 16, y + i * 16, zLevel, lastX, 16, i == fullLvl ? lastLvl : 0);
        }
        drawCutIcon(sprite, x + fullX * 16, y + fullY * 16, zLevel, lastX, lastY, fullLvl == fullY ? lastLvl : 0);
    }

    public static void drawCutIcon(TextureAtlasSprite sprite, int x, int y, float zLevel, int width, int height, int cut) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer renderer = tess.getWorldRenderer();
        renderer.startDrawingQuads();
        renderer.addVertexWithUV(x, y + height, zLevel, sprite.getMinU(), sprite.getInterpolatedV(height));
        renderer.addVertexWithUV(x + width, y + height, zLevel, sprite.getInterpolatedU(width), sprite.getInterpolatedV(height));
        renderer.addVertexWithUV(x + width, y + cut, zLevel, sprite.getInterpolatedU(width), sprite.getInterpolatedV(cut));
        renderer.addVertexWithUV(x, y + cut, zLevel, sprite.getMinU(), sprite.getInterpolatedV(cut));
        tess.draw();
    }

    public static void drawTexturedModalRect(int x, int y, float zLevel, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.startDrawingQuads();
        worldrenderer.addVertexWithUV((double) (x + 0), (double) (y + height), (double) zLevel, (double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1));
        worldrenderer.addVertexWithUV((double) (x + width), (double) (y + height), (double) zLevel, (double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1));
        worldrenderer.addVertexWithUV((double) (x + width), (double) (y + 0), (double) zLevel, (double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1));
        worldrenderer.addVertexWithUV((double) (x + 0), (double) (y + 0), (double) zLevel, (double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1));
        tessellator.draw();
    }
}