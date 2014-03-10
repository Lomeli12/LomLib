package net.lomeli.lomlib.client;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

import net.lomeli.lomlib.libs.Strings;
import net.lomeli.lomlib.util.ReflectionUtil;
import net.lomeli.lomlib.util.ToolTipUtil;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;

public class CommandLomLib extends CommandBase {

    @Override
    public String getCommandName() {
        return Strings.MOD_ID.toLowerCase();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.getCommandName();
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender commandSender) {
        return commandSender.getEntityWorld().getWorldInfo().areCommandsAllowed();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, new String[]{"calmPigmen"});
            default:
                return null;
        }
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args) {
        if (args.length != 0) {
            String argument1 = args[0];

            @SuppressWarnings("unchecked")
            List<?> entityList = icommandsender.getEntityWorld().loadedEntityList;

            if (argument1.equalsIgnoreCase("calmPigmen")) {
                for (Object entity : entityList) {
                    if (entity instanceof EntityPigZombie) {
                        ((EntityPigZombie) entity).setTarget(null);
                        try {
                            ReflectionUtil.setFieldsAccess(EntityPigZombie.class.getName(), new String[]{"angerLevel",
                                    "field_70837_d"}, new boolean[]{true, true});
                            ReflectionHelper.setPrivateValue(Integer.class, EntityPigZombie.class.getDeclaredField("angerLevel")
                                    .getInt(entity), 0, new String[]{"angerLevel"});
                            ReflectionHelper.setPrivateValue(Integer.class,
                                    EntityPigZombie.class.getDeclaredField("field_70837_d").getInt(entity), 0,
                                    new String[]{"field_70837_d"});
                            if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE
                                        + "[LomLib]: Pig Zombies should now be calm");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (argument1.equalsIgnoreCase("clearAllEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityPlayer))
                            ((Entity) entity).setDead();
                    }
                }
                if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE
                            + "[LomLib]: All non-player entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityLivingBase))
                            ((Entity) entity).setDead();
                    }
                }
                if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE
                            + "[LomLib]: All non-living entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearLivingEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof EntityLivingBase)
                        ((EntityLivingBase) entity).setDead();
                }
                if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All Living mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("clearHostiles")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (entity instanceof IMob)
                            ((Entity) entity).setDead();
                    }
                }
                if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All Hostile mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("help") || argument1.equalsIgnoreCase("?")) {
                if (FMLCommonHandler.instance().getSide() != Side.CLIENT) {
                    sendCommanderMessage(icommandsender, "/lomlib calmPigmen " + ToolTipUtil.GREEN
                            + "-Calms down all pigmen in loaded chunks.");
                    sendCommanderMessage(icommandsender, "/lomlib clearAllEntities " + ToolTipUtil.GREEN
                            + "-Clears all non-player entities in loaded chunks.");
                    sendCommanderMessage(icommandsender, "/lomlib clearEntities " + ToolTipUtil.GREEN
                            + "-Clears all non-living entities in loaded chunks.");
                    sendCommanderMessage(icommandsender, "/lomlib clearLivingEntities " + ToolTipUtil.GREEN
                            + "-Clears all living entities in loaded chunks.");
                    sendCommanderMessage(icommandsender, "/lomlib clearHostiles  " + ToolTipUtil.GREEN
                            + "-Clears all hostile entities in loaded chunks.");
                }
            }
        } else {
            if (FMLCommonHandler.instance().getSide() != Side.CLIENT)
                sendCommanderMessage(icommandsender, ToolTipUtil.GREEN + "Type /lomlib help or /lomlib ? for more info");
        }
    }

    private void sendCommanderMessage(ICommandSender sender, String message) {
        if (sender != null) {
            sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName())
                    .addChatMessage(new ChatComponentText(message));
        } else {
            // sender.addChatMessage(new ChatComponentText(message));
        }
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }
}
