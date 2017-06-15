package net.lomeli.lomlib.util;

import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTParser {
    private static final HashMap<Class, Pair<Reader, Writer>> handlers = new HashMap();
    private static final HashMap<Class, Field[]> fieldCache = new HashMap();

    static {
        map(byte.class, NBTParser::readByte, NBTParser::writeByte);
        map(short.class, NBTParser::readShort, NBTParser::writeShort);
        map(int.class, NBTParser::readInt, NBTParser::writeInt);
        map(long.class, NBTParser::readLong, NBTParser::writeLong);
        map(float.class, NBTParser::readFloat, NBTParser::writeFloat);
        map(double.class, NBTParser::readDouble, NBTParser::writeDouble);
        map(boolean.class, NBTParser::readBoolean, NBTParser::writeBoolean);
        map(char.class, NBTParser::readChar, NBTParser::writeChar);
        map(String.class, NBTParser::readString, NBTParser::writeString);
        map(NBTTagCompound.class, NBTParser::readNBT, NBTParser::writeNBT);
        map(ItemStack.class, NBTParser::readStack, NBTParser::writeStack);
        map(BlockPos.class, NBTParser::readBlockPos, NBTParser::writeBlockPos);
    }

    public static <T> T fromNBT(NBTTagCompound tag, Class<T> clazz) {
        try {
            T obj = getDummyObject(clazz);
            if (obj == null)
                return null;
            Field[] clFields = getClassFields(clazz);
            for (Field f : clFields) {
                Class<?> type = f.getType();
                if (acceptField(f, type))
                    readField(f, obj, type, tag);
                else
                    readUnknownType(f, obj, tag);
            }
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> T getDummyObject(Class<T> clazz) {
        try {
            Constructor[] constructors = clazz.getConstructors();
            if (constructors != null && constructors.length > 0) {
                for (Constructor cont : constructors) {
                    try {
                        // I was really lazy here. I could probably do this properly
                        // if I could be asked, but eh.
                        Object[] dummyArgs = new Object[cont.getParameterCount()];
                        Object newObj = cont.newInstance(dummyArgs);
                        if (newObj != null)
                            return (T) newObj;
                    } catch (Exception ex) {
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static NBTTagCompound toNBT(Object obj) {
        NBTTagCompound tag = new NBTTagCompound();
        try {
            Class<?> clazz = obj.getClass();
            Field[] clFields = getClassFields(clazz);
            for (Field f : clFields) {
                Class<?> type = f.getType();
                if (acceptField(f, type))
                    writeField(f, obj, type, tag);
                else
                    writeUnknownType(f, obj, tag);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tag;
    }

    private static void writeUnknownType(Field f, Object obj, NBTTagCompound parentTag) {
        try {
            if (!f.isAccessible())
                f.setAccessible(true);
            NBTTagCompound tag = toNBT(f.get(obj));
            parentTag.setTag(f.getName(), tag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readUnknownType(Field f, Object obj, NBTTagCompound parentTag) {
        try {
            if (!f.isAccessible())
                f.setAccessible(true);
            NBTTagCompound tag = parentTag.getCompoundTag(f.getName());
            Object value = fromNBT(tag, f.getType());
            f.set(obj, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Field[] getClassFields(Class<?> clazz) {
        if (fieldCache.containsValue(clazz))
            return fieldCache.get(clazz);
        else {
            Field[] fields = clazz.getDeclaredFields();
            for (Field f : fields) {
                if (!f.isAccessible())
                    f.setAccessible(true);
            }
            Arrays.sort(fields, (Field f1, Field f2) -> {
                return f1.getName().compareTo(f2.getName());
            });
            fieldCache.put(clazz, fields);
            return fields;
        }
    }

    private static final void writeField(Field f, Object obj, Class clazz, NBTTagCompound tag) throws IllegalArgumentException,
            IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        if (!f.isAccessible())
            f.setAccessible(true);
        handler.getRight().write(f.getName(), f.get(obj), tag);
    }

    private static final void readField(Field f, Object obj, Class clazz, NBTTagCompound tag) throws IllegalArgumentException,
            IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        if (!f.isAccessible())
            f.setAccessible(true);
        f.set(obj, handler.getLeft().read(f.getName(), tag));
    }

    private static Pair<Reader, Writer> getHandler(Class<?> clazz) {
        Pair<Reader, Writer> pair = handlers.get(clazz);
        if (pair == null)
            throw new RuntimeException("No R/W handler for  " + clazz);
        return pair;
    }

    private static boolean acceptField(Field f, Class<?> type) {
        int mods = f.getModifiers();
        if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods))
            return false;
        return handlers.containsKey(type);
    }

    private static <T extends Object> void map(Class<T> type, Reader<T> reader, Writer<T> writer) {
        handlers.put(type, Pair.of(reader, writer));
    }

    public static byte readByte(String fieldName, NBTTagCompound tag) {
        return tag.getByte(fieldName);
    }

    private static void writeByte(String fieldName, byte b, NBTTagCompound tag) {
        tag.setByte(fieldName, b);
    }

    private static short readShort(String fieldName, NBTTagCompound tag) {
        return tag.getShort(fieldName);
    }

    private static void writeShort(String fieldName, short s, NBTTagCompound tag) {
        tag.setShort(fieldName, s);
    }

    private static int readInt(String fieldName, NBTTagCompound tag) {
        return tag.getInteger(fieldName);
    }

    private static void writeInt(String fieldName, int i, NBTTagCompound tag) {
        tag.setInteger(fieldName, i);
    }

    private static long readLong(String fieldName, NBTTagCompound tag) {
        return tag.getLong(fieldName);
    }

    private static void writeLong(String fieldName, long l, NBTTagCompound tag) {
        tag.setLong(fieldName, l);
    }

    private static float readFloat(String fieldName, NBTTagCompound tag) {
        return tag.getFloat(fieldName);
    }

    private static void writeFloat(String fieldName, float f, NBTTagCompound tag) {
        tag.setFloat(fieldName, f);
    }

    private static double readDouble(String fieldName, NBTTagCompound tag) {
        return tag.getDouble(fieldName);
    }

    private static void writeDouble(String fieldName, double d, NBTTagCompound tag) {
        tag.setDouble(fieldName, d);
    }

    private static boolean readBoolean(String fieldName, NBTTagCompound tag) {
        return tag.getBoolean(fieldName);
    }

    private static void writeBoolean(String fieldName, boolean bool, NBTTagCompound tag) {
        tag.setBoolean(fieldName, bool);
    }

    private static char readChar(String fieldName, NBTTagCompound tag) {
        return (char) readInt(fieldName, tag);
    }

    private static void writeChar(String fieldName, char c, NBTTagCompound tag) {
        tag.setInteger(fieldName, Character.getNumericValue(c));
    }

    private static byte[] readByteArray(String fieldName, NBTTagCompound tag) {
        return tag.getByteArray(fieldName);
    }

    private static void writeByteArray(String fieldName, byte[] ba, NBTTagCompound tag) {
        tag.setByteArray(fieldName, ba);
    }

    private static String readString(String fieldName, NBTTagCompound tag) {
        return tag.getString(fieldName);
    }

    private static void writeString(String fieldName, String s, NBTTagCompound tag) {
        tag.setString(fieldName, s);
    }

    private static int[] readIntArray(String fieldName, NBTTagCompound tag) {
        return tag.getIntArray(fieldName);
    }

    private static void writeIntArray(String fieldName, int[] ia, NBTTagCompound tag) {
        tag.setIntArray(fieldName, ia);
    }

    private static NBTTagCompound readNBT(String fieldName, NBTTagCompound tag) {
        return tag.getCompoundTag(fieldName);
    }

    private static void writeNBT(String fieldName, NBTTagCompound n, NBTTagCompound tag) {
        tag.setTag(fieldName, n);
    }

    private static ItemStack readStack(String fieldName, NBTTagCompound tag) {
        return new ItemStack(readNBT(fieldName, tag));
    }

    private static void writeStack(String fieldName, ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound itemTag = new NBTTagCompound();
        stack.writeToNBT(itemTag);
        writeNBT(fieldName, itemTag, tag);
    }

    private static BlockPos readBlockPos(String fieldName, NBTTagCompound tag) {
        return BlockPos.fromLong(readLong(fieldName, tag));
    }

    private static void writeBlockPos(String fieldName, BlockPos pos, NBTTagCompound tag) {
        writeLong(fieldName, pos.toLong(), tag);
    }

    public static interface Writer<T extends Object> {
        public void write(String fieldName, T t, NBTTagCompound tag);
    }

    public static interface Reader<T extends Object> {
        public T read(String fieldName, NBTTagCompound tag);
    }
}
