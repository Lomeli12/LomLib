package net.lomeli.lomlib.client;

import java.lang.reflect.Field;
import java.util.List;

import net.lomeli.lomlib.LomLibCore;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Type;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.player.EntityPlayer;

import net.minecraftforge.client.event.RenderPlayerEvent;

@SideOnly(Side.CLIENT)
public class RenderPlayerCape {

    private static final Minecraft mc = Minecraft.getMinecraft();
    private int counter = 0;
    private static Field downloadImageCapeField = getHackField(2);
    private static Field locationCapeField = getHackField(4);

    @SubscribeEvent
    public void renderPlayer(ClientTickEvent event) {
        if(LomLibCore.capes && event.type == Type.CLIENT) {
            try {
                if((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)) {

                    @SuppressWarnings("unchecked")
                    List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

                    if(counter >= players.size())
                        counter = 0;

                    AbstractClientPlayer p = players.get(counter);
                    if(p != null) {
                        String lowerUsername = p.getDisplayName().toLowerCase();
                        if(p.getTextureCape() != null) {
                            if(!p.getTextureCape().isTextureUploaded()) {
                                if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                                        && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                                    System.out.println("Changing cape of: " + p.getDisplayName());
                                    if(!LomLibCore.debug)
                                        LomLibCore.logger.logBasic("Changing cape of: " + p.getDisplayName());
                                    downloadImageCapeField.set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
                                    locationCapeField.set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
                                }
                            }
                        }else {
                            if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                                    && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                                System.out.println("Changing cape of: " + p.getDisplayName());
                                if(!LomLibCore.debug)
                                    LomLibCore.logger.logBasic("Changing cape of: " + p.getDisplayName());
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
    }

    private static Field getHackField(int i) {
        Field f = AbstractClientPlayer.class.getDeclaredFields()[i];
        f.setAccessible(true);
        return f;
    }

}
