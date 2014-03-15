package net.lomeli.lomlib.client.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderUtils {
    public static void drawBlockFaces(RenderBlocks renderer, Block block, Icon icon) {
        drawBlockFaces(renderer, block, icon, icon, icon, icon, icon, icon);
    }

    public static void drawBlockFaces(RenderBlocks renderer, Block block, Icon i0, Icon i1, Icon i2, Icon i3, Icon i4, Icon i5) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glTranslatef(-0.5F, 0.0F, -0.5F);

        if (block != null) {
            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, -1F, 0F);
            renderer.renderFaceYNeg(block, 0D, -0.5D, 0D, i0);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 1F, 0F);
            renderer.renderFaceYPos(block, 0D, -0.5D, 0D, i1);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 0F, -1F);
            renderer.renderFaceZNeg(block, 0D, -0.5D, 0D, i2);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(0F, 0F, 1F);
            renderer.renderFaceZPos(block, 0D, -0.5D, 0D, i3);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(-1F, 0F, 0F);
            renderer.renderFaceXNeg(block, 0D, -0.5D, 0D, i4);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setNormal(1F, 0F, 0F);
            renderer.renderFaceXPos(block, 0D, -0.5D, 0D, i5);
            tessellator.draw();
            GL11.glTranslatef(0.5F, 0.0F, 0.5F);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    public static void renderBlock(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, Icon icon) {
        if (block.shouldSideBeRendered(world, x + 1, y, z, 6))
            renderer.renderFaceXPos(block, x, y, z, icon);
        if (block.shouldSideBeRendered(world, x - 1, y, z, 6))
            renderer.renderFaceXNeg(block, x, y, z, icon);
        if (block.shouldSideBeRendered(world, x, y, z + 1, 6))
            renderer.renderFaceZPos(block, x, y, z, icon);
        if (block.shouldSideBeRendered(world, x, y, z - 1, 6))
            renderer.renderFaceZNeg(block, x, y, z, icon);
        if (block.shouldSideBeRendered(world, x, y + 1, z, 6))
            renderer.renderFaceYPos(block, x, y, z, icon);
        if (block.shouldSideBeRendered(world, x, y - 1, z, 6))
            renderer.renderFaceYNeg(block, x, y, z, icon);
    }

    public static void applyColor(Color color, float alpha) {
        float r = (color.getRed() / 255f), g = (color.getGreen() / 255f), b = (color.getBlue() / 255f);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, alpha);
    }

    public static void applyColor(Color color) {
        applyColor(color, 1);
    }

    public static void resetColor() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }
}
