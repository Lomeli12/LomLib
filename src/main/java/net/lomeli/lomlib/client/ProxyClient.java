package net.lomeli.lomlib.client;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.renderer.InventoryEffectRenderer;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.client.gui.GuiOreDic;
import net.lomeli.lomlib.client.nei.NEIAddon;
import net.lomeli.lomlib.client.patreon.LayerCrown;
import net.lomeli.lomlib.core.Proxy;
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.entity.ItemCustomEgg;

public class ProxyClient extends Proxy {

    @Override
    public void preInit() {
        super.preInit();

        if (ObfUtil.isOptifineInstalled())
            LomLib.logger.logWarning("Optifine detected! If you run into any bugs, please test without optifine first before reporting, otherwise it WILL BE IGNORED! (Applies to both my mods and most others)");
    }

    @Override
    public void init() {
        super.init();
        FMLCommonHandler.instance().bus().register(LomLib.config);
        FMLCommonHandler.instance().bus().register(this);
        MinecraftForge.EVENT_BUS.register(this);
        LayerRenderer crownRenderer = new LayerCrown();
        ((RenderPlayer) Minecraft.getMinecraft().getRenderManager().skinMap.get("default")).addLayer(crownRenderer);
        ((RenderPlayer) Minecraft.getMinecraft().getRenderManager().skinMap.get("slim")).addLayer(crownRenderer);

        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(ItemCustomEgg.customEgg, new ItemMeshDefinition() {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack) {
                return new ModelResourceLocation("lomlib:spawnEgg", "inventory");
            }
        });
    }

    @Override
    public void postInit() {
        super.postInit();
        if (Loader.isModLoaded("NotEnoughItems"))
            NEIAddon.loadAddon();
    }

    @Override
    public void messageClient(String msg) {
        if (Minecraft.getMinecraft().thePlayer != null)
            Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentTranslation(msg));
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getMinecraft();
            if (mc.currentScreen != null) {
                if ((mc.currentScreen instanceof InventoryEffectRenderer) && !(mc.currentScreen instanceof GuiOreDic)) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_D) && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                        mc.getSoundHandler().playSound(PositionedSoundRecord.create(new ResourceLocation("gui.button.press"), 1.0F));
                        mc.displayGuiScreen(new GuiOreDic());
                    }
                }
            }
        }
    }
}
