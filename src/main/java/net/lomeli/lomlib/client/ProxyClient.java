package net.lomeli.lomlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Loader;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.addon.nei.NEIAddon;
import net.lomeli.lomlib.client.layer.LayerCrown;
import net.lomeli.lomlib.client.layer.LayerCustomBipedArmor;
import net.lomeli.lomlib.client.render.ModelGenerator;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.util.RenderUtils;

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
        RenderPlayer renderer = RenderUtils.getPlayerRenderer("default");
        RenderUtils.addLayerToRenderer(renderer, new LayerCrown());
        RenderUtils.addLayerToRenderer(renderer, new LayerCustomBipedArmor(renderer));
        renderer.layerRenderers.remove(0);
        renderer = RenderUtils.getPlayerRenderer("slim");
        RenderUtils.addLayerToRenderer(renderer, new LayerCrown());
        RenderUtils.addLayerToRenderer(renderer, new LayerCustomBipedArmor(renderer));
        renderer.layerRenderers.remove(0);
    }

    @Override
    public void postInit() {
        super.postInit();
        //if (Loader.isModLoaded("NotEnoughItems"))
        //    NEIAddon.loadAddon();
    }

    @Override
    public void messageClient(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation(msg));
    }
}