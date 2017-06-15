package net.lomeli.lomlib.client.models

import com.google.common.collect.Lists
import net.lomeli.lomlib.LomLib
import net.minecraft.block.Block
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ModelResourceLocation
import net.minecraft.item.Item
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.LoaderState

object ModelHandler {
    private val colorProviders = Lists.newArrayList<IColorProvider>()

    fun registerModel(holder: IModelHolder) {
        if (holder == null)
            return
        if (Loader.instance().loaderState != LoaderState.PREINITIALIZATION)
            LomLib.logger.logError("Must be registered in Pre-Init")
        if (holder is IColorProvider)
            colorProviders.add(holder)
        if (holder is IMeshVariant) {
            val def = holder.getCustomMesh()
            if (def != null) {
                ModelLoader.setCustomMeshDefinition(if (holder is Item) holder else if (holder is Block) Item.getItemFromBlock((holder as Block?)!!) else null, def)
                return
            }
        }

        if (holder is Item) {
            val variants = holder.getVariants()
            if (variants != null && variants.size > 0) {
                for (i in variants.indices) {
                    val name = variants[i]
                    val loc = ModelResourceLocation(name, "inventory")
                    ModelLoader.setCustomModelResourceLocation(holder, i, loc)
                }
            }
        } else if (holder is Block) {
            val variants = holder.getVariants()
            if (variants != null && variants.size > 0) {
                for (i in variants.indices) {
                    val name = variants[i]
                    var loc = ModelResourceLocation(name, "inventory")
                    if (holder is IModelVariant) {
                        loc = ModelResourceLocation(name, holder.getModelTypes()[i])
                        ModelLoader.registerItemVariants(Item.getItemFromBlock(holder)!!, loc)
                    }
                    ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(holder)!!, i, loc)
                }
            }
        }
    }

    fun registerModel(obj: Any) {
        if (obj is IModelHolder) registerModel(obj)
    }

    fun registerColorProviders() {
        val colors = Minecraft.getMinecraft().itemColors
        for (c in colorProviders) {
            if (c != null) colors.registerItemColorHandler(c.getColor(), c as Item)
        }
    }
}