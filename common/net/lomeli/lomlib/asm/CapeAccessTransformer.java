package net.lomeli.lomlib.asm;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

import static scala.tools.asm.Opcodes.ACC_PRIVATE;
import static scala.tools.asm.Opcodes.ACC_PUBLIC;
import static scala.tools.asm.Opcodes.ACC_STATIC;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import scala.tools.asm.ClassReader;
import scala.tools.asm.tree.ClassNode;
import scala.tools.asm.tree.FieldNode;
import scala.tools.asm.tree.MethodNode;

public class CapeAccessTransformer extends AccessTransformer {

    public CapeAccessTransformer() throws IOException {
        super();
    }

    private final String[] obfClassToTransform = { "beu" };
    private final String[] classToTransform = { "net.minecraft.client.entity.AbstractClientPlayer" };
    private final String[] obfFieldsToTransform = { "field_110315_c", "field_110313_e" };
    private final String[] fieldsToTransform = { "downloadImageCape", "locationCape" };
    private final String[] obfMethodsToTransform = {};
    private final String[] methodsToTransform = {};

    @Override
    public byte[] transform(String name, String newName, byte[] bytes) {
        if(contains(classToTransform, name) || contains(obfClassToTransform, name)) {
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 0);

            Iterator<FieldNode> fields = classNode.fields.iterator();
            while(fields.hasNext()) {
                FieldNode f = fields.next();
                String n = f.name;
                if(contains(fieldsToTransform, n) || contains(obfFieldsToTransform, n)) {
                    f.access = ACC_PUBLIC;
                }
            }
            Iterator<MethodNode> methods = classNode.methods.iterator();
            while(methods.hasNext()) {
                MethodNode m = methods.next();
                String n = m.name;
                if(contains(methodsToTransform, n) || contains(obfMethodsToTransform, n)) {

                    if(m.access == ACC_PRIVATE + ACC_STATIC)
                        m.access = ACC_PUBLIC + ACC_STATIC;
                    else if(m.access == ACC_PRIVATE)
                        m.access = ACC_PUBLIC;
                    else if(m.access == ACC_STATIC)
                        m.access = ACC_PUBLIC + ACC_STATIC;
                }
            }
        }
        return bytes;
    }

    private boolean contains(final String[] array, final String key) {
        return Arrays.asList(array).contains(key);
    }
}
