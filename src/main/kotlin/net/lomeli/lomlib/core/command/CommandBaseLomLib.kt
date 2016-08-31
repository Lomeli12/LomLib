package net.lomeli.lomlib.core.command

import net.lomeli.lomlib.util.LangUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString

abstract class CommandBaseLomLib : CommandBase() {

    abstract override fun getCommandName(): String

    override fun getCommandUsage(sender: ICommandSender): String = "command.lomlib.$commandName.usage"

    @Throws(CommandException::class)
    abstract override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>)

    protected fun sendMessage(sender: ICommandSender?, message: String) {
        sender?.addChatMessage(TextComponentString(LangUtil.translate(message)))
    }
}