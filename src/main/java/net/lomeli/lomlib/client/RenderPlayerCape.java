package net.lomeli.lomlib.client;

import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;

import net.lomeli.lomlib.LomLib;

public class RenderPlayerCape {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private int counter = 0;
    private static Field downloadImageCapeField = getHackField(3);
    private static Field locationCapeField = getHackField(4);

    @SubscribeEvent
    public void worldTick(ClientTickEvent tick) {
        try {
            if((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)) {
                @SuppressWarnings("unchecked")
                List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

                if(counter >= players.size())
                    counter = 0;

                AbstractClientPlayer p = players.get(counter);
                if(p != null) {
                    System.out.println(p.getDisplayName());
                    String lowerUsername = p.getDisplayName().toLowerCase();

                    if(p.getTextureCape() != null) {
                        if(!p.getTextureCape().isTextureUploaded()) {
                            if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                                    && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                                if(LomLib.debug)
                                    System.out.println("[LomLib] Changing cape of: " + p.getDisplayName());
                                downloadImageCapeField.set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
                                locationCapeField.set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
                            }
                        }
                    }else {
                        if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                                && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                            if(LomLib.debug)
                                System.out.println("[LomLib] Changing cape of: " + p.getDisplayName());
                            downloadImageCapeField.set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
                            locationCapeField.set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
                        }
                    }
                }

                counter++;
            }

        }catch(IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static Field getHackField(int i) {
        Field f = AbstractClientPlayer.class.getDeclaredFields()[i];
        f.setAccessible(true);
        return f;
    }
}
