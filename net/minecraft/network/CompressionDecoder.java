package net.minecraft.network;

import io.netty.buffer.Unpooled;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import java.util.zip.Inflater;
import io.netty.handler.codec.ByteToMessageDecoder;

public class CompressionDecoder extends ByteToMessageDecoder {
    private final Inflater inflater;
    private int threshold;
    
    public CompressionDecoder(final int integer) {
        this.threshold = integer;
        this.inflater = new Inflater();
    }
    
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> list) throws Exception {
        if (byteBuf.readableBytes() == 0) {
            return;
        }
        final FriendlyByteBuf nf5 = new FriendlyByteBuf(byteBuf);
        final int integer6 = nf5.readVarInt();
        if (integer6 == 0) {
            list.add(nf5.readBytes(nf5.readableBytes()));
        }
        else {
            if (integer6 < this.threshold) {
                throw new DecoderException(new StringBuilder().append("Badly compressed packet - size of ").append(integer6).append(" is below server threshold of ").append(this.threshold).toString());
            }
            if (integer6 > 2097152) {
                throw new DecoderException(new StringBuilder().append("Badly compressed packet - size of ").append(integer6).append(" is larger than protocol maximum of ").append(2097152).toString());
            }
            final byte[] arr7 = new byte[nf5.readableBytes()];
            nf5.readBytes(arr7);
            this.inflater.setInput(arr7);
            final byte[] arr8 = new byte[integer6];
            this.inflater.inflate(arr8);
            list.add(Unpooled.wrappedBuffer(arr8));
            this.inflater.reset();
        }
    }
    
    public void setThreshold(final int integer) {
        this.threshold = integer;
    }
}
