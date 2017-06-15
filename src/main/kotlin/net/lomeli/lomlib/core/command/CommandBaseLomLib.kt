package net.lomeli.lomlib.core.command

import net.lomeli.lomlib.util.LangUtil
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.text.TextComponentString

abstract class CommandBaseLomLib : CommandBase() {

    abstract override fun getName(): String

    override fun getUsage(sender: ICommandSender): String = "command.lomlib.$name.usage"

    @Throws(CommandException::class)
    abstract override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>)

    protected fun sendMessage(sender: ICommandSender?, message: String) {
        sender?.sendMessage(TextComponentString(LangUtil.translate(message)))
    }
}