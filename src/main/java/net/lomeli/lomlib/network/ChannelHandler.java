package net.lomeli.lomlib.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import java.util.*;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.FMLEmbeddedChannel;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.internal.FMLProxyPacket;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.lomeli.lomlib.LomLib;

/**
 * Packet pipeline class. Directs all registered packet data to be handled by
 * the packets themselves.
 *
 * @author sirgingalot some code from: cpw
 */
@ChannelHandler.Sharable
public class ChannelHandler extends MessageToMessageCodec<FMLProxyPacket, AbstractPacket> {

    protected EnumMap<Side, FMLEmbeddedChannel> channels;
    private LinkedList<Class<? extends AbstractPacket>> packets = new LinkedList<Class<? extends AbstractPacket>>();
    private boolean isPostInitialised = false;
    private String modID;

    public ChannelHandler() {
    }

    public ChannelHandler(String modID, Class<? extends AbstractPacket>... packetTypes) {
        this.modID = modID;
        for (Class<? extends AbstractPacket> clazz : packetTypes) {
            registerPacket(clazz);
        }
        this.channels = NetworkRegistry.INSTANCE.newChannel(modID, this);
    }

    /**
     * Register your packet with the pipeline. Discriminators are automatically
     * set.
     *
     * @param clazz the class to register
     * @return whether registration was successful. Failure may occur if 256
     * packets have been registered or if the registry already contains
     * this packet
     */
    public boolean registerPacket(Class<? extends AbstractPacket> clazz) {
        if (this.packets.size() > 256) {
            LomLib.logger.logError("Too many registered packets!");
            return false;
        }

        if (this.packets.contains(clazz)) {
            LomLib.logger.logInfo("Packet class is already registered!");
            return false;
        }

        if (this.isPostInitialised) {
            LomLib.logger.logInfo("It's too late to register packet");
            return false;
        }

        this.packets.add(clazz);
        return true;
    }

    /**
     * In line encoding of the packet, including discriminator setting
     */
    @Override
    protected void encode(ChannelHandlerContext ctx, AbstractPacket msg, List<Object> out) throws Exception {
        ByteBuf buffer = Unpooled.buffer();
        Class<? extends AbstractPacket> clazz = msg.getClass();
        if (!this.packets.contains(msg.getClass())) {
            throw new NullPointerException("No Packet Registered for: " + msg.getClass().getCanonicalName());
        }

        byte discriminator = (byte) this.packets.indexOf(clazz);
        buffer.writeByte(discriminator);
        msg.encodeInto(ctx, buffer);
        FMLProxyPacket proxyPacket = new FMLProxyPacket(buffer.copy(), ctx.channel().attr(NetworkRegistry.FML_CHANNEL).get());
        out.add(proxyPacket);
    }

    /**
     * In line decoding and handling of the packet
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, FMLProxyPacket msg, List<Object> out) throws Exception {
        ByteBuf payload = msg.payload();
        byte discriminator = payload.readByte();
        Class<? extends AbstractPacket> clazz = this.packets.get(discriminator);
        if (clazz == null) {
            throw new NullPointerException("No packet registered for discriminator: " + discriminator);
        }

        AbstractPacket pkt = clazz.newInstance();
        pkt.decodeInto(ctx, payload.slice());

        EntityPlayer player;
        switch (FMLCommonHandler.instance().getEffectiveSide()) {
            case CLIENT:
                player = this.getClientPlayer();
                pkt.handleClientSide(player);
                break;
            case SERVER:
                INetHandler netHandler = ctx.channel().attr(NetworkRegistry.NET_HANDLER).get();
                player = ((NetHandlerPlayServer) netHandler).playerEntity;
                pkt.handleServerSide(player);
                break;
            default:
        }
        out.add(pkt);
    }

    /**
     * Method to call from FMLPostInitializationEvent.
     * <p/>
     * Ensures that packet discriminators are common between server and client
     * by using logical sorting
     */
    public void postInitialise() {
        if (this.isPostInitialised) {
            return;
        }

        this.isPostInitialised = true;
        Collections.sort(this.packets, new Comparator<Class<? extends AbstractPacket>>() {

            @Override
            public int compare(Class<? extends AbstractPacket> clazz1, Class<? extends AbstractPacket> clazz2) {
                int com = String.CASE_INSENSITIVE_ORDER.compare(clazz1.getCanonicalName(), clazz2.getCanonicalName());
                if (com == 0) {
                    com = clazz1.getCanonicalName().compareTo(clazz2.getCanonicalName());
                }

                return com;
            }
        });
    }

    @SideOnly(Side.CLIENT)
    private EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }
}
