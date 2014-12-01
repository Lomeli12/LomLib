package net.lomeli.lomlib.client;

import com.google.gson.stream.JsonReader;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.IImageBuffer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderPlayerEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.libs.Strings;

@SideOnly(Side.CLIENT)
public class DevCapes {
    private static DevCapes instance;
    public HashMap<String, String> capes;

    public DevCapes() {
        capes = new HashMap<String, String>();
        buildCapeDatabase();
    }

    public static DevCapes getInstance() {
        if (instance == null)
            instance = new DevCapes();
        return instance;
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderPlayer(RenderPlayerEvent.Specials.Pre event) {
        if (event.entityPlayer != null && !capes.isEmpty()) {
            AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
            if (player != null) {
                String playerUUID = event.entityPlayer.getUniqueID().toString().replace("-", "");
                if (capes.containsKey(playerUUID)) {
                    ResourceLocation cape = getCape(capes.get(playerUUID));
                    try {
                        if (!isTextureLoaded(player) && cape != null)
                            player.func_152121_a(MinecraftProfileTexture.Type.CAPE, cape);
                    } catch (Exception e) {
                        LomLib.logger.logError("Could not apply cape. =/");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private ResourceLocation getCape(String i) {
        return new ResourceLocation(Strings.MOD_ID.toLowerCase() + ":capes/cape_" + i + ".png");
    }

    public boolean isTextureLoaded(AbstractClientPlayer player) {
        ResourceLocation location = player.getLocationCape();
        if (location == null)
            return false;
        return true;
    }

    public void buildCapeDatabase() {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new URL(Strings.CAPE_URL).openStream()));
            if (reader != null) {
                reader.beginObject();
                while (reader.hasNext()) {
                    String uuid = reader.nextName();
                    String cape = reader.nextString();
                    if (!capes.containsKey(uuid))
                        capes.put(uuid, cape);
                }
                reader.close();
            }
        } catch (Exception e) {
        }
    }
}