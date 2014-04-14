package net.lomeli.lomlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.Proxy;
import net.lomeli.lomlib.client.gui.element.IconRegistry;
import net.lomeli.lomlib.client.render.SmallFontRenderer;
import net.lomeli.lomlib.util.ModLoaded;

public class ProxyClient extends Proxy {

    public static SmallFontRenderer smallFontRenderer;

    @Override
    public void doStuffPre() {
        super.doStuffPre();
        ResourceUtil.initResourceUtil();

        if (LomLib.capes)
            MinecraftForge.EVENT_BUS.register(DevCapes.getInstance());

        if (isOptifineInstalled())
            LomLib.logger
                    .logWarning("Optifine detected! If you run into any bugs, please test without optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)");
    }

    @Override
    public void doStuffInit() {
        FMLCommonHandler.instance().bus().register(new LomlibEvents());
        Minecraft mc = Minecraft.getMinecraft();
        smallFontRenderer = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"),
                mc.renderEngine, false);
    }

    @Override
    public void doStuffPost() {
        super.doStuffPost();
        if (ModLoaded.isModInstalled("NotEnoughItems"))
            NEIAddon.loadAddon();
    }

    private boolean isOptifineInstalled() {
        try {
            return Class.forName("optifine.OptiFineForgeTweaker") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static class LomlibEvents {
        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void registerIcons(TextureStitchEvent.Pre event) {
            if (event.map.getTextureType() != 0 && event.map.getTextureType() == 1) {
                IconRegistry.addIcon("Icon_Redstone", new ItemStack(Items.redstone).getIconIndex());
                IconRegistry.addIcon("Icon_Info", "lomlib:icons/Icon_Information", event.map);
                IconRegistry.addIcon("Icon_Energy", "lomlib:icons/Icon_Energy", event.map);
            }
        }

        //@SubscribeEvent
        public void renderTick(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                if (Minecraft.getMinecraft().currentScreen instanceof GuiVideoSettings) {
                    GuiVideoSettings gui = (GuiVideoSettings) Minecraft.getMinecraft().currentScreen;
                    String s = "Hit G to view more options";
                    gui.drawString(Minecraft.getMinecraft().fontRenderer, s, gui.width - Minecraft.getMinecraft().fontRenderer.getStringWidth(s) - 2, gui.height - 10, 16777215);
                }
            }
        }
    }
}
