package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import io.netty.channel.socket.nio.NioSocketChannel;
import net.minecraft.util.Mth;
import com.google.common.collect.Iterables;
import java.nio.charset.StandardCharsets;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.ChannelFutureListener;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.buffer.ByteBuf;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.bootstrap.Bootstrap;
import java.net.UnknownHostException;
import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.status.ClientboundPongResponsePacket;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.status.ServerboundPingRequestPacket;
import net.minecraft.Util;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import org.apache.commons.lang3.ArrayUtils;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import net.minecraft.network.chat.TranslatableComponent;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.network.Connection;
import java.util.List;
import org.apache.logging.log4j.Logger;
import com.google.common.base.Splitter;

public class ServerStatusPinger {
    private static final Splitter SPLITTER;
    private static final Logger LOGGER;
    private final List<Connection> connections;
    
    public ServerStatusPinger() {
        this.connections = (List<Connection>)Collections.synchronizedList((List)Lists.newArrayList());
    }
    
    public void pingServer(final ServerData dwr, final Runnable runnable) throws UnknownHostException {
        final ServerAddress dwq4 = ServerAddress.parseString(dwr.ip);
        final Connection nd5 = Connection.connectToServer(InetAddress.getByName(dwq4.getHost()), dwq4.getPort(), false);
        this.connections.add(nd5);
        dwr.motd = new TranslatableComponent("multiplayer.status.pinging");
        dwr.ping = -1L;
        dwr.playerList = null;
        nd5.setListener(new ClientStatusPacketListener() {
            private boolean success;
            private boolean receivedPing;
            private long pingStart;
            
            public void handleStatusResponse(final ClientboundStatusResponsePacket um) {
                if (this.receivedPing) {
                    nd5.disconnect(new TranslatableComponent("multiplayer.status.unrequested"));
                    return;
                }
                this.receivedPing = true;
                final ServerStatus un3 = um.getStatus();
                if (un3.getDescription() != null) {
                    dwr.motd = un3.getDescription();
                }
                else {
                    dwr.motd = TextComponent.EMPTY;
                }
                if (un3.getVersion() != null) {
                    dwr.version = new TextComponent(un3.getVersion().getName());
                    dwr.protocol = un3.getVersion().getProtocol();
                }
                else {
                    dwr.version = new TranslatableComponent("multiplayer.status.old");
                    dwr.protocol = 0;
                }
                if (un3.getPlayers() != null) {
                    dwr.status = formatPlayerCount(un3.getPlayers().getNumPlayers(), un3.getPlayers().getMaxPlayers());
                    final List<Component> list4 = (List<Component>)Lists.newArrayList();
                    if (ArrayUtils.isNotEmpty((Object[])un3.getPlayers().getSample())) {
                        for (final GameProfile gameProfile8 : un3.getPlayers().getSample()) {
                            list4.add(new TextComponent(gameProfile8.getName()));
                        }
                        if (un3.getPlayers().getSample().length < un3.getPlayers().getNumPlayers()) {
                            list4.add(new TranslatableComponent("multiplayer.status.and_more", new Object[] { un3.getPlayers().getNumPlayers() - un3.getPlayers().getSample().length }));
                        }
                        dwr.playerList = list4;
                    }
                }
                else {
                    dwr.status = new TranslatableComponent("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY);
                }
                String string4 = null;
                if (un3.getFavicon() != null) {
                    final String string5 = un3.getFavicon();
                    if (string5.startsWith("data:image/png;base64,")) {
                        string4 = string5.substring("data:image/png;base64,".length());
                    }
                    else {
                        ServerStatusPinger.LOGGER.error("Invalid server icon (unknown format)");
                    }
                }
                if (!Objects.equals(string4, dwr.getIconB64())) {
                    dwr.setIconB64(string4);
                    runnable.run();
                }
                this.pingStart = Util.getMillis();
                nd5.send(new ServerboundPingRequestPacket(this.pingStart));
                this.success = true;
            }
            
            public void handlePongResponse(final ClientboundPongResponsePacket ul) {
                final long long3 = this.pingStart;
                final long long4 = Util.getMillis();
                dwr.ping = long4 - long3;
                nd5.disconnect(new TranslatableComponent("multiplayer.status.finished"));
            }
            
            public void onDisconnect(final Component nr) {
                if (!this.success) {
                    ServerStatusPinger.LOGGER.error("Can't ping {}: {}", dwr.ip, nr.getString());
                    dwr.motd = new TranslatableComponent("multiplayer.status.cannot_connect").withStyle(ChatFormatting.DARK_RED);
                    dwr.status = TextComponent.EMPTY;
                    ServerStatusPinger.this.pingLegacyServer(dwr);
                }
            }
            
            public Connection getConnection() {
                return nd5;
            }
        });
        try {
            nd5.send(new ClientIntentionPacket(dwq4.getHost(), dwq4.getPort(), ConnectionProtocol.STATUS));
            nd5.send(new ServerboundStatusRequestPacket());
        }
        catch (Throwable throwable6) {
            ServerStatusPinger.LOGGER.error(throwable6);
        }
    }
    
    private void pingLegacyServer(final ServerData dwr) {
        final ServerAddress dwq3 = ServerAddress.parseString(dwr.ip);
        ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap().group((EventLoopGroup)Connection.NETWORK_WORKER_GROUP.get())).handler((ChannelHandler)new ChannelInitializer<Channel>() {
            protected void initChannel(final Channel channel) throws Exception {
                try {
                    channel.config().setOption(ChannelOption.TCP_NODELAY, true);
                }
                catch (ChannelException ex) {}
                channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new SimpleChannelInboundHandler<ByteBuf>() {
                        public void channelActive(final ChannelHandlerContext channelHandlerContext) throws Exception {
                            super.channelActive(channelHandlerContext);
                            final ByteBuf byteBuf3 = Unpooled.buffer();
                            try {
                                byteBuf3.writeByte(254);
                                byteBuf3.writeByte(1);
                                byteBuf3.writeByte(250);
                                char[] arr4 = "MC|PingHost".toCharArray();
                                byteBuf3.writeShort(arr4.length);
                                for (final char character8 : arr4) {
                                    byteBuf3.writeChar((int)character8);
                                }
                                byteBuf3.writeShort(7 + 2 * dwq3.getHost().length());
                                byteBuf3.writeByte(127);
                                arr4 = dwq3.getHost().toCharArray();
                                byteBuf3.writeShort(arr4.length);
                                for (final char character8 : arr4) {
                                    byteBuf3.writeChar((int)character8);
                                }
                                byteBuf3.writeInt(dwq3.getPort());
                                channelHandlerContext.channel().writeAndFlush(byteBuf3).addListener((GenericFutureListener)ChannelFutureListener.CLOSE_ON_FAILURE);
                            }
                            finally {
                                byteBuf3.release();
                            }
                        }
                        
                        protected void channelRead0(final ChannelHandlerContext channelHandlerContext, final ByteBuf byteBuf) throws Exception {
                            final short short4 = byteBuf.readUnsignedByte();
                            if (short4 == 255) {
                                final String string5 = new String(byteBuf.readBytes(byteBuf.readShort() * 2).array(), StandardCharsets.UTF_16BE);
                                final String[] arr6 = (String[])Iterables.toArray(ServerStatusPinger.SPLITTER.split((CharSequence)string5), (Class)String.class);
                                if ("§1".equals(arr6[0])) {
                                    final int integer7 = Mth.getInt(arr6[1], 0);
                                    final String string6 = arr6[2];
                                    final String string7 = arr6[3];
                                    final int integer8 = Mth.getInt(arr6[4], -1);
                                    final int integer9 = Mth.getInt(arr6[5], -1);
                                    dwr.protocol = -1;
                                    dwr.version = new TextComponent(string6);
                                    dwr.motd = new TextComponent(string7);
                                    dwr.status = formatPlayerCount(integer8, integer9);
                                }
                            }
                            channelHandlerContext.close();
                        }
                        
                        public void exceptionCaught(final ChannelHandlerContext channelHandlerContext, final Throwable throwable) throws Exception {
                            channelHandlerContext.close();
                        }
                    } });
            }
        })).channel((Class)NioSocketChannel.class)).connect(dwq3.getHost(), dwq3.getPort());
    }
    
    private static Component formatPlayerCount(final int integer1, final int integer2) {
        return new TextComponent(Integer.toString(integer1)).append(new TextComponent("/").withStyle(ChatFormatting.DARK_GRAY)).append(Integer.toString(integer2)).withStyle(ChatFormatting.GRAY);
    }
    
    public void tick() {
        synchronized (this.connections) {
            final Iterator<Connection> iterator3 = (Iterator<Connection>)this.connections.iterator();
            while (iterator3.hasNext()) {
                final Connection nd4 = (Connection)iterator3.next();
                if (nd4.isConnected()) {
                    nd4.tick();
                }
                else {
                    iterator3.remove();
                    nd4.handleDisconnection();
                }
            }
        }
    }
    
    public void removeAll() {
        synchronized (this.connections) {
            final Iterator<Connection> iterator3 = (Iterator<Connection>)this.connections.iterator();
            while (iterator3.hasNext()) {
                final Connection nd4 = (Connection)iterator3.next();
                if (nd4.isConnected()) {
                    iterator3.remove();
                    nd4.disconnect(new TranslatableComponent("multiplayer.status.cancelled"));
                }
            }
        }
    }
    
    static {
        SPLITTER = Splitter.on('\0').limit(6);
        LOGGER = LogManager.getLogger();
    }
}
