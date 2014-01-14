package net.lomeli.lomlib.client;

import java.util.logging.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLibCore;

import net.minecraft.client.entity.AbstractClientPlayer;

import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.ForgeSubscribe;

@SideOnly(Side.CLIENT)
public class RenderPlayerCape {

    @ForgeSubscribe
    public void renderPlayer(RenderPlayerEvent event) {
        if (LomLibCore.capes) {
            AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
            if (player != null) {
                String lowerUsername = player.getDisplayName().toLowerCase();
                if (player.getTextureCape() != null) {
                    if (!player.getTextureCape().isTextureUploaded()) {
                        if (CapeUtil.getInstance().getUserCape(lowerUsername) != null && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                            if (LomLibCore.debug)
                                LomLibCore.logger.log(Level.INFO, "Changing cape of: " + player.getDisplayName());
                            setPlayerCape(player, lowerUsername);
                        }
                    }
                } else {
                    if (CapeUtil.getInstance().getUserCape(lowerUsername) != null && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                        if (LomLibCore.debug)
                            LomLibCore.logger.log(Level.INFO, "Changing cape of: " + player.getDisplayName());
                        setPlayerCape(player, lowerUsername);
                    }
                }
            }
        }
    }

    private void setPlayerCape(AbstractClientPlayer player, String username) {
        try {
            AbstractClientPlayer.class.getDeclaredField("downloadImageCape").set(player, CapeUtil.getInstance().getUserCape(username));
            AbstractClientPlayer.class.getDeclaredField("locationCape").set(player, CapeUtil.getInstance().getUserResource(username));
        } catch (Exception e) {
            if (LomLibCore.debug)
                LomLibCore.logger.log(Level.INFO, "Could not apply cape to " + username);
        }
    }

}
