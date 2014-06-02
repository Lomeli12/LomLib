package net.lomeli.lomlib;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.world.World;

import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Proxy {
    public void doStuffPre() {
        LomLib.logger.logBasic("Pre-Init");
    }

    public void doStuffInit() {
        LomLib.logger.logBasic("Init");
    }

    public void doStuffPost() {
        LomLib.logger.logBasic("Post-Init");
    }
    
    public EntityPlayer getPlayerFromNetHandler(INetHandler handler) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else
            return null;
    }

    @SubscribeEvent
    public void onInteractEvent(PlayerInteractEvent event) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            World world = event.entityPlayer.worldObj;
            ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
            Block block = world.getBlock(event.x, event.y, event.z);
            int meta = world.getBlockMetadata(event.x, event.y, event.z);
            if (stack != null && stack.getUnlocalizedName().equals(Items.slime_ball.getUnlocalizedName())) {
                System.out.println("Slime ball");
                if (block.getUnlocalizedName().equals(Blocks.piston.getUnlocalizedName())) {
                    world.setBlock(event.x, event.y, event.z, Blocks.sticky_piston, meta, 3);
                    if (!event.entityPlayer.capabilities.isCreativeMode)
                        event.entityPlayer.inventory.decrStackSize(event.entityPlayer.inventory.currentItem, 1);
                }
            }
        }
    }
}
