package net.lomeli.lomlib.util.nbt

import net.lomeli.lomlib.util.items.ItemUtil
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound

import net.minecraftforge.oredict.OreDictionary

/**
 * NBTHelper Using the NBT helper from pahimar's Equivalent Exchange 3 mod.

 * @author pahimar
 * *
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
object NBTUtil {

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null

     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked
     * *                  for initialization
     */
    fun initNBTTagCompound(itemStack: ItemStack) {
        if (itemStack.tagCompound == null)
            itemStack.tagCompound = NBTTagCompound()
    }

    fun hasTag(itemStack: ItemStack, keyName: String): Boolean {
        if (itemStack.tagCompound != null)
            return itemStack.tagCompound!!.hasKey(keyName)

        return false
    }

    fun hasTag(itemStack: ItemStack, keyName: String, type: Int): Boolean {
        if (itemStack.tagCompound != null)
            return itemStack.tagCompound!!.hasKey(keyName, type)

        return false
    }

    fun removeTag(itemStack: ItemStack, keyName: String) {
        if (itemStack.tagCompound != null)
            itemStack.tagCompound!!.removeTag(keyName)
    }

    // String
    fun getString(itemStack: ItemStack, keyName: String): String {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setString(itemStack, keyName, "")

        return itemStack.tagCompound!!.getString(keyName)
    }

    fun setString(itemStack: ItemStack, keyName: String, keyValue: String) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setString(keyName, keyValue)
    }

    // boolean
    fun getBoolean(itemStack: ItemStack, keyName: String): Boolean {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setBoolean(itemStack, keyName, false)

        return itemStack.tagCompound!!.getBoolean(keyName)
    }

    fun setBoolean(itemStack: ItemStack, keyName: String, keyValue: Boolean) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setBoolean(keyName, keyValue)
    }

    // byte
    fun getByte(itemStack: ItemStack, keyName: String): Byte {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setByte(itemStack, keyName, 0.toByte())

        return itemStack.tagCompound!!.getByte(keyName)
    }

    fun setByte(itemStack: ItemStack, keyName: String, keyValue: Byte) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setByte(keyName, keyValue)
    }

    // short
    fun getShort(itemStack: ItemStack, keyName: String): Short {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setShort(itemStack, keyName, 0.toShort())

        return itemStack.tagCompound!!.getShort(keyName)
    }

    fun setShort(itemStack: ItemStack, keyName: String, keyValue: Short) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setShort(keyName, keyValue)
    }

    // int
    fun getInt(itemStack: ItemStack, keyName: String): Int {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setInteger(itemStack, keyName, 0)

        return itemStack.tagCompound!!.getInteger(keyName)
    }

    fun setInteger(itemStack: ItemStack, keyName: String, keyValue: Int) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setInteger(keyName, keyValue)
    }

    // long
    fun getLong(itemStack: ItemStack, keyName: String): Long {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setLong(itemStack, keyName, 0)

        return itemStack.tagCompound!!.getLong(keyName)
    }

    fun setLong(itemStack: ItemStack, keyName: String, keyValue: Long) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setLong(keyName, keyValue)
    }

    // float
    fun getFloat(itemStack: ItemStack, keyName: String): Float {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setFloat(itemStack, keyName, 0f)

        return itemStack.tagCompound!!.getFloat(keyName)
    }

    fun setFloat(itemStack: ItemStack, keyName: String, keyValue: Float) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setFloat(keyName, keyValue)
    }

    // double
    fun getDouble(itemStack: ItemStack, keyName: String): Double {
        initNBTTagCompound(itemStack)

        if (!itemStack.tagCompound!!.hasKey(keyName))
            setDouble(itemStack, keyName, 0.0)

        return itemStack.tagCompound!!.getDouble(keyName)
    }

    fun setDouble(itemStack: ItemStack, keyName: String, keyValue: Double) {
        initNBTTagCompound(itemStack)

        itemStack.tagCompound!!.setDouble(keyName, keyValue)
    }

    fun getCompound(itemStack: ItemStack, keyName: String): NBTTagCompound {
        initNBTTagCompound(itemStack)
        if (!itemStack.tagCompound!!.hasKey(keyName, 10))
            setCompound(itemStack, keyName, NBTTagCompound())
        return itemStack.tagCompound!!.getCompoundTag(keyName)
    }

    fun setCompound(itemStack: ItemStack, keyName: String, keyValue: NBTTagCompound) {
        initNBTTagCompound(itemStack)
        itemStack.tagCompound!!.setTag(keyName, keyValue)
    }

    fun areItemStacksEqualNoNBT(stackA: ItemStack, stackB: ItemStack?): Boolean {
        if (stackB == null)
            return false

        return ItemUtil.areItemsTheSame(stackA, stackB) && if (stackA.itemDamage == OreDictionary.WILDCARD_VALUE)
            true
        else if (stackB.itemDamage == OreDictionary.WILDCARD_VALUE)
            true
        else if (stackA.hasSubtypes == false)
            true
        else
            stackB.itemDamage == stackA.itemDamage
    }

    fun doNBTsMatch(nbtA: NBTTagCompound?, nbtB: NBTTagCompound?): Boolean {
        return if (nbtA == null) if (nbtB == null) true else false else if (nbtB == null) false else nbtA == nbtB
    }

    fun getPersistedTag(player: EntityPlayer?): NBTTagCompound? {
        if (player == null) return null
        return if (player.entityData.hasKey(EntityPlayer.PERSISTED_NBT_TAG, 10)) player.entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG) else NBTTagCompound()
    }

    fun setPersistedTag(player: EntityPlayer?, tag: NBTTagCompound) {
        if (player == null) return
        player.entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, tag)
    }
}