package net.lomeli.lomlib.asm;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import net.minecraft.launchwrapper.IClassTransformer;

import net.minecraftforge.fml.common.FMLLog;

import static org.objectweb.asm.Opcodes.*;

public class ClassTransformer implements IClassTransformer {
    private final String[] renderLayerNames = new String[]{"renderArmorLayer", "func_188361_a", "a"};

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.client.renderer.entity.layers.LayerArmorBase")) {
            log("Working on %s", transformedName);

            ClassReader reader = new ClassReader(basicClass);
            ClassNode node = new ClassNode();
            reader.accept(node, 0);

            for (MethodNode method : node.methods) {
                if (matchesName(method.name, renderLayerNames)) {
                    log("Found method: %s %s", method.name, method.desc);
                    Iterator<AbstractInsnNode> iterator = method.instructions.iterator();

                    insL:
                    while (iterator.hasNext()) {
                        AbstractInsnNode anode = iterator.next();
                        if (anode.getOpcode() == ASTORE) {
                            log("Patching %s in %s", method.name, transformedName);
                            LabelNode returnNode = new LabelNode();
                            InsnList newInstructions = new InsnList();
                            newInstructions.add(new VarInsnNode(ALOAD, 1));
                            newInstructions.add(new VarInsnNode(ALOAD, 10));
                            newInstructions.add(new VarInsnNode(ALOAD, 9));
                            newInstructions.add(new MethodInsnNode(INVOKESTATIC, "net/lomeli/lomlib/client/event/ClientEventHooks", "renderHooks", "(Lnet/minecraft/entity/EntityLivingBase;Lnet/minecraft/item/ItemStack;Lnet/minecraft/inventory/EntityEquipmentSlot;)Z", false));
                            newInstructions.add(new JumpInsnNode(IFEQ, returnNode));
                            newInstructions.add(new InsnNode(RETURN));
                            newInstructions.add(returnNode);
                            method.instructions.insert(anode, newInstructions);
                            log("Patched %s in %s", method.name, transformedName);
                            break insL;
                        }
                    }
                    ClassWriter writer = new ClassWriter(0);
                    node.accept(writer);
                    return writer.toByteArray();
                }
            }
        }
        return basicClass;
    }

    private static boolean matchesName(String name, String[] possibleName) {
        for (String str : possibleName) {
            if (name.equals(str))
                return true;
        }
        return false;
    }

    private static void log(String str, Object... objs) {
        FMLLog.info("[LomLib ASM] %s", String.format(str, objs));
    }
}
