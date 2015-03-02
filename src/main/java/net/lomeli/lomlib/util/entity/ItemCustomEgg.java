package net.lomeli.lomlib.util.entity;

import java.util.List;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.*;
import net.minecraft.world.World;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCustomEgg extends Item {

    public static Item customEgg;

    public static void initCustomEggs() {
        customEgg = new ItemCustomEgg();
        GameRegistry.registerItem(customEgg, "spawnEgg");
    }

    public ItemCustomEgg() {
        super();
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabs.tabMisc);
        this.setUnlocalizedName("monsterPlacer");
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote)
            return true;
        else if (!playerIn.canPlayerEdit(pos.offset(side), side, stack))
            return false;
        else {
            if (stack.getItemDamage() >= EntityUtil.eggList.size())
                return false;
            SimpleEggInfo info = EntityUtil.eggList.get(stack.getItemDamage());
            if (info != null && info.entityClass != null) {
                IBlockState iblockstate = worldIn.getBlockState(pos);

                if (iblockstate.getBlock() == Blocks.mob_spawner) {
                    TileEntity tileentity = worldIn.getTileEntity(pos);

                    if (tileentity instanceof TileEntityMobSpawner) {
                        MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner) tileentity).getSpawnerBaseLogic();
                        mobspawnerbaselogic.setEntityName((String) EntityList.classToStringMapping.get(info.entityClass));
                        tileentity.markDirty();
                        worldIn.markBlockForUpdate(pos);

                        if (!playerIn.capabilities.isCreativeMode)
                            --stack.stackSize;

                        return true;
                    }
                }
                pos = pos.offset(side);
                double d0 = 0.0D;

                if (side == EnumFacing.UP && iblockstate instanceof BlockFence)
                    d0 = 0.5D;

                Entity entity = spawnCreature(worldIn, info, (double) pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D);

                if (entity != null) {
                    if (entity instanceof EntityLivingBase && stack.hasDisplayName())
                        entity.setCustomNameTag(stack.getDisplayName());

                    if (!playerIn.capabilities.isCreativeMode)
                        --stack.stackSize;
                }

                return true;
            }
            return false;
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (worldIn.isRemote)
            return itemStackIn;
        else {
            if (itemStackIn.getItemDamage() >= EntityUtil.eggList.size())
                return itemStackIn;
            SimpleEggInfo info = EntityUtil.eggList.get(itemStackIn.getItemDamage());
            MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

            if (movingobjectposition == null)
                return itemStackIn;
            else if (info == null || info.entityClass == null)
                return itemStackIn;
            else {
                if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
                    BlockPos blockpos = movingobjectposition.getBlockPos();

                    if (!worldIn.isBlockModifiable(playerIn, blockpos))
                        return itemStackIn;

                    if (!playerIn.canPlayerEdit(blockpos, movingobjectposition.sideHit, itemStackIn))
                        return itemStackIn;

                    if (worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid) {
                        Entity entity = spawnCreature(worldIn, info, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.5D, (double) blockpos.getZ() + 0.5D);

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
                                ((EntityLiving) entity).setCustomNameTag(itemStackIn.getDisplayName());

                            if (!playerIn.capabilities.isCreativeMode)
                                --itemStackIn.stackSize;
                            playerIn.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
                        }
                    }
                }
                return itemStackIn;
            }
        }
    }

    @Override
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
        if (EntityUtil.eggList.isEmpty())
            return;
        for (int i = 0; i < EntityUtil.eggList.size(); i++)
            subItems.add(new ItemStack(itemIn, 1, i));
    }

    @Override
    public int getColorFromItemStack(ItemStack stack, int renderPass) {
        SimpleEggInfo info = stack.getItemDamage() < EntityUtil.eggList.size() ? EntityUtil.eggList.get(stack.getItemDamage()) : null;
        return info != null ? (renderPass == 0 ? info.primaryColor : info.secondaryColor) : 16777215;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String s = ("" + StatCollector.translateToLocal(this.getUnlocalizedName() + ".name")).trim();
        if (stack.getItemDamage() < EntityUtil.eggList.size()) {
            SimpleEggInfo info = EntityUtil.eggList.get(stack.getItemDamage());
            if (info != null && info.unlocalizedName != null)
                s = s + " " + StatCollector.translateToLocal("entity." + info.unlocalizedName + ".name");
        }
        return s;
    }

    public static Entity spawnCreature(World worldIn, SimpleEggInfo eggInfo, double x, double y, double z) {
        Entity entity = null;
        if (eggInfo != null) {
            for (int j = 0; j < 1; ++j) {
                entity = eggInfo.createEntity(worldIn);

                if (entity instanceof EntityLivingBase) {
                    EntityLiving entityliving = (EntityLiving) entity;
                    entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
                    entityliving.rotationYawHead = entityliving.rotationYaw;
                    entityliving.renderYawOffset = entityliving.rotationYaw;
                    entityliving.func_180482_a(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
                    worldIn.spawnEntityInWorld(entity);
                    entityliving.playLivingSound();
                }
            }
        }

        return entity;
    }
}
