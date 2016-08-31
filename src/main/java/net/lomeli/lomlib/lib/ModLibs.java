package net.lomeli.lomlib.lib;

public class ModLibs {

    public static final String MOD_ID = "lomlib";
    public static final String MOD_NAME = "LomLib";
    public static final String CONFIG_FACTORY = "net.lomeli.lomlib.client.config.GuiConfigFactory";
    public static final String MINECRAFT_VERSION = "1.10.2";
    public static final String LANGUAGE = "Kotlin";
    public static final String KOTLIN_ADAPTER = "net.lomeli.lomlib.KotlinAdapter";

    public static final String VERSION = "@VERSION@";

    public static final String UPDATE_URL = "https://raw.githubusercontent.com/Lomeli12/LomLib/1.10/update.json";
    public static final String PATREON_URL = "https://raw.githubusercontent.com/Lomeli12/LomLib/1.10/patreon.json";

    public static final String NEI_SHAPED = "recipe.lomlib:shapedFluid";
    public static final String NEI_SHAPELESS = "recipe.lomlib:shapelessFluid";
    public static final String NEI_ANVIL = "recipe.lomlib:anvilRecipe";

    public static final String COMMON = "net.lomeli.lomlib.core.Proxy";
    public static final String CLIENT = "net.lomeli.lomlib.client.ProxyClient";

    public static final String DEPENDENCIES = "after:JEI;";

    public static boolean initialized;
}
