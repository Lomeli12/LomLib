package net.lomeli.lomlib.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiVideoSettings;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.Proxy;
import net.lomeli.lomlib.client.gui.GuiOreDic;
import net.lomeli.lomlib.client.gui.element.IconRegistry;
import net.lomeli.lomlib.client.render.SmallFontRenderer;
import net.lomeli.lomlib.util.ModLoaded;
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.ToolTipUtil;
import net.lomeli.lomlib.util.UpdateHelper;

public class ProxyClient extends Proxy {

    public static SmallFontRenderer smallFontRenderer;
    private DevCapes capes;

    @Override
    public void doStuffPre() {
        super.doStuffPre();
        ResourceUtil.initResourceUtil();

        if (LomLib.capes && setCapeAccess()) {
            capes = DevCapes.getInstance();
            MinecraftForge.EVENT_BUS.register(capes);
        }

        if (ObfUtil.isOptifineInstalled())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others). Capes disabled to prevent optifine related crashes");
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

    private boolean setCapeAccess() {
        boolean successfull = false;
        try {
            if (LomLib.debug)
                LomLib.logger.logBasic("Accessing AbstractClientPlayer fields");
            ObfUtil.setFieldAccessible(AbstractClientPlayer.class, "downloadImageCape", "field_110315_c", "c");
            ObfUtil.setFieldAccessible(AbstractClientPlayer.class, "locationCape", "field_110313_e", "e");
            successfull = true;
        } catch (Exception e) {
            e.printStackTrace();
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
                if (mc.currentScreen != null) {
                    if ((mc.currentScreen instanceof InventoryEffectRenderer) && !(mc.currentScreen instanceof GuiOreDic)) {
                        if (Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                            mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("gui.button.press"), 1.0F));
                            mc.displayGuiScreen(new GuiOreDic());
                        }
                    }
                } else if (mc.inGameHasFocus && (UpdateHelper.modList != null && !UpdateHelper.modList.isEmpty()) && !LomLib.proxy.sentMessage) {
                    for (UpdateHelper uh : UpdateHelper.modList) {
                        mc.thePlayer.addChatMessage(new ChatComponentText(ToolTipUtil.BLUE + StatCollector.translateToLocal("message." + uh.getModID() + ".update") + uh.getDownloadURL()));
                    }
                    LomLib.proxy.sentMessage = true;
                }
            }
        }

    }
}
