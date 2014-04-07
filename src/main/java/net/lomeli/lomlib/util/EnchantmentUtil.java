package net.lomeli.lomlib.util;

import net.minecraft.enchantment.Enchantment;

public class EnchantmentUtil {
    public static int getUniqueEnchantID() {
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++){
            if (Enchantment.enchantmentsList[i] == null)
                return i;
        }
        return 0;
    }
}
