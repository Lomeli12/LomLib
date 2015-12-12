package net.lomeli.lomlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.GuiModList;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.addon.nei.NEIAddon;
import net.lomeli.lomlib.client.patreon.LayerCrown;
import net.lomeli.lomlib.client.render.ModelGenerator;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.util.RenderUtils;
import net.lomeli.lomlib.util.entity.ItemCustomEgg;

public class ProxyClient extends Proxy {

    @Override
    public void preInit() {
        super.preInit();
        if (FMLClientHandler.instance().hasOptifine())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without Optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)");
    }

    @Override
    public void init() {
        super.init();
        MinecraftForge.EVENT_BUS.register(LomLib.config);
        MinecraftForge.EVENT_BUS.register(new ModelGenerator());
        MinecraftForge.EVENT_BUS.register(this);
        LayerRenderer crownRenderer = new LayerCrown();
        RenderUtils.addLayerToRenderer(Minecraft.getMinecraft().getRenderManager().skinMap.get("default"), crownRenderer);
        RenderUtils.addLayerToRenderer(Minecraft.getMinecraft().getRenderManager().skinMap.get("slim"), crownRenderer);
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemCustomEgg.customEgg, new BasicItemMesh("lomlib:spawnEgg"));
    }

    @Override
    public void postInit() {
        super.postInit();
        if (Loader.isModLoaded("NotEnoughItems"))
            NEIAddon.loadAddon();
    }

    @Override
    public void messageClient(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation(msg));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void guiPostAction(GuiScreenEvent.ActionPerformedEvent.Pre event) {
        if (LomLib.overrideModOptions && event.gui instanceof GuiIngameMenu) {
            if (event.button.id == 12) {
                //event.setCanceled(true);
                FMLClientHandler.instance().getClient().displayGuiScreen(new GuiModList(event.gui));
            }
        }
    }
}