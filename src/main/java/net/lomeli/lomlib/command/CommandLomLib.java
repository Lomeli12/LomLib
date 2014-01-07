package net.lomeli.lomlib.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.lomeli.lomlib.libs.LibraryStrings;
import net.lomeli.lomlib.util.ToolTipUtil;

public class CommandLomLib extends CommandBase {

    @Override
    public String getCommandName() {
        return LibraryStrings.MOD_ID.toLowerCase();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        return "/" + this.getCommandName() + " help";
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
            return getListOfStringsMatchingLastWord(args, new String[] { "calmPigmen" });
        default:
            return null;
        }
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args) {
        if (args.length != 0) {
            String argument1 = args[0];

            @SuppressWarnings("unchecked")
            List<Entity> entityList = icommandsender.getEntityWorld().loadedEntityList;

            if (argument1.equalsIgnoreCase("calmPigmen")) {
                for (Entity entity : entityList) {
                    if (entity instanceof EntityPigZombie) {
                        ((EntityPigZombie) entity).setTarget(null);
                        try {
                            EntityPigZombie.class.getDeclaredField("angerLevel").setInt(entity, 0);
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (argument1.equalsIgnoreCase("clearAllEntities")) {
                for (Entity entity : entityList) {
                    if (!(entity instanceof EntityPlayer)) {
                        entity.setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All non-player entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearEntities")) {
                for (Entity entity : entityList) {
                    if (!(entity instanceof EntityLivingBase)) {
                        entity.setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All non-living entities have been removed.");
            } else if (argument1.equalsIgnoreCase("clearLivingEntities")) {
                for (Entity entity : entityList) {
                    if (entity instanceof EntityLivingBase) {
                        ((EntityLivingBase)entity).setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All Living mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("clearHostiles")) {
                for (Entity entity : entityList) {
                    if (entity instanceof IMob) {
                        entity.setDead();
                    }
                }
                sendCommanderMessage(icommandsender, ToolTipUtil.BLUE + "[LomLib]: All Hostile mobs have been removed.");
            } else if (argument1.equalsIgnoreCase("help") || argument1.equalsIgnoreCase("?")) {
                sendCommanderMessage(icommandsender, "/lomlib calmPigmen " + ToolTipUtil.GREEN + "-Calms down all pigmen in loaded chunks.");
                sendCommanderMessage(icommandsender, "/lomlib clearAllEntities " + ToolTipUtil.GREEN + "-Clears all non-player entities in loaded chunks.");
                sendCommanderMessage(icommandsender, "/lomlib clearEntities " + ToolTipUtil.GREEN + "-Clears all non-living entities in loaded chunks.");
                sendCommanderMessage(icommandsender, "/lomlib clearLivingEntities " + ToolTipUtil.GREEN + "-Clears all living entities in loaded chunks.");
                sendCommanderMessage(icommandsender, "/lomlib clearHostiles  " + ToolTipUtil.GREEN + "-Clears all hostile entities in loaded chunks.");
            }
        } else
            sendCommanderMessage(icommandsender, ToolTipUtil.GREEN + "Type /lomlib help or /lomlib ? for more info");
    }

    private void sendCommanderMessage(ICommandSender sender, String message) {
        EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(sender.getCommandSenderName());
        player.func_145747_a(player.func_145748_c_().func_150258_a(message));
    }

    @Override
    public int compareTo(Object arg0) {
        return 0;
    }
}
