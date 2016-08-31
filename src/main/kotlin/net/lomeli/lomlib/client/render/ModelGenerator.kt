package net.lomeli.lomlib.client.render

import com.google.common.collect.Lists
import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.client.render.item.ISpecialRender
import net.lomeli.lomlib.client.render.item.ItemRenderWrapper
import net.lomeli.lomlib.lib.ModLibs
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

object ModelGenerator {
    private var specialRenders: ArrayList<ISpecialRender> = Lists.newArrayList<ISpecialRender>()

    fun addItemRender(renderHandler: ISpecialRender?) {
        if (ModLibs.initialized)
            LomLib.logger.logError("Render handler must be registered during pre-init. - " + renderHandler!!.resourceName())
        if (renderHandler != null && !specialRenders.contains(renderHandler))
            specialRenders.add(renderHandler)
    }

    @SubscribeEvent
    fun onModelBake(event: ModelBakeEvent) {
        if (specialRenders.size > 0) {
            for (renderHandler in specialRenders) {
                val renderer = renderHandler.getRenderer()
                event.modelRegistry.putObject(ModelResourceLocation(renderHandler.resourceName(), "inventory"), ItemRenderWrapper(renderer))
            }
        }
    }
}