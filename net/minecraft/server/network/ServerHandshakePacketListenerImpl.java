package net.minecraft.server.network;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.SharedConstants;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.handshake.ServerHandshakePacketListener;

public class ServerHandshakePacketListenerImpl implements ServerHandshakePacketListener {
    private static final Component IGNORE_STATUS_REASON;
    private final MinecraftServer server;
    private final Connection connection;
    
    public ServerHandshakePacketListenerImpl(final MinecraftServer minecraftServer, final Connection nd) {
        this.server = minecraftServer;
        this.connection = nd;
    }
    
    public void handleIntention(final ClientIntentionPacket tv) {
        switch (tv.getIntention()) {
            case LOGIN: {
                this.connection.setProtocol(ConnectionProtocol.LOGIN);
                if (tv.getProtocolVersion() > SharedConstants.getCurrentVersion().getProtocolVersion()) {
                    final Component nr3 = new TranslatableComponent("multiplayer.disconnect.outdated_server", new Object[] { SharedConstants.getCurrentVersion().getName() });
                    this.connection.send(new ClientboundLoginDisconnectPacket(nr3));
                    this.connection.disconnect(nr3);
                    break;
                }
                if (tv.getProtocolVersion() < SharedConstants.getCurrentVersion().getProtocolVersion()) {
                    final Component nr3 = new TranslatableComponent("multiplayer.disconnect.outdated_client", new Object[] { SharedConstants.getCurrentVersion().getName() });
                    this.connection.send(new ClientboundLoginDisconnectPacket(nr3));
                    this.connection.disconnect(nr3);
                    break;
                }
                this.connection.setListener(new ServerLoginPacketListenerImpl(this.server, this.connection));
                break;
            }
            case STATUS: {
                if (this.server.repliesToStatus()) {
                    this.connection.setProtocol(ConnectionProtocol.STATUS);
                    this.connection.setListener(new ServerStatusPacketListenerImpl(this.server, this.connection));
                    break;
                }
                this.connection.disconnect(ServerHandshakePacketListenerImpl.IGNORE_STATUS_REASON);
                break;
            }
            default: {
                throw new UnsupportedOperationException(new StringBuilder().append("Invalid intention ").append(tv.getIntention()).toString());
            }
        }
    }
    
    public void onDisconnect(final Component nr) {
    }
    
    public Connection getConnection() {
        return this.connection;
    }
    
    static {
        IGNORE_STATUS_REASON = new TextComponent("Ignoring status request");
    }
}
