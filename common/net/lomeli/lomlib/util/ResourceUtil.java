package net.lomeli.lomlib.util;

import java.io.File;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ResourceUtil {
    private static File mods;
    private static File assets;
    
    @SideOnly(Side.CLIENT)
    public static void initResourceUtil(){
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
    
    public static File getModsFolder() {
        if(mods == null)
            initResourceUtil();
        return mods;
    }
    
    public static File getAssetsFolder() {
        if(assets == null)
            initResourceUtil();
        return assets;
    }
}
