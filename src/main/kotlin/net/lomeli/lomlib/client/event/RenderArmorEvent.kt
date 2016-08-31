package net.lomeli.lomlib.client.event

import net.minecraft.entity.EntityLivingBase
import net.minecraft.inventory.EntityEquipmentSlot
import net.minecraft.item.ItemStack

import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.entity.living.LivingEvent
import net.minecraftforge.fml.common.eventhandler.Cancelable
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@Cancelable
@SideOnly(Side.CLIENT)
class RenderArmorEvent(entityLivingBase: EntityLivingBase, val slotStack: ItemStack?, val equipmentSlot: EntityEquipmentSlot) : LivingEvent(entityLivingBase)