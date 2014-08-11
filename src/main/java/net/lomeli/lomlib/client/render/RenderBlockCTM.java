package net.lomeli.lomlib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderBlockCTM implements ISimpleBlockRenderingHandler {
    private final int renderID;

    public RenderBlockCTM(int id) {
        this.renderID = id;
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        renderBlockInInventory(block, metadata, modelId, renderer, 1f, 1f, 1f);
    }

    protected void renderBlockInInventory(Block block, int metadata, int modelId, RenderBlocks renderer, float r, float g, float b) {
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);

        if (EntityRenderer.anaglyphEnable) {
            float f3 = (r * 30.0F + g * 59.0F + b * 11.0F) / 100.0F;
            float f4 = (r * 30.0F + g * 70.0F) / 100.0F;
            float f5 = (r * 30.0F + b * 70.0F) / 100.0F;
            r = f3;
            g = f4;
            b = f5;
        }
        RenderUtils.applyColor(r, g, b);
        renderer.colorRedTopLeft *= r;
        renderer.colorRedTopRight *= r;
        renderer.colorRedBottomLeft *= r;
        renderer.colorRedBottomRight *= r;
        renderer.colorGreenTopLeft *= g;
        renderer.colorGreenTopRight *= g;
        renderer.colorGreenBottomLeft *= g;
        renderer.colorGreenBottomRight *= g;
        renderer.colorBlueTopLeft *= b;
        renderer.colorBlueTopRight *= b;
        renderer.colorBlueBottomLeft *= b;
        renderer.colorBlueBottomRight *= b;

        RenderUtils.drawBlockFaces(renderer, block, block.getIcon(0, metadata), block.getIcon(1, metadata), block.getIcon(2, metadata), block.getIcon(3, metadata), block.getIcon(4, metadata),
                block.getIcon(5, metadata));
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        RenderCTM renderCTBlock = new RenderCTM(block, world.getBlockMetadata(x, y, z), renderer);
        return renderCTBlock.renderCTBlock(world, x, y, z);
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.renderID;
    }
}
