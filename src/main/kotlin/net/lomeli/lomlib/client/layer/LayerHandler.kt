package net.lomeli.lomlib.client.layer

import com.google.common.collect.Lists
import com.google.common.collect.Maps
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.util.client.RenderUtil
import net.minecraft.client.renderer.entity.layers.LayerRenderer
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.client.event.RenderLivingEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object LayerHandler {
    val layerMap: HashMap<Class<out EntityLivingBase>, ArrayList<LayerRenderer<out EntityLivingBase>>>

    init {
        layerMap = Maps.newHashMap()
    }

    fun addLayerForClass(clazz: Class<out EntityLivingBase>, layer: LayerRenderer<out EntityLivingBase>) {
        var layerList = Lists.newArrayList<LayerRenderer<*>>()
        if (layerMap.containsKey(clazz)) layerList = layerMap[clazz]
        layerList.add(layer)
        layerMap.put(clazz, layerList)
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