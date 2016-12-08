package net.lomeli.lomlib.client.handler

import net.lomeli.lomlib.client.render.item.ISpecialRender
import net.minecraft.client.Minecraft
import net.minecraft.util.EnumHand
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
object ClientTickHandler {
    var ticksInGame = 0f
    var partialTicks = 0f

    @SubscribeEvent
    fun renderTick(event : TickEvent.RenderTickEvent) {
        val mc = FMLClientHandler.instance().client
        if (event.phase == TickEvent.Phase.START) {
            partialTicks = event.renderTickTime
            if (mc.thePlayer != null) {
                val itemStack = mc.thePlayer.heldItemMainhand
                if (itemStack != null && itemStack.item is ISpecialRender) {
                    val special = itemStack.item as ISpecialRender
                    if (!special.canItemSwing(itemStack)) {
                        mc.thePlayer.swingProgressInt = -1
                        mc.thePlayer.isSwingInProgress = false

                    }
                }
            }
            //TODO: Stop swinging
        }
    }

    @SubscribeEvent
    fun playerTick(event : TickEvent.PlayerTickEvent) {
        if (event.side.isClient && event.phase == TickEvent.Phase.END) {
            val mc = FMLClientHandler.instance().client
            if (event.player != null) {
                val itemStack = event.player.heldItemMainhand
                if (itemStack != null && itemStack.item is ISpecialRender) {
                    val special = itemStack.item as ISpecialRender
                    if (!special.canItemSwing(itemStack) && !(event.player == mc.renderViewEntity && mc.gameSettings.thirdPersonView == 0)) {
                        if (event.player.itemInUseCount <= 0) {
                            event.player.resetActiveHand()
                            event.player.activeHand = EnumHand.MAIN_HAND
                        }
                    }
                }
            }
            //TODO: Stop swinging
        }
    }

    @SubscribeEvent
    fun clientTick(event : TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.END) {
            if (Minecraft.getMinecraft().inGameHasFocus)
                ticksInGame++
            else {
                val gui = Minecraft.getMinecraft().currentScreen
                if (gui == null || !gui.doesGuiPauseGame()) {
                    ticksInGame++
                    partialTicks = 0f
                }
            }
        }
    }
}