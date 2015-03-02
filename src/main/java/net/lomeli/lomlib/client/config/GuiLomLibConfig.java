package net.lomeli.lomlib.client.config;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import net.minecraftforge.fml.client.config.GuiConfig;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.core.Strings;

public class GuiLomLibConfig extends GuiConfig {
    public GuiLomLibConfig(GuiScreen parent) {
        super(parent, new ConfigElement(LomLib.config.getConfig().getCategory(Configuration.CATEGORY_GENERAL)).getChildElements(), Strings.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(LomLib.config.getConfig().toString()));
    }
}
