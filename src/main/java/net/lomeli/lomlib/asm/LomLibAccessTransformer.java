package net.lomeli.lomlib.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class LomLibAccessTransformer extends AccessTransformer {

    private static LomLibAccessTransformer instance;
    private static List mapFiles = new LinkedList();

    public LomLibAccessTransformer() throws IOException {
        super();
        instance = this;
        mapFiles.add("lomlib_at.cfg");
        Iterator it = mapFiles.iterator();
        while(it.hasNext()) {
            String file = (String) it.next();
            instance.readCfg(file);
        }
    }

    public static void addTransformerMap(String mapFileName) {
        if(instance == null)
            mapFiles.add(mapFileName);
        else
            instance.readCfg(mapFileName);
    }

    private void readCfg(String name) {
        try {
            Method e = AccessTransformer.class.getDeclaredMethod("readMapFile", new Class[] { String.class });
            e.setAccessible(true);
            e.invoke(this, new Object[] { name });
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
