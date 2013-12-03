package net.lomeli.lomlib.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;

@TransformerExclusions({ "net.lomeli.lomlib.asm" })
@MCVersion("1.6.4")
public class LomLibPlugin implements IFMLLoadingPlugin {
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
    }
}
