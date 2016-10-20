package net.lomeli.lomlib.client.render.old.item

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.fml.client.FMLClientHandler
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

@SuppressWarnings("deprecation")
class ItemRenderWrapper(private val itemRenderer: IItemRenderer) : IPerspectiveAwareModel {
    private val dummyList = emptyList<BakedQuad>()
    private val selfPair: Pair<out IBakedModel, Matrix4f>
    var disableRender = false

    init {
        selfPair = Pair.of<IBakedModel, Matrix4f>(this as IBakedModel, null)
    }

    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): List<BakedQuad> {
        itemRenderer.preRenderItem()
        if (!disableRender) {
            val tess = Tessellator.getInstance()
            if (tess.buffer.isDrawing)
                tess.draw()
            GlStateManager.pushMatrix()
            GlStateManager.translate(0.5, 0.5, 0.5)
            GlStateManager.scale(-1.0f, -1.0f, 1.0f)

            itemRenderer.renderItem()
            itemRenderer.postRenderItem()
            rebindTexture()

            GlStateManager.popMatrix()

            val vertexBuffer = tess.buffer
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR)
        }
        return dummyList
    }

    override fun getOverrides(): ItemOverrideList = itemRenderer.getOverrides()

    override fun handlePerspective(cameraTransformType: ItemCameraTransforms.TransformType): Pair<out IBakedModel, Matrix4f> {
        val pair = itemRenderer.handlePerspective(cameraTransformType, selfPair)

        if (itemRenderer.useVanillaCameraTransform()) {
            when (cameraTransformType) {
            //this is here since this model is Perspective aware, vanilla transforms aren't used.
                ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.firstperson_left)
                ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.firstperson_right)
                ItemCameraTransforms.TransformType.GUI -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.gui)
                ItemCameraTransforms.TransformType.HEAD -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.head)
                ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.thirdperson_left)
                ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND -> FMLClientHandler.instance().client.renderItem.isThereOneNegativeScale(this.itemCameraTransforms.thirdperson_right)
                else -> {
                }
            }
        }
        return pair
    }

    override fun isAmbientOcclusion(): Boolean = true

    override fun isGui3d(): Boolean = true

    override fun isBuiltInRenderer(): Boolean = false

    override fun getParticleTexture(): TextureAtlasSprite = FMLClientHandler.instance().client.textureMapBlocks.missingSprite

    override fun getItemCameraTransforms(): ItemCameraTransforms = itemRenderer.getCameraTransforms()

    private fun rebindTexture() {
        FMLClientHandler.instance().client.textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE)
    }
}
// *cough* borrowed *cough* from iChunUtil. Thanks iChun <3