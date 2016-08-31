package net.lomeli.lomlib.util.forge

import io.netty.buffer.ByteBuf

import java.util.HashMap

import net.minecraft.util.math.BlockPos

/**
 * Additional utils for ByteBuf
 */
object ByteUtil {

    fun writeIntArray(buffer: ByteBuf, vararg array: Int) {
        buffer.writeInt(array.size)
        for (i in array) {
            buffer.writeInt(array[i])
        }
    }

    fun readIntArray(buffer: ByteBuf): IntArray? {
        var returnArray: IntArray? = null
        val size = buffer.readInt()
        if (size > 0) {
            returnArray = IntArray(size)
            for (i in 0..size - 1) {
                returnArray[i] = buffer.readInt()
            }
        }
        return returnArray
    }

    fun writeIntMap(buffer: ByteBuf, map: HashMap<Int, Int>) {
        buffer.writeInt(map.size)
        for (entry in map.entries) {
            buffer.writeInt(entry.key)
            buffer.writeInt(entry.value)
        }
    }

    fun readIntMap(buffer: ByteBuf): HashMap<Int, Int> {
        val returnMap = HashMap<Int, Int>()
        val size = buffer.readInt()
        if (size > 0) {
            for (i in 0..size - 1) {
                val key = buffer.readInt()
                val value = buffer.readInt()
                returnMap.put(key, value)
            }
        }
        return returnMap
    }

    fun writeBlockPos(buffer: ByteBuf, pos: BlockPos) {
        buffer.writeLong(pos.toLong())
    }

    fun readBlockPos(buffer: ByteBuf): BlockPos {
        val posLong = buffer.readLong()
        return BlockPos.fromLong(posLong)
    }
}