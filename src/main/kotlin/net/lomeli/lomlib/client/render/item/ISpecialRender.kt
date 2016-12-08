package net.lomeli.lomlib.client.render.item

import net.minecraft.item.ItemStack
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
interface ISpecialRender {
    fun hasSpecialRenderer(stack : ItemStack?) : Boolean

    fun getSpecialRenderer(stack : ItemStack?) : IItemRenderer

    fun canItemSwing(stack : ItemStack?) : Boolean
}