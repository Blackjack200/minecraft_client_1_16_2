package net.minecraft.network;

import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToByteEncoder;

public class CipherEncoder extends MessageToByteEncoder<ByteBuf> {
    private final CipherBase cipher;
    
    public CipherEncoder(final Cipher cipher) {
        this.cipher = new CipherBase(cipher);
    }
    
    protected void encode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf2, final ByteBuf byteBuf3) throws Exception {
        this.cipher.encipher(byteBuf2, byteBuf3);
    }
}
