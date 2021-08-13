package net.minecraft.server.network;

import org.apache.logging.log4j.LogManager;
import io.netty.buffer.Unpooled;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.server.MinecraftServer;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER;
    private final ServerConnectionListener serverConnectionListener;
    
    public LegacyQueryHandler(final ServerConnectionListener aax) {
        this.serverConnectionListener = aax;
    }
    
    public void channelRead(final ChannelHandlerContext channelHandlerContext, final Object object) throws Exception {
        final ByteBuf byteBuf4 = (ByteBuf)object;
        byteBuf4.markReaderIndex();
        boolean boolean5 = true;
        try {
            if (byteBuf4.readUnsignedByte() != 254) {
                return;
            }
            final InetSocketAddress inetSocketAddress6 = (InetSocketAddress)channelHandlerContext.channel().remoteAddress();
            final MinecraftServer minecraftServer7 = this.serverConnectionListener.getServer();
            final int integer8 = byteBuf4.readableBytes();
            switch (integer8) {
                case 0: {
                    LegacyQueryHandler.LOGGER.debug("Ping: (<1.3.x) from {}:{}", inetSocketAddress6.getAddress(), inetSocketAddress6.getPort());
                    final String string9 = String.format("%s§%d§%d", new Object[] { minecraftServer7.getMotd(), minecraftServer7.getPlayerCount(), minecraftServer7.getMaxPlayers() });
                    this.sendFlushAndClose(channelHandlerContext, this.createReply(string9));
                    break;
                }
                case 1: {
                    if (byteBuf4.readUnsignedByte() != 1) {
                        return;
                    }
                    LegacyQueryHandler.LOGGER.debug("Ping: (1.4-1.5.x) from {}:{}", inetSocketAddress6.getAddress(), inetSocketAddress6.getPort());
                    final String string9 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] { 127, minecraftServer7.getServerVersion(), minecraftServer7.getMotd(), minecraftServer7.getPlayerCount(), minecraftServer7.getMaxPlayers() });
                    this.sendFlushAndClose(channelHandlerContext, this.createReply(string9));
                    break;
                }
                default: {
                    boolean boolean6 = byteBuf4.readUnsignedByte() == 1;
                    boolean6 &= (byteBuf4.readUnsignedByte() == 250);
                    boolean6 &= "MC|PingHost".equals(new String(byteBuf4.readBytes(byteBuf4.readShort() * 2).array(), StandardCharsets.UTF_16BE));
                    final int integer9 = byteBuf4.readUnsignedShort();
                    boolean6 &= (byteBuf4.readUnsignedByte() >= 73);
                    boolean6 &= (3 + byteBuf4.readBytes(byteBuf4.readShort() * 2).array().length + 4 == integer9);
                    boolean6 &= (byteBuf4.readInt() <= 65535);
                    boolean6 &= (byteBuf4.readableBytes() == 0);
                    if (!boolean6) {
                        return;
                    }
                    LegacyQueryHandler.LOGGER.debug("Ping: (1.6) from {}:{}", inetSocketAddress6.getAddress(), inetSocketAddress6.getPort());
                    final String string10 = String.format("§1\u0000%d\u0000%s\u0000%s\u0000%d\u0000%d", new Object[] { 127, minecraftServer7.getServerVersion(), minecraftServer7.getMotd(), minecraftServer7.getPlayerCount(), minecraftServer7.getMaxPlayers() });
                    final ByteBuf byteBuf5 = this.createReply(string10);
                    try {
                        this.sendFlushAndClose(channelHandlerContext, byteBuf5);
                    }
                    finally {
                        byteBuf5.release();
                    }
                    break;
                }
            }
            byteBuf4.release();
            boolean5 = false;
        }
        catch (RuntimeException ex) {}
        finally {
            if (boolean5) {
                byteBuf4.resetReaderIndex();
                channelHandlerContext.channel().pipeline().remove("legacy_query");
                channelHandlerContext.fireChannelRead(object);
            }
        }
    }
    
    private void sendFlushAndClose(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf) {
        channelHandlerContext.pipeline().firstContext().writeAndFlush(byteBuf).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
    }
    
    private ByteBuf createReply(final String string) {
        final ByteBuf byteBuf3 = Unpooled.buffer();
        byteBuf3.writeByte(255);
        final char[] arr4 = string.toCharArray();
        byteBuf3.writeShort(arr4.length);
        for (final char character8 : arr4) {
            byteBuf3.writeChar((int)character8);
        }
        return byteBuf3;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
