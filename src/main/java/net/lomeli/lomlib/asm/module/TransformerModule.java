package net.lomeli.lomlib.asm.module;

import net.minecraftforge.fml.common.FMLLog;

public abstract class TransformerModule {
    public abstract String getSrgClass();

    public abstract String getObfClass();

    public abstract byte[] transform(String name, String transformedName, byte[] basicClass);

    public boolean isClass(String name, String transformed) {
        return getObfClass().equals(name) || getSrgClass().equals(transformed);
    }

    protected void log(String str, Object... objs) {
        FMLLog.info("[LomLib ASM] %s", String.format(str, objs));
    }

    protected boolean matchesName(String name, String[] possibleName) {
        for (String str : possibleName) {
            if (name.equals(str))
                return true;
        }
        return false;
    }
}
