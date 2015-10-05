package net.lomeli.lomlib.core.network;

import io.netty.buffer.ByteBuf;

import net.minecraftforge.fml.relauncher.Side;

@SidedPacket
public abstract class AbstractPacket {
    /**
     * Encode the packet data into the ByteBuf stream. Complex data sets may
     * need specific data handlers.
     *
     * @param buffer the buffer to encode into
     * @link{cpw.mods.fml.common.network.ByteBuffUtils}
     */
    public abstract void encodeInto(ByteBuf buffer);

    /**
     * Decode the packet data from the ByteBuf stream. Complex data sets may
     * need specific data handlers.
     *
     * @param buffer the buffer to decode from
     * @link{cpw.mods.fml.common.network.ByteBuffUtils}
     */
    public abstract void decodeInto(ByteBuf buffer);

    /**
     * @param context - contains nethandlers
     * @param side - Side the packet is being handled on
     */
    public abstract void handlePacket(PacketContext context, Side side);
}
