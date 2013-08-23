package net.lomeli.lomlib.capes;

import java.util.EnumSet;
import java.util.List;

import net.lomeli.lomlib.LomLib;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CapesTickHandler implements ITickHandler {

	private static final Minecraft mc = Minecraft.getMinecraft();
	
	private int counter = 0;
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
		if ((mc.theWorld != null) && (mc.theWorld.playerEntities.size() > 0)){
			// List of players.
			@SuppressWarnings("unchecked")
			List<AbstractClientPlayer> players = mc.theWorld.playerEntities;

			// resets the counter if it is too high.
			if(counter >= players.size())
				counter = 0;

			AbstractClientPlayer p = players.get(counter);
			if(p != null) {

				String lowerUsername = p.username.toLowerCase();
				
				if(CapeUtil.getInstance().getUserCape(lowerUsername) != null){
					if (!p.field_110315_c.func_110557_a()) {
						if(LomLib.debug)
							System.out.println("Changing cape of: " + p.username);
						p.field_110313_e = CapeUtil.getInstance().getUserResource(lowerUsername);
						p.field_110315_c = CapeUtil.getInstance().getUserCape(lowerUsername);
					}
				}
				
			}
			counter++;
		}
	}
	
	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData){}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.CLIENT);
	}

	@Override
	public String getLabel() {
		return "LomLib";
	}

}
