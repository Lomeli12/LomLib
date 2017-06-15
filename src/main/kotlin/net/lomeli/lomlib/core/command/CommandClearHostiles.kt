package net.lomeli.lomlib.core.command

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.IMob
import net.minecraft.server.MinecraftServer

class CommandClearHostiles : CommandBaseLomLib() {
    override fun getName(): String = "clear-hostiles"

    override fun getRequiredPermissionLevel(): Int = 2

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender == null) return
        val entityList = sender.entityWorld.loadedEntityList
        if (entityList == null || entityList.isEmpty()) return
        for (entity in entityList) {
            if (entity is Entity) {
                if (entity is IMob)
                    entity.setDead()
            }
        }
        sendMessage(sender, "command.lomlib.clear-hostiles.success")
    }
}