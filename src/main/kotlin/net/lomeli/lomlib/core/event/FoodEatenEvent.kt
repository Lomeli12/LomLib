package net.lomeli.lomlib.core.event

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

import net.minecraftforge.event.entity.player.PlayerEvent

class FoodEatenEvent(player: EntityPlayer, val foodStack: ItemStack, val foodLevel: Int, val foodSaturationLevel: Float) : PlayerEvent(player)