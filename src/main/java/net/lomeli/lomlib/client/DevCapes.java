package net.lomeli.lomlib.client;

import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.RenderPlayerEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ModLoaded;
import net.lomeli.lomlib.util.ObfUtil;

@SideOnly(Side.CLIENT)
public class DevCapes {
    private static DevCapes instance;
    public HashMap<String, String> capes = new HashMap<String, String>();
    private ArrayList<AbstractClientPlayer> capePlayers = new ArrayList<AbstractClientPlayer>();

    public DevCapes() {
        buildCapeDatabase();
    }

    public static DevCapes getInstance() {
        if (instance == null)
            instance = new DevCapes();
        return instance;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPreRenderSpecials(RenderPlayerEvent.Specials.Pre event) {
        if (event.entityPlayer != null && LomLib.capes) {
            if (!ModLoaded.isModInstalled("shadersmod") && (event.entityPlayer instanceof AbstractClientPlayer) && !ObfUtil.isOptifineInstalled()) {
                AbstractClientPlayer abstractClientPlayer = (AbstractClientPlayer) event.entityPlayer;

                if (!capePlayers.contains(abstractClientPlayer)) {
                    String playerUUID = event.entityPlayer.getUniqueID().toString().replace("-", "");
                    ResourceLocation cape = new ResourceLocation("lomlib:capes/cape_" + capes.get(playerUUID) + ".png");
                    if (cape != null && capes.containsKey(playerUUID)) {
                        capePlayers.add(abstractClientPlayer);
                        String[] locationCape = new String[]{"locationCape", "field_110313_e", "e"};
                        try {
                            ObfUtil.setFieldValue(AbstractClientPlayer.class, abstractClientPlayer, cape, locationCape);
                            new Thread(new CloakThread(abstractClientPlayer, cape)).start();
                            event.renderCape = true;
                        } catch (Exception e) {
                        }
                    }
                }
            }
        }
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
            }
        } catch (Exception e) {
        }
    }

    private class CloakThread implements Runnable {
        AbstractClientPlayer abstractClientPlayer;
        ResourceLocation cape;

        public CloakThread(AbstractClientPlayer player, ResourceLocation cloak) {
            abstractClientPlayer = player;
            cape = cloak;
        }

        @Override
        public void run() {
            if (LomLib.capes) {
                try {
                    String[] locationCape = new String[]{"locationCape", "field_110313_e", "e"};
                    ObfUtil.setFieldValue(AbstractClientPlayer.class, abstractClientPlayer, cape, locationCape);
                } catch (Exception e) {
                    LomLib.logger.logError("Failed to load cape!");
                    e.printStackTrace();
                }
            }
        }
    }
}