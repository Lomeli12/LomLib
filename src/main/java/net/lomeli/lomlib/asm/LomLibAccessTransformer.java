package net.lomeli.lomlib.asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.IOException;

public class LomLibAccessTransformer extends AccessTransformer {

    public LomLibAccessTransformer() throws IOException {
        super("lomlib_at.cfg");
    }
}
