package net.lomeli.lomlib.util;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.registry.GameData;

public class FluidUtil {

    public static boolean isFluidWater(Fluid fluid) {
        return FluidRegistry.WATER.equals(fluid);
    }

    public static boolean isFluidWater(FluidStack fluid) {
        return fluid == null ? false : isFluidWater(fluid.getFluid());
    }

    public static boolean isFluidLava(Fluid fluid) {
        return FluidRegistry.LAVA.equals(fluid);
    }

    public static boolean isFluidLava(FluidStack fluid) {
        return fluid == null ? false : isFluidLava(fluid.getFluid());
    }

    public static boolean areFluidsEqual(FluidStack baseFluid, FluidStack secondFluid) {
        return (baseFluid != null && secondFluid != null) ? baseFluid.isFluidEqual(secondFluid) : false;
    }

    public static void combineFluidStacks(FluidStack baseFluid, FluidStack secondFluid) {
        if (baseFluid != null && secondFluid != null) {
            if (areFluidsEqual(baseFluid, secondFluid)) {
                baseFluid.amount += secondFluid.amount;
            }
        }
    }

    public static FluidStack getContainerStack(ItemStack stack) {
        if (stack != null) {
            return FluidContainerRegistry.isFilledContainer(stack) ? FluidContainerRegistry.getFluidForFilledItem(stack) :
                    (stack.getItem() instanceof IFluidContainerItem) ? ((IFluidContainerItem) stack.getItem()).getFluid(stack) : null;
        }
        return null;
    }

    public static Fluid getContainerFluid(ItemStack stack) {
        if (stack != null) {
            FluidStack fluidStack = getContainerStack(stack);
            return fluidStack != null ? fluidStack.getFluid() : null;
        }
        return null;
    }

    public static boolean isFilledContainer(ItemStack stack) {
        if (stack != null && stack.getItem() != null) {
            if (stack.getItem() instanceof IFluidContainerItem) {
                FluidStack fluidStack = ((IFluidContainerItem) stack.getItem()).getFluid(stack);
                if (fluidStack != null && fluidStack.getFluid() != null && fluidStack.amount > 0)
                    return true;
            }
            return FluidContainerRegistry.isFilledContainer(stack);
        }
        return false;
    }

    public static ItemStack getEmptyContainer(ItemStack stack) {
        if (stack != null && isFilledContainer(stack)) {
            if (stack.getItem() instanceof IFluidContainerItem) {
                ((IFluidContainerItem) stack.getItem()).drain(stack, Integer.MAX_VALUE, true);
                return stack;
            } else
                return FluidContainerRegistry.drainFluidContainer(stack);
        }
        return null;
    }

    public static boolean isFluidContainer(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return false;
        if (FluidContainerRegistry.isContainer(stack))
            return true;
        if (stack.getItem() instanceof IFluidContainerItem)
            return true;
        return false;
    }

    public static int getStackCapacity(ItemStack stack) {
        if (stack.getItem() instanceof IFluidContainerItem)
            return ((IFluidContainerItem) stack.getItem()).getCapacity(stack);
        return FluidContainerRegistry.getContainerCapacity(stack);
    }

    public static void fillStack(ItemStack stack, FluidStack fluid) {
        if (stack == null || stack.getItem() == null) return;
        if (stack.getItem() instanceof IFluidContainerItem) {
            ((IFluidContainerItem) stack.getItem()).fill(stack, fluid, true);
        } else
            FluidContainerRegistry.fillFluidContainer(fluid, stack);
    }

    public static List<ItemStack> getContainersForFluid(Fluid targetFluid) {
        List<ItemStack> list = Lists.newArrayList();

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
