package net.minecraft.server.network;

import java.util.function.Supplier;
import org.apache.logging.log4j.LogManager;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.network.chat.Component;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundDisconnectPacket;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ReportedException;
import net.minecraft.CrashReport;
import java.util.Iterator;
import io.netty.channel.local.LocalAddress;
import io.netty.channel.local.LocalServerChannel;
import java.net.SocketAddress;
import java.io.IOException;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.EventLoopGroup;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RateKickingConnection;
import net.minecraft.network.PacketEncoder;
import net.minecraft.network.Varint21LengthFieldPrepender;
import net.minecraft.network.PacketDecoder;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.Varint21FrameDecoder;
import io.netty.channel.ChannelHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.channel.ChannelException;
import io.netty.channel.ChannelOption;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.epoll.Epoll;
import javax.annotation.Nullable;
import java.net.InetAddress;
import java.util.Collections;
import com.google.common.collect.Lists;
import net.minecraft.network.Connection;
import io.netty.channel.ChannelFuture;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import net.minecraft.util.LazyLoadedValue;
import org.apache.logging.log4j.Logger;

public class ServerConnectionListener {
    private static final Logger LOGGER;
    public static final LazyLoadedValue<NioEventLoopGroup> SERVER_EVENT_GROUP;
    public static final LazyLoadedValue<EpollEventLoopGroup> SERVER_EPOLL_EVENT_GROUP;
    private final MinecraftServer server;
    public volatile boolean running;
    private final List<ChannelFuture> channels;
    private final List<Connection> connections;
    
    public ServerConnectionListener(final MinecraftServer minecraftServer) {
        this.channels = (List<ChannelFuture>)Collections.synchronizedList((List)Lists.newArrayList());
        this.connections = (List<Connection>)Collections.synchronizedList((List)Lists.newArrayList());
        this.server = minecraftServer;
        this.running = true;
    }
    
    public void startTcpServerListener(@Nullable final InetAddress inetAddress, final int integer) throws IOException {
        synchronized (this.channels) {
            Class<? extends ServerSocketChannel> class5;
            LazyLoadedValue<? extends EventLoopGroup> aff6;
            if (Epoll.isAvailable() && this.server.isEpollEnabled()) {
                class5 = EpollServerSocketChannel.class;
                aff6 = ServerConnectionListener.SERVER_EPOLL_EVENT_GROUP;
                ServerConnectionListener.LOGGER.info("Using epoll channel type");
            }
            else {
                class5 = NioServerSocketChannel.class;
                aff6 = ServerConnectionListener.SERVER_EVENT_GROUP;
                ServerConnectionListener.LOGGER.info("Using default channel type");
            }
            this.channels.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)class5)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
                protected void initChannel(final Channel channel) throws Exception {
                    try {
                        channel.config().setOption(ChannelOption.TCP_NODELAY, (Object)true);
                    }
                    catch (ChannelException ex) {}
                    channel.pipeline().addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30)).addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler(ServerConnectionListener.this)).addLast("splitter", (ChannelHandler)new Varint21FrameDecoder()).addLast("decoder", (ChannelHandler)new PacketDecoder(PacketFlow.SERVERBOUND)).addLast("prepender", (ChannelHandler)new Varint21LengthFieldPrepender()).addLast("encoder", (ChannelHandler)new PacketEncoder(PacketFlow.CLIENTBOUND));
                    final int integer3 = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
                    final Connection nd4 = (integer3 > 0) ? new RateKickingConnection(integer3) : new Connection(PacketFlow.SERVERBOUND);
                    ServerConnectionListener.this.connections.add((Object)nd4);
                    channel.pipeline().addLast("packet_handler", (ChannelHandler)nd4);
                    nd4.setListener(new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, nd4));
                }
            }).group((EventLoopGroup)aff6.get()).localAddress(inetAddress, integer)).bind().syncUninterruptibly());
        }
    }
    
    public SocketAddress startMemoryChannel() {
        final ChannelFuture channelFuture2;
        synchronized (this.channels) {
            channelFuture2 = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel((Class)LocalServerChannel.class)).childHandler((ChannelHandler)new ChannelInitializer<Channel>() {
                protected void initChannel(final Channel channel) throws Exception {
                    final Connection nd3 = new Connection(PacketFlow.SERVERBOUND);
                    nd3.setListener(new MemoryServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, nd3));
                    ServerConnectionListener.this.connections.add(nd3);
                    channel.pipeline().addLast("packet_handler", (ChannelHandler)nd3);
                }
            }).group((EventLoopGroup)ServerConnectionListener.SERVER_EVENT_GROUP.get()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
            this.channels.add(channelFuture2);
        }
        return channelFuture2.channel().localAddress();
    }
    
    public void stop() {
        this.running = false;
        for (final ChannelFuture channelFuture3 : this.channels) {
            try {
                channelFuture3.channel().close().sync();
            }
            catch (InterruptedException interruptedException4) {
                ServerConnectionListener.LOGGER.error("Interrupted whilst closing channel");
            }
        }
    }
    
    public void tick() {
        synchronized (this.connections) {
            final Iterator<Connection> iterator3 = (Iterator<Connection>)this.connections.iterator();
            while (iterator3.hasNext()) {
                final Connection nd4 = (Connection)iterator3.next();
                if (nd4.isConnecting()) {
                    continue;
                }
                if (nd4.isConnected()) {
                    try {
                        nd4.tick();
                    }
                    catch (Exception exception5) {
                        if (nd4.isMemoryConnection()) {
                            throw new ReportedException(CrashReport.forThrowable((Throwable)exception5, "Ticking memory connection"));
                        }
                        ServerConnectionListener.LOGGER.warn("Failed to handle packet for {}", nd4.getRemoteAddress(), exception5);
                        final Component nr6 = new TextComponent("Internal server error");
                        nd4.send(new ClientboundDisconnectPacket(nr6), (future -> nd4.disconnect(nr6)));
                        nd4.setReadOnly();
                    }
                }
                else {
                    iterator3.remove();
                    nd4.handleDisconnection();
                }
            }
        }
    }
    
    public MinecraftServer getServer() {
        return this.server;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SERVER_EVENT_GROUP = new LazyLoadedValue<NioEventLoopGroup>((java.util.function.Supplier<NioEventLoopGroup>)(() -> new NioEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Server IO #%d").setDaemon(true).build())));
        SERVER_EPOLL_EVENT_GROUP = new LazyLoadedValue<EpollEventLoopGroup>((java.util.function.Supplier<EpollEventLoopGroup>)(() -> new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Netty Epoll Server IO #%d").setDaemon(true).build())));
    }
}
