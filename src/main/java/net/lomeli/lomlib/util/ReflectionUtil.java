package net.lomeli.lomlib.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.lomeli.lomlib.LomLibCore;

import net.minecraft.client.renderer.ThreadDownloadImageData;

public class ReflectionUtil {

    public static void setFieldAccess(String className, String fieldName, boolean access, boolean debug) {
        try {
            if(debug)
                LomLibCore.logger.logBasic("Getting class " + className);
            Field[] fields = Class.forName(className).getDeclaredFields();
            for(Field field : fields) {
                if(field.getName().equalsIgnoreCase(fieldName)) {
                    field.setAccessible(access);
                    if(debug)
                        LomLibCore.logger.logBasic("Setting access for " + fieldName + " to " + access);
                    break;
                }
            }
        }catch(Exception e) {
        }
    }

    public static void setFieldAccess(String className, String fieldName, boolean access) {
        setFieldAccess(className, fieldName, access, false);
    }

    public static void setFieldsAccess(String className, String[] fieldNames, boolean[] access, boolean debug) {
        try {
            if(debug)
                LomLibCore.logger.logBasic("Getting class " + className);
            Field[] fields = Class.forName(className).getDeclaredFields();
            for(Field field : fields) {
                for(int i = 0; i < fieldNames.length; i++) {
                    if(field.getName().equalsIgnoreCase(fieldNames[i])) {
                        field.setAccessible(access[i]);
                        if(debug)
                            LomLibCore.logger.logBasic("Setting access for " + fieldNames[i] + " to " + access[i]);
                    }
                }
            }
        }catch(Exception e) {
        }
    }

    public static void setFieldsAccess(String className, String[] fieldNames, boolean[] access) {
        setFieldsAccess(className, fieldNames, access, false);
    }

    public static void setMethodAccess(String className, String methodName, boolean access, boolean debug) {
        try {
            Method[] methods = Class.forName(className).getDeclaredMethods();
            for(Method method : methods) {
                if(method.getName().equalsIgnoreCase(methodName)) {
                    method.setAccessible(access);
                }
            }
        }catch(Exception e) {
        }
    }

    public static void setMethodAccess(String className, String methodName, boolean access) {
        setMethodAccess(className, methodName, access, false);
    }

    public static Field getField(Class<?> targetClass, String fieldName) throws NoSuchFieldException, SecurityException {
        return targetClass.getDeclaredField(fieldName);
    }

    public static String getFieldString(Class<?> targetClass, String fieldName, Object instance) throws NoSuchFieldException,
            SecurityException, IllegalArgumentException, IllegalAccessException {
        return (String) getField(targetClass, fieldName).get(instance);
    }

    public static ThreadDownloadImageData getFieldDownloadImageData(Class<?> targetClass, String fieldName, Object instance)
            throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
        return (ThreadDownloadImageData) getField(targetClass, fieldName).get(instance);
    }

    public static int getIntField(Object obj, String fieldName) {
        try {
            return obj.getClass().getDeclaredField(fieldName).getInt(obj);
        }catch(Exception e) {
            return 0;
        }
    }

    public static Object getField(Object obj, String field) {
        return getField(obj, field, obj.getClass());
    }

    public static Object getField(Object obj, String field, Class<?> baseClass) {
        try {
            return baseClass.getDeclaredField(field).get(obj);
        }catch(Exception e) {
            if(LomLibCore.debug)
                LomLibCore.logger.logWarning("Could not modify field " + field + " in " + obj.toString());
            return null;
        }
    }

    public static void setField(Object obj, String field, Object set) {
        try {
            obj.getClass().getDeclaredField(field).set(obj, set);
        }catch(Exception e) {
            if(LomLibCore.debug)
                LomLibCore.logger.logWarning("Could not modify field " + field + " in " + obj.toString());
        }
    }

    public static void useMethod(Object obj, String method, Object... arguments) {
        try {
            obj.getClass().getDeclaredMethod(method, arguments.getClass()).invoke(obj, arguments);
        }catch(Exception e) {
            if(LomLibCore.debug)
                LomLibCore.logger.logWarning("Could not use method " + method + " within " + obj.toString());
        }
    }
}
