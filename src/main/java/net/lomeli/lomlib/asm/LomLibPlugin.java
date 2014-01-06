package net.lomeli.lomlib.asm;

import java.io.File;
import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@TransformerExclusions({ "net.lomeli.lomlib.asm" })
public class LomLibPlugin implements IFMLLoadingPlugin {
    public static File location;
    
    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.lomeli.lomlib.asm.LomLibAccessTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return "net.lomeli.lomlib.LomLib";
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        location = (File) data.get("coremodLocation");
    }
}
