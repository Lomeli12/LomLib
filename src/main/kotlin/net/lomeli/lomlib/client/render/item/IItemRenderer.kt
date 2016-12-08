package net.lomeli.lomlib.client.render.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.EnumHand
import net.minecraft.util.EnumHandSide
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly

@SideOnly(Side.CLIENT)
interface IItemRenderer {
    fun renderFirstPerson(hand : EnumHand, handSide : EnumHandSide, partialTicks : Float, swingProgress : Float, equipProgress : Float, stack : ItemStack?)

    fun renderThirdPerson(player : EntityPlayer?, side : EnumHandSide, stack : ItemStack?, limbSwing : Float, limbSwingAmount : Float, partialTicks : Float, ageInTicks : Float, netHeadYaw : Float, headPitch : Float, scale : Float)

    fun useRenderer(type : RenderType, hand : EnumHand, stack : ItemStack?) : Boolean
}