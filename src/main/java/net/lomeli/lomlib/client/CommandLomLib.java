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

import cpw.mods.fml.relauncher.ReflectionHelper;

public class CommandLomLib extends CommandBase {

    @Override
    public String getCommandName() {
        return Strings.MOD_ID.toLowerCase();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.getCommandName() + " help";
    }

    @Override
    @SuppressWarnings("rawtypes")
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args) {
        switch(args.length) {
        case 1 :
            return getListOfStringsMatchingLastWord(args, new String[] { "calmPigmen" });
        default:
            return null;
        }
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args) {
        if (args.length != 0) {
            String argument1 = args[0];

            List<?> entityList = icommandsender.getEntityWorld().loadedEntityList;

            if (argument1.equalsIgnoreCase("calmPigmen")) {
                boolean successful = false;
                for (Object entity : entityList) {
                    if (entity instanceof EntityPigZombie) {
                        ((EntityPigZombie) entity).setTarget(null);
                        try {
                            if (!ReflectionUtil.isFieldAccessible(EntityPigZombie.class, "angerLevel"))
                                ReflectionUtil.setFieldAccess(EntityPigZombie.class, "angerLevel", true);
                            ReflectionHelper.setPrivateValue(EntityPigZombie.class, ((EntityPigZombie) entity), 0, "angerLevel");
                            if (!successful)
                                successful = true;
                        }catch (Exception e) {
                            try {
                                if (!ReflectionUtil.isFieldAccessible(EntityPigZombie.class, "field_70837_d"))
                                    ReflectionUtil.setFieldAccess(EntityPigZombie.class, "field_70837_d", true);
                                ReflectionHelper.setPrivateValue(EntityPigZombie.class, ((EntityPigZombie) entity), 0, "field_70837_d");
                                if (!successful)
                                    successful = true;
                            }catch (Exception e1) {
                                try {
                                    if (!ReflectionUtil.isFieldAccessible(EntityPigZombie.class, "bs"))
                                        ReflectionUtil.setFieldAccess(EntityPigZombie.class, "bs", true);
                                    ReflectionHelper.setPrivateValue(EntityPigZombie.class, ((EntityPigZombie) entity), 0, "bs");
                                    if (!successful)
                                        successful = true;
                                }catch (Exception e2) {
                                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: Failed to calm Pig Zombies!");
                                    e2.printStackTrace();
                                }
                            }
                        }
                    }
                }
                if (successful)
                    sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: Pig Zombies should now be calm");
            }else if (argument1.equalsIgnoreCase("clearAllEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityPlayer))
                            ((Entity) entity).setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded non-player entities have been removed.");
            }else if (argument1.equalsIgnoreCase("clearEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityLivingBase))
                            ((Entity) entity).setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded non-living entities have been removed.");
            }else if (argument1.equalsIgnoreCase("clearLivingEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof EntityLivingBase)
                        ((EntityLivingBase) entity).setDead();
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded Living mobs have been removed.");
            }else if (argument1.equalsIgnoreCase("clearHostiles")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (entity instanceof IMob)
                            ((Entity) entity).setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded Hostile mobs have been removed.");
            }else if (argument1.equalsIgnoreCase("help") || argument1.equalsIgnoreCase("?")) {
                sendCommanderMessage(icommandsender, "Format: `" + ToolTipUtil.ORANGE + "lomlib <command>" + ToolTipUtil.WHITE + "'");
                sendCommanderMessage(icommandsender, "Available commands: ");
                sendCommanderMessage(icommandsender, "- calmPigmen : " + ToolTipUtil.GREEN + "Calms down all loaded pigmen.");
                sendCommanderMessage(icommandsender, "- clearAllEntities : " + ToolTipUtil.GREEN + "Clears all loaded non-player entities.");
                sendCommanderMessage(icommandsender, "- clearEntities : " + ToolTipUtil.GREEN + "Clears all loaded non-living entities.");
                sendCommanderMessage(icommandsender, "- clearLivingEntities : " + ToolTipUtil.GREEN + "Clears all loaded living entities.");
                sendCommanderMessage(icommandsender, "- clearHostiles : " + ToolTipUtil.GREEN + "Clears all loaded hostile entities.");
            }
        }else
            sendCommanderMessage(icommandsender, ToolTipUtil.GREEN + "Type /lomlib help or /lomlib ? for more info");
    }

    private void sendCommanderMessage(ICommandSender sender, String message) {
        if (sender != null)
            sender.addChatMessage(new ChatComponentText(message));
    }
}
