package net.lomeli.lomlib.client

import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.client.handler.ClientTickHandler
import net.lomeli.lomlib.client.layer.LayerCrown
import net.lomeli.lomlib.client.layer.LayerHandler
import net.lomeli.lomlib.client.layer.LayerItemRenderer
import net.lomeli.lomlib.client.models.ModelHandler
import net.lomeli.lomlib.client.render.item.ItemRenderHandler
import net.lomeli.lomlib.core.Proxy
import net.lomeli.lomlib.util.client.RenderUtil
import net.minecraft.client.Minecraft
import net.minecraft.util.text.TextComponentTranslation
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.client.FMLClientHandler

class ProxyClient : Proxy() {
    override fun preInit() {
        super.preInit()
        if (FMLClientHandler.instance().hasOptifine())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without Optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)")
    }

    override fun init() {
        super.init()
        ModelHandler.registerColorProviders()
        MinecraftForge.EVENT_BUS.register(LomLib.config)
        MinecraftForge.EVENT_BUS.register(ItemRenderHandler)
        MinecraftForge.EVENT_BUS.register(ClientTickHandler)
        var renderer = Minecraft.getMinecraft().renderManager.skinMap["default"]
        RenderUtil.addLayerToRenderer(renderer!!, LayerItemRenderer(renderer))
        RenderUtil.addLayerToRenderer(renderer!!, LayerCrown())
        renderer = Minecraft.getMinecraft().renderManager.skinMap["slim"]
        RenderUtil.addLayerToRenderer(renderer!!, LayerItemRenderer(renderer))
        RenderUtil.addLayerToRenderer(renderer!!, LayerCrown())
        MinecraftForge.EVENT_BUS.register(LayerHandler)
    }

    override fun postInit() {
        super.postInit()
    }

    override fun messageClient(msg : String) {
        if (Minecraft.getMinecraft().player != null)
            Minecraft.getMinecraft().player.sendStatusMessage(TextComponentTranslation(msg), true)
    }
}