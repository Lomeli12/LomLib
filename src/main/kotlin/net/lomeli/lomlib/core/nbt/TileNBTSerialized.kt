package net.lomeli.lomlib.core.nbt

import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity

abstract class TileNBTSerialized : TileEntity() {
    override fun readFromNBT(compound: NBTTagCompound) {
        super.readFromNBT(compound)
        val tag = compound.getCompoundTag("tile_data_serialized")
        if (tag != null) {
            try {
                val deserialize = NBTSerializer.fromNBT(tag, this.javaClass)
                val fields = this.javaClass.fields
                if (fields != null && fields.size > 0) {
                    for (f in fields) {
                        if (f.getAnnotation(NBT::class.java) != null) {
                            val j = deserialize.javaClass.getField(f.name)
                            if (j != null) f.set(this, j.get(deserialize))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun writeToNBT(compound: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(compound)
        val tag = NBTSerializer.toNBT(this)
        if (tag != null) compound.setTag("tile_data_serialized", tag)
        return compound
    }
}