package net.lomeli.lomlib.core.nbt;

import com.google.common.base.Strings;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public class NBTSerializer {
    private static final HashMap<Class, Pair<Reader, Writer>> handlers = new HashMap();
    private static final HashMap<Class, Field[]> fieldCache = new HashMap();

    static {
        map(byte.class, NBTSerializer::readByte, NBTSerializer::writeByte);
        map(short.class, NBTSerializer::readShort, NBTSerializer::writeShort);
        map(int.class, NBTSerializer::readInt, NBTSerializer::writeInt);
        map(long.class, NBTSerializer::readLong, NBTSerializer::writeLong);
        map(float.class, NBTSerializer::readFloat, NBTSerializer::writeFloat);
        map(double.class, NBTSerializer::readDouble, NBTSerializer::writeDouble);
        map(byte[].class, NBTSerializer::readByteArray, NBTSerializer::writeByteArray);
        map(String.class, NBTSerializer::readString, NBTSerializer::writeString);
        map(ItemStack.class, NBTSerializer::readItemStack, NBTSerializer::writeItemStack);
        map(NBTTagCompound.class, NBTSerializer::readNBTTagCompound, NBTSerializer::writeNBTTagCompound);
        map(BlockPos.class, NBTSerializer::readBlockPos, NBTSerializer::writeBlockPos);
    }

    public static NBTTagCompound toNBT(Object obj) {
        NBTTagCompound tag = new NBTTagCompound();
        try {
            Field[] fields = getClassFields(obj.getClass());
            if (fields != null && fields.length > 0) {
                for (Field f : fields) {
                    if (readableField(f)) {
                        NBT nbt = f.getAnnotation(NBT.class);
                        if (nbt != null) {
                            String key = f.getName();
                            if (!Strings.isNullOrEmpty(nbt.name())) key = nbt.name();
                            if (hasHandler(f.getType())) writeField(f, f.getType(), obj, key, tag);
                            else {
                                NBTTagCompound objTag = toNBT(f.get(obj));
                                if (objTag != null) tag.setTag(key, objTag);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error at writing to NBT " + obj, e);
        }
        return tag;
    }

    public static <T> T fromNBT(NBTTagCompound tag, Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T obj = clazz.newInstance();
        try {
            Field[] fields = getClassFields(obj.getClass());
            if (fields != null && fields.length > 0) {
                for (Field f : fields) {
                    if (readableField(f)) {
                        NBT nbt = f.getAnnotation(NBT.class);
                        if (nbt != null) {
                            String key = f.getName();
                            if (!Strings.isNullOrEmpty(nbt.name())) key = nbt.name();
                            if (hasHandler(f.getType())) readField(f, f.getType(), obj, key, tag);
                            else {
                                Object value = fromNBT(tag.getCompoundTag(key), f.getType());
                                f.set(obj, value);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading NBT Object " + tag, e);
        }
        return obj;
    }

    private static Field[] getClassFields(Class<?> clazz) {
        if (fieldCache.containsValue(clazz))
            return fieldCache.get(clazz);
        else {
            Field[] fields = clazz.getFields();
            Arrays.sort(fields, (Field f1, Field f2) -> {
                return f1.getName().compareTo(f2.getName());
            });
            fieldCache.put(clazz, fields);
            return fields;
        }
    }

    private static final void writeField(Field f, Class clazz, Object obj, String key, NBTTagCompound tag) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        handler.getRight().write(key, f.get(obj), tag);
    }

    private static final void readField(Field f, Class clazz, Object obj, String key, NBTTagCompound tag) throws IllegalArgumentException, IllegalAccessException {
        Pair<Reader, Writer> handler = getHandler(clazz);
        f.set(obj, handler.getLeft().read(key, tag));
    }

    private static Pair<Reader, Writer> getHandler(Class<?> clazz) {
        Pair<Reader, Writer> pair = handlers.get(clazz);
        if (pair == null)
            throw new RuntimeException("No R/W handler for  " + clazz);
        return pair;
    }

    private static boolean readableField(Field f) {
        int mods = f.getModifiers();
        if (Modifier.isFinal(mods) || Modifier.isStatic(mods) || Modifier.isTransient(mods))
            return false;
        return true;
    }

    private static boolean hasHandler(Class<?> type) {
        return handlers.containsKey(type);
    }

    private static <T extends Object> void map(Class<T> type, Reader<T> reader, Writer<T> writer) {
        handlers.put(type, Pair.of(reader, writer));
    }

    private static void writeByte(String key, byte b, NBTTagCompound tag) {
        tag.setByte(key, b);
    }

    private static byte readByte(String key, NBTTagCompound tag) {
        return tag.getByte(key);
    }

    private static void writeShort(String key, short s, NBTTagCompound tag) {
        tag.setShort(key, s);
    }

    private static short readShort(String key, NBTTagCompound tag) {
        return tag.getShort(key);
    }

    private static void writeInt(String key, int i, NBTTagCompound tag) {
        tag.setInteger(key, i);
    }

    private static int readInt(String key, NBTTagCompound tag) {
        return tag.getInteger(key);
    }

    private static void writeLong(String key, long l, NBTTagCompound tag) {
        tag.setLong(key, l);
    }

    private static long readLong(String key, NBTTagCompound tag) {
        return tag.getLong(key);
    }

    private static void writeFloat(String key, float f, NBTTagCompound tag) {
        tag.setFloat(key, f);
    }

    private static float readFloat(String key, NBTTagCompound tag) {
        return tag.getFloat(key);
    }

    private static void writeDouble(String key, double d, NBTTagCompound tag) {
        tag.setDouble(key, d);
    }

    private static double readDouble(String key, NBTTagCompound tag) {
        return tag.getDouble(key);
    }

    private static void writeByteArray(String key, byte[] ba, NBTTagCompound tag) {
        tag.setByteArray(key, ba);
    }

    private static byte[] readByteArray(String key, NBTTagCompound tag) {
        return tag.getByteArray(key);
    }

    private static void writeString(String key, String s, NBTTagCompound tag) {
        tag.setString(key, s);
    }

    private static String readString(String key, NBTTagCompound tag) {
        return tag.getString(key);
    }

    private static void writeItemStack(String key, ItemStack stack, NBTTagCompound tag) {
        NBTTagCompound itemTag = new NBTTagCompound();
        stack.writeToNBT(itemTag);
        tag.setTag(key, itemTag);
    }

    private static ItemStack readItemStack(String key, NBTTagCompound tag) {
        NBTTagCompound itemTag = tag.getCompoundTag(key);
        return ItemStack.loadItemStackFromNBT(itemTag);
    }

    private static void writeNBTTagCompound(String key, NBTTagCompound nbt, NBTTagCompound tag) {
        tag.setTag(key, nbt);
    }

    private static NBTTagCompound readNBTTagCompound(String key, NBTTagCompound tag) {
        return tag.getCompoundTag(key);
    }

    private static void writeBlockPos(String key, BlockPos pos, NBTTagCompound tag) {
        tag.setLong(key, pos.toLong());
    }

    private static BlockPos readBlockPos(String key, NBTTagCompound tag) {
        return BlockPos.fromLong(tag.getLong(key));
    }

    public static interface Writer<T extends Object> {
        public void write(String key, T t, NBTTagCompound tag);
    }

    public static interface Reader<T extends Object> {
        public T read(String key, NBTTagCompound tag);
    }
}
