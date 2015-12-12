package net.lomeli.lomlib.core.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;

public class CommandClearHostiles extends CommandBaseLomLib {
    @Override
    public String getCommandName() {
        return "clear-hostiles";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (sender == null) return;
        List<?> entityList = sender.getEntityWorld().loadedEntityList;
        if (entityList == null || entityList.isEmpty()) return;
        for (Object entity : entityList) {
            if (entity instanceof Entity) {
                if (entity instanceof IMob)
                    ((Entity) entity).setDead();
            }
        }
        sendMessage(sender, "command.lomlib.clear-hostiles.success");
    }
}
