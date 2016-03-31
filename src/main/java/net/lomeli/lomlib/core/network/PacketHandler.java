package net.lomeli.lomlib.core.network;

import com.google.common.base.Throwables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    private SimpleNetworkWrapper packetHandler;

    public PacketHandler(String modid, Class<? extends Message>... messages) {
        packetHandler = NetworkRegistry.INSTANCE.newSimpleChannel(modid);
        if (messages != null && messages.length > 0) {
            int count = 0;
            for (int i = 0; i < messages.length; i++) {
                Class msgClass = messages[i];
                if (msgClass != null) {
                    MessageSide side = (MessageSide) msgClass.getAnnotation(MessageSide.class);
                    if (side == null)
                        throw new RuntimeException("Message not sided. Message must use @MessageSide annotation!");
                    try {
                        if (side.serverSide()) {
                            packetHandler.registerMessage(msgClass, msgClass, count, Side.SERVER);
                            count++;
                        }
                        if (side.clientSide()) {
                            packetHandler.registerMessage(msgClass, msgClass, count, Side.CLIENT);
                            count++;
                        }
                    } catch (Exception e) {
                        throw Throwables.propagate(e);
                    }
                }
            }
        }
    }

    public void sendToAll(Message message) {
        if (message == null) return;
        packetHandler.sendToAll(message);
    }

    public void sendTo(Message message, EntityPlayerMP playerMP) {
        if (message == null || playerMP == null) return;
        packetHandler.sendTo(message, playerMP);
    }

    public void sendToServer(Message message) {
        if (message == null) return;
        packetHandler.sendToServer(message);
    }

    public void sendToDimension(Message message, int dimensionId) {
        if (message == null) return;
        packetHandler.sendToDimension(message, dimensionId);
    }

    public void sendToAllAround(Message message, NetworkRegistry.TargetPoint point) {
        if (message == null || point == null) return;
        packetHandler.sendToAllAround(message, point);
    }

    public void sendToClients(WorldServer world, BlockPos pos, Message message) {
        Chunk chunk = world.getChunkFromBlockCoords(pos);
        for (EntityPlayer player : world.playerEntities) {
            // only send to relevant players
            if (player instanceof EntityPlayerMP) {
                EntityPlayerMP playerMP = (EntityPlayerMP) player;
                if (world.getPlayerChunkManager().isPlayerWatchingChunk(playerMP, chunk.xPosition, chunk.zPosition)) {
                    sendTo(message, playerMP);
                }
            }
        }
    }
}
