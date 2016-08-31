package net.lomeli.lomlib.lib

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation

class BasicInventory : IInventory {
    private var inventory: Array<ItemStack?>
    private val baseName: String
    private var customName: String?

    constructor(size: Int, baseName: String, customName: String) {
        this.baseName = baseName;
        this.customName = customName;
        this.inventory = arrayOfNulls<ItemStack>(size)
    }

    override fun markDirty() {

    }

    override fun getSizeInventory(): Int = inventory.size

    override fun getStackInSlot(index: Int): ItemStack? = inventory[index]

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        if (this.inventory[index] != null) {
            val itemstack: ItemStack

            if (this.inventory[index]!!.stackSize <= count) {
                itemstack = this.inventory[index] as ItemStack
                this.inventory[index] = null
                return itemstack
            } else {
                itemstack = this.inventory[index]!!.splitStack(count)

                if (this.inventory[index]?.stackSize == 0)
                    this.inventory[index] = null
                return itemstack
            }
        } else
            return null
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        val stack = this.getStackInSlot(index)
        inventory[index] = null
        return stack
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        inventory[index] = stack
    }

    override fun getInventoryStackLimit(): Int = 64

    override fun isUseableByPlayer(player: EntityPlayer): Boolean = true

    override fun openInventory(player: EntityPlayer) {
    }

    override fun closeInventory(player: EntityPlayer) {
    }

    override fun isItemValidForSlot(index: Int, stack: ItemStack): Boolean = getStackInSlot(index) == null

    override fun getField(id: Int): Int = 0

    override fun setField(id: Int, value: Int) {
    }

    override fun getFieldCount(): Int = 0

    override fun clear() {
        for (i in inventory.indices)
            inventory[i] = null
    }

    override fun getName(): String = if (hasCustomName()) customName.toString() else baseName

    override fun hasCustomName(): Boolean = customName != null && !customName!!.isEmpty()

    override fun getDisplayName(): ITextComponent = TextComponentTranslation(name)

    fun setCustomName(name: String) {
        customName = name
    }

    fun readNBT(tag: NBTTagCompound): NBTTagCompound {
        val nbttaglist = tag.getTagList("Items", 10)
        for (i in 0..nbttaglist.tagCount() - 1) {
            val nbttagcompound1 = nbttaglist.getCompoundTagAt(i)
            val j = nbttagcompound1.getByte("Slot").toInt() and 255

            if (j >= 0 && j < this.inventory.size)
                this.inventory[j] = ItemStack.loadItemStackFromNBT(nbttagcompound1)
        }
        if (tag.hasKey("customName"))
            customName = tag.getString("customName")
        return tag
    }

    fun writeNBT(tag: NBTTagCompound): NBTTagCompound {
        val nbttaglist = NBTTagList()

        for (i in this.inventory.indices) {
            if (this.inventory[i] != null) {
                val nbttagcompound1 = NBTTagCompound()
                nbttagcompound1.setByte("Slot", i.toByte())
                this.inventory[i]?.writeToNBT(nbttagcompound1)
                nbttaglist.appendTag(nbttagcompound1)
            }
        }

        tag.setTag("Items", nbttaglist)
        if (hasCustomName())
            tag.setString("customName", customName!!)
        return tag
    }
}