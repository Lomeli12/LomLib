package net.lomeli.lomlib.client.addon.jei.anvil;

import mezz.jei.api.JEIManager;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.IRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;

import javax.annotation.Nonnull;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin;
import net.lomeli.lomlib.util.LangUtil;

public class AnvilRecipeCategory implements IRecipeCategory {
    @Nonnull
    private final IDrawable background;
    @Nonnull
    private final String localizedName;

    public AnvilRecipeCategory() {
        ResourceLocation location = new ResourceLocation("minecraft:textures/gui/container/anvil.png");
        localizedName = LangUtil.translate("recipe.lomlib:anvilRecipe");
        background = JEIManager.guiHelper.createDrawable(location, 15, 41, 143, 30);
    }

    @Nonnull
    @Override
    public String getUid() {
        return LomLibPlugin.ANVIL;
    }

    @Nonnull
    @Override
    public String getTitle() {
        return localizedName;
    }

    @Nonnull
    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void drawExtras(Minecraft minecraft) {

    }

    @Override
    public void setRecipe(@Nonnull IRecipeLayout recipeLayout, @Nonnull IRecipeWrapper recipeWrapper) {

    }
}
