package net.lomeli.lomlib.entity;

import java.net.SocketAddress;

import net.lomeli.lomlib.item.ItemUtil;
import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ReflectionUtil;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemInWorldManager;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetServerHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet204ClientInfo;
import net.minecraft.stats.StatBase;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;

public class FakePlayer extends EntityPlayerMP {

    public boolean isSneaking = false;
    public ItemStack previousItem = null;
    public String myName = "[" + Strings.MOD_ID + "]";

    public FakePlayer(World world) {
        super(FMLCommonHandler.instance().getMinecraftServerInstance(), world, "[" + Strings.MOD_ID + "]", new ItemInWorldManager(world));
        new NetServerHandler(FMLCommonHandler.instance().getMinecraftServerInstance(), new NetworkManagerFake(), this);
        this.addedToChunk = false;
    }

    public static boolean isBlockBreakable(FakePlayer myFakePlayer, World worldObj, int x, int y, int z) {
        int blockID = worldObj.getBlockId(x, y, z);
        if (myFakePlayer == null) {
            return Block.blocksList[blockID].getBlockHardness(worldObj, x, y, z) > -1.0F;
        }
        return Block.blocksList[blockID].getPlayerRelativeBlockHardness(myFakePlayer, worldObj, x, y, z) > -1.0F;
    }

    public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
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

        if (!ItemStack.areItemStacksEqual(itemstack1, itemstack)) {
            if (itemstack != null) {
                getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers());
            }
            if (itemstack1 != null) {
                getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers());
            }
            this.myName = ("[" + Strings.MOD_ID + "]" + (itemstack1 != null ? " using " + itemstack1.getDisplayName() : ""));
        }
        this.previousItem = (itemstack1 == null ? null : itemstack1.copy());
        this.theItemInWorldManager.updateBlockRemoving();
        ItemStack inUse = (ItemStack) ReflectionUtil.getField(this, "itemInUse", EntityPlayer.class);
        if (inUse != null)
            tickItemInUse(itemstack);
    }

    public void tickItemInUse(ItemStack updateItem) {
        ItemStack inUse = (ItemStack) ReflectionUtil.getField(this, "itemInUse", EntityPlayer.class);
        int count = ReflectionUtil.getIntField(this, "itemInUseCount");
        if ((updateItem != null) && (ItemUtil.itemsEqualWithMetadata(this.previousItem, inUse))) {
            inUse.getItem().onUsingItemTick(inUse, this, count);
            if ((count <= 25) && (count % 4 == 0)) {
                updateItemUse(updateItem, 5);
            }
            ReflectionUtil.setField(this, "itemInUseCount", count - 1);
            if ((count == 0) && (!this.worldObj.isRemote))
                onItemUseFinish();
        } else {
            clearItemInUse();
        }
    }

    protected void updateItemUse(ItemStack par1ItemStack, int par2) {
        if (par1ItemStack.getItemUseAction() == EnumAction.drink) {
            playSound("random.drink", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (par1ItemStack.getItemUseAction() == EnumAction.eat)
            playSound("random.eat", 0.5F + 0.5F * this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
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
        return new ItemStack(Item.plateDiamond);
    }

    public void addStat(StatBase par1StatBase, int par2) {
    }

    public void onDeath(DamageSource source) {
    }

    public void travelToDimension(int dim) {
    }

    public void updateClientInfo(Packet204ClientInfo pkt) {
    }

    public static class NetworkManagerFake implements INetworkManager {

        @Override
        public void setNetHandler(NetHandler nethandler) {
        }

        @Override
        public void addToSendQueue(Packet packet) {
        }

        @Override
        public void wakeThreads() {
        }

        @Override
        public void processReadPackets() {
        }

        @Override
        public SocketAddress getSocketAddress() {
            return null;
        }

        @Override
        public void serverShutdown() {
        }

        @Override
        public int packetSize() {
            return 0;
        }

        @Override
        public void networkShutdown(String s, Object... var2) {
        }

        @Override
        public void closeConnections() {
        }

    }
}
