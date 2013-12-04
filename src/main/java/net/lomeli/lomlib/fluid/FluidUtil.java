package net.lomeli.lomlib.fluid;

import java.util.ArrayList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

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
        return (baseFluid != null && baseFluid.getFluid() != null && secondFluid != null && secondFluid.getFluid() != null) ? baseFluid.getFluid().equals(secondFluid.getFluid())
                : false;
    }

    public static void combineFluidStacks(FluidStack baseFluid, FluidStack secondFluid) {
        if (baseFluid != null && secondFluid != null) {
            if (areFluidsEqual(baseFluid, secondFluid)) {
                baseFluid.amount += secondFluid.amount;
                secondFluid = null;
            }
        }
    }

    public static void writeFluidTankNBT(NBTTagCompound tag, FluidTank tank, String tagString) {
        if (tag != null && tank != null && tagString != null) {
            if (tank.getFluid() != null && tank.getFluid().getFluid() != null)
                tag.setInteger(tank.getFluid().getFluid().getName().concat(tagString), tank.getFluidAmount());
        }
    }

    public static void writeFluidTankNBT(NBTTagCompound tag, FluidTank tank) {
        writeFluidTankNBT(tag, tank, "Tank");
    }

    public static void readFluidTankNBT(NBTTagCompound tag, FluidTank tank, Fluid type, String tagString) {
        if (tag != null && tank != null && type != null && tagString != null) {
            tank.setFluid(new FluidStack(type, tag.getInteger(type.getName().concat(tagString))));
        }
    }

    public static void readFluidTankNBT(NBTTagCompound tag, FluidTank tank, Fluid type) {
        readFluidTankNBT(tag, tank, type, "Tank");
    }

    public static Fluid getContainerFluid(ItemStack stack) {
        Fluid fluid = null;
        if(stack != null){
            if(FluidContainerRegistry.isFilledContainer(stack)){
                FluidStack temp = FluidContainerRegistry.getFluidForFilledItem(stack);
                if(temp != null && temp.getFluid() != null)
                    fluid = temp.getFluid();
            }
        }
        return fluid;
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
        }

        return list;
    }
}
