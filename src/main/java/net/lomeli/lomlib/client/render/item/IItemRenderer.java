package net.lomeli.lomlib.client.render.item;

import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public interface IItemRenderer {
    void preRenderItem();

    void renderItem();

    void postRenderItem();

    ItemCameraTransforms getCameraTransforms();

    void handleBlockState(IBlockState state);

    void handleItemState(ItemStack stack);

    Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, Pair<? extends IBakedModel, Matrix4f> pair);

    boolean useVanillaCameraTransform();

    ItemOverrideList getOverrides();
}
