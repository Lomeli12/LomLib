package net.lomeli.lomlib.core.command

import net.lomeli.lomlib.util.entity.EntityUtil
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.monster.EntityPigZombie
import net.minecraft.server.MinecraftServer

class CommandCalmPigmen : CommandBaseLomLib() {
    override fun getCommandName(): String = "calm-pigmen"

    override fun getRequiredPermissionLevel(): Int = 2

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender == null) return
        val entityList = sender.entityWorld.loadedEntityList
        if (entityList == null || entityList.isEmpty()) return
        var successful = false
        for (entity in entityList) {
            if (entity is EntityPigZombie)
                successful = calmPigmen(entity)
        }
        if (successful)
            sendMessage(sender, "command.lomlib.calm-pigmen.success")
    }

    private fun calmPigmen(pigZombie: EntityPigZombie): Boolean {
        EntityUtil.setPigmenAnger(pigZombie, null, 0)
        return true
    }
}