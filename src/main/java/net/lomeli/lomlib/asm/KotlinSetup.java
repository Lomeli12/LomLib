package net.lomeli.lomlib.asm;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;

/**
 * Copied from shadowfacts Forgelin
 */
public class KotlinSetup implements IFMLCallHook {
    @Override
    public void injectData(Map<String, Object> data) {
        ClassLoader loader = (ClassLoader)data.get("classLoader");
        try {
            loader.loadClass("net.lomeli.lomlib.KotlinAdapter");
        } catch (ClassNotFoundException e) {
            // this should never happen
            throw new RuntimeException("Couldn't find Kotlin langague adapter, this shouldn't be happening", e);
        }
    }

    @Override
    public Void call() throws Exception {
        return null;
    }
}
