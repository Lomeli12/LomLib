package net.lomeli.lomlib.client.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.ForgeDirection;

public class RenderEntityItem {

    public static void renderEntityGhostItem(World world, ItemStack stack, RenderItem customRender, int x, int y, int z, ForgeDirection forgeDirection) {
        if (stack != null) {
            float scaleFactor = getGhostItemScaleFactor(stack, customRender);
            float rotationAngle = (float) (720.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);

            EntityItem ghostEntityItem = new EntityItem(world);
            ghostEntityItem.hoverStart = 0.0F;
            ghostEntityItem.setEntityItemStack(stack);

            translateGhostItemByOrientation(ghostEntityItem.getEntityItem(), x, y, z, forgeDirection);

            GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
            GL11.glRotatef(rotationAngle, 0.0F, 1.0F, 0.0F);

            customRender.doRenderItem(ghostEntityItem, 0, 0, 0, 0, 0);
        }
    }

    private static void translateGhostItemByOrientation(ItemStack ghostItemStack, double x, double y, double z, ForgeDirection forgeDirection) {

        if (ghostItemStack != null) {
            if (ghostItemStack.getItem() instanceof ItemBlock) {
                switch (forgeDirection) {
                case DOWN: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.7F, (float) z + 0.5F);
                    return;
                }
                case UP: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.25F, (float) z + 0.5F);
                    return;
                }
                case NORTH: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.7F);
                    return;
                }
                case SOUTH: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.3F);
                    return;
                }
                case EAST: {
                    GL11.glTranslatef((float) x + 0.3F, (float) y + 0.5F, (float) z + 0.5F);
                    return;
                }
                case WEST: {
                    GL11.glTranslatef((float) x + 0.70F, (float) y + 0.5F, (float) z + 0.5F);
                    return;
                }
                case UNKNOWN: {
                    return;
                }
                default: {
                    return;
                }
                }
            } else {
                switch (forgeDirection) {
                case DOWN: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.6F, (float) z + 0.5F);
                    return;
                }
                case UP: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.20F, (float) z + 0.5F);
                    return;
                }
                case NORTH: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.7F);
                    return;
                }
                case SOUTH: {
                    GL11.glTranslatef((float) x + 0.5F, (float) y + 0.4F, (float) z + 0.3F);
                    return;
                }
                case EAST: {
                    GL11.glTranslatef((float) x + 0.3F, (float) y + 0.4F, (float) z + 0.5F);
                    return;
                }
                case WEST: {
                    GL11.glTranslatef((float) x + 0.70F, (float) y + 0.4F, (float) z + 0.5F);
                    return;
                }
                case UNKNOWN: {
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
                switch (customRenderItem.getMiniBlockCount(itemStack)) {
                case 1:
                    return 0.90F;
                case 2:
                    return 0.90F;
                case 3:
                    return 0.90F;
                case 4:
                    return 0.90F;
                case 5:
                    return 0.80F;
                default:
                    return 0.90F;
                }
            } else {
                switch (customRenderItem.getMiniItemCount(itemStack)) {
                case 1:
                    return 0.65F;
                case 2:
                    return 0.65F;
                case 3:
                    return 0.65F;
                case 4:
                    return 0.65F;
                default:
                    return 0.65F;
                }
            }
        }

        return scaleFactor;
    }

}
