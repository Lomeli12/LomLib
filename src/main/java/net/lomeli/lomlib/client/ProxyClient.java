package net.lomeli.lomlib.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.Proxy;
import net.lomeli.lomlib.client.gui.element.IconRegistry;
import net.lomeli.lomlib.client.render.SmallFontRenderer;
import net.lomeli.lomlib.util.ModLoaded;
import net.lomeli.lomlib.util.ObfUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ProxyClient extends Proxy {

    public static SmallFontRenderer smallFontRenderer;

    @Override
    public void doStuffPre() {
        super.doStuffPre();
        ResourceUtil.initResourceUtil();

        if (LomLib.capes && setCapeAccess())
            MinecraftForge.EVENT_BUS.register(DevCapes.getInstance());

        if (isOptifineInstalled())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)");
    }

    @Override
    public void doStuffInit() {
        super.doStuffInit();
        FMLCommonHandler.instance().bus().register(new LomlibEvents());
        Minecraft mc = Minecraft.getMinecraft();
        smallFontRenderer = new SmallFontRenderer(mc.gameSettings, new ResourceLocation("minecraft:textures/font/ascii.png"), mc.renderEngine, false);
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
        }catch (ClassNotFoundException e) {
            return false;
        }
    }

    private boolean setCapeAccess() {
        boolean successfull = false;
        try {
            if (LomLib.debug)
                LomLib.logger.logBasic("Accessing AbstractClientPlayer fields with MCP Names");
            ObfUtil.setFieldAccess(AbstractClientPlayer.class, "downloadImageCape", true);
            ObfUtil.setFieldAccess(AbstractClientPlayer.class, "locationCape", true);
            successfull = true;
        }catch (Exception e) {
            try {
                if (LomLib.debug)
                    LomLib.logger.logBasic("Failed with MCP names, using SRG Names");
                ObfUtil.setFieldAccess(AbstractClientPlayer.class, "field_110315_c", true);
                ObfUtil.setFieldAccess(AbstractClientPlayer.class, "field_110313_e", true);
                successfull = true;
            }catch (Exception e1) {
                try {
                    if (LomLib.debug)
                        LomLib.logger.logBasic("Failed with SRG names, using Obfuscated Names");
                    ObfUtil.setFieldAccess(AbstractClientPlayer.class, "c", true);
                    ObfUtil.setFieldAccess(AbstractClientPlayer.class, "e", true);
                    successfull = true;
                }catch (Exception e2) {
                    LomLib.logger.logError("Could not set cape access!");
                    e2.printStackTrace();
                }
            }
        }
        return successfull;
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

        @SideOnly(Side.CLIENT)
        @SubscribeEvent
        public void renderTick(TickEvent.RenderTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                Minecraft mc = Minecraft.getMinecraft();
                if (mc.currentScreen instanceof GuiVideoSettings) {
                    GuiIngameMenu gui = (GuiIngameMenu) mc.currentScreen;
                    String s = "Hit H for mod Info";
                    gui.drawString(mc.fontRenderer, s, gui.width - mc.fontRenderer.getStringWidth(s) - 2, gui.height - 10, 16777215);
                    if (Keyboard.isKeyDown(Keyboard.KEY_H)) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                    }
                }
            }
        }
    }
}
