package net.lomeli.lomlib.client;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.render.RenderEntityBlock;
import net.lomeli.lomlib.entity.EntityBlock;

@SideOnly(Side.CLIENT)
public class ResourceUtil {
    public static TextureMap blockIconRegister;
    public static TextureMap itemIconRegister;
    public static final ResourceLocation MC_BLOCK_SHEET = new ResourceLocation("textures/atlas/blocks.png");
    public static final ResourceLocation MC_ITEM_SHEET = new ResourceLocation("textures/atlas/items.png");
    public static final ResourceLocation MC_FONT_DEFAULT = new ResourceLocation("textures/font/ascii.png");
    public static final ResourceLocation MC_FONT_ALTERNATE = new ResourceLocation("textures/font/ascii_sga.png");
    private static File mods;
    private static File assets;

    @SideOnly(Side.CLIENT)
    public static void initResourceUtil() {
        RenderingRegistry.registerEntityRenderingHandler(EntityBlock.class, RenderEntityBlock.INSTANCE);
        mods = new File(Minecraft.getMinecraft().mcDataDir, "mods");
        assets = new File(Minecraft.getMinecraft().mcDataDir, "assets");
    }

    public static ResourceLocation getResourceUtil(String modid, String resource) {
        return new ResourceLocation(modid, resource);
    }

    public static ResourceLocation getGuiResource(String modid, String gui) {
        return getResourceUtil(modid, "textures/gui/" + gui);
    }

    public static ResourceLocation getEntityTexture(String modid, String texture) {
        return getResourceUtil(modid, "textures/entities/" + texture);
    }

    public static ResourceLocation getIcon(String modid, String icon) {
        return getResourceUtil(modid, "textures/icons/" + icon);
    }

    public static ResourceLocation getModelTexture(String modid, String texture) {
        return getResourceUtil(modid, "textures/model/" + texture);
    }

    public static ResourceLocation getResource(String modid, String folder, String icon) {
        return getResourceUtil(modid, "textures/" + folder + "/" + icon);
    }

    public static IIcon getIconfromRegistry(String modid, String folder, String icon) {
        return ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(ResourceUtil.getResource(modid, folder, icon))).getAtlasSprite("missingno");
    }

    public static File getModsFolder() {
        if (mods == null)
            initResourceUtil();
        return mods;
    }

    public static File getAssetsFolder() {
        if (assets == null)
            initResourceUtil();
        return assets;
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    public static final void setBlockTextureSheet() {
        bindTexture(MC_BLOCK_SHEET);
    }

    public static final void setItemTextureSheet() {
        bindTexture(MC_ITEM_SHEET);
    }
}
