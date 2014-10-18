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
import net.lomeli.lomlib.util.ObfUtil;
import net.lomeli.lomlib.util.ToolTipUtil;
import net.lomeli.lomlib.util.version.VersionChecker;

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
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, new String[]{"calmPigmen"});
            default:
                return null;
        }
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args) {
        if (args.length > 0) {
            String argument1 = args[0];

            List<?> entityList = icommandsender.getEntityWorld().loadedEntityList;

            if (argument1.equalsIgnoreCase("calmPigmen")) {
                boolean successful = false;
                for (Object entity : entityList) {
                    if (entity instanceof EntityPigZombie) {
                        ((EntityPigZombie) entity).setTarget(null);
                        successful = calmPigmen(icommandsender, ((EntityPigZombie) entity));
                    }
                }
                if (successful)
                    sendMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: Pig Zombies should now be calm");
            } else if (argument1.equalsIgnoreCase("clearAllEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityPlayer))
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded non-player entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityLivingBase))
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded non-living entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearLiving")) {
                for (Object entity : entityList) {
                    if ((entity instanceof EntityLivingBase) && !(entity instanceof EntityPlayer))
                        ((EntityLivingBase) entity).setDead();
                }
                sendMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded Living mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("clearHostiles")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (entity instanceof IMob)
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All loaded Hostile mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("allowFly")) {
                String username = args[1];
                EntityPlayer player = icommandsender.getEntityWorld().getPlayerEntityByName(username);
                if (player != null && !player.capabilities.isCreativeMode)
                    player.capabilities.allowFlying = !player.capabilities.allowFlying;
                else
                    sendMessage(icommandsender, ToolTipUtil.ORANGE + "allowFly <playername>");
            } else if (argument1.equalsIgnoreCase("update")) {
                if (args.length >= 2) {
                    String id = args[1];
                    VersionChecker.beginModDownloader(icommandsender, id);
                } else
                    sendMessage(icommandsender, ToolTipUtil.RED + "/lomlib update [modid]");
            } else if (argument1.equalsIgnoreCase("help") || argument1.equalsIgnoreCase("?"))
                displayHelp(icommandsender);
        } else
            sendMessage(icommandsender, ToolTipUtil.GREEN + "Type /lomlib help or /lomlib ? for more info");
    }

    private boolean calmPigmen(ICommandSender sender, EntityPigZombie pigZombie) {
        try {
            if (!ObfUtil.isFieldAccessible(EntityPigZombie.class, "angerLevel", "field_70837_d", "bs"))
                ObfUtil.setFieldAccessible(EntityPigZombie.class, "angerLevel", "field_70837_d", "bs");
            ObfUtil.setFieldValue(EntityPigZombie.class, pigZombie, 0, "angerLevel", "field_70837_d", "bs");
            return true;
        } catch (Exception e) {
            sendMessage(sender, ToolTipUtil.BLUE + "[LomLib]: Failed to calm Pig Zombies!");
            e.printStackTrace();
            return false;
        }
    }

    private void displayHelp(ICommandSender sender) {
        sendMessage(sender, "Format: `" + ToolTipUtil.ORANGE + "lomlib <command> <command Argument>" + ToolTipUtil.WHITE + "'");
        sendMessage(sender, "Available commands: ");
        sendMessage(sender, "- calmPigmen : " + ToolTipUtil.GREEN + "Calms down all loaded pigmen.");
        sendMessage(sender, "- clearAllEntities : " + ToolTipUtil.GREEN + "Clears all loaded non-player entities.");
        sendMessage(sender, "- clearEntities : " + ToolTipUtil.GREEN + "Clears all loaded non-living entities.");
        sendMessage(sender, "- clearLivingEntities : " + ToolTipUtil.GREEN + "Clears all loaded living entities.");
        sendMessage(sender, "- clearHostiles : " + ToolTipUtil.GREEN + "Clears all loaded hostile entities.");
        sendMessage(sender, "- allowFly <player> : " + ToolTipUtil.GREEN + "Enable/Disable flying for a player");
        sendMessage(sender, "- update <modid] : " + ToolTipUtil.GREEN + "Begin downloading an updated version of a mod.");
    }

    private void sendMessage(ICommandSender sender, String message) {
        if (sender != null)
            sender.addChatMessage(new ChatComponentText(message));
    }
}
