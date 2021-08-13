package net.minecraft.realms;

import org.apache.logging.log4j.LogManager;
import java.net.UnknownHostException;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import java.net.InetAddress;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.client.gui.screens.Screen;
import org.apache.logging.log4j.Logger;

public class RealmsConnect {
    private static final Logger LOGGER;
    private final Screen onlineScreen;
    private volatile boolean aborted;
    private Connection connection;
    
    public RealmsConnect(final Screen doq) {
        this.onlineScreen = doq;
    }
    
    public void connect(final String string, final int integer) {
        final Minecraft djw4 = Minecraft.getInstance();
        djw4.setConnectedToRealms(true);
        NarrationHelper.now(I18n.get("mco.connect.success"));
        new Thread("Realms-connect-task") {
            public void run() {
                InetAddress inetAddress2 = null;
                try {
                    inetAddress2 = InetAddress.getByName(string);
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection = Connection.connectToServer(inetAddress2, integer, djw4.options.useNativeTransport());
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.setListener(new ClientHandshakePacketListenerImpl(RealmsConnect.this.connection, djw4, RealmsConnect.this.onlineScreen, (Consumer<Component>)(nr -> {})));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.send(new ClientIntentionPacket(string, integer, ConnectionProtocol.LOGIN));
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.this.connection.send(new ServerboundHelloPacket(djw4.getUser().getGameProfile()));
                }
                catch (UnknownHostException unknownHostException3) {
                    djw4.getClientPackSource().clearServerPack();
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.LOGGER.error("Couldn't connect to world", (Throwable)unknownHostException3);
                    final DisconnectedRealmsScreen eoa4 = new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, CommonComponents.CONNECT_FAILED, new TranslatableComponent("disconnect.genericReason", new Object[] { "Unknown host '" + string + "'" }));
                    djw4.execute(() -> djw.setScreen(eoa4));
                }
                catch (Exception exception3) {
                    djw4.getClientPackSource().clearServerPack();
                    if (RealmsConnect.this.aborted) {
                        return;
                    }
                    RealmsConnect.LOGGER.error("Couldn't connect to world", (Throwable)exception3);
                    String string4 = exception3.toString();
                    if (inetAddress2 != null) {
                        final String string5 = new StringBuilder().append(inetAddress2).append(":").append(integer).toString();
                        string4 = string4.replaceAll(string5, "");
                    }
                    final DisconnectedRealmsScreen eoa5 = new DisconnectedRealmsScreen(RealmsConnect.this.onlineScreen, CommonComponents.CONNECT_FAILED, new TranslatableComponent("disconnect.genericReason", new Object[] { string4 }));
                    djw4.execute(() -> djw.setScreen(eoa5));
                }
            }
        }.start();
    }
    
    public void abort() {
        this.aborted = true;
        if (this.connection != null && this.connection.isConnected()) {
            this.connection.disconnect(new TranslatableComponent("disconnect.genericReason"));
            this.connection.handleDisconnection();
        }
    }
    
    public void tick() {
        if (this.connection != null) {
            if (this.connection.isConnected()) {
                this.connection.tick();
            }
            else {
                this.connection.handleDisconnection();
            }
        }
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
