package net.lomeli.lomlib.client.layer;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;

import net.lomeli.lomlib.client.event.RenderArmorEvent;

public class LayerCustomBipedArmor extends LayerBipedArmor {
    public LayerCustomBipedArmor(RenderPlayer rendererIn) {
        super(rendererIn);
    }

    @Override
    public void renderArmorLayer(EntityLivingBase entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale, EntityEquipmentSlot equipmentSlot) {
        ItemStack stack = getItemStackFromSlot(entityLivingBaseIn, equipmentSlot);
        if (MinecraftForge.EVENT_BUS.post(new RenderArmorEvent(stack, equipmentSlot)))
            return;
        super.renderArmorLayer(entityLivingBaseIn, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale, equipmentSlot);
    }
}
