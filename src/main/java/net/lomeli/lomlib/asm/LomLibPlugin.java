package net.lomeli.lomlib.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class LomLibPlugin implements IFMLLoadingPlugin {
    public static File location;

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.lomeli.lomlib.asm.LomLibAccessTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "net.lomeli.lomlib.LomLibCore";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        location = (File) data.get("coremodLocation");
    }

    @Override
    public String getAccessTransformerClass() {
        return "net.lomeli.lomlib.asm.LomLibAccessTransformer";
    }
}
