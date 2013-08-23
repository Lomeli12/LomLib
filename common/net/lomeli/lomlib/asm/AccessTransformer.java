package net.lomeli.lomlib.asm;

import static scala.tools.asm.Opcodes.ACC_PUBLIC;
import static scala.tools.asm.Opcodes.ACC_PRIVATE;
import static scala.tools.asm.Opcodes.ACC_STATIC;

import java.util.Arrays;
import java.util.Iterator;
import java.util.logging.Level;

import net.lomeli.lomlib.LomLib;
import net.minecraft.launchwrapper.IClassTransformer;

import scala.tools.asm.ClassReader;
import scala.tools.asm.ClassWriter;
import scala.tools.asm.tree.ClassNode;
import scala.tools.asm.tree.FieldNode;
import scala.tools.asm.tree.MethodNode;

public class AccessTransformer implements IClassTransformer{
	
	private final String[] obfClassToTransform = {"ber"};
    private final String[] classToTransform = {"net.minecraft.client.entity.AbstractClientPlayer"};
    private final String[] obfFieldsToTransform = {"c", "e"};
    private final String[] fieldsToTransform = {"field_110315_c", "field_110313_e", "field_110312_d", "field_110316_a"};
    private final String[] obfMethodsToTransform = {};
    private final String[] methodsToTransform = {};
	
	@Override
	public byte[] transform(String name, String newName, byte[] bytes) {
		if(contains(classToTransform, name) || contains(obfClassToTransform, name)) {
			LomLib.logger.log(Level.INFO, "[DevCapes]: **Transforming AbstractClientPlayer**");
            ClassNode classNode = new ClassNode();
            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(classNode, 0);

            Iterator<FieldNode> fields = classNode.fields.iterator();
            while(fields.hasNext()) {
            	FieldNode f = fields.next();
            	String n = f.name;
                if(contains(fieldsToTransform, n) || contains(obfFieldsToTransform, n)) {
                	if(LomLib.debug)
                		LomLib.logger.log(Level.INFO, "[DevCapes]: Found field " + f.name + ", making it public.");
                    f.access = ACC_PUBLIC;
                }
            }
            Iterator<MethodNode> methods = classNode.methods.iterator();
            while(methods.hasNext()) {
            	MethodNode m = methods.next();
                String n = m.name;
                if(contains(methodsToTransform, n) || contains(obfMethodsToTransform, n)) {
                    if(LomLib.debug)
                    	LomLib.logger.log(Level.INFO, "[DevCapes]: Found method " + m.name + ", making it public.");
                    
                    if(m.access == ACC_PRIVATE + ACC_STATIC)
                    	m.access = ACC_PUBLIC + ACC_STATIC;
                    else if(m.access == ACC_PRIVATE)
                    	m.access = ACC_PUBLIC;
                    else if(m.access == ACC_STATIC)
                    	m.access = ACC_PUBLIC + ACC_STATIC;
                }
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        else
        	return bytes;
	}

	private boolean contains(final String[] array, final String key) {
		return Arrays.asList(array).contains(key);
    }
}
