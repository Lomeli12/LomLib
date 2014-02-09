package net.lomeli.lomlib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderConnectedTextures implements ISimpleBlockRenderingHandler {
    protected int renderID;

    public static RenderFakeBlock fakeBlock = new RenderFakeBlock();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        block.setBlockBoundsForItemRender();
        renderer.setRenderBoundsFromBlock(block);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        float f = 1.0F;
        float f1 = 1.0F;
        float f2 = 1.0F;

        if(EntityRenderer.anaglyphEnable) {
            float f3 = (f * 30.0F + f1 * 59.0F + f2 * 11.0F) / 100.0F;
            float f4 = (f * 30.0F + f1 * 70.0F) / 100.0F;
            float f5 = (f * 30.0F + f2 * 70.0F) / 100.0F;
            f = f3;
            f1 = f4;
            f2 = f5;
        }

        GL11.glColor4f(f, f1, f2, 1.0F);
        renderer.colorRedTopLeft *= f;
        renderer.colorRedTopRight *= f;
        renderer.colorRedBottomLeft *= f;
        renderer.colorRedBottomRight *= f;
        renderer.colorGreenTopLeft *= f1;
        renderer.colorGreenTopRight *= f1;
        renderer.colorGreenBottomLeft *= f1;
        renderer.colorGreenBottomRight *= f1;
        renderer.colorBlueTopLeft *= f2;
        renderer.colorBlueTopRight *= f2;
        renderer.colorBlueBottomLeft *= f2;
        renderer.colorBlueBottomRight *= f2;

        RenderUtils.drawBlockFaces(renderer, block, block.getIcon(0, metadata), block.getIcon(1, metadata),
                block.getIcon(2, metadata), block.getIcon(3, metadata), block.getIcon(4, metadata), block.getIcon(5, metadata));
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if(renderer.hasOverrideBlockTexture())
            return renderer.renderStandardBlock(block, x, y, z);

        fakeBlock.setWorld(renderer.blockAccess);
        fakeBlock.curBlock = (world.getBlock(x, y, z).hashCode() * 16 + world.getBlockMetadata(x, y, z));
        block.setBlockBoundsBasedOnState(fakeBlock.blockAccess, x, y, z);
        fakeBlock.setRenderBoundsFromBlock(block);
        return fakeBlock.renderStandardBlock(block, x, y, z);
    }

    @Override
    public int getRenderId() {
        return this.renderID;
    }

    public RenderConnectedTextures setRenderID(int id) {
        this.renderID = id;
        return this;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

}
