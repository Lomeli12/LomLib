package net.lomeli.lomlib.client.render.item

import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
interface IItemRenderer {
    fun renderFirstPerson(hand: EnumHand, handSide: EnumHandSide, partialTicks: Float, swingProgress: Float, equipProgress: Float, stack: ItemStack?)

    fun useRenderer(type: RenderType, hand: EnumHand, stack: ItemStack?) : Boolean
}