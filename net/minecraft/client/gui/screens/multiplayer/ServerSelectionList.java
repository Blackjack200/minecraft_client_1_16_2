package net.minecraft.client.gui.screens.multiplayer;

import java.net.UnknownHostException;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.AbstractTexture;
import org.apache.commons.lang3.Validate;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.Objects;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Collections;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.TextComponent;
import com.google.common.hash.Hashing;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.gui.Font;
import net.minecraft.Util;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import net.minecraft.DefaultUncaughtExceptionHandler;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import java.util.Iterator;
import net.minecraft.client.server.LanServer;
import net.minecraft.client.multiplayer.ServerList;
import java.util.function.Predicate;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.chat.NarratorChatListener;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.concurrent.ThreadPoolExecutor;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.components.ObjectSelectionList;

public class ServerSelectionList extends ObjectSelectionList<Entry> {
    private static final Logger LOGGER;
    private static final ThreadPoolExecutor THREAD_POOL;
    private static final ResourceLocation ICON_MISSING;
    private static final ResourceLocation ICON_OVERLAY_LOCATION;
    private static final Component SCANNING_LABEL;
    private static final Component CANT_RESOLVE_TEXT;
    private static final Component CANT_CONNECT_TEXT;
    private static final Component CLIENT_OUT_OF_DATE_TOOLTIP;
    private static final Component SERVER_OUT_OF_DATE_TOOLTIP;
    private static final Component NO_CONNECTION_TOOLTIP;
    private static final Component PINGING_TOOLTIP;
    private final JoinMultiplayerScreen screen;
    private final List<OnlineServerEntry> onlineServers;
    private final Entry lanHeader;
    private final List<NetworkServerEntry> networkServers;
    
    public ServerSelectionList(final JoinMultiplayerScreen dqz, final Minecraft djw, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7) {
        super(djw, integer3, integer4, integer5, integer6, integer7);
        this.onlineServers = (List<OnlineServerEntry>)Lists.newArrayList();
        this.lanHeader = new LANHeader();
        this.networkServers = (List<NetworkServerEntry>)Lists.newArrayList();
        this.screen = dqz;
    }
    
    private void refreshEntries() {
        this.clearEntries();
        this.onlineServers.forEach(this::addEntry);
        this.addEntry(this.lanHeader);
        this.networkServers.forEach(this::addEntry);
    }
    
    @Override
    public void setSelected(@Nullable final Entry a) {
        super.setSelected(a);
        if (this.getSelected() instanceof OnlineServerEntry) {
            NarratorChatListener.INSTANCE.sayNow(new TranslatableComponent("narrator.select", new Object[] { ((AbstractSelectionList<OnlineServerEntry>)this).getSelected().serverData.name }).getString());
        }
        this.screen.onSelectedChange();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        final Entry a5 = this.getSelected();
        return (a5 != null && a5.keyPressed(integer1, integer2, integer3)) || super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    protected void moveSelection(final SelectionDirection b) {
        this.moveSelection(b, (java.util.function.Predicate<Entry>)(a -> !(a instanceof LANHeader)));
    }
    
    public void updateOnlineServers(final ServerList dws) {
        this.onlineServers.clear();
        for (int integer3 = 0; integer3 < dws.size(); ++integer3) {
            this.onlineServers.add(new OnlineServerEntry(this.screen, dws.get(integer3)));
        }
        this.refreshEntries();
    }
    
    public void updateNetworkServers(final List<LanServer> list) {
        this.networkServers.clear();
        for (final LanServer emz4 : list) {
            this.networkServers.add(new NetworkServerEntry(this.screen, emz4));
        }
        this.refreshEntries();
    }
    
    @Override
    protected int getScrollbarPosition() {
        return super.getScrollbarPosition() + 30;
    }
    
    @Override
    public int getRowWidth() {
        return super.getRowWidth() + 85;
    }
    
    @Override
    protected boolean isFocused() {
        return this.screen.getFocused() == this;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        THREAD_POOL = (ThreadPoolExecutor)new ScheduledThreadPoolExecutor(5, new ThreadFactoryBuilder().setNameFormat("Server Pinger #%d").setDaemon(true).setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(ServerSelectionList.LOGGER)).build());
        ICON_MISSING = new ResourceLocation("textures/misc/unknown_server.png");
        ICON_OVERLAY_LOCATION = new ResourceLocation("textures/gui/server_selection.png");
        SCANNING_LABEL = new TranslatableComponent("lanServer.scanning");
        CANT_RESOLVE_TEXT = new TranslatableComponent("multiplayer.status.cannot_resolve").withStyle(ChatFormatting.DARK_RED);
        CANT_CONNECT_TEXT = new TranslatableComponent("multiplayer.status.cannot_connect").withStyle(ChatFormatting.DARK_RED);
        CLIENT_OUT_OF_DATE_TOOLTIP = new TranslatableComponent("multiplayer.status.client_out_of_date");
        SERVER_OUT_OF_DATE_TOOLTIP = new TranslatableComponent("multiplayer.status.server_out_of_date");
        NO_CONNECTION_TOOLTIP = new TranslatableComponent("multiplayer.status.no_connection");
        PINGING_TOOLTIP = new TranslatableComponent("multiplayer.status.pinging");
    }
    
