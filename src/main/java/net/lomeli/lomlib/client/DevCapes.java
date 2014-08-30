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
                    if (!isTextureLoaded(player) && cape != null)
                        player.func_152121_a(MinecraftProfileTexture.Type.CAPE, cape);
                    //PlayerCape cape = new PlayerCape(playerUUID, capes.get(playerUUID));
                    //if (!isTextureLoaded(player))
                    //    cape.loadTexture(player);
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

    private class PlayerCape {
        private ITextureObject texture;
        private ResourceLocation resource;

        public PlayerCape(String uuid, String capeURL) {
            this.resource = new ResourceLocation("cloaks/" + uuid);
            this.texture = new ThreadDownloadImageData(null, capeURL, null, new HDImageBuffer());
        }

        public ResourceLocation getResource() {
            return this.resource;
        }

        public ITextureObject getTexture() {
            return this.texture;
        }

        public void loadTexture(AbstractClientPlayer player) {
            player.func_152121_a(MinecraftProfileTexture.Type.CAPE, resource);
            Minecraft.getMinecraft().renderEngine.loadTexture(resource, this.texture);
        }
    }

    @SideOnly(Side.CLIENT)
    public class HDImageBuffer implements IImageBuffer {
        @Override
        public BufferedImage parseUserSkin(final BufferedImage texture) {
            if (texture == null)
                return null;
            int imageWidth = texture.getWidth(null) <= 64 ? 64 : texture.getWidth(null);
            int imageHeight = texture.getHeight(null) <= 32 ? 32 : texture.getHeight(null);

            BufferedImage capeImage = new BufferedImage(imageWidth, imageHeight, 2);

            Graphics graphics = capeImage.getGraphics();
            graphics.drawImage(texture, 0, 0, null);
            graphics.dispose();

            return capeImage;
        }

        @Override
        public void func_152634_a() {
        }
    }
}