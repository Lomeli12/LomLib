package net.lomeli.lomlib.client.models.tabula

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.model.BakedQuad
import net.minecraft.client.renderer.block.model.IBakedModel
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ItemOverrideList
import net.minecraft.client.renderer.texture.TextureAtlasSprite
import net.minecraft.util.EnumFacing
import net.minecraftforge.client.model.IPerspectiveAwareModel
import net.minecraftforge.common.model.TRSRTransformation
import org.apache.commons.lang3.tuple.Pair
import javax.vecmath.Matrix4f

class BakedTabulaModel : IPerspectiveAwareModel {
    private val quads: ImmutableList<BakedQuad>
    private val particle: TextureAtlasSprite
    private val transforms: ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>

    constructor(quads: ImmutableList<BakedQuad>, particle: TextureAtlasSprite, transforms: ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>) {
        this.quads = quads
        this.particle = particle
        this.transforms = transforms
    }

    override fun getQuads(state: IBlockState?, side: EnumFacing?, rand: Long): MutableList<BakedQuad>? {
        return this.quads
    }

    override fun isAmbientOcclusion(): Boolean {
        return true
    }

    override fun isGui3d(): Boolean {
        return false
    }

    override fun isBuiltInRenderer(): Boolean {
        return false
    }

    override fun getParticleTexture(): TextureAtlasSprite {
        return this.particle
    }

    override fun getItemCameraTransforms(): ItemCameraTransforms {
        return ItemCameraTransforms.DEFAULT
    }

    override fun getOverrides(): ItemOverrideList {
        return ItemOverrideList.NONE
    }

    override fun handlePerspective(type: ItemCameraTransforms.TransformType): Pair<out IBakedModel, Matrix4f> {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, transforms, type)
    }
}