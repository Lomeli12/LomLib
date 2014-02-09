package net.lomeli.lomlib.asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LomLibAccessTransformer extends AccessTransformer {

    private List<String> obfNames = new ArrayList<String>();
    private String[] deobfName = { "net.minecraft.util.MathHelper", "net.minecraft.client.gui.GuiMainMenu", "net.minecraft.client.renderer.entity.RenderPlayer",
            "net.minecraft.client.renderer.entity.RendererLivingEntity", "net.minecraft.client.renderer.ItemRenderer" };

    public LomLibAccessTransformer() throws IOException {
        super("lomlib_at.cfg");
        obfNames.add("ls");
        obfNames.add("blt");
        obfNames.add("bhj");
        obfNames.add("bhb");
        obfNames.add("bfj");
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        super.transform(name, transformedName, bytes);
        /*
       for (int i = 0; i < obfNames.size(); i++) {
            if (name.equals(obfNames.get(i)) || name.equals(deobfName[i])) {
                System.out.println("[LomLib]: Patching " + name);
                patchClassInJar(name, bytes, name, LomLibPlugin.location);
            }
        }
        */
        return bytes;
    }

    public byte[] patchClassInJar(String name, byte[] bytes, String ObfName, File location) {
        try {
            ZipFile zip = new ZipFile(location);
            ZipEntry entry = null;
            if (obfNames.contains(name))
                entry = zip.getEntry("net/minecraft/src/" + name + ".class");
            else
                entry = zip.getEntry(name.replace('.', '/') + ".class");

            if (entry == null)
                System.out.println("[LomLib]: " + name + " not found in " + location.getName());
            else {
                InputStream zin = zip.getInputStream(entry);
                bytes = new byte[(int) entry.getSize()];
                zin.read(bytes);
                zin.close();
            }
            zip.close();
            System.out.println("[LomLib]: Succesfully patched " + name);
        } catch (Exception e) {
            throw new RuntimeException("[LomLib]: Error overriding " + name + " from " + location.getName(), e);
        }
        return bytes;
    }
}
