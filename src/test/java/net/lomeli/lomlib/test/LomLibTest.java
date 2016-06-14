package net.lomeli.lomlib.test;

import net.minecraft.inventory.EntityEquipmentSlot;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.event.RenderArmorEvent;

@Mod(modid = "lomlibtest", name = "LomLibTest", version = "1.0.0", dependencies = "required-after:LomLib;")
public class LomLibTest {

    @Mod.Instance("lomlibtest")
    public static LomLibTest instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLClientHandler.instance().getSide().isClient())
            MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void armorEventTest(RenderArmorEvent event) {
        if (event.getEquipmentSlot() == EntityEquipmentSlot.CHEST && event.getSlotStack() != null)
            event.setCanceled(true);
    }
}
