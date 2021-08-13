package net.minecraft.network;

import java.util.List;
import io.netty.channel.ChannelHandlerContext;
import javax.crypto.Cipher;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.MessageToMessageDecoder;

public class CipherDecoder extends MessageToMessageDecoder<ByteBuf> {
    private final CipherBase cipher;
    
    public CipherDecoder(final Cipher cipher) {
        this.cipher = new CipherBase(cipher);
    }
    
    protected void decode(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf, final List<Object> list) throws Exception {
        list.add(this.cipher.decipher(channelHandlerContext, byteBuf));
    }
}
