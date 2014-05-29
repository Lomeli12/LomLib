package net.lomeli.lomlib.client.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.client.render.RenderEntityBlock.BlockInterface;

/**
 * Rendering stuff I can't be asked to rewrite!
 * 
 * @author Anthony
 */
@SideOnly(Side.CLIENT)
public class RenderUtils {
    public static final Color blankColor = new Color(255, 255, 255);
    public static final ResourceLocation BLOCK_TEXTURE = TextureMap.locationBlocksTexture;
    public static final ResourceLocation ITEM_TEXTURE = TextureMap.locationItemsTexture;
    private static Map<Fluid, int[]> flowingRenderCache = new HashMap<Fluid, int[]>();
    private static Map<Fluid, int[]> stillRenderCache = new HashMap<Fluid, int[]>();
    public static final int DISPLAY_STAGES = 100;
    private static final BlockInterface liquidBlock = new BlockInterface();
    public static final ResourceLocation texEnchant = new ResourceLocation("textures/misc/enchanted_item_glint.png");
    public static final float magicNum = 0.0625F;

    public static void drawBlockFaces(RenderBlocks renderer, Block block, IIcon icon) {
        drawBlockFaces(renderer, block, icon, icon, icon, icon, icon, icon);
    }

    public static void drawBlockFaces(RenderBlocks renderer, Block block, IIcon i0, IIcon i1, IIcon i2, IIcon i3, IIcon i4, IIcon i5) {
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

    public static void renderBlock(IBlockAccess world, int x, int y, int z, Block block, RenderBlocks renderer, IIcon icon) {
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

    public static void bindTexture(String modid, String texture) {
        ResourceLocation rl = new ResourceLocation(modid + ":" + texture);
        bindTexture(rl);
    }

    public static void bindTexture(ResourceLocation rl) {
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(rl);
    }

    public static void resetColor() {
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    public static IIcon multiRenderPass(int renderPass, IIcon... icons) {
        return renderPass < icons.length ? icons[renderPass] : icons[0];
    }

    public static void applyColor(Color color) {
        applyColor(color, 1);
    }

    public static void applyColor(Color color, float alpha) {
        applyColor(color.getRGB(), alpha);
    }

    public static void applyColor(int rgb) {
        applyColor(rgb, 1);
    }

    public static void applyColor(float r, float g, float b) {
        applyColor(r, g, b, 1);
    }

    public static void applyColor(int rgb, float alpha) {
        float r = (rgb >> 16 & 255) / 255.0F;
        float g = (rgb >> 8 & 255) / 255.0F;
        float b = (rgb & 255) / 255.0F;
        applyColor(r, g, b, alpha);
    }

    public static void applyColor(float r, float g, float b, float alpha) {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(r, g, b, alpha);
    }

    public static IIcon getFluidTexture(FluidStack fluidStack, boolean flowing) {
        if (fluidStack == null)
            return null;
        return getFluidTexture(fluidStack.getFluid(), flowing);
    }

    public static IIcon getFluidTexture(Fluid fluid, boolean flowing) {
        if (fluid == null)
            return null;
        IIcon icon = flowing ? fluid.getFlowingIcon() : fluid.getStillIcon();
        if (icon == null)
            icon = ((TextureMap) getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
        return icon;
    }

    public static ResourceLocation getFluidSheet(FluidStack liquid) {
        if (liquid == null)
            return BLOCK_TEXTURE;
        return getFluidSheet(liquid.getFluid());
    }

    public static ResourceLocation getFluidSheet(Fluid liquid) {
        return BLOCK_TEXTURE;
    }

    public static void setColorForFluidStack(FluidStack fluidstack) {
        if (fluidstack != null)
            applyColor(fluidstack.getFluid().getColor(fluidstack));
    }

    public static int[] getFluidDisplayLists(FluidStack fluidStack, World world, boolean flowing) {
        if (fluidStack == null)
            return null;
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null)
            return null;
        Map<Fluid, int[]> cache = flowing ? flowingRenderCache : stillRenderCache;
        int[] diplayLists = cache.get(fluid);
        if (diplayLists != null)
            return diplayLists;

        diplayLists = new int[DISPLAY_STAGES];

        if (fluid.getBlock() != null) {
            liquidBlock.baseBlock = fluid.getBlock();
            liquidBlock.texture = getFluidTexture(fluidStack, flowing);
        }else {
            liquidBlock.baseBlock = Blocks.water;
            liquidBlock.texture = getFluidTexture(fluidStack, flowing);
        }

        cache.put(fluid, diplayLists);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_CULL_FACE);

        for (int s = 0; s < DISPLAY_STAGES; ++s) {
            diplayLists[s] = GLAllocation.generateDisplayLists(1);
            GL11.glNewList(diplayLists[s], 4864);

            liquidBlock.minX = 0.01f;
            liquidBlock.minY = 0;
            liquidBlock.minZ = 0.01f;

            liquidBlock.maxX = 0.99f;
            liquidBlock.maxY = (float) s / (float) DISPLAY_STAGES;
            liquidBlock.maxZ = 0.99f;

            RenderEntityBlock.INSTANCE.renderBlock(liquidBlock, world, 0, 0, 0, false, true);

            GL11.glEndList();
        }

        GL11.glColor4f(1, 1, 1, 1);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);

        return diplayLists;
    }

    public static void renderItemIn3d(ItemStack stack) {
        TextureManager textureManager = getTextureManager();
        if (textureManager == null)
            return;
        Item item = stack.getItem();

        GL11.glPushMatrix();

        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-0.5F, -0.5F, 1 / 32.0F);

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        int passes = item.getRenderPasses(stack.getItemDamage());
        for (int pass = 0; pass < passes; pass++) {
            textureManager.bindTexture(((stack.getItemSpriteNumber() == 0) ? TextureMap.locationBlocksTexture : TextureMap.locationItemsTexture));
            IIcon icon = item.getIcon(stack, pass);
            float minU = icon.getMinU();
            float maxU = icon.getMaxU();
            float minV = icon.getMinV();
            float maxV = icon.getMaxV();
            applyColor(item.getColorFromItemStack(stack, pass));
            ItemRenderer.renderItemIn2D(tessellator, maxU, minV, minU, maxV, icon.getIconWidth(), icon.getIconHeight(), 0.0625F);
        }

        if (stack.hasEffect(0)) {
            GL11.glDepthFunc(GL11.GL_EQUAL);
            GL11.glDisable(GL11.GL_LIGHTING);
            textureManager.bindTexture(texEnchant);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
            float f7 = 0.76F;
            GL11.glColor4f(0.5F * f7, 0.25F * f7, 0.8F * f7, 1.0F);
            GL11.glMatrixMode(GL11.GL_TEXTURE);
            GL11.glPushMatrix();
            float f8 = 0.125F;
            GL11.glScalef(f8, f8, f8);
            float f9 = Minecraft.getSystemTime() % 3000L / 3000.0F * 8.0F;
            GL11.glTranslatef(f9, 0.0F, 0.0F);
            GL11.glRotatef(-50.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glPushMatrix();
            GL11.glScalef(f8, f8, f8);
            f9 = Minecraft.getSystemTime() % 4873L / 4873.0F * 8.0F;
            GL11.glTranslatef(-f9, 0.0F, 0.0F);
            GL11.glRotatef(10.0F, 0.0F, 0.0F, 1.0F);
            ItemRenderer.renderItemIn2D(tessellator, 0.0F, 0.0F, 1.0F, 1.0F, 256, 256, 0.0625F);
            GL11.glPopMatrix();
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        GL11.glPopMatrix();
    }

    public static void renderEntityGhostItem(World world, ItemStack stack, RenderItem customRender, int x, int y, int z, ForgeDirection forgeDirection) {
        GL11.glPushMatrix();
        if (stack != null) {
            float scaleFactor = getGhostItemScaleFactor(stack, customRender);
            float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            EntityItem ghostEntityItem = new EntityItem(world);
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(stack);

            translateGhostItemByOrientation(ghostEntityItem.getEntityItem(), x, y, z, forgeDirection);

            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

            customRender.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
        }
        GL11.glPopMatrix();
    }

    private static void translateGhostItemByOrientation(ItemStack ghostItemStack, double x, double y, double z, ForgeDirection forgeDirection) {

        if (ghostItemStack != null) {
            if (ghostItemStack.getItem() instanceof ItemBlock) {
                switch(forgeDirection) {
                case DOWN : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.7F, (float) z + 0.5F);
                    return;
                }
                case UP : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.25F, (float) z + 0.5F);
                    return;
                }
                case NORTH : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.7F);
                    return;
                }
                case SOUTH : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.3F);
                    return;
                }
                case EAST : {
                    GL11.glTranslatef((float) x + 0.3F, (float) y + 0.5F, (float) z + 0.5F);
                    return;
                }
                case WEST : {
                    GL11.glTranslatef((float) x + 0.70F, (float) y + 0.5F, (float) z + 0.5F);
                    return;
                }
                case UNKNOWN : {
                    return;
                }
                default: {
                    return;
                }
                }
            }else {
                switch(forgeDirection) {
                case DOWN : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
                    return;
                }
                case UP : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.20F, (float) z + 0.5F);
                    return;
                }
                case NORTH : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.7F);
                    return;
                }
                case SOUTH : {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.3F);
                    return;
                }
                case EAST : {
                    GL11.glTranslatef((float) x + 0.3F, (float) y + 0.4F, (float) z + 0.5F);
                    return;
                }
                case WEST : {
                    GL11.glTranslatef((float) x + 0.70F, (float) y + 0.4F, (float) z + 0.5F);
                    return;
                }
                case UNKNOWN : {
                    return;
                }
                default: {
                    return;
                }
                }
            }
        }
    }

    private static float getGhostItemScaleFactor(ItemStack itemStack, RenderItem customRenderItem) {

        float scaleFactor = 1.0F;

        if (itemStack != null) {
            if (itemStack.getItem() instanceof ItemBlock) {
                switch(customRenderItem.getMiniBlockCount(itemStack, (byte) 0)) {
                case 1 :
                    return 0.90F;
                case 2 :
                    return 0.90F;
                case 3 :
                    return 0.90F;
                case 4 :
                    return 0.90F;
                case 5 :
                    return 0.80F;
                default:
                    return 0.90F;
                }
            }else {
                switch(customRenderItem.getMiniItemCount(itemStack, (byte) 0)) {
                case 1 :
                    return 0.65F;
                case 2 :
                    return 0.65F;
                case 3 :
                    return 0.65F;
                case 4 :
                    return 0.65F;
                default:
                    return 0.65F;
                }
            }
        }

        return scaleFactor;
    }

    public static TextureManager getTextureManager() {
        return Minecraft.getMinecraft().getTextureManager();
    }
}
