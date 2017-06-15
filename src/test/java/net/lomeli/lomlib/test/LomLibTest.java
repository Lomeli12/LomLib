package net.lomeli.lomlib.test;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.event.RenderArmorEvent;
import net.lomeli.lomlib.core.event.FoodEatenEvent;
import net.lomeli.lomlib.util.LogUtil;
import net.lomeli.lomlib.util.items.ItemUtil;

@Mod(modid = "lomlibtest", name = "LomLibTest", version = "1.0.0", dependencies = "required-after:lomlib;")
public class LomLibTest {

    @Mod.Instance("lomlibtest")
    public static LomLibTest instance;

    public static LogUtil log = new LogUtil("LomLib Test");

    public static Item renderTester;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        renderTester = new ItemRenderTest();
        ItemUtil.INSTANCE.registerItem(renderTester, "renderTester");
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (FMLClientHandler.instance().getSide().isClient())
            MinecraftForge.EVENT_BUS.register(this);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void armorEventTest(RenderArmorEvent event) {
        if (!event.getSlotStack().isEmpty() && event.getSlotStack().getItem() instanceof ItemArmor)
            log.logDebug("Equipped %s in %s slot", event.getSlotStack().getDisplayName(), event.getEquipmentSlot());
    }

    @SubscribeEvent
    public void onFoodStatsTest(FoodEatenEvent event) {
        if (event.getEntityPlayer().world.isRemote) return;
        if (event.getFoodStack().isEmpty()) {
            log.logInfo("Something went wrong...");
            return;
        }
        if (!event.getEntityPlayer().world.isRemote)
            log.logInfo("%s ate a(n) %s and got %s to Food Lvl and %s to Saturation.", event.getEntityPlayer().getName(), event.getFoodStack().getDisplayName(), event.getFoodLevel(), event.getFoodSaturationLevel());
    }
}
