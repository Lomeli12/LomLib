package net.lomeli.lomlib.client;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StatCollector;

import net.lomeli.lomlib.libs.Strings;

public class FluidAnvilRecipeHandler extends TemplateRecipeHandler {

    @Override
    public void drawBackground(int i) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GuiDraw.changeTexture(getGuiTexture());
        GuiDraw.drawTexturedModalRect(70, 0, 16, 5, 169, 64);
    }

    @Override
    public void drawExtras(int i) {
        GuiDraw.drawTexturedModalRect(59, 20, 0, 166, 109, 181);
    }

    @Override
    public String getRecipeName() {
        return StatCollector.translateToLocal(Strings.NEI_ANVIL);
    }

    @Override
    public Class<? extends GuiContainer> getGuiClass() {
        return GuiRepair.class;
    }

    @Override
    public String getGuiTexture() {
        return "minecraft:textures/gui/container/anvil.png";
    }

    public String getRecipeId() {
        return Strings.MOD_ID.toLowerCase() + ".anvilRecipe";
    }

    @Override
    public String getOverlayIdentifier() {
        return "anvil";
    }

    public class CachedIORecipe extends TemplateRecipeHandler.CachedRecipe {

        @Override
        public PositionedStack getResult() {
            return null;
        }

    }

}
