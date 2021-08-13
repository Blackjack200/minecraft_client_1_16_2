package net.minecraft.network;

import javax.crypto.ShortBufferException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import javax.crypto.Cipher;

public class CipherBase {
    private final Cipher cipher;
    private byte[] heapIn;
    private byte[] heapOut;
    
    protected CipherBase(final Cipher cipher) {
        this.heapIn = new byte[0];
        this.heapOut = new byte[0];
        this.cipher = cipher;
    }
    
    private byte[] bufToByte(final ByteBuf byteBuf) {
        final int integer3 = byteBuf.readableBytes();
        if (this.heapIn.length < integer3) {
            this.heapIn = new byte[integer3];
        }
        byteBuf.readBytes(this.heapIn, 0, integer3);
        return this.heapIn;
    }
    
    protected ByteBuf decipher(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf) throws ShortBufferException {
        final int integer4 = byteBuf.readableBytes();
        final byte[] arr5 = this.bufToByte(byteBuf);
        final ByteBuf byteBuf2 = channelHandlerContext.alloc().heapBuffer(this.cipher.getOutputSize(integer4));
        byteBuf2.writerIndex(this.cipher.update(arr5, 0, integer4, byteBuf2.array(), byteBuf2.arrayOffset()));
        return byteBuf2;
    }
    
    protected void encipher(final ByteBuf byteBuf1, final ByteBuf byteBuf2) throws ShortBufferException {
        final int integer4 = byteBuf1.readableBytes();
        final byte[] arr5 = this.bufToByte(byteBuf1);
        final int integer5 = this.cipher.getOutputSize(integer4);
        if (this.heapOut.length < integer5) {
            this.heapOut = new byte[integer5];
        }
        byteBuf2.writeBytes(this.heapOut, 0, this.cipher.update(arr5, 0, integer4, this.heapOut));
    }
}
