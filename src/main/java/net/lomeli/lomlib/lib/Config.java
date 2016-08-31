package net.lomeli.lomlib.lib;

import net.lomeli.lomlib.core.config.annotations.ConfigBoolean;

public class Config {
    @ConfigBoolean(defaultValue = true, comment = "config.lomlib.patreon")
    public static boolean crown = false;
    @ConfigBoolean(defaultValue = true, comment = "config.lomlib.update")
    public static boolean checkForUpdates = false;
}
