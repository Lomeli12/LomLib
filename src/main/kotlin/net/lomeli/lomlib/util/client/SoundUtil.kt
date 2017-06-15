package net.lomeli.lomlib.util.client

import net.minecraft.entity.Entity
import net.minecraft.util.ResourceLocation
import net.minecraft.util.SoundCategory
import net.minecraft.util.SoundEvent

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT) object SoundUtil {
    private var size = 0

    init {
        size = SoundEvent.REGISTRY.keys.size
    }

    @SideOnly(Side.CLIENT) fun register(name: String): SoundEvent {
        val loc = ResourceLocation(name)
        val e = SoundEvent(loc)
        SoundEvent.REGISTRY.register(size, loc, e)
        size++
        return e
    }

    @SideOnly(Side.CLIENT) fun playSoundAtEntity(entity: Entity?, event: SoundEvent, category: SoundCategory, volume: Float, pitch: Float) {
        if (entity != null && entity.world != null)
            entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, event, category, volume, pitch)
    }
}
