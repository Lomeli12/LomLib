package net.lomeli.lomlib.client.config

import net.minecraft.client.gui.GuiScreen

import net.minecraftforge.fml.client.config.GuiConfig

import net.lomeli.lomlib.LomLib
import net.lomeli.lomlib.lib.ModLibs

class GuiLomLibConfig(parent: GuiScreen) : GuiConfig(parent, LomLib.config!!.configElements, ModLibs.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(LomLib.config!!.title))