package net.lomeli.lomlib.client

import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.client.layer.LayerCrown
import net.lomeli.lomlib.client.layer.LayerHandler
import net.lomeli.lomlib.client.models.ModelHandler
import net.lomeli.lomlib.client.render.ModelGenerator
import net.lomeli.lomlib.client.render.item.ItemRenderHandler
import net.lomeli.lomlib.core.Proxy
import net.minecraft.client.Minecraft
import net.minecraft.entity.player.EntityPlayer
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
        MinecraftForge.EVENT_BUS.register(ModelGenerator)
        MinecraftForge.EVENT_BUS.register(ItemRenderHandler)
        LayerHandler.addLayerForClass(EntityPlayer::class.java, LayerCrown())
        MinecraftForge.EVENT_BUS.register(LayerHandler)
    }

    override fun postInit() {
        super.postInit()
    }

    override fun messageClient(msg : String) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(TextComponentTranslation(msg))
    }
}