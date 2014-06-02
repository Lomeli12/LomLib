package net.lomeli.lomlib.network;

import java.util.EnumMap;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;

import net.lomeli.lomlib.LomLib;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.FMLOutboundHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;

@Sharable
public class BasicPacketHandler extends SimpleChannelInboundHandler<AbstractPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AbstractPacket msg) throws Exception {
        try {
            switch(FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT :
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                msg.handleClientSide(LomLib.proxy.getPlayerFromNetHandler(netHandler));
                break;
            case SERVER :
                msg.handleServerSide();
                break;
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Send this message to all clients.
     * <p/>
     * Adapted from CPW's code in
     * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     */
    public static void sendToAll(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the specified player.
     * <p/>
     * Adapted from CPW's code in
     * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param player
     *            The player to send it to
     */
    public static void sendTo(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message, EntityPlayer player) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within a certain range of a point.
     * <p/>
     * Adapted from CPW's code in
     * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param point
     *            The
     *            {@link cpw.mods.fml.common.network.NetworkRegistry.TargetPoint}
     *            around which to send
     */
    public static void sendToAllAround(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message, NetworkRegistry.TargetPoint point) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to everyone within the supplied dimension.
     * <p/>
     * Adapted from CPW's code in
     * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     * @param dimensionId
     *            The dimension id to target
     */
    public static void sendToDimension(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message, int dimensionId) {
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
        channel.get(Side.SERVER).attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
        channel.get(Side.SERVER).writeAndFlush(message);
    }

    /**
     * Send this message to the server.
     * <p/>
     * Adapted from CPW's code in
     * cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper
     * 
     * @param message
     *            The message to send
     */
    public static void sendToServer(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message) {
        channel.get(Side.CLIENT).attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
        channel.get(Side.CLIENT).writeAndFlush(message);
    }

    public static void sendToAllExcept(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message, EntityPlayer player) {
        for (int i = 0; i < FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.size(); i++) {
            EntityPlayer p = (EntityPlayer) FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().playerEntityList.get(i);
            if (!player.getCommandSenderName().equalsIgnoreCase(p.getCommandSenderName()))
                sendTo(channel, message, p);
        }
    }

    public static void sendEverywhere(EnumMap<Side, FMLEmbeddedChannel> channel, AbstractPacket message) {
        sendToAll(channel, message);
        sendToServer(channel, message);
    }
}
