package net.lomeli.lomlib.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import cpw.mods.fml.common.network.FMLIndexedMessageToMessageCodec;

public class BasicChannelHandler extends FMLIndexedMessageToMessageCodec<AbstractPacket> {
    
    @SuppressWarnings("unchecked")
    public BasicChannelHandler(Class<? extends AbstractPacket>...classes) {
        if (classes.length > 0) {
            for (int i = 0; i < classes.length; i++) {
                this.addDiscriminator(i, classes[i]);
            }
        }
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, AbstractPacket msg, ByteBuf target) throws Exception {
        msg.encodeInto(target);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf source, AbstractPacket msg) {
        msg.decodeInto(source);
    }
}
