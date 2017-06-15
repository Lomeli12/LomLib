package net.lomeli.lomlib.client.config

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen

import net.minecraftforge.fml.client.IModGuiFactory

class GuiConfigFactory : IModGuiFactory {
    override fun initialize(minecraftInstance: Minecraft) {
    }

    override fun mainConfigGuiClass(): Class<out GuiScreen> = GuiLomLibConfig::class.java

    override fun runtimeGuiCategories(): Set<IModGuiFactory.RuntimeOptionCategoryElement>? = null

    override fun getHandlerFor(element: IModGuiFactory.RuntimeOptionCategoryElement): IModGuiFactory.RuntimeOptionGuiHandler? = null

    override fun hasConfigGui(): Boolean = true

    override fun createConfigGui(parentScreen: GuiScreen?): GuiScreen = GuiLomLibConfig(parentScreen!!)
}