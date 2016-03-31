package net.lomeli.lomlib.test;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(modid = "lomlibtest", name = "LomLibTest", version = "1.0.0", dependencies = "required-after:LomLib;")
public class LomLibTest {

    @Mod.Instance("lomlibtest")
    public static LomLibTest instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLClientHandler.instance().getSide().isClient())
            MinecraftForge.EVENT_BUS.register(new RenderArmorEventTest());
    }
}
