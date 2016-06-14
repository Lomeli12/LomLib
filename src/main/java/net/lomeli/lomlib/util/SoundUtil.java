package net.lomeli.lomlib.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SoundUtil {
    private static int size = 0;

    static {
        size = SoundEvent.REGISTRY.getKeys().size();
    }

    @SideOnly(Side.CLIENT)
    public static SoundEvent register(String name) {
        ResourceLocation loc = new ResourceLocation(name);
        SoundEvent e = new SoundEvent(loc);
        SoundEvent.REGISTRY.register(size, loc, e);
        size++;
        return e;
    }

    @SideOnly(Side.CLIENT)
    public static void playSoundAtEntity(Entity entity, SoundEvent event, SoundCategory category, float volume, float pitch) {
        if (entity != null && entity.worldObj != null)
            entity.worldObj.playSound(null, entity.posX, entity.posY, entity.posZ, event, category, volume, pitch);
    }
}
