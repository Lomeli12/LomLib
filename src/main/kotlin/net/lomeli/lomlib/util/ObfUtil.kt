package net.lomeli.lomlib.util

import java.lang.reflect.Field
import java.lang.reflect.Method

import net.minecraftforge.fml.relauncher.ReflectionHelper

object ObfUtil {
    val isObf: Boolean
        get() {
            try {
                val fields = Class.forName("net.minecraft.world.World").declaredFields
                for (f in fields) {
                    f.isAccessible = true
                    if (f.name.equals("thunderingStrength"))
                        return false
                }
            } catch (e: Exception) {
                return true
            }

            return true
        }

    fun isFieldAccessible(clazz: Class<*>, vararg names: String): Boolean {
        try {
            val fields = clazz.declaredFields
            for (field in fields) {
                for (fieldName in names) {
                    if (field.name.equals(fieldName))
                        return field.isAccessible
                }
            }
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToFindFieldException(names, e)
        }

        return false
    }

    fun setFieldAccessible(clazz: Class<*>, vararg names: String) {
        try {
            val field = getField(clazz, *names)
            if (field != null)
                field.isAccessible = true
        } catch (e: Exception) {
        }

    }

    fun <T, E> setFieldValue(clazz: Class<out E>, instance: E?, value: Any, vararg names: String) {
        try {
            val field = getField(clazz, *names)
            field?.set(instance, value)
        } catch (e: Exception) {
        }

    }

    fun <T, E> getFieldValue(clazz: Class<out E>, instance: E?, vararg names: String): T {
        try {
            return getField(clazz, *names)!!.get(instance) as T
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToAccessFieldException(names, e)
        }

    }

    fun getField(clazz: Class<*>, vararg names: String): Field? {
        try {
            val fields = clazz.declaredFields
            for (field in fields) {
                for (fieldName in names) {
                    if (field.name.equals(fieldName)) {
                        field.isAccessible = true
                        return field
                    }
                }
            }
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToFindFieldException(names, e)
        }

        return null
    }

    fun isMethodAccessable(clazz: Class<*>, vararg names: String): Boolean {
        try {
            val methods = clazz.declaredMethods
            for (method in methods) {
                for (methodName in names) {
                    if (method.name.equals(methodName))
                        return method.isAccessible
                }
            }
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToFindMethodException(names, e)
        }

        return false
    }

    fun <T, E> invokeMethod(clazz: Class<out E>, instance: E?, names: Array<String>, vararg args: Any): Any? {
        try {
            val method = getMethod(clazz, *names)
            if (method != null)
                return method.invoke(instance, *args)
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToFindMethodException(names, e)
        }

        return null
    }

    fun getMethod(clazz: Class<*>, vararg names: String): Method? {
        try {
            val methods = clazz.declaredMethods
            for (method in methods) {
                for (methodName in names) {
                    if (method.name.equals(methodName)) {
                        method.isAccessible = true
                        return method
                    }
                }
            }
        } catch (e: Exception) {
            throw ReflectionHelper.UnableToFindMethodException(names, e)
        }

        return null
    }
}