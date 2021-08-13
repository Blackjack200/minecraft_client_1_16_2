package net.minecraft.server.network;

import org.apache.logging.log4j.LogManager;
import io.netty.channel.ChannelFuture;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import java.security.PrivateKey;
import net.minecraft.DefaultUncaughtExceptionHandler;
import javax.annotation.Nullable;
import java.net.SocketAddress;
import java.net.InetSocketAddress;
import java.net.InetAddress;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.util.UUID;
import java.math.BigInteger;
import net.minecraft.util.Crypt;
import java.util.Arrays;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import org.apache.commons.lang3.Validate;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import javax.crypto.SecretKey;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.network.protocol.login.ServerLoginPacketListener;

public class ServerLoginPacketListenerImpl implements ServerLoginPacketListener {
    private static final AtomicInteger UNIQUE_THREAD_ID;
    private static final Logger LOGGER;
    private static final Random RANDOM;
    private final byte[] nonce;
    private final MinecraftServer server;
    public final Connection connection;
    private State state;
    private int tick;
    private GameProfile gameProfile;
    private final String serverId = "";
    private SecretKey secretKey;
    private ServerPlayer delayedAcceptPlayer;
    
    public ServerLoginPacketListenerImpl(final MinecraftServer minecraftServer, final Connection nd) {
        this.nonce = new byte[4];
        this.state = State.HELLO;
        this.server = minecraftServer;
        this.connection = nd;
        ServerLoginPacketListenerImpl.RANDOM.nextBytes(this.nonce);
    }
    
