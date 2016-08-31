package net.lomeli.lomlib.util.forge

import com.google.common.collect.Lists
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.fluids.*
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack

object FluidUtil {

    fun isFluidWater(fluid: Fluid): Boolean = FluidRegistry.WATER == fluid

    fun isFluidWater(fluid: FluidStack?): Boolean = if (fluid == null) false else isFluidWater(fluid.fluid)

    fun isFluidLava(fluid: Fluid): Boolean = FluidRegistry.LAVA == fluid

    fun isFluidLava(fluid: FluidStack?): Boolean = if (fluid == null) false else isFluidLava(fluid.fluid)

    fun areFluidsEqual(baseFluid: FluidStack?, secondFluid: FluidStack?): Boolean = if (baseFluid != null && secondFluid != null) baseFluid.isFluidEqual(secondFluid) else false

    fun combineFluidStacks(baseFluid: FluidStack?, secondFluid: FluidStack?) {
        if (baseFluid != null && secondFluid != null) {
            if (areFluidsEqual(baseFluid, secondFluid)) {
                baseFluid.amount += secondFluid.amount
            }
        }
    }

    fun getContainerStack(stack: ItemStack?): FluidStack? {
        if (stack != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            var capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH) as FluidHandlerItemStack
            if (capability is FluidHandlerItemStack)
                return capability.fluid
        }
        return null
    }

    fun getContainerFluid(stack: ItemStack?): Fluid? {
        if (stack != null) {
            val fluidStack = getContainerStack(stack)
            return fluidStack?.fluid
        }
        return null
    }

    fun isFilledContainer(stack: ItemStack?): Boolean {
        if (stack != null && stack.item != null && stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            var capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH) as FluidHandlerItemStack
            return capability.drain(1, false) != null
        }
        return false
    }

    fun getEmptyContainer(stack: ItemStack?): ItemStack? {
        if (stack != null && isFilledContainer(stack)) {
            if (stack.item === Items.LAVA_BUCKET || stack.item === Items.WATER_BUCKET || stack.item === Items.MILK_BUCKET)
                return ItemStack(Items.BUCKET)
            else if (stack.item is IFluidContainerItem) {
                (stack.item as IFluidContainerItem).drain(stack, (stack.item as IFluidContainerItem).getCapacity(stack), true)
                return stack
            } else
                return FluidContainerRegistry.drainFluidContainer(stack)
        }
        return null
    }

    fun isFluidContainer(stack: ItemStack?): Boolean {
        if (stack == null || stack.item == null) return false
        return stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)
    }

    fun getStackCapacity(stack: ItemStack): Int {
        if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            var capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)
            return capability.tankProperties[0].capacity
        }
        if (stack.item is IFluidContainerItem)
            return (stack.item as IFluidContainerItem).getCapacity(stack)
        return FluidContainerRegistry.getContainerCapacity(stack)
    }

    fun fillStack(stack: ItemStack?, fluid: FluidStack) {
        if (stack == null || stack.item == null) return
        if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
            var capability = stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)
            capability.fill(fluid, true)
            return
        }
        if (stack.item is IFluidContainerItem) {
            (stack.item as IFluidContainerItem).fill(stack, fluid, true)
        } else
            FluidContainerRegistry.fillFluidContainer(fluid, stack)
    }

    fun getContainersForFluid(targetFluid: Fluid?): List<ItemStack> {
        val list = Lists.newArrayList<ItemStack>()

        if (targetFluid != null) {
            for (data in FluidContainerRegistry.getRegisteredFluidContainerData()) {
                val fluid = data.fluid.fluid
                if (fluid != null) {
                    if (fluid == targetFluid || fluid.name == targetFluid.name)
                        list.add(data.filledContainer)
                }
            }
            val itemIterator = Item.REGISTRY.iterator()
            if (itemIterator != null) {
                while (itemIterator.hasNext()) {
                    val item = itemIterator.next()
                    val stackItem = ItemStack(item)
                    stackItem.tagCompound = NBTTagCompound()
                    if (stackItem.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)) {
                        var capability = stackItem.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, EnumFacing.NORTH)
                        if (capability is FluidHandlerItemStack) {
                            val fluidStack = capability.fluid
                            if (fluidStack?.fluid == targetFluid || fluidStack?.fluid?.name == targetFluid.name) {
                                list.add(stackItem)
                                continue
                            }
                        }
                    }
                    if (item is IFluidContainerItem) {
                        for (i in 0..item.maxDamage) {
                            val stack = ItemStack(item, 1, i)
                            if (item.fill(stack, FluidStack(targetFluid, Fluid.BUCKET_VOLUME), true) == 0)
                                break
                            val fluidStack = item.getFluid(stack)
                            if (fluidStack != null && fluidStack.fluid != null) {
                                val fluid = fluidStack.fluid
                                if (fluid == targetFluid || fluid.name == targetFluid.name)
                                    list.add(stack)
                            }
                        }
                    }
                }
            }
        }

        return list
    }
}
