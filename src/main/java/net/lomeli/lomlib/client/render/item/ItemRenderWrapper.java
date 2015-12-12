package net.lomeli.lomlib.client.render.item;

import org.apache.commons.lang3.tuple.Pair;

import javax.vecmath.Matrix4f;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.fml.client.FMLClientHandler;

@SuppressWarnings("deprecation")
public class ItemRenderWrapper implements IFlexibleBakedModel, ISmartBlockModel, ISmartItemModel, IPerspectiveAwareModel {
    private static List<BakedQuad> dummyList = Collections.emptyList();
    private final Pair<IBakedModel, Matrix4f> selfPair;
    public boolean disableRender = false;
    private IItemRenderer itemRenderer;

    public ItemRenderWrapper(IItemRenderer renderer) {
        this.itemRenderer = renderer;
        selfPair = Pair.of((IBakedModel) this, null);
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return dummyList;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        itemRenderer.preRenderItem();
        if (!disableRender) {
            Tessellator tess = Tessellator.getInstance();
            tess.draw();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.5D, 0.5D, 0.5D);
            GlStateManager.scale(-1.0F, -1.0F, 1.0F);

            itemRenderer.renderItem();
            itemRenderer.postRenderItem();
            rebindTexture();

            GlStateManager.popMatrix();

            WorldRenderer worldRenderer = tess.getWorldRenderer();
            worldRenderer.begin(1, DefaultVertexFormats.POSITION_TEX_COLOR);
        }
        return dummyList;
    }

    @Override
    public VertexFormat getFormat() {
        return DefaultVertexFormats.ITEM;
    }

    @Override
    public Pair<IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        Pair<IBakedModel, Matrix4f> pair = itemRenderer.handlePerspective(cameraTransformType, selfPair);

        if (itemRenderer.useVanillaCameraTransform()) {
            switch (cameraTransformType) { //this is here since this model is Perspective aware, vanilla transforms aren't used.
                case FIRST_PERSON:
                    FMLClientHandler.instance().getClient().getRenderItem().func_183005_a(this.getItemCameraTransforms().firstPerson);
                    break;
                case GUI:
                    FMLClientHandler.instance().getClient().getRenderItem().func_183005_a(this.getItemCameraTransforms().gui);
                    break;
                case HEAD:
                    FMLClientHandler.instance().getClient().getRenderItem().func_183005_a(this.getItemCameraTransforms().head);
                    break;
                case THIRD_PERSON:
                    FMLClientHandler.instance().getClient().getRenderItem().func_183005_a(this.getItemCameraTransforms().thirdPerson);
                    break;
                default:
                    break;
            }
        }
        return pair;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        itemRenderer.handleBlockState(state);
        return this;
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        itemRenderer.handleItemState(stack);
        return this;
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
