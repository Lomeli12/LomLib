package net.lomeli.lomlib.entity;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import net.lomeli.lomlib.block.BlockUtil;

public class EntityUtil {

    public static void sendToChat(EntityPlayer player, String message) {
        player.addChatComponentMessage(new ChatComponentText(message));
    }

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
        return isHostileEntity(entity) ? entity.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEAD) : false;
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
     * @param hostile
     */
    public static void entityDropItem(EntityLivingBase entity, ItemStack itemStack, double dropRate, boolean hostile) {
        if (hostile) {
            if (isHostileEntity(entity)) {
                double random = Math.random();

                if (random < dropRate)
                    entity.entityDropItem(itemStack, 0.0F);
            }
        }else {
            double random = Math.random();
            if (random < dropRate)
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
        if (source.getDamageType().equals("player"))
            return true;
        if (source.getSourceOfDamage() instanceof EntityArrow) {
            if (((EntityArrow) source.getSourceOfDamage()).shootingEntity != null) {
                if (((EntityArrow) source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer)
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
    public static boolean transformEntityItem(World world, int x, int y, int z, EntityPlayer player, ItemStack init, ItemStack transformation, ItemStack requiredItem, boolean effect) {
        List entityList = world.getEntitiesWithinAABB(EntityItem.class, player.boundingBox.expand(15D, 15D, 15D));
        for (int i = 0; i < entityList.size(); i++) {
            Entity ent = (Entity) entityList.get(i);
            if (ent != null && ent instanceof EntityItem) {
                EntityItem item = (EntityItem) ent;
                if (BlockUtil.isAboveBlock(item, x, y, z)) {
                    if (item != null && item.getEntityItem().getUnlocalizedName().equals(init.getUnlocalizedName())) {
                        if (player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getUnlocalizedName().equals(requiredItem.getUnlocalizedName())
                                && player.getCurrentEquippedItem().getItemDamage() == requiredItem.getItemDamage()) {
                            ItemStack manual = transformation;
                            manual.stackSize = item.getEntityItem().stackSize;
                            if (manual != null) {
                                if (!world.isRemote) {
                                    item.setEntityItemStack(manual);
                                    player.getCurrentEquippedItem().damageItem(manual.stackSize, player);
                                }
                                if (effect) {
                                    for (int k = 0; k < 2; ++k) {
                                        world.spawnParticle("largesmoke", item.posX + (world.rand.nextDouble() - 0.5D) * item.width, (item.posY + 0.5D) + world.rand.nextDouble() * item.height, item.posZ
                                                + (world.rand.nextDouble() - 0.5D) * item.width, 0.0D, 0.0D, 0.0D);
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

    public static boolean teleportRandomly(EntityLivingBase entity) {
        double var1 = entity.posX + (entity.worldObj.rand.nextDouble() - 0.5D) * 64.0D;
        double var3 = entity.posY + (entity.worldObj.rand.nextInt(64) - 32);
        double var5 = entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5D) * 64.0D;
        return teleportTo(entity, var1, var3, var5);
    }

    public static boolean teleportToEntity(EntityLivingBase entity, Entity par1Entity) {
        Vec3 var2 = entity.worldObj.getWorldVec3Pool().getVecFromPool(entity.posX - par1Entity.posX, entity.boundingBox.minY + entity.height / 2.0F - par1Entity.posY + par1Entity.getEyeHeight(),
                entity.posZ - par1Entity.posZ);
        var2 = var2.normalize();
        double var3 = 16.0D;
        double var5 = entity.posX + (entity.worldObj.rand.nextDouble() - 0.5D) * 8.0D - var2.xCoord * var3;
        double var7 = entity.posY + (entity.worldObj.rand.nextInt(16) - 8) - var2.yCoord * var3;
        double var9 = entity.posZ + (entity.worldObj.rand.nextDouble() - 0.5D) * 8.0D - var2.zCoord * var3;
        return teleportTo(entity, var5, var7, var9);
    }

    public static boolean teleportTo(EntityLivingBase entity, double par1, double par3, double par5) {
        double var7 = entity.posX;
        double var9 = entity.posY;
        double var11 = entity.posZ;
        entity.posX = par1;
        entity.posY = par3;
        entity.posZ = par5;
        boolean var13 = false;
        int var14 = MathHelper.floor_double(entity.posX);
        int var15 = MathHelper.floor_double(entity.posY);
        int var16 = MathHelper.floor_double(entity.posZ);
        Block var18;

        if (entity.worldObj.blockExists(var14, var15, var16)) {
            boolean var17 = false;

            while (!var17 && var15 > 0) {
                var18 = entity.worldObj.getBlock(var14, var15 - 1, var16);

                if (var18 != null && var18.getMaterial().blocksMovement())
                    var17 = true;
                else {
                    --entity.posY;
                    --var15;
                }
            }

            if (var17) {
                entity.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);

                if (entity.worldObj.getCollidingBoundingBoxes(entity, entity.boundingBox).isEmpty() && !entity.worldObj.isAnyLiquid(entity.boundingBox))
                    var13 = true;
            }
        }

        if (!var13) {
            entity.setPositionAndUpdate(var7, var9, var11);
            return false;
        }else {
            short var30 = 128;

            for (int j = 0; j < var30; ++j) {
                double var19 = j / (var30 - 1.0D);
                float var21 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float var22 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                float var23 = (entity.worldObj.rand.nextFloat() - 0.5F) * 0.2F;
                double var24 = var7 + (entity.posX - var7) * var19 + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width * 2.0D;
                double var26 = var9 + (entity.posY - var9) * var19 + entity.worldObj.rand.nextDouble() * entity.height;
                double var28 = var11 + (entity.posZ - var11) * var19 + (entity.worldObj.rand.nextDouble() - 0.5D) * entity.width * 2.0D;
                entity.worldObj.spawnParticle("portal", var24, var26, var28, var21, var22, var23);
            }

            entity.worldObj.playSoundEffect(var7, var9, var11, "mob.endermen.portal", 1.0F, 1.0F);
            entity.playSound("mob.endermen.portal", 1.0F, 1.0F);
            return true;
        }
    }

    public static boolean isFakePlayer(EntityPlayer player) {
        if (player.getGameProfile() == null || player.getGameProfile().getName() == null || ("[Minecraft]".equals(player.getGameProfile().getName()))
                || ("[ExtraUtilities:FakePlayer]".equals(player.getGameProfile().getName())))
            return true;
        if (player instanceof FakePlayer)
            return true;
        if (player instanceof EntityPlayerMP) {
            EntityPlayerMP mp = (EntityPlayerMP) player;
            if (mp.playerNetServerHandler == null)
                return true;
            try {
                mp.getPlayerIP();
                mp.playerNetServerHandler.netManager.getSocketAddress().toString();
            }catch (Exception e) {
                return true;
            }
            return !MinecraftServer.getServer().getConfigurationManager().playerEntityList.contains(player);
        }
        return false;
    }
}
