package net.lomeli.lomlib.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

import net.lomeli.lomlib.util.LangUtil;

public class CommandBaseLomLib extends CommandBase {

    @Override
    public String getCommandName() {
        return null;
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "command.lomlib." + getCommandName() + ".usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
    }

    protected void sendMessage(ICommandSender sender, String message) {
        if (sender != null)
            sender.addChatMessage(new ChatComponentText(LangUtil.translate(message)));
    }
}
