package net.lomeli.lomlib.client.render.old.item

import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.item.ItemStack
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

interface IItemRenderer {
    fun preRenderItem()

    fun renderItem()

    fun postRenderItem()

    fun getCameraTransforms(): ItemCameraTransforms

    fun handleBlockState(state: IBlockState)

    fun handleItemState(stack: ItemStack)

    fun handlePerspective(cameraTransformType: ItemCameraTransforms.TransformType, pair: Pair<out IBakedModel, Matrix4f>): Pair<out IBakedModel, Matrix4f>

    fun useVanillaCameraTransform(): Boolean

    fun getOverrides(): ItemOverrideList
}