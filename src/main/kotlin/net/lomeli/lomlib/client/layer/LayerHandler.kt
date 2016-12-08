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
    val layerMap: HashMap<Class<out EntityLivingBase>, ArrayList<LayerRenderer<out EntityLivingBase>>>
    var playerLayerList : ArrayList<LayerRenderer<out EntityPlayer>>

    init {
        layerMap = Maps.newHashMap()
        playerLayerList = Lists.newArrayList()
    }

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
        for (entry in layerMap.entries) {
            if (entry.key.isAssignableFrom(entity.javaClass))
                return entry.value
        }
        return null
    }

    private fun removeEntity(entity: EntityLivingBase) {
        var key: Class<out EntityLivingBase>? = null
        for (entry in layerMap.entries) {
            if (entry.key.isAssignableFrom(entity.javaClass)) {
                key = entry.key
                break
            }
        }
        if (key != null)
            layerMap.remove(key)
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