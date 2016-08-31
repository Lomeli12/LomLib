package net.lomeli.lomlib.util

import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack

object EnchantUtil {
    fun getPlayerXP(player: EntityPlayer): Int {
        return (getExperienceForLevel(player.experienceLevel) + player.experience * player.xpBarCap()) as Int
    }

    fun addPlayerXP(player: EntityPlayer, amount: Int) {
        val experience = getPlayerXP(player) + amount
        player.experienceTotal = experience
        player.experienceLevel = getLevelForExperience(experience)
        val expForLevel = getExperienceForLevel(player.experienceLevel)
        player.experience = (experience - expForLevel).toFloat() / player.xpBarCap() as Float
    }

    fun getExperienceForLevel(level: Int): Int {
        if (level == 0) return 0
        if (level > 0 && level < 16)
            return level * 17
        else if (level > 15 && level < 31)
            return (1.5 * Math.pow(level.toDouble(), 2.0) - 29.5 * level + 360).toInt()
        else
            return (3.5 * Math.pow(level.toDouble(), 2.0) - 151.5 * level + 2220).toInt()
    }

    fun getXpToNextLevel(level: Int): Int {
        val levelXP = getLevelForExperience(level)
        val nextXP = getExperienceForLevel(level + 1)
        return nextXP - levelXP
    }

    fun getLevelForExperience(experience: Int): Int {
        var i = 0
        while (getExperienceForLevel(i) <= experience) {
            i++
        }
        return i - 1
    }

    fun stackHasEnchant(stack: ItemStack?, enchantment: Enchantment?): Boolean {
        if (stack == null || enchantment == null)
            return false
        else {
            val enchantmentId = Enchantment.getEnchantmentID(enchantment)
            val nbttaglist = stack!!.getEnchantmentTagList()
            if (nbttaglist == null)
                return false
            else {
                for (i in 0..nbttaglist!!.tagCount() - 1) {
                    val id = nbttaglist!!.getCompoundTagAt(i).getShort("id")
                    if (id as Int == enchantmentId)
                        return true
                }
                return false
            }
        }
    }
}