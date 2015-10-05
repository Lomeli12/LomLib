package net.lomeli.lomlib.lib;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;

public class BasicInventory implements IInventory {
    private String baseName, customName;
    private ItemStack[] inventory;

    public BasicInventory(int size, String name, String custom) {
        inventory = new ItemStack[size];
        baseName = name;
        customName = custom;
    }

    public BasicInventory(int size, String name) {
        this(size, name, null);
    }

    @Override
    public void markDirty() {

    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (this.inventory[index] != null) {
            ItemStack itemstack;

            if (this.inventory[index].stackSize <= count) {
                itemstack = this.inventory[index];
                this.inventory[index] = null;
                return itemstack;
            } else {
                itemstack = this.inventory[index].splitStack(count);

                if (this.inventory[index].stackSize == 0)
                    this.inventory[index] = null;
                return itemstack;
            }
        } else
            return null;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = this.getStackInSlot(index);
        inventory[index] = null;
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory[index] = stack;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return getStackInSlot(index) == null;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        for (int i = 0; i < inventory.length; i++)
            inventory[i] = null;
    }

    @Override
    public String getName() {
        return hasCustomName() ? customName : baseName;
    }

    @Override
    public boolean hasCustomName() {
        return customName != null && !customName.isEmpty();
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentTranslation(getName());
    }

    public void setCustomName(String name) {
        customName = name;
    }

    public NBTTagCompound readNBT(NBTTagCompound tag) {
        NBTTagList nbttaglist = tag.getTagList("Items", 10);
        for (int i = 0; i < nbttaglist.tagCount(); ++i) {
            NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
            int j = nbttagcompound1.getByte("Slot") & 255;

            if (j >= 0 && j < this.inventory.length)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
        }
        if (tag.hasKey("customName"))
            customName = tag.getString("customName");
        return tag;
    }

    public NBTTagCompound writeNBT(NBTTagCompound tag) {
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i) {
            if (this.inventory[i] != null) {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        tag.setTag("Items", nbttaglist);
        if (hasCustomName())
            tag.setString("customName", customName);
        return tag;
    }
}
