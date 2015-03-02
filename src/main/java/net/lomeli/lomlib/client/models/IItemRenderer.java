package net.lomeli.lomlib.client.models;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

public interface IItemRenderer {
    public void renderItem(RenderType type, EntityLivingBase entity, ItemStack stack, float renderTick);
}
