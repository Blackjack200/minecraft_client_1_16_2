package net.minecraft.network;

import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.protocol.Packet;
import java.io.IOException;
import io.netty.util.AttributeKey;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.protocol.PacketFlow;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.Logger;
import io.netty.handler.codec.ByteToMessageDecoder;

public class PacketDecoder extends ByteToMessageDecoder {
    private static final Logger LOGGER;
    private static final Marker MARKER;
    private final PacketFlow flow;
    
    public PacketDecoder(final PacketFlow ok) {
        this.flow = ok;
    }
    
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> list) throws Exception {
        if (byteBuf.readableBytes() == 0) {
            return;
        }
        final FriendlyByteBuf nf5 = new FriendlyByteBuf(byteBuf);
        final int integer6 = nf5.readVarInt();
        final Packet<?> oj7 = ((ConnectionProtocol)channelHandlerContext.channel().attr((AttributeKey)Connection.ATTRIBUTE_PROTOCOL).get()).createPacket(this.flow, integer6);
        if (oj7 == null) {
            throw new IOException(new StringBuilder().append("Bad packet id ").append(integer6).toString());
        }
        oj7.read(nf5);
        if (nf5.readableBytes() > 0) {
            throw new IOException(new StringBuilder().append("Packet ").append(((ConnectionProtocol)channelHandlerContext.channel().attr((AttributeKey)Connection.ATTRIBUTE_PROTOCOL).get()).getId()).append("/").append(integer6).append(" (").append(oj7.getClass().getSimpleName()).append(") was larger than I expected, found ").append(nf5.readableBytes()).append(" bytes extra whilst reading packet ").append(integer6).toString());
        }
        list.add(oj7);
        if (PacketDecoder.LOGGER.isDebugEnabled()) {
            PacketDecoder.LOGGER.debug(PacketDecoder.MARKER, " IN: [{}:{}] {}", channelHandlerContext.channel().attr((AttributeKey)Connection.ATTRIBUTE_PROTOCOL).get(), integer6, oj7.getClass().getName());
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
        MARKER = MarkerManager.getMarker("PACKET_RECEIVED", Connection.PACKET_MARKER);
    }
}
