package net.lomeli.lomlib.entity;

import java.util.List;
import java.util.Random;

import net.lomeli.lomlib.block.BlockUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

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

    public static boolean isUndeadEntity(EntityLivingBase entity) {
        if(isHostileEntity(entity))
            return entity.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEAD);
        return false;
    }

    public static boolean isEntityMoving(Entity entity) {
        if(entity != null && (entity.motionX != 0 || entity.motionY != 0 || entity.motionZ != 0))
            return true;
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
    public static void entityDropItem(EntityLivingBase entity, ItemStack itemStack, double dropRate, boolean hostile) {
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

    public static void spawnItemOnPlayer(World worldObj, EntityPlayer player, ItemStack stack) {
        Random rand = new Random();
        EntityItem entityItem = new EntityItem(worldObj, player.posX, player.posY, player.posZ, stack);
        float factor = 0.05F;
        entityItem.motionX = rand.nextGaussian() * factor;
        entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = rand.nextGaussian() * factor;
        worldObj.spawnEntityInWorld(entityItem);
    }

    @SuppressWarnings("rawtypes")
    public static boolean transformEntityItem(World world, int x, int y, int z, EntityPlayer player, int initId,
            ItemStack transformation, ItemStack requiredItem, boolean effect) {
        List entityList = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(15D, 15D, 15D));
        for(int i = 0; i < entityList.size(); i++) {
            Entity ent = (Entity) entityList.get(i);
            if(ent != null && ent instanceof EntityItem) {
                EntityItem item = (EntityItem) ent;
                if(BlockUtil.isAboveBlock(item, x, y, z)) {
                    if(item != null && item.getEntityItem().itemID == initId) {
                        if(player.getCurrentEquippedItem() != null
                                && player.getCurrentEquippedItem().itemID == requiredItem.itemID) {
                            ItemStack manual = transformation;
                            manual.stackSize = item.getEntityItem().stackSize;
                            if(manual != null) {
                                if(!world.isRemote) {
                                    item.setEntityItemStack(manual);
                                    player.getCurrentEquippedItem().damageItem(manual.stackSize, player);
                                }
                                if(effect) {
                                    for(int k = 0; k < 2; ++k) {
                                        world.spawnParticle("largesmoke", item.posX + (world.rand.nextDouble() - 0.5D)
                                                * (double) item.width, (item.posY + 0.5D) + world.rand.nextDouble()
                                                * (double) item.height, item.posZ + (world.rand.nextDouble() - 0.5D)
                                                * (double) item.width, 0.0D, 0.0D, 0.0D);
                                    }
                                }
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
