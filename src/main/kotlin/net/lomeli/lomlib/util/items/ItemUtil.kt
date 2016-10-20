package net.lomeli.lomlib.util.items

import net.minecraft.entity.item.EntityItem
import net.minecraft.inventory.IInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World

import net.minecraftforge.oredict.OreDictionary

import net.lomeli.lomlib.util.nbt.NBTUtil
import net.minecraftforge.fml.common.registry.GameRegistry

object ItemUtil {
    fun consumeItem(stack: ItemStack): ItemStack? {
        if (stack.stackSize == 1) {
            if (stack.item.hasContainerItem(stack))
                return stack.item.getContainerItem(stack)
            else
                return null
        } else {
            stack.splitStack(1)

            return stack
        }
    }

    fun itemsEqualWithMetadata(stackA: ItemStack?, stackB: ItemStack?): Boolean = if (stackA == null) if (stackB == null) true else false else stackB != null && areItemsTheSame(stackA, stackB) && (stackA.hasSubtypes == false || stackA.itemDamage == stackB.itemDamage)

    fun itemsEqualWithMetadata(stackA: ItemStack?, stackB: ItemStack?, checkNBT: Boolean): Boolean {
        return if (stackA == null)
            if (stackB == null) true else false
        else
            stackB != null && areItemsTheSame(stackA, stackB) && stackA.itemDamage == stackB.itemDamage
                    && (!checkNBT || NBTUtil.doNBTsMatch(stackA.tagCompound, stackB.tagCompound))
    }

    fun areItemsTheSame(a: ItemStack, b: ItemStack): Boolean = a.item === b.item && a.itemDamage == b.itemDamage

    fun cloneStack(item: Item?, stackSize: Int): ItemStack? {
        if (item == null)
            return null

        val stack = ItemStack(item, stackSize)

        return stack
    }

    fun cloneStack(stack: ItemStack?, stackSize: Int): ItemStack? {
        if (stack == null)
            return null

        val retStack = stack.copy()
        retStack.stackSize = stackSize

        return retStack
    }

    fun dropItemStackIntoWorld(stack: ItemStack?, world: World, x: Double, y: Double, z: Double, velocity: Boolean) {
        if (stack != null) {
            var x2 = 0.5f
            var y2 = 0.0f
            var z2 = 0.5f

            if (velocity) {
                x2 = world.rand.nextFloat() * 0.8f + 0.1f
                y2 = world.rand.nextFloat() * 0.8f + 0.1f
                z2 = world.rand.nextFloat() * 0.8f + 0.1f
            }
            val entity = EntityItem(world, x + x2, y + y2, z + z2, stack.copy())

            if (velocity) {
                entity.motionX = (world.rand.nextGaussian().toFloat() * 0.05f).toDouble()
                entity.motionY = (world.rand.nextGaussian().toFloat() * 0.05f + 0.2f).toDouble()
                entity.motionZ = (world.rand.nextGaussian().toFloat() * 0.05f).toDouble()
            } else {
                entity.motionY = -0.0500000007450581
                entity.motionX = 0.0
                entity.motionZ = 0.0
            }

            world.spawnEntityInWorld(entity)
        }
    }

    fun canStacksMerge(stack1: ItemStack?, stack2: ItemStack?): Boolean {
        if (stack1 == null || stack2 == null)
            return false
        if (!stack1.isItemEqual(stack2))
            return false
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2))
            return false
        return true
    }

    fun searchForPossibleSlot(stack: ItemStack, inventory: IInventory): Int {
        for (i in 0..inventory.sizeInventory - 1) {
            val item = inventory.getStackInSlot(i)
            if (item == null || item.item == null)
                return i
            else if (canStacksMerge(stack, item) && item.stackSize + stack.stackSize < item.maxStackSize)
                return i
        }
        return -1
    }

    /**
     * Attempts to put an item into the given inventory. Returns false if it fails.

     * @param inventory
     * *
     * @param stack
     * *
     * @param doMove
     * *
     * @return
     */
    fun insertIntoInventory(inventory: IInventory?, stack: ItemStack?, doMove: Boolean): Boolean {
        if (stack == null) return false
        if (inventory == null) return false

        val slot = searchForPossibleSlot(stack, inventory)

        if (doMove && slot > -1 && slot < inventory.sizeInventory)
            return tryInsertStack(inventory, slot, stack, true)

        return slot != -1
    }

    fun tryInsertStack(targetInventory: IInventory, slot: Int, stack: ItemStack, canMerge: Boolean): Boolean {
        if (targetInventory.isItemValidForSlot(slot, stack)) {
            val targetStack = targetInventory.getStackInSlot(slot)
            if (targetStack == null) {
                val limit = targetInventory.inventoryStackLimit
                if (limit < stack.stackSize)
                    targetInventory.setInventorySlotContents(slot, stack.splitStack(limit))
                else {
                    targetInventory.setInventorySlotContents(slot, stack.copy())
                    stack.stackSize = 0
                }
                return true
            } else if (canMerge) {
                if (targetInventory.isItemValidForSlot(slot, stack) && canStacksMerge(stack, targetStack)) {
                    val space = targetStack.maxStackSize - targetStack.stackSize
                    val mergeAmount = Math.min(space, stack.stackSize)
                    val copy = targetStack.copy()
                    copy.stackSize += mergeAmount
                    targetInventory.setInventorySlotContents(slot, copy)
                    stack.stackSize -= mergeAmount
                    return true
                }
            }
        }
        return false
    }

    fun getSlotWithStack(inventory: IInventory, stack: ItemStack): Int {
        for (i in 0..inventory.sizeInventory - 1) {
            val item = inventory.getStackInSlot(i)
            if (item != null && OreDictionary.itemMatches(item, stack, false))
                return i
        }
        return -1
    }

    fun hasItemStack(inventory: IInventory, stack: ItemStack): Boolean = getSlotWithStack(inventory, stack) != -1

    fun hasItem(inventory : IInventory, item: Item) : Boolean {
        for (i in 0..inventory.sizeInventory - 1) {
            val slotItem = inventory.getStackInSlot(i)
            if (slotItem != null && slotItem.item == item) return true
        }
        return false
    }

    fun consumeItem(inventory : IInventory, item: Item) : Boolean {
        for (i in 0..inventory.sizeInventory - 1) {
            val slotItem = inventory.getStackInSlot(i)
            if (slotItem != null && slotItem.item == item && slotItem.stackSize > 0) {
                slotItem.stackSize--
                if (slotItem.stackSize <= 0)
                    inventory.setInventorySlotContents(i, null)
                return true
            }
        }
        return false
    }

    fun setEntityItemAge(item: EntityItem, age: Int) {
        item.age = age
    }

    fun registerItem(item : Item, name : String) {
        item.setRegistryName(name)
        GameRegistry.register(item)
    }
}
