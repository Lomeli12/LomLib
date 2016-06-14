package net.lomeli.lomlib.client.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Cancelable
@SideOnly(Side.CLIENT)
public class RenderArmorEvent extends LivingEvent {
    private final ItemStack slotStack;
    private final EntityEquipmentSlot equipmentSlot;

    public RenderArmorEvent(EntityLivingBase entityLivingBase, ItemStack slotStack, EntityEquipmentSlot armorSlot) {
        super(entityLivingBase);
        this.slotStack = slotStack;
        this.equipmentSlot = armorSlot;
    }

    public ItemStack getSlotStack() {
        return slotStack;
    }

    public EntityEquipmentSlot getEquipmentSlot() {
        return equipmentSlot;
    }
}
