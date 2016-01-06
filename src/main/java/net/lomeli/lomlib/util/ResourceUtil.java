package net.lomeli.lomlib.util;

import java.io.File;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class ResourceUtil {

    public static ResourceLocation getResource(String modid, String resource) {
        return new ResourceLocation(modid, resource);
    }

    public static ResourceLocation getGuiResource(String modid, String gui) {
        if (!gui.endsWith(".png") && !gui.endsWith(".PNG"))
            gui += ".png";
        return getResource(modid, "textures/gui/" + gui);
    }

    public static ResourceLocation getEntityTexture(String modid, String texture) {
        if (!texture.endsWith(".png") && !texture.endsWith(".PNG"))
            texture += ".png";
        return getResource(modid, "textures/entities/" + texture);
    }

    public static ResourceLocation getModelTexture(String modid, String texture) {
        if (!texture.endsWith(".png") && !texture.endsWith(".PNG"))
            texture += ".png";
        return getResource(modid, "textures/model/" + texture);
    }

    public static TextureAtlasSprite getSprite(String modid, String resourceLoc) {
        return FMLClientHandler.instance().getClient().getTextureMapBlocks().getAtlasSprite(modid + ":" + resourceLoc);
    }

    public static File getModsFolder() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile("mods");
    }

    public static File getAssetsFolder() {
        return new File(FMLClientHandler.instance().getClient().mcDataDir, "assets");
    }

    public static File getConfigFolder() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getFile("config");
    }

    public static TextureAtlasSprite getSprite(ResourceLocation resource) {
        return resource != null ? getSprite(resource.getResourceDomain(), resource.getResourcePath()) : null;
    }

    public static void bindTexture(ResourceLocation texture) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
    }

    public static boolean registerTexture(TextureMap map, ResourceLocation resource) {
        return map.setTextureEntry(resource.toString(), map.registerSprite(resource));
    }
}
