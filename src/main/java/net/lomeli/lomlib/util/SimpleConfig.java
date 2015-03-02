package net.lomeli.lomlib.util;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class SimpleConfig {
    protected Configuration config;
    protected String modid;

    public SimpleConfig(String modid, Configuration config) {
        this.config = config;
        this.modid = modid;
    }

    public Configuration getConfig() {
        return config;
    }

    public abstract void loadConfig();

    @SubscribeEvent
    public void onConfigChange(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if (eventArgs.modID.equalsIgnoreCase(modid))
            loadConfig();
    }
}
