package net.lomeli.lomlib.core.command;

import java.util.List;

import net.lomeli.lomlib.util.EntityUtil;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.server.MinecraftServer;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
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
        EntityUtil.setPigmenAnger(pigZombie, null, 0);
        return true;
    }
}
