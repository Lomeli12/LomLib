package net.lomeli.lomlib.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.MinecraftForge;

public class EventHooks {

    public static void onFoodEaten(EntityPlayer player, ItemStack stack, int foodLevel, float foodSaturation) {
        FoodEatenEvent event = new FoodEatenEvent(player, stack, foodLevel, foodSaturation);
        MinecraftForge.EVENT_BUS.post(event);
    }

    public static void onCakeEaten(EntityPlayer player, int foodLevel, float foodSaturation) {
        onFoodEaten(player, new ItemStack(Items.CAKE), foodLevel, foodSaturation);
    }

    public static void onFoodEaten(EntityPlayer player, ItemFood itemFood, ItemStack stack) {
        onFoodEaten(player, stack, itemFood.getHealAmount(stack), itemFood.getSaturationModifier(stack));
    }
}
