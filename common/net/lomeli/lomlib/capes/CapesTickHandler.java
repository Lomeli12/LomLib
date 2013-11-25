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

	private boolean giveUp = false;
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if ((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0) && !giveUp) {
			// List of players.
			@SuppressWarnings("unchecked")
			List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

			// resets the counter if it is too high.
			if (counter >= players.size())
				counter = 0;

			AbstractClientPlayer p = players.get(counter);
			if (p != null) {

				String lowerUsername = p.username.toLowerCase();

				if(!p.getTextureCape().isTextureUploaded()){
				    if(LomLib.debug)
				        LomLib.logger.log(Level.INFO, "Changing cape of: " + p.username);
				    p.downloadImageCape = CapeUtil.getInstance().getUserCape(lowerUsername);
				    p.locationCape = CapeUtil.getInstance().getUserResource(lowerUsername);
				}
				/*
				if (p.getTextureCape() != null
						&& CapeUtil.getInstance().getUserCape(lowerUsername) != null) {
					try {
						if(!p.getTextureCape().isTextureUploaded()){
							LomLib.logger.log(Level.FINE,
									"Changing cape of: " + p.username);
							p.getClass().getDeclaredField("downloadImageCape").set(p, CapeUtil.getInstance().getUserCape(lowerUsername));
							p.getClass().getDeclaredField("locationCape").set(p, CapeUtil.getInstance().getUserResource(lowerUsername));
						}
					}catch (Exception e){
						e.printStackTrace();
						this.giveUp = true;
					}
				}*/

			}
			counter++;
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
