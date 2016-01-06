package net.lomeli.lomlib.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import net.minecraftforge.oredict.OreDictionary;

/**
 * NBTHelper Using the NBT helper from pahimar's Equivalent Exchange 3 mod.
 *
 * @author pahimar
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public class NBTUtil {

    /**
     * Initializes the NBT Tag Compound for the given ItemStack if it is null
     *
     * @param itemStack The ItemStack for which its NBT Tag Compound is being checked
     *                  for initialization
     */
    public static void initNBTTagCompound(ItemStack itemStack) {
        if (itemStack.getTagCompound() == null)
            itemStack.setTagCompound(new NBTTagCompound());
    }

    public static boolean hasTag(ItemStack itemStack, String keyName) {
        if (itemStack.getTagCompound() != null)
            return itemStack.getTagCompound().hasKey(keyName);

        return false;
    }

    public static boolean hasTag(ItemStack itemStack, String keyName, int type) {
        if (itemStack.getTagCompound() != null)
            return itemStack.getTagCompound().hasKey(keyName, type);

        return false;
    }

    public static void removeTag(ItemStack itemStack, String keyName) {
        if (itemStack.getTagCompound() != null)
            itemStack.getTagCompound().removeTag(keyName);
    }

    // String
    public static String getString(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setString(itemStack, keyName, "");

        return itemStack.getTagCompound().getString(keyName);
    }

    public static void setString(ItemStack itemStack, String keyName, String keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setString(keyName, keyValue);
    }

    // boolean
    public static boolean getBoolean(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setBoolean(itemStack, keyName, false);

        return itemStack.getTagCompound().getBoolean(keyName);
    }

    public static void setBoolean(ItemStack itemStack, String keyName, boolean keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setBoolean(keyName, keyValue);
    }

    // byte
    public static byte getByte(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setByte(itemStack, keyName, (byte) 0);

        return itemStack.getTagCompound().getByte(keyName);
    }

    public static void setByte(ItemStack itemStack, String keyName, byte keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setByte(keyName, keyValue);
    }

    // short
    public static short getShort(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setShort(itemStack, keyName, (short) 0);

        return itemStack.getTagCompound().getShort(keyName);
    }

    public static void setShort(ItemStack itemStack, String keyName, short keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setShort(keyName, keyValue);
    }

    // int
    public static int getInt(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setInteger(itemStack, keyName, 0);

        return itemStack.getTagCompound().getInteger(keyName);
    }

    public static void setInteger(ItemStack itemStack, String keyName, int keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setInteger(keyName, keyValue);
    }

    // long
    public static long getLong(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setLong(itemStack, keyName, 0);

        return itemStack.getTagCompound().getLong(keyName);
    }

    public static void setLong(ItemStack itemStack, String keyName, long keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setLong(keyName, keyValue);
    }

    // float
    public static float getFloat(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setFloat(itemStack, keyName, 0);

        return itemStack.getTagCompound().getFloat(keyName);
    }

    public static void setFloat(ItemStack itemStack, String keyName, float keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setFloat(keyName, keyValue);
    }

    // double
    public static double getDouble(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);

        if (!itemStack.getTagCompound().hasKey(keyName))
            setDouble(itemStack, keyName, 0);

        return itemStack.getTagCompound().getDouble(keyName);
    }

    public static void setDouble(ItemStack itemStack, String keyName, double keyValue) {
        initNBTTagCompound(itemStack);

        itemStack.getTagCompound().setDouble(keyName, keyValue);
    }

    public static NBTTagCompound getCompound(ItemStack itemStack, String keyName) {
        initNBTTagCompound(itemStack);
        if (!itemStack.getTagCompound().hasKey(keyName, 10))
            setCompound(itemStack, keyName, new NBTTagCompound());
        return itemStack.getTagCompound().getCompoundTag(keyName);
    }

    public static void setCompound(ItemStack itemStack, String keyName, NBTTagCompound keyValue) {
        initNBTTagCompound(itemStack);
        itemStack.getTagCompound().setTag(keyName, keyValue);
    }

    public static boolean areItemStacksEqualNoNBT(ItemStack stackA, ItemStack stackB) {
        if (stackB == null)
            return false;

        return ItemUtil.areItemsTheSame(stackA, stackB)
                && (stackA.getItemDamage() == OreDictionary.WILDCARD_VALUE ? true : stackB.getItemDamage() == OreDictionary.WILDCARD_VALUE ? true : stackA.getHasSubtypes() == false ? true : stackB
                .getItemDamage() == stackA.getItemDamage());
    }

    public static boolean doNBTsMatch(NBTTagCompound nbtA, NBTTagCompound nbtB) {
        return nbtA == null ? nbtB == null ? true : false : nbtB == null ? false : nbtA.equals(nbtB);
    }

    public static NBTTagCompound getPersistedTag(EntityPlayer player) {
        if (player == null) return null;
        return player.getEntityData().hasKey(EntityPlayer.PERSISTED_NBT_TAG, 10) ? player.getEntityData().getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG) : new NBTTagCompound();
    }

    public static void setPersistedTag(EntityPlayer player, NBTTagCompound tag) {
        if (player == null) return;
        player.getEntityData().setTag(EntityPlayer.PERSISTED_NBT_TAG, tag);
    }
}