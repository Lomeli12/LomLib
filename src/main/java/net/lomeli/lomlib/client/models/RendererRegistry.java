package net.lomeli.lomlib.client.models;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;

import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RendererRegistry {
    private HashMap<Item, IItemRenderer> itemRendererList;
    private static RendererRegistry INSTANCE;

    public static RendererRegistry instance() {
        if (INSTANCE == null)
            INSTANCE = new RendererRegistry();
        return INSTANCE;
    }

    public static void initLayerRenderer() {
        MinecraftForge.EVENT_BUS.register(instance());
        RendererLivingEntity rendererLivingEntity = (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().skinMap.get("default");
        if (rendererLivingEntity != null)
            rendererLivingEntity.addLayer(new LayerItemModel(rendererLivingEntity));
        rendererLivingEntity = (RendererLivingEntity) Minecraft.getMinecraft().getRenderManager().skinMap.get("slim");
        if (rendererLivingEntity != null)
            rendererLivingEntity.addLayer(new LayerItemModel(rendererLivingEntity));
        Map<Class<?>, Render> renderMap = Minecraft.getMinecraft().getRenderManager().entityRenderMap;
        for (Map.Entry<Class<?>, Render> entry : renderMap.entrySet()) {
            Render render = entry.getValue();
            if (render != null && render instanceof RendererLivingEntity) {
                RendererLivingEntity renderLiving = (RendererLivingEntity) render;
                if (renderLiving.getMainModel() instanceof ModelBiped)
                    renderLiving.addLayer(new LayerItemModel(renderLiving));
            }
        }
    }

    private RendererRegistry() {
        itemRendererList = new HashMap<Item, IItemRenderer>();
    }

    public void addItemRenderer(IItemRenderer renderer, Item item) {
        if (item == null || renderer == null)
            return;
        itemRendererList.put(item, renderer);
    }

    public boolean itemHasRenderer(Item item) {
        return item != null && itemRendererList.containsKey(item);
    }

    public boolean stackHasRenderer(ItemStack stack) {
        return stack != null && itemHasRenderer(stack.getItem());
    }

    public IItemRenderer getRenderer(Item item) {
        return itemRendererList.get(item);
    }

    @SubscribeEvent
    public void renderHand(RenderHandEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP player = mc.thePlayer;
        if (player != null) {
            ItemStack stack = player.getHeldItem();
            if (stack != null && stack.getItem() != null && RendererRegistry.instance().stackHasRenderer(stack)) {
                boolean flag = mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase) mc.getRenderViewEntity()).isPlayerSleeping();
                if (mc.gameSettings.thirdPersonView == 0 && !flag && !mc.gameSettings.hideGUI && !mc.playerController.isSpectator()) {
                    Item item = stack.getItem();
                    IItemRenderer renderer = RendererRegistry.instance().getRenderer(item);
                    renderItemFirstPerson(player, event.partialTicks, renderer, stack);
                }
            }
        }
    }

    public void renderItemFirstPerson(EntityPlayerSP entityPlayerSP, float partialTick, IItemRenderer renderer, ItemStack item) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        float pitch = entityPlayerSP.prevRotationPitch + (entityPlayerSP.rotationPitch - entityPlayerSP.prevRotationPitch) * partialTick;
        float yaw = entityPlayerSP.prevRotationYaw + (entityPlayerSP.rotationYaw - entityPlayerSP.prevRotationYaw) * partialTick;

        GlStateManager.translate(0, entityPlayerSP.getEyeHeight() / 2, 0);

        GlStateManager.rotate(yaw, 0, -1, 0);
        GlStateManager.rotate(pitch, 1, 0, 0);
        RenderHelper.enableStandardItemLighting();

        int i = mc.theWorld.getCombinedLight(new BlockPos(entityPlayerSP.posX, entityPlayerSP.posY + (double) entityPlayerSP.getEyeHeight(), entityPlayerSP.posZ), 0);
        float f = (float) (i & 65535);
        float fl = (float) (i >> 16);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, f, fl);

        mc.entityRenderer.enableLightmap();
        renderer.renderItem(RenderType.FIRST_PERSON, entityPlayerSP, item, partialTick);
        mc.entityRenderer.disableLightmap();

        RenderHelper.disableStandardItemLighting();
        GlStateManager.popMatrix();
    }
}
