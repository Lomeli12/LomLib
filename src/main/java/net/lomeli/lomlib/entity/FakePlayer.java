package net.lomeli.lomlib.entity;

import java.net.SocketAddress;

import com.mojang.authlib.GameProfile;

import net.lomeli.lomlib.item.ItemUtil;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ReflectionUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.server.management.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.play.client.C16PacketClientStatus;
import net.minecraft.network.Packet;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import cpw.mods.fml.common.FMLCommonHandler;

public class FakePlayer extends EntityPlayerMP {

    public boolean isSneaking = false;
    public ItemStack previousItem = null;
    public String myName = "[" + Strings.MOD_ID + "]";

    public FakePlayer(World world) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), (WorldServer) world, new GameProfile("[" + Strings.MOD_ID
                + "]", "[" + Strings.MOD_ID + "] FakePlayer"), new ItemInWorldManager(world));
        new NetHandlerPlayServer(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManagerFake(false), this);
        this.addedToChunk = false;
    }

    public static boolean isBlockBreakable(FakePlayer myFakePlayer, World worldObj, int x, int y, int z) {
        if(myFakePlayer == null)
            return worldObj.getBlock(x, y, z).getBlockHardness(worldObj, x, y, z) > -1.0F;
        return worldObj.getBlock(x, y, z).getPlayerRelativeBlockHardness(myFakePlayer, worldObj, x, y, z) > -1.0F;
    }

    public void sendChatToPlayer(ChatComponentText chatmessagecomponent) {
    }

    public boolean canCommandSenderUseCommand(int var1, String var2) {
        return false;
    }

    public ChunkCoordinates getPlayerCoordinates() {
        return null;
    }

    public void setItemInHand(ItemStack m_item) {
        this.inventory.currentItem = 0;
        this.inventory.setInventorySlotContents(0, m_item);
    }

    public void setItemInHand(int slot) {
        this.inventory.currentItem = slot;
    }

    public void openGui(Object mod, int modGuiId, World world, int x, int y, int z) {
    }

    public double getDistanceSq(double x, double y, double z) {
        return 0.0D;
    }

    public double getDistance(double x, double y, double z) {
        return 0.0D;
    }

    public boolean isSneaking() {
        return this.isSneaking;
    }

    public void onUpdate() {
        ItemStack itemstack = this.previousItem;
        ItemStack itemstack1 = getHeldItem();

        if(!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
            if(itemstack != null) {
                getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers());
            }
            if(itemstack1 != null) {
                getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers());
            }
            this.myName = ("[" + Strings.MOD_ID + "]" + (itemstack1 != null ? " using " + itemstack1.getDisplayName() : ""));
        }
        this.previousItem = (itemstack1 == null ? null : itemstack1.copy());
        this.theItemInWorldManager.updateBlockRemoving();
        ItemStack inUse = (ItemStack) ReflectionUtil.getField(this, "itemInUse", EntityPlayer.class);
        if(inUse != null)
            tickItemInUse(itemstack);
    }

    public void tickItemInUse(ItemStack updateItem) {
        ItemStack inUse = (ItemStack) ReflectionUtil.getField(this, "itemInUse", EntityPlayer.class);
        int count = ReflectionUtil.getIntField(this, "itemInUseCount");
        if((updateItem != null) && (ItemUtil.itemsEqualWithMetadata(this.previousItem, inUse))) {
            inUse.getItem().onUsingTick(inUse, this, count);
            if((count <= 25) && (count % 4 == 0)) {
                updateItemUse(updateItem, 5);
            }
            ReflectionUtil.setField(this, "itemInUseCount", count - 1);
            if((count == 0) && (!this.worldObj.isRemote))
                onItemUseFinish();
        }else {
            clearItemInUse();
        }
    }

    protected void updateItemUse(ItemStack par1ItemStack, int par2) {
        if(par1ItemStack.getItemUseAction() == EnumAction.drink) {
            playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if(par1ItemStack.getItemUseAction() == EnumAction.eat)
            playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2),
                    (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
    }

    public String getEntityName() {
        return this.myName;
    }

    public String getTranslatedEntityName() {
        return getEntityName();
    }

    public float getEyeHeight() {
        return 1.1F;
    }

    public ItemStack getCurrentArmor(int par1) {
        return new ItemStack(Items.diamond_chestplate);
    }

    public void addStat(StatBase par1StatBase, int par2) {
    }

    public void onDeath(DamageSource source) {
    }

    public void travelToDimension(int dim) {
    }

    public void updateClientInfo(C16PacketClientStatus pkt) {
    }

    public static class NetworkManagerFake extends NetworkManager {

        public NetworkManagerFake(boolean p_i45147_1_) {
            super(p_i45147_1_);
        }

        @Override
        public void setNetHandler(INetHandler nethandler) {
        }

        @Override
        public SocketAddress getSocketAddress() {
            return null;
        }
    }
}
