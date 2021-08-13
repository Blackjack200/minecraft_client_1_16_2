package net.minecraft.client.gui.screens;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.Util;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.network.chat.CommonComponents;
import java.net.UnknownHostException;
import net.minecraft.network.protocol.login.ServerboundHelloPacket;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.network.ConnectionProtocol;
import net.minecraft.network.PacketListener;
import java.util.function.Consumer;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import java.net.InetAddress;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.Connection;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectScreen extends Screen {
    private static final AtomicInteger UNIQUE_THREAD_ID;
    private static final Logger LOGGER;
    private Connection connection;
    private boolean aborted;
    private final Screen parent;
    private Component status;
    private long lastNarration;
    
    public ConnectScreen(final Screen doq, final Minecraft djw, final ServerData dwr) {
        super(NarratorChatListener.NO_TITLE);
        this.status = new TranslatableComponent("connect.connecting");
        this.lastNarration = -1L;
        this.minecraft = djw;
        this.parent = doq;
        final ServerAddress dwq5 = ServerAddress.parseString(dwr.ip);
        djw.clearLevel();
        djw.setCurrentServer(dwr);
        this.connect(dwq5.getHost(), dwq5.getPort());
    }
    
    public ConnectScreen(final Screen doq, final Minecraft djw, final String string, final int integer) {
        super(NarratorChatListener.NO_TITLE);
        this.status = new TranslatableComponent("connect.connecting");
        this.lastNarration = -1L;
        this.minecraft = djw;
        this.parent = doq;
        djw.clearLevel();
        this.connect(string, integer);
    }
    
    private void connect(final String string, final int integer) {
        ConnectScreen.LOGGER.info("Connecting to {}, {}", string, integer);
        final Thread thread4 = new Thread(new StringBuilder().append("Server Connector #").append(ConnectScreen.UNIQUE_THREAD_ID.incrementAndGet()).toString()) {
            public void run() {
                InetAddress inetAddress2 = null;
                try {
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    inetAddress2 = InetAddress.getByName(string);
                    ConnectScreen.this.connection = Connection.connectToServer(inetAddress2, integer, ConnectScreen.this.minecraft.options.useNativeTransport());
                    ConnectScreen.this.connection.setListener(new ClientHandshakePacketListenerImpl(ConnectScreen.this.connection, ConnectScreen.this.minecraft, ConnectScreen.this.parent, (Consumer<Component>)(nr -> dnq.updateStatus(nr))));
                    ConnectScreen.this.connection.send(new ClientIntentionPacket(string, integer, ConnectionProtocol.LOGIN));
                    ConnectScreen.this.connection.send(new ServerboundHelloPacket(ConnectScreen.this.minecraft.getUser().getGameProfile()));
                }
                catch (UnknownHostException unknownHostException3) {
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    ConnectScreen.LOGGER.error("Couldn't connect to server", (Throwable)unknownHostException3);
                    ConnectScreen.this.minecraft.execute(() -> ConnectScreen.this.minecraft.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, CommonComponents.CONNECT_FAILED, new TranslatableComponent("disconnect.genericReason", new Object[] { "Unknown host" }))));
                }
                catch (Exception exception3) {
                    if (ConnectScreen.this.aborted) {
                        return;
                    }
                    ConnectScreen.LOGGER.error("Couldn't connect to server", (Throwable)exception3);
                    final String string4 = (inetAddress2 == null) ? exception3.toString() : exception3.toString().replaceAll(new StringBuilder().append(inetAddress2).append(":").append(integer).toString(), "");
                    ConnectScreen.this.minecraft.execute(() -> ConnectScreen.this.minecraft.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, CommonComponents.CONNECT_FAILED, new TranslatableComponent("disconnect.genericReason", new Object[] { string4 }))));
                }
            }
        };
        thread4.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(ConnectScreen.LOGGER));
        thread4.start();
    }
    
    private void updateStatus(final Component nr) {
        this.status = nr;
    }
    
    @Override
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
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    protected void init() {
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20, CommonComponents.GUI_CANCEL, dlg -> {
            this.aborted = true;
            if (this.connection != null) {
                this.connection.disconnect(new TranslatableComponent("connect.aborted"));
            }
            this.minecraft.setScreen(this.parent);
        }));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final long long6 = Util.getMillis();
        if (long6 - this.lastNarration > 2000L) {
            this.lastNarration = long6;
            NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.joining").getString());
        }
        GuiComponent.drawCenteredString(dfj, this.font, this.status, this.width / 2, this.height / 2 - 50, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        UNIQUE_THREAD_ID = new AtomicInteger(0);
        LOGGER = LogManager.getLogger();
    }
}
