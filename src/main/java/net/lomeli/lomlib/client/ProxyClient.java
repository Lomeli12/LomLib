package net.lomeli.lomlib.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.nei.NEIAddon;
import net.lomeli.lomlib.client.patreon.LayerCrown;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.entity.ItemCustomEgg;

public class ProxyClient extends Proxy {

    @Override
    public void preInit() {
        super.preInit();

        if (ObfUtil.isOptifineInstalled())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)");
    }

    @Override
    public void init() {
        super.init();
        FMLCommonHandler.instance().bus().register(LomLib.config);
        LayerRenderer crownRenderer = new LayerCrown();
        RendererLivingEntity rendererLivingEntity = (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().skinMap.get("default");
        if (rendererLivingEntity != null)
            rendererLivingEntity.addLayer(crownRenderer);
        rendererLivingEntity = (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().skinMap.get("slim");
        if (rendererLivingEntity != null)
            rendererLivingEntity.addLayer(crownRenderer);

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemCustomEgg.customEgg, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation("lomlib:spawnEgg", "inventory");
            }
        });
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
}
