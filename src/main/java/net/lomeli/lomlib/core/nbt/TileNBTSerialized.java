package net.lomeli.lomlib.core.nbt;

import java.lang.reflect.Field;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class TileNBTSerialized extends TileEntity {
    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        NBTTagCompound tag = compound.getCompoundTag("tile_data_serialized");
        if (tag != null) {
            try {
                Object deserialize = NBTSerializer.fromNBT(tag, this.getClass());
                Field[] fields = this.getClass().getFields();
                if (fields != null && fields.length > 0) {
                    for (Field f : fields) {
                        if (f.getAnnotation(NBT.class) != null) {
                            Field j = deserialize.getClass().getField(f.getName());
                            if (j != null) f.set(this, j.get(deserialize));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagCompound tag = NBTSerializer.toNBT(this);
        if (tag != null) compound.setTag("tile_data_serialized", tag);
        return compound;
    }
}
