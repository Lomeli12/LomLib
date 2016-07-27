package net.lomeli.lomlib.asm;

import com.google.common.collect.Lists;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.util.List;

import net.minecraft.launchwrapper.IClassTransformer;

import net.lomeli.lomlib.asm.module.BlockCakeModule;
import net.lomeli.lomlib.asm.module.ItemFoodModule;
import net.lomeli.lomlib.asm.module.LayerArmorBaseModule;
import net.lomeli.lomlib.asm.module.TransformerModule;

public class ClassTransformer implements IClassTransformer {
    private List<TransformerModule> moduleList;

    public ClassTransformer() {
        moduleList = Lists.newArrayList();
        moduleList.add(new LayerArmorBaseModule());
        moduleList.add(new ItemFoodModule());
        moduleList.add(new BlockCakeModule());
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (TransformerModule module : moduleList) {
            if (module.isClass(name, transformedName)) {
                return module.transform(name, transformedName, basicClass);
            }
        }
        return basicClass;
    }
}
