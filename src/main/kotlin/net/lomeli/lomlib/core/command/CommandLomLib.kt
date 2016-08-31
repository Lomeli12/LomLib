package net.lomeli.lomlib.core.command

import com.google.common.collect.Lists
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos

class CommandLomLib : CommandBaseLomLib {
    internal var commands: MutableList<String>
    internal var modCommands: MutableList<CommandBase>

    constructor() : super(){
        modCommands = Lists.newArrayList<CommandBase>()
        commands = Lists.newArrayList<String>()

        modCommands.add(CommandCalmPigmen())
        modCommands.add(CommandClearEntities())
        modCommands.add(CommandClearHostiles())

        for (command in modCommands)
            commands.add(command.commandName)
    }

    override fun getCommandName(): String = "lomlib"

    override fun getRequiredPermissionLevel(): Int = 0

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender != null) {
            if (args != null && args.size >= 1) {

                var helpFlag = false
                if (args[0].equals("help", ignoreCase = true) || args[0].equals("?", ignoreCase = true)) {
                    sendMessage(sender, getCommandUsage(sender))
                    helpFlag = true
                }
                for (commandBase in modCommands) {
                    if (helpFlag)
                        sendMessage(sender, "/" + commandName + " " + commandBase.commandName)
                    else if (commandBase.commandName.equals(args[0], ignoreCase = true) && commandBase.checkPermission(server, sender))
                        commandBase.execute(server, sender, args)
                }
            } else
                sendMessage(sender, getCommandUsage(sender))
        }
    }

    override fun getTabCompletionOptions(server: MinecraftServer?, sender: ICommandSender?, args: Array<String>?, pos: BlockPos?): List<String> {
        if (sender != null && args != null) {
            if (args.size == 1)
                return CommandBase.getListOfStringsMatchingLastWord(args, commands)
            else if (args.size >= 2) {
                for (command in modCommands) {
                    if (command.commandName.equals(args[0], ignoreCase = true))
                        return command.getTabCompletionOptions(server, sender, args, pos)
                }
            }
        }
        return Lists.newArrayList()
    }
}