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
            enchantTag.setShort("id", (short) enchant.effectId);
            enchantTag.setShort("lvl", lvl);
            enchantList.appendTag(enchantTag);
            book.stackTagCompound.setTag("StoredEnchantments", enchantList);
        }
        return book;
    }

    public static ItemStack addEnchantment(ItemStack stack, Enchantment enchant, short lvl) {
        if (stack == null || enchant == null)
            return null;
        if (stack.getTagCompound() == null)
            stack.stackTagCompound = new NBTTagCompound();

        NBTTagList enchantList = stack.getEnchantmentTagList();
        NBTTagCompound enchantTag = new NBTTagCompound();

        if (itemHasEnchant(stack, enchant)) {
            if (lvl > 0) {
                for (int i = 0; i < enchantList.tagCount(); i++) {
                    NBTTagCompound enc = enchantList.getCompoundTagAt(i);
                    if (enc.hasKey("id")) {
                        if (enc.getShort("id") == (short) enchant.effectId) {
                            enc.setShort("lvl", lvl);
                            break;
                        }
                    }
                }
            } else {
                for (int i = 0; i < enchantList.tagCount(); i++) {
                    NBTTagCompound enc = enchantList.getCompoundTagAt(i);
                    if (enc.hasKey("id")) {
                        if (enc.getShort("id") == (short) enchant.effectId) {
                            enchantList.removeTag(i);
                            break;
                        }
                    }
                }
            }
        } else {
            if (lvl > 0) {
                enchantTag.setShort("id", (short) enchant.effectId);
                enchantTag.setShort("lvl", lvl);
                enchantList.appendTag(enchantTag);
            }
        }
        stack.stackTagCompound.setTag("ench", enchantList);

        return stack;
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
