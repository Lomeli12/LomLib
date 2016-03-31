package net.lomeli.lomlib.client.event;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderArmorEvent extends Event {
    public final ItemStack slotStack;
    public final EntityEquipmentSlot equipmentSlot;

    public RenderArmorEvent(ItemStack slotStack, EntityEquipmentSlot armorSlot) {
        this.slotStack = slotStack;
        this.equipmentSlot = armorSlot;
    }
}
