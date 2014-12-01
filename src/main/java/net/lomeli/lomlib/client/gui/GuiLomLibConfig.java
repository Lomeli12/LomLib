package net.lomeli.lomlib.client.gui;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import cpw.mods.fml.client.config.GuiConfig;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.libs.Strings;

public class GuiLomLibConfig extends GuiConfig {
    public GuiLomLibConfig(GuiScreen parent) {
        super(parent, new ConfigElement(LomLib.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Strings.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(LomLib.config.toString()));
    }
}
