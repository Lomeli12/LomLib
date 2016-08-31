package net.lomeli.lomlib.core.command

import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.server.MinecraftServer

class CommandClearEntities : CommandBaseLomLib() {
    override fun getCommandName(): String = "clear-entities"

    override fun getRequiredPermissionLevel(): Int = 2

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        if (sender == null) return
        val entityList = sender.entityWorld.loadedEntityList
        if (entityList == null || entityList.isEmpty()) return
        try {
            for (entity in entityList) {
                if (entity is Entity) {
                    if (entity !is EntityLivingBase)
                        entity.setDead()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        sendMessage(sender, "command.lomlib.clear-entities.success")
    }


}