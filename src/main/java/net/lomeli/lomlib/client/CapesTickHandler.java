package net.lomeli.lomlib.client;

import java.lang.reflect.Field;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

@SideOnly(Side.CLIENT)
public class CapesTickHandler implements ITickHandler {

    private static final Minecraft mc = Minecraft.getMinecraft();

    private int counter = 0;
    private static Field downloadImageCapeField = getHackField(2);
    private static Field locationCapeField = getHackField(4);

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        try {
            if ((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)) {

                @SuppressWarnings("unchecked")
                List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

                if (counter >= players.size())
                    counter = 0;

                AbstractClientPlayer p = players.get(counter);
                if (p != null) {

                    String lowerUsername = p.username.toLowerCase();

                    if (p.getTextureCape() != null) {
                        if (!p.getTextureCape().isTextureUploaded()) {
                            if (CapeUtil.getInstance().getUserCape(lowerUsername) != null && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                                if (LomLib.debug)
                                    LomLib.logger.log(Level.INFO, "Changing cape of: " + p.username);
                                downloadImageCapeField.set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
                                locationCapeField.set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
                            }
                        }
                    } else {
                        if (CapeUtil.getInstance().getUserCape(lowerUsername) != null && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                            if (LomLib.debug)
                                LomLib.logger.log(Level.INFO, "Changing cape of: " + p.username);
                            downloadImageCapeField.set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
                            locationCapeField.set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
                        }
                    }
                }
                counter++;
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setPlayerCape(AbstractClientPlayer player, String username) {
        try {
            player.getClass().getDeclaredField("downloadImageCape").set(player, CapeUtil.getInstance().getUserCape(username));
            player.getClass().getDeclaredField("locationCape").set(player, CapeUtil.getInstance().getUserResource(username));
            // AbstractClientPlayer.class.getDeclaredField("downloadImageCape").set(player,
            // CapeUtil.getInstance().getUserCape(username));
            // AbstractClientPlayer.class.getDeclaredField("locationCape").set(player,
            // CapeUtil.getInstance().getUserResource(username));
        } catch (Exception e) {
            if (LomLib.debug)
                LomLib.logger.log(Level.INFO, "Could not apply cape to " + username);
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "LomLib";
    }

    private static Field getHackField(int i) {
        Field f = AbstractClientPlayer.class.getDeclaredFields()[i];
        f.setAccessible(true);
        return f;
    }

}
