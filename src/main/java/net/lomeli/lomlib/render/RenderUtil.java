package net.lomeli.lomlib.render;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.RenderUtils;
import codechicken.lib.vec.BlockCoord;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.Translation;

@SideOnly(Side.CLIENT)
public class RenderUtil {
    
    private static Set<LightCache> renderQueue = new HashSet<LightCache>();
    
    @ForgeSubscribe
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Tessellator tess = Tessellator.instance;
        WorldClient w = event.context.theWorld;
        
        GL11.glPushMatrix();
        RenderUtils.translateToWorldCoords(event.context.mc.renderViewEntity, event.partialTicks);
        prepareRenderState();

        for (Iterator<LightCache> it = renderQueue.iterator(); it.hasNext();) {
            LightCache cc = it.next();
            renderGlow(tess, w, cc);
        }

        restoreRenderState();
        GL11.glPopMatrix();
    }
    
    public static void addGlow(int x, int y, int z, int color, Cuboid6 box, int alpha){
        renderQueue.add(new LightCache(x, y, z, color, box, alpha));
    }
    
    public static void addGlow(int x, int y, int z, int color, Cuboid6 box){
        renderQueue.add(new LightCache(x, y, z, color, box));
    }
    
    private static void renderGlow(Tessellator tess, World world, LightCache cc) {
        CCRenderState.setBrightness(world, cc.pos.x, cc.pos.y, cc.pos.z);
        renderGlow(tess, cc.cube, cc.color, cc.t, cc.alpha);
    }
    
    public static void renderGlow(Tessellator tess, Cuboid6 cuboid, int colour, Transformation t, int alpha) {
        tess.setColorRGBA_I(colour, alpha);
        RenderUtils.renderBlock(cuboid, 0, t, null, null);
    }
    
    public static void renderGlow(Tessellator tess, Cuboid6 cuboid, int colour, Transformation t) {
        tess.setColorRGBA_I(colour, 128);
        RenderUtils.renderBlock(cuboid, 0, t, null, null);
    }
    
    public static void prepareRenderState() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(false);
        CCRenderState.reset();
        CCRenderState.startDrawing(7);
    }

    public static void restoreRenderState() {
        CCRenderState.draw();
        GL11.glDepthMask(true);
        GL11.glColor3f(1, 1, 1);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
    }
    
    private static class LightCache {
        final BlockCoord pos;
        final int color, alpha;
        final Cuboid6 cube;
        final Translation t;

        public LightCache(int x, int y, int z, int colorIndex, Cuboid6 cube) {
            this.pos = new BlockCoord(x, y, z);
            this.color = colorIndex;
            this.cube = cube;
            this.alpha = 128;
            t = new Translation(x, y, z);
        }
        
        public LightCache(int x, int y, int z, int colorIndex, Cuboid6 cube, int alpha) {
            this.pos = new BlockCoord(x, y, z);
            this.color = colorIndex;
            this.cube = cube;
            t = new Translation(x, y, z);
            this.alpha = alpha;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof LightCache) {
                LightCache o2 = (LightCache) o;
                return o2.pos.equals(pos) &&
                        o2.cube.min.equalsT(cube.min) &&
                        o2.cube.max.equalsT(cube.max);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return pos.hashCode();
        }
    }
}
