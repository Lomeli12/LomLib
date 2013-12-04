package net.lomeli.lomlib.asm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;

import net.lomeli.lomlib.LomLib;
import net.minecraft.client.renderer.ThreadDownloadImageData;

public class DeofUtil {

	public static void setFieldAccess(String className, String fieldName,
			boolean access, boolean debug) {
		try {
			if (debug)
				LomLib.logger.log(Level.INFO, "Getting class " + className);
			Field[] fields = Class.forName(className).getDeclaredFields();
			for (Field field : fields) {
				if (field.getName().equalsIgnoreCase(fieldName)) {
					field.setAccessible(access);
					if (debug)
						LomLib.logger.log(Level.INFO, "Setting access for "
								+ fieldName + " to " + access);
					break;
				}
			}
		} catch (Exception e) {
		}
	}

	public static void setFieldAccess(String className, String fieldName,
			boolean access) {
		setFieldAccess(className, fieldName, access, false);
	}

	public static void setFieldsAccess(String className, String[] fieldNames,
			boolean[] access, boolean debug) {
		try {
			if (debug)
				LomLib.logger.log(Level.INFO, "Getting class " + className);
			Field[] fields = Class.forName(className).getDeclaredFields();
			for (Field field : fields) {
				for (int i = 0; i < fieldNames.length; i++) {
					if (field.getName().equalsIgnoreCase(fieldNames[i])) {
						field.setAccessible(access[i]);
						if (debug)
							LomLib.logger.log(Level.INFO, "Setting access for "
									+ fieldNames[i] + " to " + access[i]);
					}
				}
			}
		} catch (Exception e) {
		}
	}

	public static void setFieldsAccess(String className, String[] fieldNames,
			boolean[] access) {
		setFieldsAccess(className, fieldNames, access, false);
	}

	public static void setMethodAccess(String className, String methodName,
			boolean access, boolean debug) {
		try {
			Method[] methods = Class.forName(className).getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().equalsIgnoreCase(methodName)) {
					method.setAccessible(access);
				}
			}
		} catch (Exception e) {
		}
	}

	public static void setMethodAccess(String className, String methodName,
			boolean access) {
		setMethodAccess(className, methodName, access, false);
	}

	public static Field getField(Class<?> targetClass, String fieldName)
			throws NoSuchFieldException, SecurityException {
		return targetClass.getDeclaredField(fieldName);
	}
	
	public static String getFieldString(Class<?> targetClass, String fieldName,
			Object instance) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		return (String) getField(targetClass, fieldName).get(instance);
	}
	
	public static ThreadDownloadImageData getFieldDownloadImageData(Class<?> targetClass, String fieldName,
			Object instance) throws NoSuchFieldException, SecurityException,
			IllegalArgumentException, IllegalAccessException {
		return (ThreadDownloadImageData) getField(targetClass, fieldName).get(instance);
	}
}
