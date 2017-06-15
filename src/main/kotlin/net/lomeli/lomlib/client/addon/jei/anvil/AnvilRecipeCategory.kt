package net.lomeli.lomlib.client.addon.jei.anvil

import mezz.jei.api.IGuiHelper
import mezz.jei.api.gui.IDrawable
import mezz.jei.api.gui.IRecipeLayout
import mezz.jei.api.ingredients.IIngredients
import mezz.jei.api.recipe.IRecipeCategory
import mezz.jei.api.recipe.IRecipeWrapper

import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation

import net.lomeli.lomlib.client.addon.jei.LomLibPlugin
import net.lomeli.lomlib.util.LangUtil

class AnvilRecipeCategory(guiHelper: IGuiHelper) : IRecipeCategory<FluidAnvilRecipeWrapper> {
    private val background: IDrawable
    private val localizedName: String

    init {
        val location = ResourceLocation("minecraft:textures/gui/container/anvil.png")
        localizedName = LangUtil.translate("recipe.lomlib:anvilRecipe")
        background = guiHelper.createDrawable(location, 15, 41, 143, 30)
    }

    override fun getUid(): String {
        return LomLibPlugin.ANVIL
    }

    override fun getTitle(): String {
        return localizedName
    }

    override fun getBackground(): IDrawable {
        return background
    }

    override fun drawExtras(minecraft: Minecraft) {
    }

    fun setRecipe(recipeLayout: IRecipeLayout, recipeWrapper: FluidAnvilRecipeWrapper) {
        if (recipeWrapper !is FluidAnvilRecipeWrapper)
            return
    }

    fun drawAnimations(minecraft: Minecraft) {
    }

    override fun setRecipe(recipeLayout : IRecipeLayout?, recipeWrapper : FluidAnvilRecipeWrapper?, ingredients : IIngredients?) {
    }

    override fun getIcon(): IDrawable? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getTooltipStrings(mouseX: Int, mouseY: Int): MutableList<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}