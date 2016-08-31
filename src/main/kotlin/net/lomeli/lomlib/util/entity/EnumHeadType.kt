package net.lomeli.lomlib.util.entity

/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Zoe Lee (Kihira)
 *
 * See LICENSE for full License
 */
import com.mojang.authlib.GameProfile
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTUtil

enum class EnumHeadType private constructor(val id: Int) {

    NONE(-1),
    SKELETON(0),
    WITHERSKELETON(1),
    ZOMBIE(2),
    PLAYER(3),
    CREEPER(4);


    companion object {

        fun getHead(headType: EnumHeadType, owner: GameProfile): ItemStack = getHead(headType.id, owner)

        fun getHead(id: Int, owner: GameProfile?): ItemStack {
            val itemStack = ItemStack(Items.SKULL, 1, id)
            if (owner != null) {
                val tag = NBTTagCompound()
                val gameProfileTag = NBTTagCompound()

                NBTUtil.writeGameProfile(gameProfileTag, owner)
                tag.setTag("SkullOwner", gameProfileTag)
                itemStack.tagCompound = tag
            }
            return itemStack
        }

        fun fromId(id: Int): EnumHeadType {
            when (id) {
                0 -> return EnumHeadType.SKELETON
                1 -> return EnumHeadType.WITHERSKELETON
                2 -> return EnumHeadType.ZOMBIE
                3 -> return EnumHeadType.PLAYER
                4 -> return EnumHeadType.CREEPER
                else -> return EnumHeadType.NONE
            }
        }
    }
}