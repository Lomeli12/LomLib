package net.lomeli.lomlib.capes;

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

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        if((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)) {

            @SuppressWarnings("unchecked")
            List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

            if(counter >= players.size())
                counter = 0;

            AbstractClientPlayer p = players.get(counter);
            if(p != null) {

                String lowerUsername = p.username.toLowerCase();

                if(p.getTextureCape() != null) {
                    if(!p.getTextureCape().isTextureUploaded()) {
                        if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                                && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                            if(LomLib.debug)
                                LomLib.logger.log(Level.INFO, "Changing cape of: " + p.username);
                            setPlayerCape(p, lowerUsername);
                        }
                    }
                }else {
                    if(CapeUtil.getInstance().getUserCape(lowerUsername) != null
                            && CapeUtil.getInstance().getUserResource(lowerUsername) != null) {
                        if(LomLib.debug)
                            LomLib.logger.log(Level.INFO, "Changing cape of: " + p.username);
                        setPlayerCape(p, lowerUsername);
                    }
                }
            }
            counter++;
        }
    }

    private void setPlayerCape(AbstractClientPlayer player, String username) {
        try {
            AbstractClientPlayer.class.getDeclaredField("downloadImageCape").set(player,
                    CapeUtil.getInstance().getUserCape(username));
            AbstractClientPlayer.class.getDeclaredField("locationCape").set(player,
                    CapeUtil.getInstance().getUserResource(username));
        }catch(Exception e) {
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

}
