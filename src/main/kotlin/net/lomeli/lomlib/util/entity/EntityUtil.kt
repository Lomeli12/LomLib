package net.lomeli.lomlib.util.entity

import net.lomeli.lomlib.util.MathUtil
import java.util.regex.Pattern

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.EnumCreatureAttribute
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.monster.EntityPigZombie
import net.minecraft.entity.monster.IMob
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.SoundEvents
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.util.math.RayTraceResult
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

import net.minecraftforge.common.util.FakePlayer

object EntityUtil {

    /**
     * Check if entity is hostile

     * @param entity
     * *
     * @return true if entity is instance of IMob
     */
    fun isHostileEntity(entity: EntityLivingBase): Boolean = entity is IMob

    /**
     * Check if entity is undead

     * @param entity
     * *
     * @return
     */
    fun isUndeadEntity(entity: EntityLivingBase): Boolean = entity.creatureAttribute == EnumCreatureAttribute.UNDEAD

    fun isEntityMoving(entity: Entity?): Boolean = entity != null && (entity.motionX != 0.0 || entity.motionY != 0.0 || entity.motionZ != 0.0)

    /**
     * Makes entity drop an itemStack.

     * @param entity
     * *
     * @param itemStack
     * *
     * @param dropRate
     */
    fun entityDropItem(entity: EntityLivingBase, itemStack: ItemStack, dropRate: Double) {
        val random = Math.random()
        if (random < dropRate)
            entity.entityDropItem(itemStack, 0.0f)
    }

    /**
     * Gets the source of the damage

     * @param source
     * *
     * @return
     */
    fun getSourceOfDamage(source: DamageSource?): Entity? {
        if (source != null)
            return if (source.isProjectile) source.entity else source.sourceOfDamage
        return null
    }

    /**
     * Checks if damage source was from player.

     * @param source
     * *
     * @return true if player caused damage, else false
     */
    fun damageFromPlayer(source: DamageSource?): Boolean {
        if (source != null && (source.getDamageType() == "player" || source.sourceOfDamage is EntityPlayer || source.entity is EntityPlayer))
            return true
        return false
    }

    fun spawnItemOnEntity(worldObj: World, entity: Entity, stack: ItemStack) {
        val entityItem = EntityItem(worldObj, entity.posX, entity.posY, entity.posZ, stack)
        val factor = 0.05f
        entityItem.motionX = entity.worldObj.rand.nextGaussian() * factor
        entityItem.motionY = entity.worldObj.rand.nextGaussian() * factor + 0.2f
        entityItem.motionZ = entity.worldObj.rand.nextGaussian() * factor
        worldObj.spawnEntityInWorld(entityItem)
    }

    fun teleportRandomly(world: World, telporter: EntityLivingBase): Boolean {
        val d0 = telporter.posX + (world.rand.nextDouble() - 0.5) * 64.0
        val d1 = telporter.posY + (world.rand.nextInt(64) - 32).toDouble()
        val d2 = telporter.posZ + (world.rand.nextDouble() - 0.5) * 64.0
        return teleportTo(world, telporter, d0, d1, d2)
    }

    fun teleportToEntity(world: World, telporter: EntityLivingBase, target: Entity): Boolean {
        var vec3d = Vec3d(telporter.posX - target.posX, telporter.entityBoundingBox.minY + (telporter.height / 2.0f).toDouble() - target.posY + target.eyeHeight.toDouble(), telporter.posZ - target.posZ)
        vec3d = vec3d.normalize()
        val d0 = 16.0
        val d1 = telporter.posX + (world.rand.nextDouble() - 0.5) * 8.0 - vec3d.xCoord * d0
        val d2 = telporter.posY + (world.rand.nextInt(16) - 8).toDouble() - vec3d.yCoord * d0
        val d3 = telporter.posZ + (world.rand.nextDouble() - 0.5) * 8.0 - vec3d.zCoord * d0
        return teleportTo(world, telporter, d1, d2, d3)
    }

    fun teleportTo(world: World, telporter: EntityLivingBase, x: Double, y: Double, z: Double): Boolean {
        val event = net.minecraftforge.event.entity.living.EnderTeleportEvent(telporter, x, y, z, 0f)
        if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false
        val flag = telporter.attemptTeleport(event.targetX, event.targetY, event.targetZ)

        if (flag) {
            world.playSound(null as EntityPlayer, telporter.prevPosX, telporter.prevPosY, telporter.prevPosZ, SoundEvents.ENTITY_ENDERMEN_TELEPORT, telporter.soundCategory, 1.0f, 1.0f)
            telporter.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1.0f, 1.0f)
        }

        return flag
    }

    private val FAKE_PLAYER_PATTERN = Pattern.compile("^(?:\\[.*\\])|(?:ComputerCraft)$")

    /**
     * Checks player is a FakePlayer

     * @param player
     * *
     * @return
     */
    fun isFakePlayer(player: EntityPlayer?): Boolean = if (player != null) player !is EntityPlayerMP || player is FakePlayer || FAKE_PLAYER_PATTERN.matcher(player.name).matches() else false
    /**
     * Really poorly done ray

     * @param player
     * *
     * @param world
     * *
     * @return
     */
    fun rayTrace(player: EntityPlayer, world: World, hitLiquids: Boolean = true): RayTraceResult? {
        val f = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch)
        val f1 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw)
        val d0 = player.prevPosX + (player.posX - player.prevPosX)
        val d1 = player.prevPosY + (player.posY - player.prevPosY) + (if (world.isRemote) player.getEyeHeight() - player.defaultEyeHeight else player.getEyeHeight()).toDouble()
        val d2 = player.prevPosZ + (player.posZ - player.prevPosZ)
        val vec3 = Vec3d(d0, d1, d2)
        val f2 = MathUtil.cos((-f1 * 0.017453292f - Math.PI.toFloat()).toDouble()).toFloat()
        val f3 = MathUtil.sin((-f1 * 0.017453292f - Math.PI.toFloat()).toDouble()).toFloat()
        val f4 = (-MathUtil.cos((-f * 0.017453292f).toDouble())).toFloat()
        val f5 = MathUtil.sin((-f * 0.017453292f).toDouble()).toFloat()
        val f6 = f3 * f4
        val f7 = f2 * f4
        var d3 = 5.0
        if (player is net.minecraft.entity.player.EntityPlayerMP)
            d3 = player.interactionManager.blockReachDistance
        val vec31 = vec3.addVector(f6.toDouble() * d3, f5.toDouble() * d3, f7.toDouble() * d3)
        return world.rayTraceBlocks(vec3, vec31, hitLiquids, !hitLiquids, false)
    }

    fun setAttackTarget(entity: EntityLiving, target: EntityLivingBase) {
        entity.attackTarget = target
    }

    fun setPigmenAnger(pig: EntityPigZombie, target: EntityLivingBase?, angerLevel: Int) {
        pig.setAttackTarget(target)
        pig.angerLevel = angerLevel
    }
}
