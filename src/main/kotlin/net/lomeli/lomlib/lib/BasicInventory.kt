package net.lomeli.lomlib.lib

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.IInventory
import net.minecraft.inventory.ItemStackHelper
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.NonNullList
import net.minecraft.util.text.ITextComponent
import net.minecraft.util.text.TextComponentTranslation

class BasicInventory : IInventory {
    private var contents: NonNullList<ItemStack>
    private val baseName: String
    private var customName: String?

    constructor(size: Int, baseName: String, customName: String) {
        this.baseName = baseName
        this.customName = customName
        this.contents = NonNullList.withSize(size, ItemStack.EMPTY)
    }

    override fun markDirty() {
    }

    override fun getSizeInventory(): Int = contents.size

    override fun getStackInSlot(index: Int): ItemStack? = contents[index]

    override fun decrStackSize(index: Int, count: Int): ItemStack? {
        if (this.contents[index] != ItemStack.EMPTY) {
            val itemstack: ItemStack

            if (this.contents[index].count <= count) {
                itemstack = this.contents[index]
                this.contents[index] = null
                return itemstack
            } else {
                itemstack = this.contents[index].splitStack(count)

                if (this.contents[index].count == 0)
                    this.contents[index] = null
                return itemstack
            }
        } else
            return null
    }

    override fun removeStackFromSlot(index: Int): ItemStack? {
        val stack = this.getStackInSlot(index)
        contents[index] = ItemStack.EMPTY
        return stack
    }

    override fun setInventorySlotContents(index: Int, stack: ItemStack?) {
        if (stack == null || stack.isEmpty) contents[index] = ItemStack.EMPTY
        else contents[index] = stack
    }

    override fun getInventoryStackLimit(): Int = 64

    override fun isUsableByPlayer(player: EntityPlayer?): Boolean = true

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
        for (i in contents.indices)
            contents[i] = ItemStack.EMPTY
    }

    override fun isEmpty(): Boolean {
        throw UnsupportedOperationException("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getName(): String = if (hasCustomName()) customName.toString() else baseName

    override fun hasCustomName(): Boolean = customName != null && !customName!!.isEmpty()

    override fun getDisplayName(): ITextComponent = TextComponentTranslation(name)

    fun setCustomName(name: String) {
        customName = name
    }

    fun readNBT(tag: NBTTagCompound): NBTTagCompound {
        ItemStackHelper.loadAllItems(tag, this.contents)
        if (tag.hasKey("customName"))
            customName = tag.getString("customName")
        return tag
    }

    fun writeNBT(tag: NBTTagCompound): NBTTagCompound {
        ItemStackHelper.saveAllItems(tag, this.contents)
        if (hasCustomName())
            tag.setString("customName", customName!!)
        return tag
    }
}