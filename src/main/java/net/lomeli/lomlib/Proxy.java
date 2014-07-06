package net.lomeli.lomlib;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ModLoaded;
import net.lomeli.lomlib.util.UpdateHelper;

public class Proxy {
    public UpdateHelper updateHelper;
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
        updateHelper = new UpdateHelper(Strings.MOD_NAME, Strings.MOD_ID.toLowerCase());
        sentMessage = false;
        try {
            updateHelper.check(Strings.UPDATE_URL, Strings.MAJOR, Strings.MINOR, Strings.REVISION);
        } catch (Exception e) {
        }
        if (!updateHelper.isUpdate()) {
            if (ModLoaded.isModInstalled("VersionChecker")) {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("modDisplayName", Strings.MOD_NAME);
                tag.setString("oldVersion", Strings.VERSION);
                tag.setString("newVersion", updateHelper.getNewVersion()[0] + "." + updateHelper.getNewVersion()[1] + "." + updateHelper.getNewVersion()[2]);
                tag.setString("updateUrl", updateHelper.getDownloadURL());
                tag.setBoolean("isDirectLink", true);
                FMLInterModComms.sendMessage("VersionChecker", "addUpdate", tag);
            }
        }
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
}
