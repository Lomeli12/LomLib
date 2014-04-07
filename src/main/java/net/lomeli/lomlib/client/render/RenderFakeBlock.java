package net.lomeli.lomlib.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RenderFakeBlock extends RenderBlocks {
    public static final double[] u = {-1.0D, 1.0D, 1.0D, -1.0D};
    public static final double[] v = {1.0D, 1.0D, -1.0D, -1.0D};

    public int curBlock = 0;
    public boolean isOpaque = false;

    public void setWorld(IBlockAccess blockAccess) {
        this.blockAccess = blockAccess;
    }

    public void setLightAndColor(double u2, double v2, int side) {
        if (this.enableAO) {
            Tessellator tessellator = Tessellator.instance;
            double u = 0.0D;
            double v = 0.0D;

            if ((side == 0) || (side == 1)) {
                u = 1.0D - u2;
                v = 1.0D - v2;
            } else if (side == 2) {
                u = v2;
                v = 1.0D - u2;
            } else if (side == 3) {
                u = u2;
                v = v2;
            } else if (side == 4) {
                u = v2;
                v = 1.0D - u2;
            } else if (side == 5) {
                u = 1.0D - v2;
                v = u2;
            }

            tessellator.setBrightness(this.mixAoBrightness(this.brightnessTopLeft, this.brightnessTopRight,
                    this.brightnessBottomLeft, this.brightnessBottomRight, u * v, v * (1.0D - u), (1.0D - v) * u, (1.0D - u)
                    * (1.0D - v)));

        }
    }

    public float mix(double tl, double tr, double bl, double br, double u, double v) {
        return (float) (tl * u * v + tr * (1.0D - u) * v + bl * u * (1.0D - v) + br * (1.0D - u) * (1.0D - v));
    }

    public void renderSide(Block block, double x, double y, double z, double ox, double oy, double oz, int ax, int ay, int az,
                           int bx, int by, int bz, IconConnected icon, int side) {
        Tessellator tessellator = Tessellator.instance;

        this.isOpaque = block.isOpaqueCube();

        for (int j = 0; j < 4; j++) {
            int i = getType(block, side, (int) x, (int) y, (int) z, ax * (int) u[j], ay * (int) u[j], az * (int) u[j], bx
                    * (int) v[j], by * (int) v[j], bz * (int) v[j], (int) (ox * 2.0D - 1.0D), (int) (oy * 2.0D - 1.0D),
                    (int) (oz * 2.0D - 1.0D));
            icon.setType(i);
            double cx = x + ox + ax * u[j] / 4.0D + bx * v[j] / 4.0D;
            double cy = y + oy + ay * u[j] / 4.0D + by * v[j] / 4.0D;
            double cz = z + oz + az * u[j] / 4.0D + bz * v[j] / 4.0D;

            for (int k = 0; k < 4; k++) {
                setLightAndColor(0.5D + u[j] * 0.25D + u[k] * 0.25D, 0.5D + v[j] * 0.25D + v[k] * 0.25D, side);
                tessellator.addVertexWithUV(cx + u[k] * ax * 0.25D + v[k] * bx * 0.25D, cy + u[k] * ay * 0.25D + v[k] * by
                        * 0.25D, cz + u[k] * az * 0.25D + v[k] * bz * 0.25D,
                        icon.getInterpolatedU(16.0D - (8.0D + u[j] * 4.0D + u[k] * 4.0D)),
                        icon.getInterpolatedV(16.0D - (8.0D + v[j] * 4.0D + v[k] * 4.0D)));
            }

            icon.resetType();
        }
    }

    public int getSideFromDir(int dx, int dy, int dz) {
        if (dy < 0)
            return 0;
        if (dy > 0)
            return 1;
        if (dz < 0)
            return 2;
        if (dz > 0)
            return 3;
        if (dx < 0)
            return 4;
        if (dx > 0)
            return 5;

        return 0;
    }

    public boolean matchBlock(int side2, int x2, int y2, int z2) {
        return this.curBlock == this.blockAccess.getBlock(x2, y2, z2).hashCode() * 16
                + this.blockAccess.getBlockMetadata(x2, y2, z2);
    }

    public int getType(Block block, int side, int x, int y, int z, int ax, int ay, int az, int bx, int by, int bz, int cx,
                       int cy, int cz) {
        int sidea = getSideFromDir(ax, ay, az);
        int sideb = getSideFromDir(bx, by, bz);

        boolean a = (matchBlock(side, x + ax, y + ay, z + az)) && (!matchBlock(sidea, x + cx, y + cy, z + cz))
                && (!matchBlock(Facing.oppositeSide[sidea], x + ax + cx, y + ay + cy, z + az + cz));
        boolean b = (matchBlock(side, x + bx, y + by, z + bz)) && (!matchBlock(sideb, x + cx, y + cy, z + cz))
                && (!matchBlock(Facing.oppositeSide[sideb], x + bx + cx, y + by + cy, z + bz + cz));
        if (a) {
            if (b) {
                if (matchBlock(side, x + ax + bx, y + ay + by, z + az + bz)) {
                    if ((matchBlock(Facing.oppositeSide[sidea], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz))
                            || (matchBlock(Facing.oppositeSide[sideb], x + ax + bx + cx, y + ay + by + cy, z + az + bz + cz))
                            || (matchBlock(sidea, x + bx + cx, y + by + cy, z + bz + cz))
                            || (matchBlock(sideb, x + ax + cx, y + ay + cy, z + az + cz)))
                        return 4;

                    return 3;
                }
                return 4;
            }

            return 2;
        }
        if (b)
            return 1;

        return 0;
    }

    @Override
    public void renderFaceYNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 0.5D, 0.0D, 0.5D, 1, 0, 0, 0, 0, -1, (IconConnected) icon, 0);
        else
            super.renderFaceYNeg(block, x, y, z, icon);
    }

    @Override
    public void renderFaceYPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 0.5D, 1.0D, 0.5D, -1, 0, 0, 0, 0, -1, (IconConnected) icon, 1);
        else
            super.renderFaceYPos(block, x, y, z, icon);
    }

    @Override
    public void renderFaceXNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 0.0D, 0.5D, 0.5D, 0, 0, -1, 0, 1, 0, (IconConnected) icon, 4);
        else
            super.renderFaceXNeg(block, x, y, z, icon);
    }

    @Override
    public void renderFaceXPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 1.0D, 0.5D, 0.5D, 0, 0, 1, 0, 1, 0, (IconConnected) icon, 5);
        else
            super.renderFaceXPos(block, x, y, z, icon);
    }

    @Override
    public void renderFaceZNeg(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 0.5D, 0.5D, 0.0D, 1, 0, 0, 0, 1, 0, (IconConnected) icon, 2);
        else
            super.renderFaceZNeg(block, x, y, z, icon);
    }

    @Override
    public void renderFaceZPos(Block block, double x, double y, double z, IIcon icon) {
        if (this.hasOverrideBlockTexture())
            icon = this.overrideBlockTexture;

        if ((icon instanceof IconConnected))
            renderSide(block, x, y, z, 0.5D, 0.5D, 1.0D, -1, 0, 0, 0, 1, 0, (IconConnected) icon, 3);
        else
            super.renderFaceZPos(block, x, y, z, icon);
    }
}
