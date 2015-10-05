package net.lomeli.lomlib.client.render.item;

import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;

@SuppressWarnings("deprecation")
public interface IItemRenderer {
    public void preRenderItem();

    public void renderItem();

    public void postRenderItem();

    public ItemCameraTransforms getCameraTransforms();

    public void handleBlockState(IBlockState state);

    public void handleItemState(ItemStack stack);

    public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType, Pair<IBakedModel, Matrix4f> pair);

    public boolean useVanillaCameraTransform();
}
