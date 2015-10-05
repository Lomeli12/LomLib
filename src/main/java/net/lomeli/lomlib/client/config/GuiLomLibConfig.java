package net.lomeli.lomlib.client.config;

import net.minecraft.client.gui.GuiScreen;

import net.minecraftforge.fml.client.config.GuiConfig;

import net.lomeli.lomlib.LomLib;
import net.lomeli.lomlib.lib.ModLibs;

public class GuiLomLibConfig extends GuiConfig {
    public GuiLomLibConfig(GuiScreen parent) {
        super(parent, LomLib.config.getConfigElements(), ModLibs.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(LomLib.config.getTitle()));
    }
}
