package net.lomeli.lomlib.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({ "net.lomeli.lomlib.asm" })
public class LoadingPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.lomeli.lomlib.asm.CapeAccessTransformer" };
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

    @Override
    public String[] getLibraryRequestClass() {
        return null;
    }
}
