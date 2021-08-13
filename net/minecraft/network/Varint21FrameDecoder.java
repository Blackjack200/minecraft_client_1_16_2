package net.minecraft.network;

import io.netty.handler.codec.CorruptedFrameException;
import io.netty.buffer.Unpooled;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class Varint21FrameDecoder extends ByteToMessageDecoder {
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        final byte[] arr5 = new byte[3];
        for (int integer6 = 0; integer6 < arr5.length; ++integer6) {
            if (!byteBuf.isReadable()) {
                byteBuf.resetReaderIndex();
                return;
            }
            arr5[integer6] = byteBuf.readByte();
            if (arr5[integer6] >= 0) {
                final FriendlyByteBuf nf7 = new FriendlyByteBuf(Unpooled.wrappedBuffer(arr5));
                try {
                    final int integer7 = nf7.readVarInt();
                    if (byteBuf.readableBytes() < integer7) {
                        byteBuf.resetReaderIndex();
                        return;
                    }
                    list.add(byteBuf.readBytes(integer7));
                    return;
                }
                finally {
                    nf7.release();
                }
            }
        }
        throw new CorruptedFrameException("length wider than 21-bit");
    }
}
