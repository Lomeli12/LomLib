package net.lomeli.lomlib.client.event;

import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class RenderArmorEvent extends Event {
    public final ItemStack slotStack;
    public final int armorSlot;

    public RenderArmorEvent(ItemStack slotStack, int armorSlot) {
        this.slotStack = slotStack;
        this.armorSlot = armorSlot;
    }
}
