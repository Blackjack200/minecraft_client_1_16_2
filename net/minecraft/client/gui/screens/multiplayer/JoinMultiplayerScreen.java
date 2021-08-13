package net.minecraft.client.gui.screens.multiplayer;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.server.LanServer;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.EditServerScreen;
import net.minecraft.client.gui.screens.DirectJoinServerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.server.LanServerDetection;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import java.util.List;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ServerList;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import org.apache.logging.log4j.Logger;
import net.minecraft.client.gui.screens.Screen;

public class JoinMultiplayerScreen extends Screen {
    private static final Logger LOGGER;
    private final ServerStatusPinger pinger;
    private final Screen lastScreen;
    protected ServerSelectionList serverSelectionList;
    private ServerList servers;
    private Button editButton;
    private Button selectButton;
    private Button deleteButton;
    private List<Component> toolTip;
    private ServerData editingServer;
    private LanServerDetection.LanServerList lanServerList;
    private LanServerDetection.LanServerDetector lanServerDetector;
    private boolean initedOnce;
    
    public JoinMultiplayerScreen(final Screen doq) {
        super(new TranslatableComponent("multiplayer.title"));
        this.pinger = new ServerStatusPinger();
        this.lastScreen = doq;
    }
    
    @Override
    protected void init() {
        super.init();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        if (this.initedOnce) {
            this.serverSelectionList.updateSize(this.width, this.height, 32, this.height - 64);
        }
        else {
            this.initedOnce = true;
            (this.servers = new ServerList(this.minecraft)).load();
            this.lanServerList = new LanServerDetection.LanServerList();
            try {
                (this.lanServerDetector = new LanServerDetection.LanServerDetector(this.lanServerList)).start();
            }
            catch (Exception exception2) {
                JoinMultiplayerScreen.LOGGER.warn("Unable to start LAN server detection: {}", exception2.getMessage());
            }
            (this.serverSelectionList = new ServerSelectionList(this, this.minecraft, this.width, this.height, 32, this.height - 64, 36)).updateOnlineServers(this.servers);
        }
        this.children.add(this.serverSelectionList);
        this.selectButton = this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 52, 100, 20, new TranslatableComponent("selectServer.select"), dlg -> this.joinSelectedServer()));
        this.<Button>addButton(new Button(this.width / 2 - 50, this.height - 52, 100, 20, new TranslatableComponent("selectServer.direct"), dlg -> {
            this.editingServer = new ServerData(I18n.get("selectServer.defaultName"), "", false);
            this.minecraft.setScreen(new DirectJoinServerScreen(this, this::directJoinCallback, this.editingServer));
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 4 + 50, this.height - 52, 100, 20, new TranslatableComponent("selectServer.add"), dlg -> {
            this.editingServer = new ServerData(I18n.get("selectServer.defaultName"), "", false);
            this.minecraft.setScreen(new EditServerScreen(this, this::addServerCallback, this.editingServer));
            return;
        }));
        final ServerSelectionList.Entry a3;
        ServerData dwr4;
        this.editButton = this.<Button>addButton(new Button(this.width / 2 - 154, this.height - 28, 70, 20, new TranslatableComponent("selectServer.edit"), dlg -> {
            a3 = this.serverSelectionList.getSelected();
            if (a3 instanceof ServerSelectionList.OnlineServerEntry) {
                dwr4 = ((ServerSelectionList.OnlineServerEntry)a3).getServerData();
                (this.editingServer = new ServerData(dwr4.name, dwr4.ip, false)).copyFrom(dwr4);
                this.minecraft.setScreen(new EditServerScreen(this, this::editServerCallback, this.editingServer));
            }
            return;
        }));
        final ServerSelectionList.Entry a4;
        String string4;
        Component nr5;
        final TranslatableComponent translatableComponent;
        Component nr6;
        Component nr7;
        Component nr8;
        this.deleteButton = this.<Button>addButton(new Button(this.width / 2 - 74, this.height - 28, 70, 20, new TranslatableComponent("selectServer.delete"), dlg -> {
            a4 = this.serverSelectionList.getSelected();
            if (a4 instanceof ServerSelectionList.OnlineServerEntry) {
                string4 = ((ServerSelectionList.OnlineServerEntry)a4).getServerData().name;
                if (string4 != null) {
                    nr5 = new TranslatableComponent("selectServer.deleteQuestion");
                    new TranslatableComponent("selectServer.deleteWarning", new Object[] { string4 });
                    nr6 = translatableComponent;
                    nr7 = new TranslatableComponent("selectServer.deleteButton");
                    nr8 = CommonComponents.GUI_CANCEL;
                    this.minecraft.setScreen(new ConfirmScreen(this::deleteCallback, nr5, nr6, nr7, nr8));
                }
            }
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 + 4, this.height - 28, 70, 20, new TranslatableComponent("selectServer.refresh"), dlg -> this.refreshServerList()));
        this.<Button>addButton(new Button(this.width / 2 + 4 + 76, this.height - 28, 75, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.onSelectedChange();
    }
    
    @Override
    public void tick() {
        super.tick();
        if (this.lanServerList.isDirty()) {
            final List<LanServer> list2 = this.lanServerList.getServers();
            this.lanServerList.markClean();
            this.serverSelectionList.updateNetworkServers(list2);
        }
        this.pinger.tick();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        if (this.lanServerDetector != null) {
            this.lanServerDetector.interrupt();
            this.lanServerDetector = null;
        }
        this.pinger.removeAll();
    }
    
    private void refreshServerList() {
        this.minecraft.setScreen(new JoinMultiplayerScreen(this.lastScreen));
    }
    
    private void deleteCallback(final boolean boolean1) {
        final ServerSelectionList.Entry a3 = this.serverSelectionList.getSelected();
        if (boolean1 && a3 instanceof ServerSelectionList.OnlineServerEntry) {
            this.servers.remove(((ServerSelectionList.OnlineServerEntry)a3).getServerData());
            this.servers.save();
            this.serverSelectionList.setSelected(null);
            this.serverSelectionList.updateOnlineServers(this.servers);
        }
        this.minecraft.setScreen(this);
    }
    
    private void editServerCallback(final boolean boolean1) {
        final ServerSelectionList.Entry a3 = this.serverSelectionList.getSelected();
        if (boolean1 && a3 instanceof ServerSelectionList.OnlineServerEntry) {
            final ServerData dwr4 = ((ServerSelectionList.OnlineServerEntry)a3).getServerData();
            dwr4.name = this.editingServer.name;
            dwr4.ip = this.editingServer.ip;
            dwr4.copyFrom(this.editingServer);
            this.servers.save();
            this.serverSelectionList.updateOnlineServers(this.servers);
        }
        this.minecraft.setScreen(this);
    }
    
    private void addServerCallback(final boolean boolean1) {
        if (boolean1) {
            this.servers.add(this.editingServer);
            this.servers.save();
            this.serverSelectionList.setSelected(null);
            this.serverSelectionList.updateOnlineServers(this.servers);
        }
        this.minecraft.setScreen(this);
    }
    
    private void directJoinCallback(final boolean boolean1) {
        if (boolean1) {
            this.join(this.editingServer);
        }
        else {
            this.minecraft.setScreen(this);
        }
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (super.keyPressed(integer1, integer2, integer3)) {
            return true;
        }
        if (integer1 == 294) {
            this.refreshServerList();
            return true;
        }
        if (this.serverSelectionList.getSelected() == null) {
            return false;
        }
        if (integer1 == 257 || integer1 == 335) {
            this.joinSelectedServer();
            return true;
        }
        return this.serverSelectionList.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.toolTip = null;
        this.renderBackground(dfj);
        this.serverSelectionList.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 20, 16777215);
        super.render(dfj, integer2, integer3, float4);
        if (this.toolTip != null) {
            this.renderComponentTooltip(dfj, this.toolTip, integer2, integer3);
        }
    }
    
    public void joinSelectedServer() {
        final ServerSelectionList.Entry a2 = this.serverSelectionList.getSelected();
        if (a2 instanceof ServerSelectionList.OnlineServerEntry) {
            this.join(((ServerSelectionList.OnlineServerEntry)a2).getServerData());
        }
        else if (a2 instanceof ServerSelectionList.NetworkServerEntry) {
            final LanServer emz3 = ((ServerSelectionList.NetworkServerEntry)a2).getServerData();
            this.join(new ServerData(emz3.getMotd(), emz3.getAddress(), true));
        }
    }
    
    private void join(final ServerData dwr) {
        this.minecraft.setScreen(new ConnectScreen(this, this.minecraft, dwr));
    }
    
    public void setSelected(final ServerSelectionList.Entry a) {
        this.serverSelectionList.setSelected(a);
        this.onSelectedChange();
    }
    
    protected void onSelectedChange() {
        this.selectButton.active = false;
        this.editButton.active = false;
        this.deleteButton.active = false;
        final ServerSelectionList.Entry a2 = this.serverSelectionList.getSelected();
        if (a2 != null && !(a2 instanceof ServerSelectionList.LANHeader)) {
            this.selectButton.active = true;
            if (a2 instanceof ServerSelectionList.OnlineServerEntry) {
                this.editButton.active = true;
                this.deleteButton.active = true;
            }
        }
    }
    
    public ServerStatusPinger getPinger() {
        return this.pinger;
    }
    
    public void setToolTip(final List<Component> list) {
        this.toolTip = list;
    }
    
    public ServerList getServers() {
        return this.servers;
    }
    
    static {
        LOGGER = LogManager.getLogger();
    }
}
