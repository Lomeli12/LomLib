package net.lomeli.lomlib.client.event;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHooks {
    public static boolean renderHooks(EntityLivingBase entityLivingBase, ItemStack stack, EntityEquipmentSlot slot) {
        return MinecraftForge.EVENT_BUS.post(new RenderArmorEvent(entityLivingBase, stack, slot));
    }
}
