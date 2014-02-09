package net.lomeli.lomlib.client.gui;

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
        this.inventory.closeInventory();
        ;
    }

    @Override
    public boolean canTakeStack(EntityPlayer par1EntityPlayer) {

        return false;
    }

    @Override
    public void putStack(ItemStack par1ItemStack) {
        if(par1ItemStack != null) {
            int meta = par1ItemStack.getItemDamage();
            this.inventory.setInventorySlotContents(this.slotNumber, new ItemStack(par1ItemStack.getItem(), 1, meta));
            this.inventory.closeInventory();
        }
    }

}
