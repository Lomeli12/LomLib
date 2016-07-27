package net.lomeli.lomlib.asm.module;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

import java.util.Iterator;

import static org.objectweb.asm.Opcodes.*;

public class ItemFoodModule extends TransformerModule{
    private final String[] onItemUseFinshNames = {"onItemUseFinish", "func_77654_b", "a"};

    @Override
    public String getSrgClass() {
        return "net.minecraft.item.ItemFood";
    }

    @Override
    public String getObfClass() {
        return "adt";
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        log("Working on %s", transformedName);

        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept(node, 0);

        for (MethodNode method : node.methods) {
            if (matchesName(method.name, onItemUseFinshNames)) {
                log("Found method: %s %s", method.name, method.desc);
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();


                int counter = 0;
                insL:
                while (iterator.hasNext()) {
                    AbstractInsnNode anode = iterator.next();
                    if (anode.getOpcode() == INVOKEVIRTUAL)
                        ++counter;
                    if (counter == 2) {
                        log("Patching %s in %s", method.name, transformedName);
                        InsnList newInstructions = new InsnList();
                        newInstructions.add(new VarInsnNode(ALOAD, 4));
                        newInstructions.add(new VarInsnNode(ALOAD, 0));
                        newInstructions.add(new VarInsnNode(ALOAD, 1));
                        newInstructions.add(new MethodInsnNode(INVOKESTATIC, "net/lomeli/lomlib/core/event/EventHooks", "onFoodEaten", "(Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/item/ItemFood;Lnet/minecraft/item/ItemStack;)V", false));
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
        return basicClass;
    }
}
