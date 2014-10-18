package net.lomeli.lomlib;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.version.VersionChecker;

public class Proxy {
    public VersionChecker updater;
    public boolean sentMessage;

    public void doStuffPre() {
        LomLib.logger.logBasic("Pre-Init");
    }

    public void doStuffInit() {
        LomLib.logger.logBasic("Init");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void doStuffPost() {
        LomLib.logger.logBasic("Post-Init");
    }

    public void checkForUpdate() {
        updater = new VersionChecker(Strings.UPDATE_URL, Strings.MOD_ID, Strings.MOD_NAME, Strings.MAJOR, Strings.MINOR, Strings.REVISION);
        updater.checkForUpdates();
    }

    public void messageClient(String msg){
    }

    @SubscribeEvent
    public void onInteractEvent(PlayerInteractEvent event) {
        if (LomLib.slime) {
            if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                World world = event.entityPlayer.worldObj;
                ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
                Block block = world.getBlock(event.x, event.y, event.z);
                int meta = world.getBlockMetadata(event.x, event.y, event.z);
                if (stack != null && stack.getUnlocalizedName().equals(Items.slime_ball.getUnlocalizedName())) {
                    if (block.getUnlocalizedName().equals(Blocks.piston.getUnlocalizedName())) {
                        world.setBlock(event.x, event.y, event.z, Blocks.sticky_piston, meta, 3);
                        if (!event.entityPlayer.capabilities.isCreativeMode)
                            event.entityPlayer.inventory.decrStackSize(event.entityPlayer.inventory.currentItem, 1);
                    }
                }
            }
        }
    }
}
