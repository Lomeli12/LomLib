package net.lomeli.lomlib.client.render.item;

import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.fml.client.FMLClientHandler;

@SuppressWarnings("deprecation")
public class ItemRenderWrapper implements IPerspectiveAwareModel {
    private static List<BakedQuad> dummyList = Collections.emptyList();
    private final Pair<? extends IBakedModel, Matrix4f> selfPair;
    public boolean disableRender = false;
    private IItemRenderer itemRenderer;

    public ItemRenderWrapper(IItemRenderer renderer) {
        this.itemRenderer = renderer;
        selfPair = Pair.of((IBakedModel) this, null);
    }

    @Override
    public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
        itemRenderer.preRenderItem();
        if (!disableRender) {
            Tessellator tess = Tessellator.getInstance();
            if (tess.getBuffer().isDrawing)
                tess.draw();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5D, 0.5D, 0.5D);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);

            itemRenderer.renderItem();
            itemRenderer.postRenderItem();
            rebindTexture();

            GlStateManager.popMatrix();

            VertexBuffer vertexBuffer = tess.getBuffer();
            vertexBuffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
        }
        return dummyList;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return itemRenderer.getOverrides();
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Pair<? extends IBakedModel, Matrix4f> pair = itemRenderer.handlePerspective(cameraTransformType, selfPair);

        if (itemRenderer.useVanillaCameraTransform()) {
            switch (cameraTransformType) { //this is here since this model is Perspective aware, vanilla transforms aren't used.
                case FIRST_PERSON_LEFT_HAND:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().firstperson_left);
                    break;
                case FIRST_PERSON_RIGHT_HAND:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().firstperson_right);
                    break;
                case GUI:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().gui);
                    break;
                case HEAD:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().head);
                    break;
                case THIRD_PERSON_LEFT_HAND:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().thirdperson_left);
                    break;
                case THIRD_PERSON_RIGHT_HAND:
                    FMLClientHandler.instance().getClient().getRenderItem().isThereOneNegativeScale(this.getItemCameraTransforms().thirdperson_right);
                    break;
                default:
                    break;
            }
        }
        return pair;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return FMLClientHandler.instance().getClient().getTextureMapBlocks().getMissingSprite();
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return itemRenderer.getCameraTransforms();
    }

    protected void rebindTexture() {
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
    }
}
// *cough* borrowed *cough* from iChunUtil. Thanks iChun <3