    public abstract static class Entry extends ObjectSelectionList.Entry<Entry> {
    }
    
    public static class LANHeader extends Entry {
        private final Minecraft minecraft;
        
        public LANHeader() {
            this.minecraft = Minecraft.getInstance();
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            final int n = integer3 + integer6 / 2;
            this.minecraft.font.getClass();
            final int integer9 = n - 9 / 2;
            this.minecraft.font.draw(dfj, ServerSelectionList.SCANNING_LABEL, (float)(this.minecraft.screen.width / 2 - this.minecraft.font.width(ServerSelectionList.SCANNING_LABEL) / 2), (float)integer9, 16777215);
            String string13 = null;
            switch ((int)(Util.getMillis() / 300L % 4L)) {
                default: {
                    string13 = "O o o";
                    break;
                }
                case 1:
                case 3: {
                    string13 = "o O o";
                    break;
                }
                case 2: {
                    string13 = "o o O";
                    break;
                }
            }
            final Font font = this.minecraft.font;
            final String string14 = string13;
            final float float11 = (float)(this.minecraft.screen.width / 2 - this.minecraft.font.width(string13) / 2);
            final int n2 = integer9;
            this.minecraft.font.getClass();
            font.draw(dfj, string14, float11, (float)(n2 + 9), 8421504);
        }
    }
    
    public static class NetworkServerEntry extends Entry {
        private static final Component LAN_SERVER_HEADER;
        private static final Component HIDDEN_ADDRESS_TEXT;
        private final JoinMultiplayerScreen screen;
        protected final Minecraft minecraft;
        protected final LanServer serverData;
        private long lastClickTime;
        
        protected NetworkServerEntry(final JoinMultiplayerScreen dqz, final LanServer emz) {
            this.screen = dqz;
            this.serverData = emz;
            this.minecraft = Minecraft.getInstance();
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.minecraft.font.draw(dfj, NetworkServerEntry.LAN_SERVER_HEADER, (float)(integer4 + 32 + 3), (float)(integer3 + 1), 16777215);
            this.minecraft.font.draw(dfj, this.serverData.getMotd(), (float)(integer4 + 32 + 3), (float)(integer3 + 12), 8421504);
            if (this.minecraft.options.hideServerAddress) {
                this.minecraft.font.draw(dfj, NetworkServerEntry.HIDDEN_ADDRESS_TEXT, (float)(integer4 + 32 + 3), (float)(integer3 + 12 + 11), 3158064);
            }
            else {
                this.minecraft.font.draw(dfj, this.serverData.getAddress(), (float)(integer4 + 32 + 3), (float)(integer3 + 12 + 11), 3158064);
            }
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            this.screen.setSelected(this);
            if (Util.getMillis() - this.lastClickTime < 250L) {
                this.screen.joinSelectedServer();
            }
            this.lastClickTime = Util.getMillis();
            return false;
        }
        
        public LanServer getServerData() {
            return this.serverData;
        }
        
        static {
            LAN_SERVER_HEADER = new TranslatableComponent("lanServer.title");
            HIDDEN_ADDRESS_TEXT = new TranslatableComponent("selectServer.hiddenAddress");
        }
    }
    
    public class OnlineServerEntry extends Entry {
        private final JoinMultiplayerScreen screen;
        private final Minecraft minecraft;
        private final ServerData serverData;
        private final ResourceLocation iconLocation;
        private String lastIconB64;
        private DynamicTexture icon;
        private long lastClickTime;
        
