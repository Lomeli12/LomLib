package net.lomeli.lomlib.util;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameData;

public class FluidUtil {

    public static boolean isFluidWater(Fluid fluid) {
        return fluid.equals(FluidRegistry.WATER);
    }

    public static boolean isFluidWater(FluidStack fluid) {
        return isFluidWater(fluid.getFluid());
    }

    public static boolean isFluidLava(Fluid fluid) {
        return fluid.equals(FluidRegistry.LAVA);
    }

    public static boolean isFluidLava(FluidStack fluid) {
        return isFluidLava(fluid.getFluid());
    }

    public static boolean areFluidsEqual(FluidStack baseFluid, FluidStack secondFluid) {
        return (baseFluid != null && secondFluid != null) ? baseFluid.isFluidEqual(secondFluid) : false;
    }

    public static void combineFluidStacks(FluidStack baseFluid, FluidStack secondFluid) {
        if (baseFluid != null && secondFluid != null) {
            if (areFluidsEqual(baseFluid, secondFluid)) {
                baseFluid.amount += secondFluid.amount;
                secondFluid = null;
            }
        }
    }

    public static Fluid getContainerFluid(ItemStack stack) {
        Fluid fluid = null;
        if (stack != null) {
            if (FluidContainerRegistry.isFilledContainer(stack)) {
                FluidStack temp = FluidContainerRegistry.getFluidForFilledItem(stack);
                if (temp != null && temp.getFluid() != null)
                    fluid = temp.getFluid();
            } else if (stack.getItem() instanceof IFluidContainerItem) {
                FluidStack fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
                if (fluidStack != null && fluidStack.getFluid() != null)
                    fluid = fluidStack.getFluid();
            }
        }
        return fluid;
    }

    public static boolean isFilledContainer(ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            if (stack.getItem() instanceof IFluidContainerItem) {
                FluidStack fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
                if (fluidStack != null && fluidStack.getFluid() != null)
                    return true;
            }
            return FluidContainerRegistry.isFilledContainer(stack);
        }
        return false;
    }

    public static ArrayList<ItemStack> getContainersForFluid(Fluid targetFluid) {
        ArrayList<ItemStack> list = new ArrayList<ItemStack>();

        if (targetFluid != null) {
            for (FluidContainerRegistry.FluidContainerData data : FluidContainerRegistry.getRegisteredFluidContainerData()) {
                Fluid fluid = data.fluid.getFluid();
                if (fluid != null) {
                    if (fluid.equals(targetFluid) || fluid.getID() == targetFluid.getID())
                        list.add(data.filledContainer);
                }
            }
            Iterator<Item> itemIterator = GameData.getItemRegistry().iterator();
            if (itemIterator != null) {
                while (itemIterator.hasNext()) {
                    Item item = itemIterator.next();
                    if (item instanceof IFluidContainerItem) {
                        for (int i = 0; i <= item.getMaxDamage(); i++) {
                            ItemStack stack = new ItemStack(item, 1, i);
                            if (((IFluidContainerItem) item).fill(stack, new FluidStack(targetFluid, FluidContainerRegistry.BUCKET_VOLUME), true) == 0)
                                break;
                            FluidStack fluidStack = ((IFluidContainerItem) item).getFluid(stack);
                            if (fluidStack != null && fluidStack.getFluid() != null) {
                                Fluid fluid = fluidStack.getFluid();
                                if (fluid.equals(targetFluid) || fluid.getID() == targetFluid.getID())
                                    list.add(stack);
                            }
                        }
                    }
                }
            }
        }

        return list;
    }
}
