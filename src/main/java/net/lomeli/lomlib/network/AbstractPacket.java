package net.lomeli.lomlib.network;

import io.netty.buffer.ByteBuf;

import net.minecraft.entity.player.EntityPlayer;

public abstract class AbstractPacket {
    /**
     * Encode the packet data into the ByteBuf stream. Complex data sets may
     * need specific data handlers.
     *
     * @param buffer the buffer to encode into
     * @link{cpw.mods.fml.common.network.ByteBuffUtils )
     */
    public abstract void encodeInto(ByteBuf buffer);

    /**
     * Decode the packet data from the ByteBuf stream. Complex data sets may
     * need specific data handlers.
     *
     * @param buffer the buffer to decode from
     * @link{cpw.mods.fml.common.network.ByteBuffUtils )
     */
    public abstract void decodeInto(ByteBuf buffer);

    /**
     * Handle a packet on the client side. Note this occurs after decoding has
     * completed.
     *
     * @param player the player reference
     */
    public abstract void handleClientSide(EntityPlayer player);

    /**
     * Handle a packet on the server side. Note this occurs after decoding has
     * completed.
     */
    public abstract void handleServerSide();
}