    public void tick() {
        if (this.state == State.READY_TO_ACCEPT) {
            this.handleAcceptedLogin();
        }
        else if (this.state == State.DELAY_ACCEPT) {
            final ServerPlayer aah2 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
            if (aah2 == null) {
                this.state = State.READY_TO_ACCEPT;
                this.server.getPlayerList().placeNewPlayer(this.connection, this.delayedAcceptPlayer);
                this.delayedAcceptPlayer = null;
            }
        }
        if (this.tick++ == 600) {
            this.disconnect(new TranslatableComponent("multiplayer.disconnect.slow_login"));
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public void disconnect(final Component nr) {
        try {
            ServerLoginPacketListenerImpl.LOGGER.info("Disconnecting {}: {}", this.getUserName(), nr.getString());
            this.connection.send(new ClientboundLoginDisconnectPacket(nr));
            this.connection.disconnect(nr);
        }
        catch (Exception exception3) {
            ServerLoginPacketListenerImpl.LOGGER.error("Error whilst disconnecting player", (Throwable)exception3);
        }
    }
    
    public void handleAcceptedLogin() {
        if (!this.gameProfile.isComplete()) {
            this.gameProfile = this.createFakeProfile(this.gameProfile);
        }
        final Component nr2 = this.server.getPlayerList().canPlayerLogin(this.connection.getRemoteAddress(), this.gameProfile);
        if (nr2 != null) {
            this.disconnect(nr2);
        }
        else {
            this.state = State.ACCEPTED;
            if (this.server.getCompressionThreshold() >= 0 && !this.connection.isMemoryConnection()) {
                this.connection.send(new ClientboundLoginCompressionPacket(this.server.getCompressionThreshold()), (channelFuture -> this.connection.setupCompression(this.server.getCompressionThreshold())));
            }
            this.connection.send(new ClientboundGameProfilePacket(this.gameProfile));
            final ServerPlayer aah3 = this.server.getPlayerList().getPlayer(this.gameProfile.getId());
            if (aah3 != null) {
                this.state = State.DELAY_ACCEPT;
                this.delayedAcceptPlayer = this.server.getPlayerList().getPlayerForLogin(this.gameProfile);
            }
            else {
                this.server.getPlayerList().placeNewPlayer(this.connection, this.server.getPlayerList().getPlayerForLogin(this.gameProfile));
            }
        }
    }
    
    public void onDisconnect(final Component nr) {
        ServerLoginPacketListenerImpl.LOGGER.info("{} lost connection: {}", this.getUserName(), nr.getString());
    }
    
    public String getUserName() {
        if (this.gameProfile != null) {
            return new StringBuilder().append(this.gameProfile).append(" (").append(this.connection.getRemoteAddress()).append(")").toString();
        }
        return String.valueOf(this.connection.getRemoteAddress());
    }
    
    public void handleHello(final ServerboundHelloPacket ug) {
        Validate.validState(this.state == State.HELLO, "Unexpected hello packet", new Object[0]);
        this.gameProfile = ug.getGameProfile();
        if (this.server.usesAuthentication() && !this.connection.isMemoryConnection()) {
            this.state = State.KEY;
            this.connection.send(new ClientboundHelloPacket("", this.server.getKeyPair().getPublic(), this.nonce));
        }
        else {
            this.state = State.READY_TO_ACCEPT;
        }
    }
    
    public void handleKey(final ServerboundKeyPacket uh) {
        Validate.validState(this.state == State.KEY, "Unexpected key packet", new Object[0]);
        final PrivateKey privateKey3 = this.server.getKeyPair().getPrivate();
        if (!Arrays.equals(this.nonce, uh.getNonce(privateKey3))) {
            throw new IllegalStateException("Invalid nonce!");
        }
        this.secretKey = uh.getSecretKey(privateKey3);
        this.state = State.AUTHENTICATING;
        this.connection.setEncryptionKey(this.secretKey);
        final Thread thread4 = new Thread(new StringBuilder().append("User Authenticator #").append(ServerLoginPacketListenerImpl.UNIQUE_THREAD_ID.incrementAndGet()).toString()) {
            public void run() {
                final GameProfile gameProfile2 = ServerLoginPacketListenerImpl.this.gameProfile;
                try {
                    final String string3 = new BigInteger(Crypt.digestData("", ServerLoginPacketListenerImpl.this.server.getKeyPair().getPublic(), ServerLoginPacketListenerImpl.this.secretKey)).toString(16);
                    ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.server.getSessionService().hasJoinedServer(new GameProfile((UUID)null, gameProfile2.getName()), string3, this.getAddress());
                    if (ServerLoginPacketListenerImpl.this.gameProfile != null) {
                        ServerLoginPacketListenerImpl.LOGGER.info("UUID of player {} is {}", ServerLoginPacketListenerImpl.this.gameProfile.getName(), ServerLoginPacketListenerImpl.this.gameProfile.getId());
                        ServerLoginPacketListenerImpl.this.state = State.READY_TO_ACCEPT;
                    }
                    else if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
                        ServerLoginPacketListenerImpl.LOGGER.warn("Failed to verify username but will let them in anyway!");
                        ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.createFakeProfile(gameProfile2);
                        ServerLoginPacketListenerImpl.this.state = State.READY_TO_ACCEPT;
                    }
                    else {
                        ServerLoginPacketListenerImpl.this.disconnect(new TranslatableComponent("multiplayer.disconnect.unverified_username"));
                        ServerLoginPacketListenerImpl.LOGGER.error("Username '{}' tried to join with an invalid session", gameProfile2.getName());
                    }
                }
                catch (AuthenticationUnavailableException authenticationUnavailableException3) {
                    if (ServerLoginPacketListenerImpl.this.server.isSingleplayer()) {
                        ServerLoginPacketListenerImpl.LOGGER.warn("Authentication servers are down but will let them in anyway!");
                        ServerLoginPacketListenerImpl.this.gameProfile = ServerLoginPacketListenerImpl.this.createFakeProfile(gameProfile2);
                        ServerLoginPacketListenerImpl.this.state = State.READY_TO_ACCEPT;
                    }
                    else {
                        ServerLoginPacketListenerImpl.this.disconnect(new TranslatableComponent("multiplayer.disconnect.authservers_down"));
                        ServerLoginPacketListenerImpl.LOGGER.error("Couldn't verify username because servers are unavailable");
                    }
                }
            }
            
            @Nullable
            private InetAddress getAddress() {
                final SocketAddress socketAddress2 = ServerLoginPacketListenerImpl.this.connection.getRemoteAddress();
                return (ServerLoginPacketListenerImpl.this.server.getPreventProxyConnections() && socketAddress2 instanceof InetSocketAddress) ? ((InetSocketAddress)socketAddress2).getAddress() : null;
            }
        };
        thread4.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(ServerLoginPacketListenerImpl.LOGGER));
        thread4.start();
    }
    
    public void handleCustomQueryPacket(final ServerboundCustomQueryPacket uf) {
        this.disconnect(new TranslatableComponent("multiplayer.disconnect.unexpected_query_response"));
    }
    
    protected GameProfile createFakeProfile(final GameProfile gameProfile) {
        final UUID uUID3 = Player.createPlayerUUID(gameProfile.getName());
        return new GameProfile(uUID3, gameProfile.getName());
    }
    
    static {
        UNIQUE_THREAD_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
        RANDOM = new Random();
    }
    
    enum State {
        HELLO, 
        KEY, 
        AUTHENTICATING, 
        NEGOTIATING, 
        READY_TO_ACCEPT, 
        DELAY_ACCEPT, 
        ACCEPTED;
    }
}
