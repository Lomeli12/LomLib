package net.lomeli.lomlib.client.addon.jei;

import mezz.jei.api.recipe.IRecipeWrapper;

import java.util.Collections;
import java.util.List;

import net.minecraftforge.fluids.FluidStack;

public abstract class BlankRecipeWrapper implements IRecipeWrapper {
    @Override
    public List getInputs() {
        return Collections.emptyList();
    }

    @Override
    public List getOutputs() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidInputs() {
        return Collections.emptyList();
    }

    @Override
    public List<FluidStack> getFluidOutputs() {
        return Collections.emptyList();
    }
}
