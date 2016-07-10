package net.lomeli.lomlib.util;

import java.util.regex.Pattern;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
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

    public static boolean teleportToEntity(World world, EntityLivingBase telporter, Entity target) {
        Vec3d vec3d = new Vec3d(telporter.posX - target.posX, telporter.getEntityBoundingBox().minY + (double) (telporter.height / 2.0F) - target.posY + (double) target.getEyeHeight(), telporter.posZ - target.posZ);
        vec3d = vec3d.normalize();
        double d0 = 16.0D;
        double d1 = telporter.posX + (world.rand.nextDouble() - 0.5D) * 8.0D - vec3d.xCoord * d0;
        double d2 = telporter.posY + (double) (world.rand.nextInt(16) - 8) - vec3d.yCoord * d0;
        double d3 = telporter.posZ + (world.rand.nextDouble() - 0.5D) * 8.0D - vec3d.zCoord * d0;
        return teleportTo(world, telporter, d1, d2, d3);
    }

    public static boolean teleportTo(World world, EntityLivingBase telporter, double x, double y, double z) {
        net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(telporter, x, y, z, 0);
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
        boolean flag = telporter.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ());

        if (flag) {
            world.playSound((EntityPlayer) null, telporter.prevPosX, telporter.prevPosY, telporter.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, telporter.getSoundCategory(), 1.0F, 1.0F);
            telporter.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0F, 1.0F);
        }

        return flag;
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
    public static RayTraceResult rayTrace(EntityPlayer player, World world) {
        return rayTrace(player, world, true);
    }

    public static RayTraceResult rayTrace(EntityPlayer player, World world, boolean hitLiquids) {
        float f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch);
        float f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw);
        double d0 = player.prevPosX + (player.posX - player.prevPosX);
        double d1 = player.prevPosY + (player.posY - player.prevPosY) + (double) (world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight());
        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ);
        Vec3d vec3 = new Vec3d(d0, d1, d2);
        float f2 = (float) MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
        float f3 = (float) MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
        float f4 = (float) -MathHelper.cos(-f * 0.017453292F);
        float f5 = (float) MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = 5.0D;
        if (player instanceof net.minecraft.entity.player.EntityPlayerMP)
            d3 = ((EntityPlayerMP) player).interactionManager.getBlockReachDistance();
        Vec3d vec31 = vec3.addVector((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
        return world.rayTraceBlocks(vec3, vec31, hitLiquids, !hitLiquids, false);
    }

    public static void setAttackTarget(EntityLiving entity, EntityLivingBase target) {
        entity.attackTarget = target;
    }

    public static void setPigmenAnger(EntityPigZombie pig, EntityLivingBase target, int angerLevel) {
        pig.setAttackTarget(target);
        pig.angerLevel = angerLevel;
    }
}
