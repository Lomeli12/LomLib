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
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Lots of stuff copied from Tinker's
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {
    public static final ResourceLocation TEXTURE_MAP = TextureMap.locationBlocksTexture;
    public static final ResourceLocation texEnchant = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static final float magicNum = 0.0625F;

    /**
     * Allows adding custom layers to a entity renderer
     *
     * @param renderer
     * @param layer
     */
    public static void addLayerToRenderer(RendererLivingEntity renderer, LayerRenderer layer) {
        if (renderer != null && layer != null)
            renderer.addLayer(layer);
    }

    /**
     * Get an entity's renderer
     *
     * @param clazz
     * @return
     */
    public static RendererLivingEntity getEntityRenderer(Class<?> clazz) {
        return (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().entityRenderMap.get(clazz);
    }

    public static RenderPlayer getPlayerRenderer(String type) {
        return Minecraft.getMinecraft().getRenderManager().getSkinMap().get(type);
    }

    public static void setPlayerUseCount(EntityPlayer player, int count) {
        if (player != null) player.itemInUseCount = count;
    }

    public static RenderItem getNewRenderItems() {
        return new RenderItem(getTextureManager(), new ModelManager(Minecraft.getMinecraft().getTextureMapBlocks()));
    }

    /**
     * Translate to player head
     *
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
     *
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
     *
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
        WorldRenderer worldRenderer = Tessellator.getInstance().getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.color(var8, var9, var10, var7);
        worldRenderer.pos(par3, par2, z);
        worldRenderer.pos(par1, par2, z);
        worldRenderer.color(var12, var13, var14, var11);
        worldRenderer.pos(par1, par4, z);
        worldRenderer.pos(par3, par4, z);
        Tessellator.getInstance().draw();
        GlStateManager.shadeModel(0x1D00);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    /**
     * Renders the given texture tiled into a GUI
     */
    public static void renderTiledTextureAtlas(int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        putTiledTextureQuads(worldrenderer, x, y, width, height, depth, sprite);

        tessellator.draw();
    }

    public static void drawCutIcon(TextureAtlasSprite sprite, int x, int y, float zLevel, int width, int height, int cut) {
        Tessellator tess = Tessellator.getInstance();
        WorldRenderer renderer = tess.getWorldRenderer();
        renderer.begin(7, renderer.getVertexFormat());
        renderer.pos(x, y + height, zLevel).tex(sprite.getMinU(), sprite.getInterpolatedV(height)).endVertex();
        renderer.pos(x + width, y + height, zLevel).tex(sprite.getInterpolatedU(width), sprite.getInterpolatedV(height)).endVertex();
        renderer.pos(x + width, y + cut, zLevel).tex(sprite.getInterpolatedU(width), sprite.getInterpolatedV(cut)).endVertex();
        renderer.pos(x, y + cut, zLevel).tex(sprite.getMinU(), sprite.getInterpolatedV(cut)).endVertex();
        tess.draw();
    }

    public static void drawTexturedModalRect(int x, int y, float zLevel, int textureX, int textureY, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(7, DefaultVertexFormats.POSITION_TEX);
        renderer.pos((double) (x + 0), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        renderer.pos((double) (x + width), (double) (y + height), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + height) * f1)).endVertex();
        renderer.pos((double) (x + width), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + width) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        renderer.pos((double) (x + 0), (double) (y + 0), (double) zLevel).tex((double) ((float) (textureX + 0) * f), (double) ((float) (textureY + 0) * f1)).endVertex();
        tessellator.draw();
    }

    /**
     * Renders a fluid block, call from TESR. x/y/z is the rendering offset.
     *
     * @param fluid Fluid to render
     * @param pos   BlockPos where the Block is rendered. Used for brightness.
     * @param x     Rendering offset. TESR x parameter.
     * @param y     Rendering offset. TESR x parameter.
     * @param z     Rendering offset. TESR x parameter.
     * @param w     Width. 1 = full X-Width
     * @param h     Height. 1 = full Y-Height
     * @param d     Depth. 1 = full Z-Depth
     */
    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double w, double h, double d) {
        double wd = (1d - w) / 2d;
        double hd = (1d - h) / 2d;
        double dd = (1d - d) / 2d;

        renderFluidCuboid(fluid, pos, x, y, z, wd, hd, dd, 1d - wd, 1d - hd, 1d - dd);
    }

    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2) {
        int color = fluid.getFluid().getColor(fluid);
        renderFluidCuboid(fluid, pos, x, y, z, x1, y1, z1, x2, y2, z2, color);
    }

    /**
     * Renders block with offset x/y/z from x1/y1/z1 to x2/y2/z2 inside the block local coordinates, so from 0-1
     */
    public static void renderFluidCuboid(FluidStack fluid, BlockPos pos, double x, double y, double z, double x1, double y1, double z1, double x2, double y2, double z2, int color) {
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer renderer = tessellator.getWorldRenderer();
        renderer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        bindTexture(TextureMap.locationBlocksTexture);
        //RenderUtil.setColorRGBA(color);
        int brightness = FMLClientHandler.instance().getClient().theWorld.getCombinedLight(pos, fluid.getFluid().getLuminosity());

        pre(x, y, z);

        TextureAtlasSprite still = FMLClientHandler.instance().getClient().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getStill(fluid).toString());
        TextureAtlasSprite flowing = FMLClientHandler.instance().getClient().getTextureMapBlocks().getTextureExtry(fluid.getFluid().getFlowing(fluid).toString());

        // x/y/z2 - x/y/z1 is because we need the width/height/depth
        putTexturedQuad(renderer, still,   x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.DOWN,  color, brightness, false);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.NORTH, color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.EAST,  color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.SOUTH, color, brightness, true);
        putTexturedQuad(renderer, flowing, x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.WEST,  color, brightness, true);
        putTexturedQuad(renderer, still  , x1, y1, z1, x2-x1, y2-y1, z2-z1, EnumFacing.UP,    color, brightness, false);

        tessellator.draw();

        post();
    }

    public static void pre(double x, double y, double z) {
        GlStateManager.pushMatrix();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (Minecraft.isAmbientOcclusionEnabled())
            GL11.glShadeModel(GL11.GL_SMOOTH);
        else
            GL11.glShadeModel(GL11.GL_FLAT);

        GlStateManager.translate(x, y, z);
    }

    public static void post() {
        GlStateManager.disableBlend();
        RenderHelper.enableStandardItemLighting();
        GlStateManager.popMatrix();
    }

    public static void putTexturedQuad(WorldRenderer renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face,
                                       int color, int brightness, boolean flowing) {
        int l1 = brightness >> 0x10 & 0xFFFF;
        int l2 = brightness & 0xFFFF;

        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;

        putTexturedQuad(renderer, sprite, x, y, z, w, h, d, face, r, g, b, a, l1, l2, flowing);
    }

    // x and x+w has to be within [0,1], same for y/h and z/d
    public static void putTexturedQuad(WorldRenderer renderer, TextureAtlasSprite sprite, double x, double y, double z, double w, double h, double d, EnumFacing face,
                                       int r, int g, int b, int a, int light1, int light2, boolean flowing) {
        if(sprite == null)
            return;
        double minU;
        double maxU;
        double minV;
        double maxV;

        double size = 16f;
        if (flowing) size = 8f;

        double x1 = x;
        double x2 = x + w;
        double y1 = y;
        double y2 = y + h;
        double z1 = z;
        double z2 = z + d;

        double xt1 = x1 % 1d;
        double xt2 = xt1 + w;
        while (xt2 > 1f) xt2 -= 1f;
        double yt1 = y1 % 1d;
        double yt2 = yt1 + h;
        while (yt2 > 1f) yt2 -= 1f;
        double zt1 = z1 % 1d;
        double zt2 = zt1 + d;
        while (zt2 > 1f) zt2 -= 1f;

        // flowing stuff should start from the bottom, not from the start
        if (flowing) {
            double tmp = 1d - yt1;
            yt1 = 1d - yt2;
            yt2 = tmp;
        }

        switch (face) {
            case DOWN:
            case UP:
                minU = sprite.getInterpolatedU(xt1 * size);
                maxU = sprite.getInterpolatedU(xt2 * size);
                minV = sprite.getInterpolatedV(zt1 * size);
                maxV = sprite.getInterpolatedV(zt2 * size);
                break;
            case NORTH:
            case SOUTH:
                minU = sprite.getInterpolatedU(xt2 * size);
                maxU = sprite.getInterpolatedU(xt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            case WEST:
            case EAST:
                minU = sprite.getInterpolatedU(zt2 * size);
                maxU = sprite.getInterpolatedU(zt1 * size);
                minV = sprite.getInterpolatedV(yt1 * size);
                maxV = sprite.getInterpolatedV(yt2 * size);
                break;
            default:
                minU = sprite.getMinU();
                maxU = sprite.getMaxU();
                minV = sprite.getMinV();
                maxV = sprite.getMaxV();
        }

        switch (face) {
            case DOWN:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                break;
            case UP:
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case NORTH:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
            case SOUTH:
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case WEST:
                renderer.pos(x1, y1, z1).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y1, z2).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y2, z2).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x1, y2, z1).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                break;
            case EAST:
                renderer.pos(x2, y1, z1).color(r, g, b, a).tex(minU, maxV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z1).color(r, g, b, a).tex(minU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y2, z2).color(r, g, b, a).tex(maxU, minV).lightmap(light1, light2).endVertex();
                renderer.pos(x2, y1, z2).color(r, g, b, a).tex(maxU, maxV).lightmap(light1, light2).endVertex();
                break;
        }
    }

    public static void drawFluid(int x, int y, int width, int height, float depth, FluidStack fluidStack) {
        if (fluidStack == null || fluidStack.getFluid() == null)
            return;
        TextureAtlasSprite fluidSprite = FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite(fluidStack.getFluid().getStill(fluidStack).toString());
        applyColor(fluidStack.getFluid().getColor(fluidStack));
        renderTiledTextureAtlas(x, y, width, height, depth, fluidSprite);
    }

    /**
     * Adds a quad to the rendering pipeline. Call startDrawingQuads beforehand. You need to call draw() yourself.
     */
    public static void putTiledTextureQuads(WorldRenderer renderer, int x, int y, int width, int height, float depth, TextureAtlasSprite sprite) {
        float u1 = sprite.getMinU();
        float v1 = sprite.getMinV();

        // tile vertically
        do {
            int renderHeight = Math.min(sprite.getIconHeight(), height);
            height -= renderHeight;

            float v2 = sprite.getInterpolatedV((16f * renderHeight) / (float) sprite.getIconHeight());

            // we need to draw the quads per width too
            int x2 = x;
            int width2 = width;
            // tile horizontally
            do {
                int renderWidth = Math.min(sprite.getIconWidth(), width2);
                width2 -= renderWidth;

                float u2 = sprite.getInterpolatedU((16f * renderWidth) / (float) sprite.getIconWidth());

                renderer.pos(x2, y, depth).tex(u1, v1).endVertex();
                renderer.pos(x2, y + renderHeight, depth).tex(u1, v2).endVertex();
                renderer.pos(x2 + renderWidth, y + renderHeight, depth).tex(u2, v2).endVertex();
                renderer.pos(x2 + renderWidth, y, depth).tex(u2, v1).endVertex();

                x2 += renderWidth;
            } while (width2 > 0);

            y += renderHeight;
        } while (height > 0);
    }
}