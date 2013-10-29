package net.lomeli.lomlib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class PhantomSlot extends Slot {

    public PhantomSlot(IInventory par1iInventory, int par2, int par3, int par4) {
        super(par1iInventory, par2, par3, par4);
    }

    @Override
    public void onPickupFromSlot(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack) {
        this.inventory.setInventorySlotContents(this.slotNumber, null);
        this.inventory.onInventoryChanged();
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {
        return false;
    }

    @Override
    public void putStack(ItemStack par1ItemStack) {
        if(par1ItemStack != null) {
            int id = par1ItemStack.itemID, meta = par1ItemStack.getItemDamage();
            this.inventory.setInventorySlotContents(this.slotNumber, new ItemStack(id, 1, meta));
            this.inventory.onInventoryChanged();
        }
    }

}
