package net.lomeli.lomlib.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

@IFMLLoadingPlugin.TransformerExclusions({ "net.lomeli.lomlib.asm" })
public class LoadingPlugin implements IFMLLoadingPlugin {
    @Override
    @Deprecated
    public String[] getLibraryRequestClass() {
        return null;
    }

    @Override
    public String[] getASMTransformerClass() {
        return new String[] { "net.lomeli.lomlib.asm.AccessTransformer" };
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return "net.lomeli.lomlib.asm.LoadingPlugin";
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }
}
