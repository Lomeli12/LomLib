package net.lomeli.lomlib.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagList;

/**
 * From OpenModsLib https://github.com/OpenMods/OpenModsLib/blob/master/src/main/java/openmods/utils/EnchantmentUtils.java
 */
public class EnchantUtil {
    public static int getPlayerXP(EntityPlayer player) {
        return (int) (getExperienceForLevel(player.experienceLevel) + (player.experience * player.xpBarCap()));
    }

    public static void addPlayerXP(EntityPlayer player, int amount) {
        int experience = getPlayerXP(player) + amount;
        player.experienceTotal = experience;
        player.experienceLevel = getLevelForExperience(experience);
        int expForLevel = getExperienceForLevel(player.experienceLevel);
        player.experience = (float) (experience - expForLevel) / (float) player.xpBarCap();
    }

    public static int getExperienceForLevel(int level) {
        if (level == 0) return 0;
        if (level > 0 && level < 16)
            return level * 17;
        else if (level > 15 && level < 31)
            return (int) (1.5 * Math.pow(level, 2) - 29.5 * level + 360);
        else
            return (int) (3.5 * Math.pow(level, 2) - 151.5 * level + 2220);
    }

    public static int getXpToNextLevel(int level) {
        int levelXP = getLevelForExperience(level);
        int nextXP = getExperienceForLevel(level + 1);
        return nextXP - levelXP;
    }

    public static int getLevelForExperience(int experience) {
        int i = 0;
        while (getExperienceForLevel(i) <= experience) {
            i++;
        }
        return i - 1;
    }

    public static boolean stackHasEnchant(ItemStack stack, Enchantment enchantment) {
        if (stack == null || enchantment == null) return false;
        else {
            int enchantmentId = Enchantment.getEnchantmentID(enchantment);
            NBTTagList nbttaglist = stack.getEnchantmentTagList();
            if (nbttaglist == null)
                return false;
            else {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    int id = nbttaglist.getCompoundTagAt(i).getShort("id");
                    if (id == enchantmentId)
                        return true;
                }
                return false;
            }
        }
    }
}
