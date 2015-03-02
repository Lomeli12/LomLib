package net.lomeli.lomlib.core;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class CommandLomLib extends CommandBase {

    @Override
    public String getName() {
        return Strings.MOD_ID.toLowerCase();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.getName() + " help";
    }

    @Override
    public void execute(ICommandSender icommandsender, String[] args) throws CommandException {
        if (args.length > 0) {
            String argument1 = args[0];

            List<?> entityList = icommandsender.getEntityWorld().loadedEntityList;

            if (argument1.equalsIgnoreCase("calmPigmen")) {
                boolean successful = false;
                for (Object entity : entityList) {
                    if (entity instanceof EntityPigZombie)
                        successful = calmPigmen(icommandsender, ((EntityPigZombie) entity));
                }
                if (successful)
                    sendMessage(icommandsender, EnumChatFormatting.BLUE + "[LomLib]: Pig Zombies should now be calm");
            } else if (argument1.equalsIgnoreCase("clearAllEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityPlayer))
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, EnumChatFormatting.BLUE + "[LomLib]: All loaded non-player entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearEntities")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (!(entity instanceof EntityLivingBase))
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, EnumChatFormatting.BLUE + "[LomLib]: All loaded non-living entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearLiving")) {
                for (Object entity : entityList) {
                    if ((entity instanceof EntityLivingBase) && !(entity instanceof EntityPlayer))
                        ((EntityLivingBase) entity).setDead();
                }
                sendMessage(icommandsender, EnumChatFormatting.BLUE + "[LomLib]: All loaded Living mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("clearHostiles")) {
                for (Object entity : entityList) {
                    if (entity instanceof Entity) {
                        if (entity instanceof IMob)
                            ((Entity) entity).setDead();
                    }
                }
                sendMessage(icommandsender, EnumChatFormatting.BLUE + "[LomLib]: All loaded Hostile mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("allowFly")) {
                String username = args[1];
                EntityPlayer player = icommandsender.getEntityWorld().getPlayerEntityByName(username);
                if (player != null && !player.capabilities.isCreativeMode)
                    player.capabilities.allowFlying = !player.capabilities.allowFlying;
                else
                    sendMessage(icommandsender, EnumChatFormatting.GOLD + "allowFly <playername>");
            } else if (argument1.equalsIgnoreCase("help") || argument1.equalsIgnoreCase("?"))
                displayHelp(icommandsender);
        } else
            sendMessage(icommandsender, EnumChatFormatting.GREEN + "Type /lomlib help or /lomlib ? for more info");
    }

    private boolean calmPigmen(ICommandSender sender, EntityPigZombie pigZombie) {
        pigZombie.angerLevel = 0;
        pigZombie.setAttackTarget(null);
        return true;
    }

    private void displayHelp(ICommandSender sender) {
        sendMessage(sender, "Format: `" + EnumChatFormatting.GOLD + "lomlib <command> <command Argument>" + EnumChatFormatting.WHITE + "'");
        sendMessage(sender, "Available commands: ");
        sendMessage(sender, "- calmPigmen : " + EnumChatFormatting.GREEN + "Calms down all loaded pigmen.");
        sendMessage(sender, "- clearAllEntities : " + EnumChatFormatting.GREEN + "Clears all loaded non-player entities.");
        sendMessage(sender, "- clearEntities : " + EnumChatFormatting.GREEN + "Clears all loaded non-living entities.");
        sendMessage(sender, "- clearLivingEntities : " + EnumChatFormatting.GREEN + "Clears all loaded living entities.");
        sendMessage(sender, "- clearHostiles : " + EnumChatFormatting.GREEN + "Clears all loaded hostile entities.");
        sendMessage(sender, "- allowFly <player> : " + EnumChatFormatting.GREEN + "Enable/Disable flying for a player");
    }

    private void sendMessage(ICommandSender sender, String message) {
        if (sender != null)
            sender.addChatMessage(new ChatComponentText(message));
    }
}
