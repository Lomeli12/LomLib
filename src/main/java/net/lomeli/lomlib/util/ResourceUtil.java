package net.lomeli.lomlib.util;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;

public class ResourceUtil {
    private static File mods;
    private static File assets;

    public static ResourceLocation getResourceUtil(String modid, String resource) {
        return new ResourceLocation(modid, resource);
    }

    public static ResourceLocation getGuiResource(String modid, String gui) {
        return getResourceUtil(modid, "textures/gui/" + gui);
    }

    public static ResourceLocation getEntityTexture(String modid, String texture) {
        return getResourceUtil(modid, "textures/entities/" + texture);
    }

    public static ResourceLocation getModelTexture(String modid, String texture) {
        return getResourceUtil(modid, "textures/model/" + texture);
    }

    public static ResourceLocation getResource(String modid, String folder, String icon) {
        return getResourceUtil(modid, folder + "/" + icon);
    }

    public static File getModsFolder() {
        if (mods == null)
            mods = MinecraftServer.getServer().getFile("mods");
        return mods;
    }

    public static File getAssetsFolder() {
        if (assets == null)
            assets = new File(Minecraft.getMinecraft().mcDataDir, "assets");
        return assets;
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }
}
