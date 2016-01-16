package net.lomeli.lomlib.client.layer;

import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;

import net.lomeli.lomlib.client.event.RenderArmorEvent;

public class LayerCustomBipedArmor extends LayerBipedArmor {
    public LayerCustomBipedArmor(RenderPlayer rendererIn) {
        super(rendererIn);
    }

    @Override
    public void renderLayer(EntityLivingBase entitylivingbaseIn, float p_177182_2_, float p_177182_3_, float p_177182_4_, float p_177182_5_, float p_177182_6_, float p_177182_7_, float p_177182_8_, int armorSlot) {
        ItemStack stack = this.getCurrentArmor(entitylivingbaseIn, armorSlot);
        if (MinecraftForge.EVENT_BUS.post(new RenderArmorEvent(stack, armorSlot)))
            return;
        super.renderLayer(entitylivingbaseIn, p_177182_2_, p_177182_3_, p_177182_4_, p_177182_5_, p_177182_6_, p_177182_7_, p_177182_8_, armorSlot);
    }
}
