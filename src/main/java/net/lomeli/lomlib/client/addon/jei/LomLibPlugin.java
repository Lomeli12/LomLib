package net.lomeli.lomlib.client.addon.jei;

import mezz.jei.api.*;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;

import net.lomeli.lomlib.client.addon.jei.anvil.AnvilRecipeCategory;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapedFluidWrapper;
import net.lomeli.lomlib.client.addon.jei.crafting.ShapelessFluidWrapper;
import net.lomeli.lomlib.core.recipes.ShapedFluidRecipe;
import net.lomeli.lomlib.core.recipes.ShapelessFluidRecipe;

@JEIPlugin
public class LomLibPlugin implements IModPlugin {
    public static final String CRAFTING = "minecraft.crafting";
    public static final String ANVIL = "lomlib.anvil";
    private IJeiHelpers jeiHelpers;

    @Override
    public void register(IModRegistry registry) {
        jeiHelpers = registry.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        registry.addRecipeCategories(new AnvilRecipeCategory(guiHelper));

        registry.handleRecipes(ShapedFluidRecipe.class, new IRecipeWrapperFactory<ShapedFluidRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(ShapedFluidRecipe recipe) {
                return new ShapedFluidWrapper(jeiHelpers, recipe);
            }
        }, VanillaRecipeCategoryUid.CRAFTING);
        registry.handleRecipes(ShapelessFluidRecipe.class, new IRecipeWrapperFactory<ShapelessFluidRecipe>() {
            @Override
            public IRecipeWrapper getRecipeWrapper(ShapelessFluidRecipe recipe) {
                return new ShapelessFluidWrapper(jeiHelpers, recipe);
            }
        }, VanillaRecipeCategoryUid.CRAFTING);
        //registry.addRecipeHandlers(new ShapedFluidRecipeHandler(), new ShapelessFluidRecipeHandler(), new FluidAnvilRecipeHandler());

        /*registry.getRecipeTransferRegistry().addRecipeTransferHandler(ContainerRepair.class, ANVIL, 0, 2, 3, 36);
        List<Object> recipes = Lists.newArrayList();
        recipes.addAll(AnvilRecipeManager.INSTANCE.getRegistry());
        registry.addRecipes(recipes);*/
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }
}
