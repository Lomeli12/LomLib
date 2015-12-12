package net.lomeli.lomlib.core.command;

import java.util.List;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityPigZombie;

public class CommandCalmPigmen extends CommandBaseLomLib {
    @Override
    public String getCommandName() {
        return "calm-pigmen";
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
        boolean successful = false;
        for (Object entity : entityList) {
            if (entity instanceof EntityPigZombie)
                successful = calmPigmen((EntityPigZombie) entity);
        }
        if (successful)
            sendMessage(sender, "command.lomlib.calm-pigmen.success");
    }

    private boolean calmPigmen(EntityPigZombie pigZombie) {
        pigZombie.angerLevel = 0;
        pigZombie.setAttackTarget(null);
        return true;
    }
}
