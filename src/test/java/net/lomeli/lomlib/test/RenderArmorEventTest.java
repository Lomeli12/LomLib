package net.lomeli.lomlib.test;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.event.RenderArmorEvent;

@SideOnly(Side.CLIENT)
public class RenderArmorEventTest {

    @SubscribeEvent
    public void armorEventTest(RenderArmorEvent event) {
        //event.setCanceled(true);
    }
}
