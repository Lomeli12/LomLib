package net.lomeli.lomlib.lib;

public class ModLibs {

    public static final String MOD_ID = "LomLib";
    public static final String MOD_NAME = MOD_ID;
    public static final String CONFIG_FACTORY = "net.lomeli.lomlib.client.config.GuiConfigFactory";
    public static final String MINECRAFT_VERSION = "1.9";

    public static final String VERSION = "@VERSION@";

    public static final String UPDATE_URL = "https://raw.githubusercontent.com/Lomeli12/LomLib/1.9/update.json";
    public static final String PATREON_URL = "https://raw.githubusercontent.com/Lomeli12/LomLib/1.9/update.json";

    public static final String NEI_SHAPED = "recipe.lomlib:shapedFluid";
    public static final String NEI_SHAPELESS = "recipe.lomlib:shapelessFluid";
    public static final String NEI_ANVIL = "recipe.lomlib:anvilRecipe";

    public static final String COMMON = "net.lomeli.lomlib.core.Proxy";
    public static final String CLIENT = "net.lomeli.lomlib.client.ProxyClient";

    public static final String ISBRH = "isbrhcore";
    public static final String DEPENDENCIES = "after:" + ISBRH + ";";

    public static boolean initialized;
}
