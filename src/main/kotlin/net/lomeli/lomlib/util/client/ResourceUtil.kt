package net.lomeli.lomlib.util.client

import java.io.File

import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.util.ResourceLocation

import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler

object ResourceUtil {

    fun getResource(modid: String, resource: String): ResourceLocation = ResourceLocation(modid, resource)

    fun getGuiResource(modid: String, gui: String): ResourceLocation {
        return getResource(modid, "textures/gui/" + gui)
    }

    fun getEntityTexture(modid: String, texture: String): ResourceLocation {
        return getResource(modid, "textures/entities/" + texture)
    }

    fun getModelTexture(modid: String, texture: String): ResourceLocation {
        return getResource(modid, "textures/model/" + texture)
    }

    fun getSprite(modid: String, resourceLoc: String): TextureAtlasSprite = FMLClientHandler.instance().client.textureMapBlocks.getAtlasSprite(modid + ":" + resourceLoc)

    val modsFolder: File
        get() = FMLCommonHandler.instance().minecraftServerInstance.getFile("mods")

    val assetsFolder: File
        get() = File(FMLClientHandler.instance().client.mcDataDir, "assets")

    val configFolder: File
        get() = FMLCommonHandler.instance().minecraftServerInstance.getFile("config")

    fun getSprite(resource: ResourceLocation?): TextureAtlasSprite? = if (resource != null) getSprite(resource.resourceDomain, resource.resourcePath) else null

    fun bindTexture(texture: ResourceLocation) {
        FMLClientHandler.instance().client.renderEngine.bindTexture(texture)
    }

    fun registerTexture(map: TextureMap, resource: ResourceLocation): Boolean = map.setTextureEntry(map.registerSprite(resource))
}