        protected OnlineServerEntry(final JoinMultiplayerScreen dqz, final ServerData dwr) {
            this.screen = dqz;
            this.serverData = dwr;
            this.minecraft = Minecraft.getInstance();
            this.iconLocation = new ResourceLocation(new StringBuilder().append("servers/").append(Hashing.sha1().hashUnencodedChars((CharSequence)dwr.ip)).append("/icon").toString());
            this.icon = (DynamicTexture)this.minecraft.getTextureManager().getTexture(this.iconLocation);
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            if (!this.serverData.pinged) {
                this.serverData.pinged = true;
                this.serverData.ping = -2L;
                this.serverData.motd = TextComponent.EMPTY;
                this.serverData.status = TextComponent.EMPTY;
                ServerSelectionList.THREAD_POOL.submit(() -> {
                    try {
                        this.screen.getPinger().pingServer(this.serverData, () -> this.minecraft.execute(this::updateServerList));
                    }
                    catch (UnknownHostException unknownHostException2) {
                        this.serverData.ping = -1L;
                        this.serverData.motd = ServerSelectionList.CANT_RESOLVE_TEXT;
                    }
                    catch (Exception exception2) {
                        this.serverData.ping = -1L;
                        this.serverData.motd = ServerSelectionList.CANT_CONNECT_TEXT;
                    }
                });
            }
            final boolean boolean10 = this.serverData.protocol > SharedConstants.getCurrentVersion().getProtocolVersion();
            final boolean boolean11 = this.serverData.protocol < SharedConstants.getCurrentVersion().getProtocolVersion();
            final boolean boolean12 = boolean10 || boolean11;
            this.minecraft.font.draw(dfj, this.serverData.name, (float)(integer4 + 32 + 3), (float)(integer3 + 1), 16777215);
            final List<FormattedCharSequence> list15 = this.minecraft.font.split(this.serverData.motd, integer5 - 32 - 2);
            for (int integer9 = 0; integer9 < Math.min(list15.size(), 2); ++integer9) {
                final Font font = this.minecraft.font;
                final FormattedCharSequence aex = (FormattedCharSequence)list15.get(integer9);
                final float float11 = (float)(integer4 + 32 + 3);
                final int n = integer3 + 12;
                this.minecraft.font.getClass();
                font.draw(dfj, aex, float11, (float)(n + 9 * integer9), 8421504);
            }
            final Component nr16 = boolean12 ? this.serverData.version.copy().withStyle(ChatFormatting.DARK_RED) : this.serverData.status;
            final int integer10 = this.minecraft.font.width(nr16);
            this.minecraft.font.draw(dfj, nr16, (float)(integer4 + integer5 - integer10 - 15 - 2), (float)(integer3 + 1), 8421504);
            int integer11 = 0;
            int integer12;
            Component nr17;
            List<Component> list16;
            if (boolean12) {
                integer12 = 5;
                nr17 = (boolean10 ? ServerSelectionList.CLIENT_OUT_OF_DATE_TOOLTIP : ServerSelectionList.SERVER_OUT_OF_DATE_TOOLTIP);
                list16 = this.serverData.playerList;
            }
            else if (this.serverData.pinged && this.serverData.ping != -2L) {
                if (this.serverData.ping < 0L) {
                    integer12 = 5;
                }
                else if (this.serverData.ping < 150L) {
                    integer12 = 0;
                }
                else if (this.serverData.ping < 300L) {
                    integer12 = 1;
                }
                else if (this.serverData.ping < 600L) {
                    integer12 = 2;
                }
                else if (this.serverData.ping < 1000L) {
                    integer12 = 3;
                }
                else {
                    integer12 = 4;
                }
                if (this.serverData.ping < 0L) {
                    nr17 = ServerSelectionList.NO_CONNECTION_TOOLTIP;
                    list16 = (List<Component>)Collections.emptyList();
                }
                else {
                    nr17 = new TranslatableComponent("multiplayer.status.ping", new Object[] { this.serverData.ping });
                    list16 = this.serverData.playerList;
                }
            }
            else {
                integer11 = 1;
                integer12 = (int)(Util.getMillis() / 100L + integer2 * 2 & 0x7L);
                if (integer12 > 4) {
                    integer12 = 8 - integer12;
                }
                nr17 = ServerSelectionList.PINGING_TOOLTIP;
                list16 = (List<Component>)Collections.emptyList();
            }
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            this.minecraft.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
            GuiComponent.blit(dfj, integer4 + integer5 - 15, integer3, (float)(integer11 * 10), (float)(176 + integer12 * 8), 10, 8, 256, 256);
            final String string22 = this.serverData.getIconB64();
            if (!Objects.equals(string22, this.lastIconB64)) {
                if (this.uploadServerIcon(string22)) {
                    this.lastIconB64 = string22;
                }
                else {
                    this.serverData.setIconB64(null);
                    this.updateServerList();
                }
            }
            if (this.icon != null) {
                this.drawIcon(dfj, integer4, integer3, this.iconLocation);
            }
            else {
                this.drawIcon(dfj, integer4, integer3, ServerSelectionList.ICON_MISSING);
            }
            final int integer13 = integer7 - integer4;
            final int integer14 = integer8 - integer3;
            if (integer13 >= integer5 - 15 && integer13 <= integer5 - 5 && integer14 >= 0 && integer14 <= 8) {
                this.screen.setToolTip((List<Component>)Collections.singletonList(nr17));
            }
            else if (integer13 >= integer5 - integer10 - 15 - 2 && integer13 <= integer5 - 15 - 2 && integer14 >= 0 && integer14 <= 8) {
                this.screen.setToolTip(list16);
            }
            if (this.minecraft.options.touchscreen || boolean9) {
                this.minecraft.getTextureManager().bind(ServerSelectionList.ICON_OVERLAY_LOCATION);
                GuiComponent.fill(dfj, integer4, integer3, integer4 + 32, integer3 + 32, -1601138544);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                final int integer15 = integer7 - integer4;
                final int integer16 = integer8 - integer3;
                if (this.canJoin()) {
                    if (integer15 < 32 && integer15 > 16) {
                        GuiComponent.blit(dfj, integer4, integer3, 0.0f, 32.0f, 32, 32, 256, 256);
                    }
                    else {
                        GuiComponent.blit(dfj, integer4, integer3, 0.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
                if (integer2 > 0) {
                    if (integer15 < 16 && integer16 < 16) {
                        GuiComponent.blit(dfj, integer4, integer3, 96.0f, 32.0f, 32, 32, 256, 256);
                    }
                    else {
                        GuiComponent.blit(dfj, integer4, integer3, 96.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
                if (integer2 < this.screen.getServers().size() - 1) {
                    if (integer15 < 16 && integer16 > 16) {
                        GuiComponent.blit(dfj, integer4, integer3, 64.0f, 32.0f, 32, 32, 256, 256);
                    }
                    else {
                        GuiComponent.blit(dfj, integer4, integer3, 64.0f, 0.0f, 32, 32, 256, 256);
                    }
                }
            }
        }
        
        public void updateServerList() {
            this.screen.getServers().save();
        }
        
        protected void drawIcon(final PoseStack dfj, final int integer2, final int integer3, final ResourceLocation vk) {
            this.minecraft.getTextureManager().bind(vk);
            RenderSystem.enableBlend();
            GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 32, 32, 32, 32);
            RenderSystem.disableBlend();
        }
        
        private boolean canJoin() {
            return true;
        }
        
        private boolean uploadServerIcon(@Nullable final String string) {
            if (string == null) {
                this.minecraft.getTextureManager().release(this.iconLocation);
                if (this.icon != null && this.icon.getPixels() != null) {
                    this.icon.getPixels().close();
                }
                this.icon = null;
            }
            else {
                try {
                    final NativeImage deq3 = NativeImage.fromBase64(string);
                    Validate.validState(deq3.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(deq3.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    if (this.icon == null) {
                        this.icon = new DynamicTexture(deq3);
                    }
                    else {
                        this.icon.setPixels(deq3);
                        this.icon.upload();
                    }
                    this.minecraft.getTextureManager().register(this.iconLocation, this.icon);
                }
                catch (Throwable throwable3) {
                    ServerSelectionList.LOGGER.error("Invalid icon for server {} ({})", this.serverData.name, this.serverData.ip, throwable3);
                    return false;
                }
            }
            return true;
        }
        
        public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
            if (Screen.hasShiftDown()) {
                final ServerSelectionList drb5 = this.screen.serverSelectionList;
                final int integer4 = drb5.children().indexOf(this);
                if ((integer1 == 264 && integer4 < this.screen.getServers().size() - 1) || (integer1 == 265 && integer4 > 0)) {
                    this.swap(integer4, (integer1 == 264) ? (integer4 + 1) : (integer4 - 1));
                    return true;
                }
            }
            return super.keyPressed(integer1, integer2, integer3);
        }
        
        private void swap(final int integer1, final int integer2) {
            this.screen.getServers().swap(integer1, integer2);
            this.screen.serverSelectionList.updateOnlineServers(this.screen.getServers());
            final Entry a4 = (Entry)this.screen.serverSelectionList.children().get(integer2);
            this.screen.serverSelectionList.setSelected(a4);
            AbstractSelectionList.this.ensureVisible(a4);
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            final double double3 = double1 - AbstractSelectionList.this.getRowLeft();
            final double double4 = double2 - AbstractSelectionList.this.getRowTop(ServerSelectionList.this.children().indexOf(this));
            if (double3 <= 32.0) {
                if (double3 < 32.0 && double3 > 16.0 && this.canJoin()) {
                    this.screen.setSelected(this);
                    this.screen.joinSelectedServer();
                    return true;
                }
                final int integer2 = this.screen.serverSelectionList.children().indexOf(this);
                if (double3 < 16.0 && double4 < 16.0 && integer2 > 0) {
                    this.swap(integer2, integer2 - 1);
                    return true;
                }
                if (double3 < 16.0 && double4 > 16.0 && integer2 < this.screen.getServers().size() - 1) {
                    this.swap(integer2, integer2 + 1);
                    return true;
                }
            }
            this.screen.setSelected(this);
            if (Util.getMillis() - this.lastClickTime < 250L) {
                this.screen.joinSelectedServer();
            }
            this.lastClickTime = Util.getMillis();
            return false;
        }
        
        public ServerData getServerData() {
            return this.serverData;
        }
    }
}
