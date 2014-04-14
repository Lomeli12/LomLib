package net.lomeli.lomlib.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EnchantmentUtil {
    public static int getUniqueEnchantID() {
        for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
            if (Enchantment.enchantmentsList[i] == null)
                return i;
        }
        return 0;
    }

    public static ItemStack getEnchantedBook(Enchantment enchant, short lvl) {
        ItemStack book = new ItemStack(Items.enchanted_book);
        if (enchant != null && lvl > 0) {
            book.stackTagCompound = new NBTTagCompound();
            NBTTagList enchantList = new NBTTagList();
            NBTTagCompound enchantTag = new NBTTagCompound();
            enchantTag.setShort("id", (short)enchant.effectId);
            enchantTag.setShort("lvl", lvl);
            enchantList.appendTag(enchantTag);
            book.stackTagCompound.setTag("StoredEnchantments", enchantList);
        }
        return book;
    }

    public static boolean itemHasEnchant(ItemStack stack, Enchantment enchant) {
        if (stack != null) {
            NBTTagList enchantList = stack.getEnchantmentTagList();
            if (enchantList != null) {
                for (int i = 0; i < enchantList.tagCount(); i++) {
                    short id = enchantList.getCompoundTagAt(i).getShort("id");
                    Enchantment ench = Enchantment.enchantmentsList[id];
                    if (ench != null && ench.effectId == enchant.effectId)
                        return true;
                }
            }
        }
        return false;
    }
}
