package net.lomeli.lomlib.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;

public class EntityUtil {
    /**
     * Check if entity is hostile
     * 
     * @param entity
     * @return true if entity is instance of IMob
     */
    public static boolean isHostileEntity(EntityLivingBase entity) {
        return (entity instanceof IMob);
    }
    
    public static boolean isUndeadEntity(EntityLivingBase entity){
        if (isHostileEntity(entity))
            return entity.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEAD);
        return false;
    }

    /**
     * Makes entity drop an itemStack.
     * 
     * @param entity
     * @param itemStack
     * @param dropRate
     * @param hostile
     */
    public static void entityDropItem(EntityLivingBase entity,
            ItemStack itemStack, double dropRate, boolean hostile) {
        if(hostile) {
            if(isHostileEntity(entity)) {
                double random = Math.random();

                if(random < dropRate)
                    entity.entityDropItem(itemStack, 0.0F);
            }
        }else {
            double random = Math.random();
            if(random < dropRate)
                entity.entityDropItem(itemStack, 0.0F);
        }
    }

    /**
     * Checks if damage source was from player.
     * 
     * @param source
     * @return true if player caused damage, else false
     */
    public static boolean wasEntityKilledByPlayer(DamageSource source) {
        if(source.getDamageType().equals("player"))
            return true;
        if(source.getSourceOfDamage() instanceof EntityArrow) {
            if(((EntityArrow) source.getSourceOfDamage()).shootingEntity != null) {
                if(((EntityArrow) source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer)
                    return true;
            }
        }
        return false;
    }
}
