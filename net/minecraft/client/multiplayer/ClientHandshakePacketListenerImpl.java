package net.minecraft.client.multiplayer;

import org.apache.logging.log4j.LogManager;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.login.ServerboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.client.gui.screens.DisconnectedScreen;
import net.minecraft.realms.DisconnectedRealmsScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.realms.RealmsScreen;
import net.minecraft.network.PacketListener;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.login.ClientboundGameProfilePacket;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.exceptions.InvalidCredentialsException;
import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
import java.security.PublicKey;
import javax.crypto.SecretKey;
import net.minecraft.util.HttpUtil;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.login.ServerboundKeyPacket;
import java.math.BigInteger;
import net.minecraft.util.Crypt;
import net.minecraft.network.protocol.login.ClientboundHelloPacket;
import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import net.minecraft.network.protocol.login.ClientLoginPacketListener;

public class ClientHandshakePacketListenerImpl implements ClientLoginPacketListener {
    private static final Logger LOGGER;
    private final Minecraft minecraft;
    @Nullable
    private final Screen parent;
    private final Consumer<Component> updateStatus;
    private final Connection connection;
    private GameProfile localGameProfile;
    
    public ClientHandshakePacketListenerImpl(final Connection nd, final Minecraft djw, @Nullable final Screen doq, final Consumer<Component> consumer) {
        this.connection = nd;
        this.minecraft = djw;
        this.parent = doq;
        this.updateStatus = consumer;
    }
    
    public void handleHello(final ClientboundHelloPacket ub) {
        final SecretKey secretKey3 = Crypt.generateSecretKey();
        final PublicKey publicKey4 = ub.getPublicKey();
        final String string5 = new BigInteger(Crypt.digestData(ub.getServerId(), publicKey4, secretKey3)).toString(16);
        final ServerboundKeyPacket uh6 = new ServerboundKeyPacket(secretKey3, publicKey4, ub.getNonce());
        this.updateStatus.accept(new TranslatableComponent("connect.authorizing"));
        HttpUtil.DOWNLOAD_EXECUTOR.submit(() -> {
            final Component nr5 = this.authenticateServer(string5);
            if (nr5 != null) {
                if (this.minecraft.getCurrentServer() == null || !this.minecraft.getCurrentServer().isLan()) {
                    this.connection.disconnect(nr5);
                    return;
                }
                ClientHandshakePacketListenerImpl.LOGGER.warn(nr5.getString());
            }
            this.updateStatus.accept(new TranslatableComponent("connect.encrypting"));
            this.connection.send(uh6, (future -> this.connection.setEncryptionKey(secretKey3)));
        });
    }
    
    @Nullable
    private Component authenticateServer(final String string) {
        try {
            this.getMinecraftSessionService().joinServer(this.minecraft.getUser().getGameProfile(), this.minecraft.getUser().getAccessToken(), string);
        }
        catch (AuthenticationUnavailableException authenticationUnavailableException3) {
            return new TranslatableComponent("disconnect.loginFailedInfo", new Object[] { new TranslatableComponent("disconnect.loginFailedInfo.serversUnavailable") });
        }
        catch (InvalidCredentialsException invalidCredentialsException3) {
            return new TranslatableComponent("disconnect.loginFailedInfo", new Object[] { new TranslatableComponent("disconnect.loginFailedInfo.invalidSession") });
        }
        catch (AuthenticationException authenticationException3) {
            return new TranslatableComponent("disconnect.loginFailedInfo", new Object[] { authenticationException3.getMessage() });
        }
        return null;
    }
    
    private MinecraftSessionService getMinecraftSessionService() {
        return this.minecraft.getMinecraftSessionService();
    }
    
    public void handleGameProfile(final ClientboundGameProfilePacket ua) {
        this.updateStatus.accept(new TranslatableComponent("connect.joining"));
        this.localGameProfile = ua.getGameProfile();
        this.connection.setProtocol(ConnectionProtocol.PLAY);
        this.connection.setListener(new ClientPacketListener(this.minecraft, this.parent, this.connection, this.localGameProfile));
    }
    
    public void onDisconnect(final Component nr) {
        if (this.parent != null && this.parent instanceof RealmsScreen) {
            this.minecraft.setScreen(new DisconnectedRealmsScreen(this.parent, CommonComponents.CONNECT_FAILED, nr));
        }
        else {
            this.minecraft.setScreen(new DisconnectedScreen(this.parent, CommonComponents.CONNECT_FAILED, nr));
        }
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    public void handleDisconnect(final ClientboundLoginDisconnectPacket ud) {
        this.connection.disconnect(ud.getReason());
    }
    
    public void handleCompression(final ClientboundLoginCompressionPacket uc) {
        if (!this.connection.isMemoryConnection()) {
            this.connection.setupCompression(uc.getCompressionThreshold());
        }
    }
    
    public void handleCustomQuery(final ClientboundCustomQueryPacket tz) {
        this.updateStatus.accept(new TranslatableComponent("connect.negotiating"));
        this.connection.send(new ServerboundCustomQueryPacket(tz.getTransactionId(), null));
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
