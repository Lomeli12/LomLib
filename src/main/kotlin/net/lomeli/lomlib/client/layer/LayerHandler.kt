package net.lomeli.lomlib.client.layer

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.util.client.RenderUtil
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object LayerHandler {
    val layerMap = Maps.newHashMap<Class<out EntityLivingBase>, ArrayList<LayerRenderer<out EntityLivingBase>>>()
    var playerLayerList = Lists.newArrayList<LayerRenderer<out EntityPlayer>>()

    fun addLayerForClass(clazz: Class<out EntityLivingBase>, layer: LayerRenderer<out EntityLivingBase>) {
        if (clazz == EntityPlayer::class.java) {
            addPlayerLayer(layer as LayerRenderer<out EntityPlayer>)
            return
        }
        var layerList = Lists.newArrayList<LayerRenderer<*>>()
        if (layerMap.containsKey(clazz)) layerList = layerMap[clazz]
        layerList.add(layer)
        layerMap.put(clazz, layerList)
    }

    fun addPlayerLayer(layer: LayerRenderer<out EntityPlayer>) {
        playerLayerList.add(layer)
    }

    fun getEntityLayers(entity: EntityLivingBase): ArrayList<LayerRenderer<out EntityLivingBase>>? {
        val out = layerMap.entries.stream().filter({entry -> entry.key.isAssignableFrom(entity.javaClass)}).findFirst().orElse(null)
        return out?.value
    }

    private fun removeEntity(entity: EntityLivingBase) {
        val query = layerMap.entries.stream().filter({entry -> entry.key.isAssignableFrom(entity.javaClass)}).findFirst().orElse(null)
        layerMap.remove(query.key)
    }

    @SubscribeEvent fun renderPre(event: RenderLivingEvent.Pre<EntityLivingBase>) {
        if (event.entity is EntityPlayer && playerLayerList.size > 0) {
            LomLib.logger.logInfo("Adding player layers!")
            for (layer in playerLayerList)
                RenderUtil.addLayerToRenderer(event.renderer, layer)
            return
        } else {
            val layerList = getEntityLayers(event.entity)
            if (layerList != null && layerList.isNotEmpty()) {
                LomLib.logger.logInfo("Adding layers for ${event.entity.javaClass}")
                for (layer in layerList) {
                    RenderUtil.addLayerToRenderer(event.renderer, layer)
                }
                removeEntity(event.entity)
            }
        }
    }
}