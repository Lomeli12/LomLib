package net.lomeli.lomlib.util;

import java.util.regex.Pattern;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import net.minecraftforge.common.util.FakePlayer;

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

    /**
     * Check if entity is undead
     *
     * @param entity
     * @return
     */
    public static boolean isUndeadEntity(EntityLivingBase entity) {
        return entity.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEAD);
    }

    public static boolean isEntityMoving(Entity entity) {
        return (entity != null && (entity.motionX != 0 || entity.motionY != 0 || entity.motionZ != 0));
    }

    /**
     * Makes entity drop an itemStack.
     *
     * @param entity
     * @param itemStack
     * @param dropRate
     */
    public static void entityDropItem(EntityLivingBase entity, ItemStack itemStack, double dropRate) {
        double random = Math.random();
        if (random < dropRate)
            entity.entityDropItem(itemStack, 0.0F);
    }

    /**
     * Gets the source of the damage
     *
     * @param source
     * @return
     */
    public static Entity getSourceOfDamage(DamageSource source) {
        if (source != null)
            return source.isProjectile() ? source.getEntity() : source.getSourceOfDamage();
        return null;
    }

    /**
     * Checks if damage source was from player.
     *
     * @param source
     * @return true if player caused damage, else false
     */
    public static boolean damageFromPlayer(DamageSource source) {
        if (source != null && (source.getDamageType().equals("player") || source.getSourceOfDamage() instanceof EntityPlayer || source.getEntity() instanceof EntityPlayer))
            return true;
        return false;
    }

    public static void spawnItemOnEntity(World worldObj, Entity entity, ItemStack stack) {
        EntityItem entityItem = new EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, stack);
        float factor = 0.05F;
        entityItem.motionX = entity.worldObj.rand.nextGaussian() * factor;
        entityItem.motionY = entity.worldObj.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = entity.worldObj.rand.nextGaussian() * factor;
        worldObj.spawnEntityInWorld(entityItem);
    }

    public static boolean teleportRandomly(World world, EntityLivingBase telporter) {
        double d0 = telporter.posX + (world.rand.nextDouble() - 0.5D) * 64.0D;
        double d1 = telporter.posY + (double) (world.rand.nextInt(64) - 32);
        double d2 = telporter.posZ + (world.rand.nextDouble() - 0.5D) * 64.0D;
        return teleportTo(world, telporter, d0, d1, d2);
    }

    public static boolean teleportToEntity(World world, EntityLivingBase telporter, Entity p_70816_1_) {
        Vec3 vec3 = new Vec3(telporter.posX - p_70816_1_.posX, telporter.getEntityBoundingBox().minY + (double) (telporter.height / 2.0F) - p_70816_1_.posY + (double) p_70816_1_.getEyeHeight(), telporter.posZ - p_70816_1_.posZ);
        vec3 = vec3.normalize();
        double d0 = 16.0D;
        double d1 = telporter.posX + (world.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
        double d2 = telporter.posY + (double) (world.rand.nextInt(16) - 8) - vec3.yCoord * d0;
        double d3 = telporter.posZ + (world.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
        return teleportTo(world, telporter, d1, d2, d3);
    }

    public static boolean teleportTo(World worldObj, EntityLivingBase telporter, double p_70825_1_, double p_70825_3_, double p_70825_5_) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(telporter, p_70825_1_, p_70825_3_, p_70825_5_, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        double d3 = telporter.posX;
        double d4 = telporter.posY;
        double d5 = telporter.posZ;
        telporter.posX = event.targetX;
        telporter.posY = event.targetY;
        telporter.posZ = event.targetZ;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(telporter.posX, telporter.posY, telporter.posZ);

        if (worldObj.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                Block block = worldObj.getBlockState(blockpos1).getBlock();

                if (block.getMaterial().blocksMovement())
                    flag1 = true;
                else {
                    --telporter.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                telporter.setPositionAndUpdate(telporter.posX, telporter.posY, telporter.posZ);

                if (worldObj.getCollidingBoundingBoxes(telporter, telporter.getEntityBoundingBox()).isEmpty() && !worldObj.isAnyLiquid(telporter.getEntityBoundingBox()))
                    flag = true;
            }
        }

        if (!flag) {
            telporter.setPosition(d3, d4, d5);
            return false;
        } else {
            short short1 = 128;

            for (int i = 0; i < short1; ++i) {
                double d9 = (double) i / ((double) short1 - 1.0D);
                float f = (worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f1 = (worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float f2 = (worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double d6 = d3 + (telporter.posX - d3) * d9 + (worldObj.rand.nextDouble() - 0.5D) * (double) telporter.width * 2.0D;
                double d7 = d4 + (telporter.posY - d4) * d9 + worldObj.rand.nextDouble() * (double) telporter.height;
                double d8 = d5 + (telporter.posZ - d5) * d9 + (worldObj.rand.nextDouble() - 0.5D) * (double) telporter.width * 2.0D;
                worldObj.spawnParticle(EnumParticleTypes.PORTAL, d6, d7, d8, (double) f, (double) f1, (double) f2, new int[0]);
            }

            worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
            telporter.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    private static final Pattern FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$");

    /**
     * Checks player is a FakePlayer
     *
     * @param player
     * @return
     */
    public static boolean isFakePlayer(EntityPlayer player) {
        return player != null ? !(player instanceof EntityPlayerMP) || (player instanceof FakePlayer) || FAKE_PLAYER_PATTERN.matcher(player.getName()).matches() : false;
    }

    /**
     * Really poorly done ray
     *
     * @param player
     * @param world
     * @return
     */
    public static MovingObjectPosition rayTrace(EntityPlayer player, World world) {
        return rayTrace(player, world, true);
    }

    public static MovingObjectPosition rayTrace(EntityPlayer player, World world, boolean hitLiquids) {
        float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
        float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
        double d0 = player.prevPosX + (player.posX - player.prevPosX);
        double d1 = player.prevPosY + (player.posY - player.prevPosY) + (double) (world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ);
        Vec3 vec3 = new Vec3(d0, d1, d2);
        float f2 = (float) MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = (float) MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
        float f4 = (float) -MathHelper.cos(-f * 0.017453292F);
        float f5 = (float) MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP)
            d3 = ((net.minecraft.entity.player.EntityPlayerMP) player).theItemInWorldManager.getBlockReachDistance();
        Vec3 vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return world.rayTraceBlocks(vec3, vec31, hitLiquids, !hitLiquids, false);
    }
}
