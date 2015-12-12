package net.lomeli.lomlib.client.addon.nei;

import codechicken.nei.NEIServerUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.core.recipes.AnvilRecipeManager;
import net.lomeli.lomlib.core.recipes.IAnvilRecipe;
import net.lomeli.lomlib.lib.ModLibs;

import static codechicken.lib.gui.GuiDraw.*;

public class AnvilRecipeHandler extends TemplateRecipeHandler {
    @Override
    public String getGuiTexture() {
        return "textures/gui/container/anvil.png";
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(ModLibs.NEI_ANVIL);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiRepair.class;
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        if (result != null) {
            for (IAnvilRecipe recipe : AnvilRecipeManager.getRegistry()) {
                if (NEIServerUtils.areStacksSameTypeCrafting(recipe.recipeOutput(), result)) {
                    Object[] inputs = recipe.recipeInputs();
                    if (inputs != null && inputs.length > 0)
                        arecipes.add(new CachedAnvilRecipe(inputs[0], inputs.length >= 2 ? inputs[1] : null, result, recipe.recipeCost()));
                }
            }
        }
    }

    @Override
    public void loadUsageRecipes(ItemStack ingredient) {
        if (ingredient != null) {
            for (IAnvilRecipe recipe : AnvilRecipeManager.getRegistry()) {
                if (recipe.doItemsMatch(ingredient, 0) || recipe.doItemsMatch(ingredient, 1)) {
                    Object[] inputs = recipe.recipeInputs();
                    if (inputs != null && inputs.length > 0)
                        arecipes.add(new CachedAnvilRecipe(inputs[0], inputs.length >= 2 ? inputs[1] : null, recipe.recipeOutput(), recipe.recipeCost()));
                }
            }
        }
    }

    @Override
    public int recipiesPerPage() {
        return 2;
    }

    @Override
    public void drawBackground(int recipe) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        changeTexture(getGuiTexture());
        drawTexturedModalRect(10, 25, 15, 41, 143, 30);
    }

    @Override
    public void drawExtras(int recipe) {
        CachedAnvilRecipe cRecipe = (CachedAnvilRecipe) arecipes.get(recipe);
        if (cRecipe.expCost > 0) {
            int x = 70, y = 14, k = 8453920;
            if (cRecipe.expCost >= 40)
                k = 16736352;
            int l = -16777216 | (k & 16579836) >> 2 | k & -16777216;
            String s = String.format(StatCollector.translateToLocal("container.repair.cost"), cRecipe.expCost);
            drawStringC(s, x, y + 1, l, false);
            drawStringC(s, x + 1, y, l, false);
            drawStringC(s, x + 1, y + 1, l, false);
            drawStringC(s, x, y, k, false);
        }
    }

    public class CachedAnvilRecipe extends CachedRecipe {
        public List<PositionedStack> inputs;
        public PositionedStack output;
        public int expCost;

        public CachedAnvilRecipe(ItemStack output, int expCost) {
            this.expCost = expCost;
            this.inputs = new ArrayList<PositionedStack>();
            for (IAnvilRecipe recipe : AnvilRecipeManager.getRegistry()) {
                if (NEIServerUtils.areStacksSameTypeCrafting(recipe.recipeOutput(), output)) {
                    if (this.output == null)
                        this.output = new PositionedStack(output, 129, 31);
                    Object[] inputs = recipe.recipeInputs();
                    if (inputs != null && inputs.length > 0) {
                        this.inputs.add(new PositionedStack(inputs[0], 22, 31));
                        if (inputs.length >= 2)
                            this.inputs.add(new PositionedStack(inputs[1], 71, 31));
                    }
                    break;
                }
            }
        }

        public CachedAnvilRecipe(Object input1, Object input2, ItemStack output, int expCost) {
            this.output = new PositionedStack(output, 129, 31);
            this.inputs = new ArrayList<PositionedStack>();
            this.inputs.add(new PositionedStack(input1, 22, 31));
            if (input2 != null)
                this.inputs.add(new PositionedStack(input2, 71, 31));
            this.expCost = expCost;
        }

        @Override
        public List<PositionedStack> getIngredients() {
            return this.inputs;
        }

        @Override
        public PositionedStack getResult() {
            return output;
        }
    }
}
