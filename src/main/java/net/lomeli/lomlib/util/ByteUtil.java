package net.lomeli.lomlib.util;

import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class ByteUtil {

    public static void writeIntArray(ByteBuf buffer, int... array) {
        buffer.writeInt(array.length);
        for (int i = 0; i < array.length; i++) {
            buffer.writeInt(array[i]);
        }
    }

    public static int[] readIntArray(ByteBuf buffer) {
        int[] returnArray = null;
        int size = buffer.readInt();
        if (size > 0) {
            returnArray = new int[size];
            for (int i = 0; i < size; i++) {
                returnArray[i] = buffer.readInt();
            }
        }
        return returnArray;
    }

    public static void writeIntMap(ByteBuf buffer, HashMap<Integer, Integer> map) {
        buffer.writeInt(map.size());
        for (Map.Entry<Integer, Integer> mapEntry : map.entrySet()) {
            int key = mapEntry.getKey();
            int value = mapEntry.getValue();
            buffer.writeInt(key);
            buffer.writeInt(value);
        }
    }

    public static HashMap<Integer, Integer> readIntMap(ByteBuf buffer) {
        HashMap<Integer, Integer> returnMap = new HashMap<Integer, Integer>();
        int size = buffer.readInt();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                int key = buffer.readInt();
                int value = buffer.readInt();
                returnMap.put(key, value);
            }
        }
        return returnMap;
    }
}
