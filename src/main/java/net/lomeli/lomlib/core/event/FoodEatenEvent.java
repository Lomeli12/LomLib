package net.lomeli.lomlib.core.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import net.minecraftforge.event.entity.player.PlayerEvent;

public class FoodEatenEvent extends PlayerEvent {
    private final ItemStack foodStack;
    private int foodLevel;
    private float foodSaturationLevel;

    public FoodEatenEvent(EntityPlayer player, ItemStack foodStack, int foodLevel, float foodSaturationLevel) {
        super(player);
        this.foodStack = foodStack;
        this.foodLevel = foodLevel;
        this.foodSaturationLevel = foodSaturationLevel;
    }

    public float getFoodSaturationLevel() {
        return foodSaturationLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public ItemStack getFoodStack() {
        return foodStack;
    }
}
