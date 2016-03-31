package net.lomeli.lomlib.core.command;

import com.google.common.collect.Lists;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public class CommandLomLib extends CommandBaseLomLib {
    List<String> commands;
    List<CommandBase> modCommands;

    public CommandLomLib() {
        modCommands = Lists.newArrayList();
        commands = Lists.newArrayList();

        modCommands.add(new CommandCalmPigmen());
        modCommands.add(new CommandClearEntities());
        modCommands.add(new CommandClearHostiles());

        for (CommandBase command : modCommands)
            commands.add(command.getCommandName());
    }

    @Override
    public String getCommandName() {
        return "lomlib";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (sender != null) {
            if (args != null && args.length >= 1) {
                boolean helpFlag = false;
                if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("?")) {
                    sendMessage(sender, getCommandUsage(sender));
                    helpFlag = true;
                }
                for (CommandBase commandBase : modCommands) {
                    if (helpFlag)
                        sendMessage(sender, "/" + getCommandName() + " " + commandBase.getCommandName());
                    else if (commandBase.getCommandName().equalsIgnoreCase(args[0]) && commandBase.checkPermission(server, sender))
                        commandBase.execute(server, sender, args);
                }
            } else
                sendMessage(sender, getCommandUsage(sender));
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if (sender != null && args != null) {
            if (args.length == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands);
            else if (args.length >= 2) {
                for (CommandBase command : modCommands) {
                    if (command.getCommandName().equalsIgnoreCase(args[0]))
                        return command.getTabCompletionOptions(server, sender, args, pos);
                }
            }
        }
        return null;
    }
}
