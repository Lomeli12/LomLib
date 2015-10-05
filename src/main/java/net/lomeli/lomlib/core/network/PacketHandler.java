package net.lomeli.lomlib.core.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

import net.lomeli.lomlib.LomLib;

@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {
    private EnumMap<Side, FMLEmbeddedChannel> channel;
    private BasicIndexCodec packetCodec;

    public PacketHandler(String modid, Class<? extends AbstractPacket>... classes) {
        LomLib.logger.logInfo("Creating packet handler for " + modid);
        packetCodec = new BasicIndexCodec(classes);
        channel = NetworkRegistry.INSTANCE.newChannel(modid, packetCodec, this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket msg) throws Exception {
        Side side = FMLCommonHandler.instance().getEffectiveSide();
        INetHandler iNetHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
        PacketContext context = new PacketContext(iNetHandler, side);
        SidedPacket sidedPacket = msg.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null)
            throw new RuntimeException("Packet not sided. Packet must use @SidedPacket annotation!");
        switch (side) {
            case CLIENT:
                if (sidedPacket.acceptedClientSide())
                    msg.handlePacket(context, side);
                break;
            case SERVER:
                if (sidedPacket.acceptedServerSide())
                    msg.handlePacket(context, side);
                break;
        }
    }

    /**
     * Send this message to all clients.
     * <p>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToAll(AbstractPacket message) {
        SidedPacket sidedPacket = message.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null) {
            LomLib.logger.logError("Packet class must be sided using SidedPacket.");
            return;
        }
        if (!sidedPacket.acceptedClientSide()) {
            LomLib.logger.logError("Packet must be accepted on client side.");
            return;
        }
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public void sendTo(AbstractPacket message, EntityPlayer player) {
        SidedPacket sidedPacket = message.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null) {
            LomLib.logger.logError("Packet class must be sided using SidedPacket.");
            return;
        }
        if (!sidedPacket.acceptedClientSide()) {
            LomLib.logger.logError("Packet must be accepted on client side.");
            return;
        }
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param point   The
     *                {@link net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint}
     *                around which to send
     */
    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point) {
        SidedPacket sidedPacket = message.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null) {
            LomLib.logger.logError("Packet class must be sided using SidedPacket.");
            return;
        }
        if (!sidedPacket.acceptedClientSide()) {
            LomLib.logger.logError("Packet must be accepted on client side.");
            return;
        }
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(AbstractPacket message, int dimensionId) {
        SidedPacket sidedPacket = message.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null) {
            LomLib.logger.logError("Packet class must be sided using SidedPacket.");
            return;
        }
        if (!sidedPacket.acceptedClientSide()) {
            LomLib.logger.logError("Packet must be accepted on client side.");
            return;
        }
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToServer(AbstractPacket message) {
        SidedPacket sidedPacket = message.getClass().getAnnotation(SidedPacket.class);
        if (sidedPacket == null) {
            LomLib.logger.logError("Packet class must be sided using SidedPacket.");
            return;
        }
        if (!sidedPacket.acceptedServerSide()) {
            LomLib.logger.logError("Packet must be accepted server side.");
            return;
        }
        channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channel.get(Side.CLIENT).writeAndFlush(message);
    }

    public void sendEverywhere(AbstractPacket message) {
        sendToAll(message);
        sendToServer(message);
    }
}
