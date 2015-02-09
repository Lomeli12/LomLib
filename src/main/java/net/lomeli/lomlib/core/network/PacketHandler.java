package net.lomeli.lomlib.core.network;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.EnumMap;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Sharable
public class PacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {
    private EnumMap<Side, FMLEmbeddedChannel> channel;
    private BasicIndexCodec packetCodec;

    public PacketHandler(String modid, Class<? extends AbstractPacket>... classes) {
        packetCodec = new BasicIndexCodec(classes);
        channel = NetworkRegistry.INSTANCE.newChannel(modid, packetCodec, this);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket msg) throws Exception {
        try {
            switch (FMLCommonHandler.instance().getEffectiveSide()) {
                case CLIENT:
                    INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                    msg.handleClientSide(getPlayer(netHandler, FMLCommonHandler.instance().getEffectiveSide()));
                    break;
                case SERVER:
                    msg.handleServerSide();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EntityPlayer getPlayer(INetHandler handler, Side side) {
        if (handler instanceof NetHandlerPlayServer)
            return ((NetHandlerPlayServer) handler).playerEntity;
        else if (side == Side.CLIENT) return Minecraft.getMinecraft().thePlayer;
        else return null;
    }

    /**
     * Send this message to all clients.
     * <p/>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToAll(AbstractPacket message) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param player  The player to send it to
     */
    public void sendTo(AbstractPacket message, EntityPlayer player) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     * @param point   The
     *                {@link net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint}
     *                around which to send
     */
    public void sendToAllAround(AbstractPacket message, NetworkRegistry.TargetPoint point) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message     The message to send
     * @param dimensionId The dimension id to target
     */
    public void sendToDimension(AbstractPacket message, int dimensionId) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in
     * net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper
     *
     * @param message The message to send
     */
    public void sendToServer(AbstractPacket message) {
        channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channel.get(Side.CLIENT).writeAndFlush(message);
    }

    public void sendEverywhere(AbstractPacket message) {
        sendToAll(message);
        sendToServer(message);
    }
}
