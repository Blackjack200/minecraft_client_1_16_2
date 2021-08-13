package com.mojang.realmsclient;

import com.mojang.realmsclient.util.RealmsPersistence;
import net.minecraft.client.gui.components.TickableWidget;
import com.mojang.realmsclient.util.RealmsTextureManager;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.AbstractSelectionList;
import com.mojang.realmsclient.gui.screens.RealmsCreateRealmScreen;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.realms.RealmsObjectSelectionList;
import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.Font;
import net.minecraft.client.Minecraft;
import com.mojang.realmsclient.dto.RegionPingResult;
import com.mojang.realmsclient.dto.PingResult;
import com.mojang.realmsclient.client.Ping;
import com.mojang.realmsclient.gui.screens.RealmsPendingInvitesScreen;
import java.util.Arrays;
import java.util.Collection;
import com.google.common.collect.ImmutableList;
import java.util.function.Predicate;
import net.minecraft.server.packs.resources.ResourceManager;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.util.concurrent.TimeUnit;
import net.minecraft.util.Mth;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.gui.screens.RealmsLongConfirmationScreen;
import com.mojang.realmsclient.gui.screens.RealmsConfigureWorldScreen;
import com.mojang.realmsclient.gui.screens.RealmsParentalConsentScreen;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
import com.mojang.realmsclient.gui.screens.RealmsClientOutdatedScreen;
import com.mojang.realmsclient.client.RealmsClient;
import com.mojang.realmsclient.dto.RealmsServerPlayerLists;
import com.mojang.realmsclient.dto.RealmsServerPlayerList;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.client.resources.language.I18n;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.Iterator;
import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.MultiLineLabel;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.components.Button;
import com.google.common.util.concurrent.RateLimiter;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsMainScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final ResourceLocation ON_ICON_LOCATION;
    private static final ResourceLocation OFF_ICON_LOCATION;
    private static final ResourceLocation EXPIRED_ICON_LOCATION;
    private static final ResourceLocation EXPIRES_SOON_ICON_LOCATION;
    private static final ResourceLocation LEAVE_ICON_LOCATION;
    private static final ResourceLocation INVITATION_ICONS_LOCATION;
    private static final ResourceLocation INVITE_ICON_LOCATION;
    private static final ResourceLocation WORLDICON_LOCATION;
    private static final ResourceLocation LOGO_LOCATION;
    private static final ResourceLocation CONFIGURE_LOCATION;
    private static final ResourceLocation QUESTIONMARK_LOCATION;
    private static final ResourceLocation NEWS_LOCATION;
    private static final ResourceLocation POPUP_LOCATION;
    private static final ResourceLocation DARKEN_LOCATION;
    private static final ResourceLocation CROSS_ICON_LOCATION;
    private static final ResourceLocation TRIAL_ICON_LOCATION;
    private static final ResourceLocation BUTTON_LOCATION;
    private static final Component NO_PENDING_INVITES_TEXT;
    private static final Component PENDING_INVITES_TEXT;
    private static final List<Component> TRIAL_MESSAGE_LINES;
    private static final Component SERVER_UNITIALIZED_TEXT;
    private static final Component SUBSCRIPTION_EXPIRED_TEXT;
    private static final Component SUBSCRIPTION_RENEW_TEXT;
    private static final Component TRIAL_EXPIRED_TEXT;
    private static final Component SUBSCRIPTION_CREATE_TEXT;
    private static final Component SELECT_MINIGAME_PREFIX;
    private static final Component POPUP_TEXT;
    private static final Component SERVER_EXPIRED_TOOLTIP;
    private static final Component SERVER_EXPIRES_SOON_TOOLTIP;
    private static final Component SERVER_EXPIRES_IN_DAY_TOOLTIP;
    private static final Component SERVER_OPEN_TOOLTIP;
    private static final Component SERVER_CLOSED_TOOLTIP;
    private static final Component LEAVE_SERVER_TOOLTIP;
    private static final Component CONFIGURE_SERVER_TOOLTIP;
    private static final Component SERVER_INFO_TOOLTIP;
    private static final Component NEWS_TOOLTIP;
    private static List<ResourceLocation> teaserImages;
    private static final RealmsDataFetcher REALMS_DATA_FETCHER;
    private static boolean overrideConfigure;
    private static int lastScrollYPosition;
    private static volatile boolean hasParentalConsent;
    private static volatile boolean checkedParentalConsent;
    private static volatile boolean checkedClientCompatability;
    private static Screen realmsGenericErrorScreen;
    private static boolean regionsPinged;
    private final RateLimiter inviteNarrationLimiter;
    private boolean dontSetConnectedToRealms;
    private final Screen lastScreen;
    private volatile RealmSelectionList realmSelectionList;
    private long selectedServerId;
    private Button playButton;
    private Button backButton;
    private Button renewButton;
    private Button configureButton;
    private Button leaveButton;
    private List<Component> toolTip;
    private List<RealmsServer> realmsServers;
    private volatile int numberOfPendingInvites;
    private int animTick;
    private boolean hasFetchedServers;
    private boolean popupOpenedByUser;
    private boolean justClosedPopup;
    private volatile boolean trialsAvailable;
    private volatile boolean createdTrial;
    private volatile boolean showingPopup;
    private volatile boolean hasUnreadNews;
    private volatile String newsLink;
    private int carouselIndex;
    private int carouselTick;
    private boolean hasSwitchedCarouselImage;
    private List<KeyCombo> keyCombos;
    private int clicks;
    private ReentrantLock connectLock;
    private MultiLineLabel formattedPopup;
    private HoveredElement hoveredElement;
    private Button showPopupButton;
    private Button pendingInvitesButton;
    private Button newsButton;
    private Button createTrialButton;
    private Button buyARealmButton;
    private Button closeButton;
    
    public RealmsMainScreen(final Screen doq) {
        this.selectedServerId = -1L;
        this.realmsServers = (List<RealmsServer>)Lists.newArrayList();
        this.connectLock = new ReentrantLock();
        this.formattedPopup = MultiLineLabel.EMPTY;
        this.lastScreen = doq;
        this.inviteNarrationLimiter = RateLimiter.create(0.01666666753590107);
    }
    
    private boolean shouldShowMessageInList() {
        if (!hasParentalConsent() || !this.hasFetchedServers) {
            return false;
        }
        if (this.trialsAvailable && !this.createdTrial) {
            return true;
        }
        for (final RealmsServer dgn3 : this.realmsServers) {
            if (dgn3.ownerUUID.equals(this.minecraft.getUser().getUuid())) {
                return false;
            }
        }
        return true;
    }
    
    public boolean shouldShowPopup() {
        return hasParentalConsent() && this.hasFetchedServers && (this.popupOpenedByUser || (this.trialsAvailable && !this.createdTrial && this.realmsServers.isEmpty()) || this.realmsServers.isEmpty());
    }
    
    public void init() {
        this.keyCombos = (List<KeyCombo>)Lists.newArrayList((Object[])new KeyCombo[] { new KeyCombo(new char[] { '3', '2', '1', '4', '5', '6' }, () -> RealmsMainScreen.overrideConfigure = !RealmsMainScreen.overrideConfigure), new KeyCombo(new char[] { '9', '8', '7', '1', '2', '3' }, () -> {
                if (RealmsClient.currentEnvironment == RealmsClient.Environment.STAGE) {
                    this.switchToProd();
                }
                else {
                    this.switchToStage();
                }
            }), new KeyCombo(new char[] { '9', '8', '7', '4', '5', '6' }, () -> {
                if (RealmsClient.currentEnvironment == RealmsClient.Environment.LOCAL) {
                    this.switchToProd();
                }
                else {
                    this.switchToLocal();
                }
            }) });
        if (RealmsMainScreen.realmsGenericErrorScreen != null) {
            this.minecraft.setScreen(RealmsMainScreen.realmsGenericErrorScreen);
            return;
        }
        this.connectLock = new ReentrantLock();
        if (RealmsMainScreen.checkedClientCompatability && !hasParentalConsent()) {
            this.checkParentalConsent();
        }
        this.checkClientCompatability();
        this.checkUnreadNews();
        if (!this.dontSetConnectedToRealms) {
            this.minecraft.setConnectedToRealms(false);
        }
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        if (hasParentalConsent()) {
            RealmsMainScreen.REALMS_DATA_FETCHER.forceUpdate();
        }
        this.showingPopup = false;
        if (hasParentalConsent() && this.hasFetchedServers) {
            this.addButtons();
        }
        this.realmSelectionList = new RealmSelectionList();
        if (RealmsMainScreen.lastScrollYPosition != -1) {
            this.realmSelectionList.setScrollAmount(RealmsMainScreen.lastScrollYPosition);
        }
        this.<RealmSelectionList>addWidget(this.realmSelectionList);
        this.magicalSpecialHackyFocus(this.realmSelectionList);
        this.formattedPopup = MultiLineLabel.create(this.font, RealmsMainScreen.POPUP_TEXT, 100);
    }
    
    private static boolean hasParentalConsent() {
        return RealmsMainScreen.checkedParentalConsent && RealmsMainScreen.hasParentalConsent;
    }
    
    public void addButtons() {
        this.configureButton = this.<Button>addButton(new Button(this.width / 2 - 190, this.height - 32, 90, 20, new TranslatableComponent("mco.selectServer.configure"), dlg -> this.configureClicked(this.findServer(this.selectedServerId))));
        final RealmsServer dgn3;
        this.playButton = this.<Button>addButton(new Button(this.width / 2 - 93, this.height - 32, 90, 20, new TranslatableComponent("mco.selectServer.play"), dlg -> {
            dgn3 = this.findServer(this.selectedServerId);
            if (dgn3 == null) {
                return;
            }
            else {
                this.play(dgn3, this);
                return;
            }
        }));
        this.backButton = this.<Button>addButton(new Button(this.width / 2 + 4, this.height - 32, 90, 20, CommonComponents.GUI_BACK, dlg -> {
            if (!this.justClosedPopup) {
                this.minecraft.setScreen(this.lastScreen);
            }
            return;
        }));
        this.renewButton = this.<Button>addButton(new Button(this.width / 2 + 100, this.height - 32, 90, 20, new TranslatableComponent("mco.selectServer.expiredRenew"), dlg -> this.onRenew()));
        this.leaveButton = this.<Button>addButton(new Button(this.width / 2 - 202, this.height - 32, 90, 20, new TranslatableComponent("mco.selectServer.leave"), dlg -> this.leaveClicked(this.findServer(this.selectedServerId))));
        this.pendingInvitesButton = this.<PendingInvitesButton>addButton(new PendingInvitesButton());
        this.newsButton = this.<NewsButton>addButton(new NewsButton());
        this.showPopupButton = this.<ShowPopupButton>addButton(new ShowPopupButton());
        this.closeButton = this.<CloseButton>addButton(new CloseButton());
        this.createTrialButton = this.<Button>addButton(new Button(this.width / 2 + 52, this.popupY0() + 137 - 20, 98, 20, new TranslatableComponent("mco.selectServer.trial"), dlg -> {
            if (!this.trialsAvailable || this.createdTrial) {
                return;
            }
            else {
                Util.getPlatform().openUri("https://aka.ms/startjavarealmstrial");
                this.minecraft.setScreen(this.lastScreen);
                return;
            }
        }));
        this.buyARealmButton = this.<Button>addButton(new Button(this.width / 2 + 52, this.popupY0() + 160 - 20, 98, 20, new TranslatableComponent("mco.selectServer.buy"), dlg -> Util.getPlatform().openUri("https://aka.ms/BuyJavaRealms")));
        final RealmsServer dgn4 = this.findServer(this.selectedServerId);
        this.updateButtonStates(dgn4);
    }
    
    private void updateButtonStates(@Nullable final RealmsServer dgn) {
        this.playButton.active = (this.shouldPlayButtonBeActive(dgn) && !this.shouldShowPopup());
        this.renewButton.visible = this.shouldRenewButtonBeActive(dgn);
        this.configureButton.visible = this.shouldConfigureButtonBeVisible(dgn);
        this.leaveButton.visible = this.shouldLeaveButtonBeVisible(dgn);
        final boolean boolean3 = this.shouldShowPopup() && this.trialsAvailable && !this.createdTrial;
        this.createTrialButton.visible = boolean3;
        this.createTrialButton.active = boolean3;
        this.buyARealmButton.visible = this.shouldShowPopup();
        this.closeButton.visible = (this.shouldShowPopup() && this.popupOpenedByUser);
        this.renewButton.active = !this.shouldShowPopup();
        this.configureButton.active = !this.shouldShowPopup();
        this.leaveButton.active = !this.shouldShowPopup();
        this.newsButton.active = true;
        this.pendingInvitesButton.active = true;
        this.backButton.active = true;
        this.showPopupButton.active = !this.shouldShowPopup();
    }
    
    private boolean shouldShowPopupButton() {
        return (!this.shouldShowPopup() || this.popupOpenedByUser) && hasParentalConsent() && this.hasFetchedServers;
    }
    
    private boolean shouldPlayButtonBeActive(@Nullable final RealmsServer dgn) {
        return dgn != null && !dgn.expired && dgn.state == RealmsServer.State.OPEN;
    }
    
    private boolean shouldRenewButtonBeActive(@Nullable final RealmsServer dgn) {
        return dgn != null && dgn.expired && this.isSelfOwnedServer(dgn);
    }
    
    private boolean shouldConfigureButtonBeVisible(@Nullable final RealmsServer dgn) {
        return dgn != null && this.isSelfOwnedServer(dgn);
    }
    
    private boolean shouldLeaveButtonBeVisible(@Nullable final RealmsServer dgn) {
        return dgn != null && !this.isSelfOwnedServer(dgn);
    }
    
    @Override
    public void tick() {
        super.tick();
        this.justClosedPopup = false;
        ++this.animTick;
        --this.clicks;
        if (this.clicks < 0) {
            this.clicks = 0;
        }
        if (hasParentalConsent()) {
            RealmsMainScreen.REALMS_DATA_FETCHER.init();
            if (RealmsMainScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.SERVER_LIST)) {
                final List<RealmsServer> list2 = RealmsMainScreen.REALMS_DATA_FETCHER.getServers();
                this.realmSelectionList.clear();
                final boolean boolean3 = !this.hasFetchedServers;
                if (boolean3) {
                    this.hasFetchedServers = true;
                }
                if (list2 != null) {
                    boolean boolean4 = false;
                    for (final RealmsServer dgn6 : list2) {
                        if (this.isSelfOwnedNonExpiredServer(dgn6)) {
                            boolean4 = true;
                        }
                    }
                    this.realmsServers = list2;
                    if (this.shouldShowMessageInList()) {
                        this.realmSelectionList.addMessageEntry(new TrialEntry());
                    }
                    for (final RealmsServer dgn6 : this.realmsServers) {
                        ((RealmsObjectSelectionList<ServerEntry>)this.realmSelectionList).addEntry(new ServerEntry(dgn6));
                    }
                    if (!RealmsMainScreen.regionsPinged && boolean4) {
                        RealmsMainScreen.regionsPinged = true;
                        this.pingRegions();
                    }
                }
                if (boolean3) {
                    this.addButtons();
                }
            }
            if (RealmsMainScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
                this.numberOfPendingInvites = RealmsMainScreen.REALMS_DATA_FETCHER.getPendingInvitesCount();
                if (this.numberOfPendingInvites > 0 && this.inviteNarrationLimiter.tryAcquire(1)) {
                    NarrationHelper.now(I18n.get("mco.configure.world.invite.narration", this.numberOfPendingInvites));
                }
            }
            if (RealmsMainScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE) && !this.createdTrial) {
                final boolean boolean5 = RealmsMainScreen.REALMS_DATA_FETCHER.isTrialAvailable();
                if (boolean5 != this.trialsAvailable && this.shouldShowPopup()) {
                    this.trialsAvailable = boolean5;
                    this.showingPopup = false;
                }
                else {
                    this.trialsAvailable = boolean5;
                }
            }
            if (RealmsMainScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.LIVE_STATS)) {
                final RealmsServerPlayerLists dgs2 = RealmsMainScreen.REALMS_DATA_FETCHER.getLivestats();
                for (final RealmsServerPlayerList dgr4 : dgs2.servers) {
                    for (final RealmsServer dgn6 : this.realmsServers) {
                        if (dgn6.id == dgr4.serverId) {
                            dgn6.updateServerPing(dgr4);
                            break;
                        }
                    }
                }
            }
            if (RealmsMainScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.UNREAD_NEWS)) {
                this.hasUnreadNews = RealmsMainScreen.REALMS_DATA_FETCHER.hasUnreadNews();
                this.newsLink = RealmsMainScreen.REALMS_DATA_FETCHER.newsLink();
            }
            RealmsMainScreen.REALMS_DATA_FETCHER.markClean();
            if (this.shouldShowPopup()) {
                ++this.carouselTick;
            }
            if (this.showPopupButton != null) {
                this.showPopupButton.visible = this.shouldShowPopupButton();
            }
        }
    }
    
    private void pingRegions() {
        new Thread(() -> {
            final List<RegionPingResult> list2 = Ping.pingAllRegions();
            final RealmsClient dfy3 = RealmsClient.create();
            final PingResult dgj4 = new PingResult();
            dgj4.pingResults = list2;
            dgj4.worldIds = this.getOwnedNonExpiredWorldIds();
            try {
                dfy3.sendPingResults(dgj4);
            }
            catch (Throwable throwable5) {
                RealmsMainScreen.LOGGER.warn("Could not send ping result to Realms: ", throwable5);
            }
        }).start();
    }
    
    private List<Long> getOwnedNonExpiredWorldIds() {
        final List<Long> list2 = (List<Long>)Lists.newArrayList();
        for (final RealmsServer dgn4 : this.realmsServers) {
            if (this.isSelfOwnedNonExpiredServer(dgn4)) {
                list2.add(dgn4.id);
            }
        }
        return list2;
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
        this.stopRealmsFetcher();
    }
    
    private void onRenew() {
        final RealmsServer dgn2 = this.findServer(this.selectedServerId);
        if (dgn2 == null) {
            return;
        }
        final String string3 = "https://aka.ms/ExtendJavaRealms?subscriptionId=" + dgn2.remoteSubscriptionId + "&profileId=" + this.minecraft.getUser().getUuid() + "&ref=" + (dgn2.expiredTrial ? "expiredTrial" : "expiredRealm");
        this.minecraft.keyboardHandler.setClipboard(string3);
        Util.getPlatform().openUri(string3);
    }
    
    private void checkClientCompatability() {
        if (!RealmsMainScreen.checkedClientCompatability) {
            RealmsMainScreen.checkedClientCompatability = true;
            new Thread("MCO Compatability Checker #1") {
                public void run() {
                    final RealmsClient dfy2 = RealmsClient.create();
                    try {
                        final RealmsClient.CompatibleVersionResponse a3 = dfy2.clientCompatible();
                        if (a3 == RealmsClient.CompatibleVersionResponse.OUTDATED) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, true);
                            RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(RealmsMainScreen.realmsGenericErrorScreen));
                            return;
                        }
                        if (a3 == RealmsClient.CompatibleVersionResponse.OTHER) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsClientOutdatedScreen(RealmsMainScreen.this.lastScreen, false);
                            RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(RealmsMainScreen.realmsGenericErrorScreen));
                            return;
                        }
                        RealmsMainScreen.this.checkParentalConsent();
                    }
                    catch (RealmsServiceException dhf3) {
                        RealmsMainScreen.checkedClientCompatability = false;
                        RealmsMainScreen.LOGGER.error("Couldn't connect to realms", (Throwable)dhf3);
                        if (dhf3.httpResultCode == 401) {
                            RealmsMainScreen.realmsGenericErrorScreen = new RealmsGenericErrorScreen(new TranslatableComponent("mco.error.invalid.session.title"), new TranslatableComponent("mco.error.invalid.session.message"), RealmsMainScreen.this.lastScreen);
                            RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(RealmsMainScreen.realmsGenericErrorScreen));
                        }
                        else {
                            RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf3, RealmsMainScreen.this.lastScreen)));
                        }
                    }
                }
            }.start();
        }
    }
    
    private void checkUnreadNews() {
    }
    
    private void checkParentalConsent() {
        new Thread("MCO Compatability Checker #1") {
            public void run() {
                final RealmsClient dfy2 = RealmsClient.create();
                try {
                    final Boolean boolean3 = dfy2.mcoEnabled();
                    if (boolean3) {
                        RealmsMainScreen.LOGGER.info("Realms is available for this user");
                        RealmsMainScreen.hasParentalConsent = true;
                    }
                    else {
                        RealmsMainScreen.LOGGER.info("Realms is not available for this user");
                        RealmsMainScreen.hasParentalConsent = false;
                        RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(new RealmsParentalConsentScreen(RealmsMainScreen.this.lastScreen)));
                    }
                    RealmsMainScreen.checkedParentalConsent = true;
                }
                catch (RealmsServiceException dhf3) {
                    RealmsMainScreen.LOGGER.error("Couldn't connect to realms", (Throwable)dhf3);
                    RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf3, RealmsMainScreen.this.lastScreen)));
                }
            }
        }.start();
    }
    
    private void switchToStage() {
        if (RealmsClient.currentEnvironment != RealmsClient.Environment.STAGE) {
            new Thread("MCO Stage Availability Checker #1") {
                public void run() {
                    final RealmsClient dfy2 = RealmsClient.create();
                    try {
                        final Boolean boolean3 = dfy2.stageAvailable();
                        if (boolean3) {
                            RealmsClient.switchToStage();
                            RealmsMainScreen.LOGGER.info("Switched to stage");
                            RealmsMainScreen.REALMS_DATA_FETCHER.forceUpdate();
                        }
                    }
                    catch (RealmsServiceException dhf3) {
                        RealmsMainScreen.LOGGER.error(new StringBuilder().append("Couldn't connect to Realms: ").append(dhf3).toString());
                    }
                }
            }.start();
        }
    }
    
    private void switchToLocal() {
        if (RealmsClient.currentEnvironment != RealmsClient.Environment.LOCAL) {
            new Thread("MCO Local Availability Checker #1") {
                public void run() {
                    final RealmsClient dfy2 = RealmsClient.create();
                    try {
                        final Boolean boolean3 = dfy2.stageAvailable();
                        if (boolean3) {
                            RealmsClient.switchToLocal();
                            RealmsMainScreen.LOGGER.info("Switched to local");
                            RealmsMainScreen.REALMS_DATA_FETCHER.forceUpdate();
                        }
                    }
                    catch (RealmsServiceException dhf3) {
                        RealmsMainScreen.LOGGER.error(new StringBuilder().append("Couldn't connect to Realms: ").append(dhf3).toString());
                    }
                }
            }.start();
        }
    }
    
    private void switchToProd() {
        RealmsClient.switchToProd();
        RealmsMainScreen.REALMS_DATA_FETCHER.forceUpdate();
    }
    
    private void stopRealmsFetcher() {
        RealmsMainScreen.REALMS_DATA_FETCHER.stop();
    }
    
    private void configureClicked(final RealmsServer dgn) {
        if (this.minecraft.getUser().getUuid().equals(dgn.ownerUUID) || RealmsMainScreen.overrideConfigure) {
            this.saveListScrollPosition();
            this.minecraft.setScreen(new RealmsConfigureWorldScreen(this, dgn.id));
        }
    }
    
    private void leaveClicked(@Nullable final RealmsServer dgn) {
        if (dgn != null && !this.minecraft.getUser().getUuid().equals(dgn.ownerUUID)) {
            this.saveListScrollPosition();
            final Component nr3 = new TranslatableComponent("mco.configure.world.leave.question.line1");
            final Component nr4 = new TranslatableComponent("mco.configure.world.leave.question.line2");
            this.minecraft.setScreen(new RealmsLongConfirmationScreen(this::leaveServer, RealmsLongConfirmationScreen.Type.Info, nr3, nr4, true));
        }
    }
    
    private void saveListScrollPosition() {
        RealmsMainScreen.lastScrollYPosition = (int)this.realmSelectionList.getScrollAmount();
    }
    
    @Nullable
    private RealmsServer findServer(final long long1) {
        for (final RealmsServer dgn5 : this.realmsServers) {
            if (dgn5.id == long1) {
                return dgn5;
            }
        }
        return null;
    }
    
    private void leaveServer(final boolean boolean1) {
        if (boolean1) {
            new Thread("Realms-leave-server") {
                public void run() {
                    try {
                        final RealmsServer dgn2 = RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId);
                        if (dgn2 != null) {
                            final RealmsClient dfy3 = RealmsClient.create();
                            dfy3.uninviteMyselfFrom(dgn2.id);
                            RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.removeServer(dgn2));
                        }
                    }
                    catch (RealmsServiceException dhf2) {
                        RealmsMainScreen.LOGGER.error("Couldn't configure world");
                        RealmsMainScreen.this.minecraft.execute(() -> RealmsMainScreen.this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf2, RealmsMainScreen.this)));
                    }
                }
            }.start();
        }
        this.minecraft.setScreen(this);
    }
    
    private void removeServer(final RealmsServer dgn) {
        RealmsMainScreen.REALMS_DATA_FETCHER.removeItem(dgn);
        this.realmsServers.remove(dgn);
        this.realmSelectionList.children().removeIf(b -> b instanceof ServerEntry && ((ServerEntry)b).serverData.id == this.selectedServerId);
        this.realmSelectionList.setSelected(null);
        this.updateButtonStates(null);
        this.selectedServerId = -1L;
        this.playButton.active = false;
    }
    
    public void removeSelection() {
        this.selectedServerId = -1L;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.keyCombos.forEach(KeyCombo::reset);
            this.onClosePopup();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    private void onClosePopup() {
        if (this.shouldShowPopup() && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
        }
        else {
            this.minecraft.setScreen(this.lastScreen);
        }
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        this.keyCombos.forEach(dfs -> dfs.keyPressed(character));
        return true;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.hoveredElement = HoveredElement.NONE;
        this.toolTip = null;
        this.renderBackground(dfj);
        this.realmSelectionList.render(dfj, integer2, integer3, float4);
        this.drawRealmsLogo(dfj, this.width / 2 - 50, 7);
        if (RealmsClient.currentEnvironment == RealmsClient.Environment.STAGE) {
            this.renderStage(dfj);
        }
        if (RealmsClient.currentEnvironment == RealmsClient.Environment.LOCAL) {
            this.renderLocal(dfj);
        }
        if (this.shouldShowPopup()) {
            this.drawPopup(dfj, integer2, integer3);
        }
        else {
            if (this.showingPopup) {
                this.updateButtonStates(null);
                if (!this.children.contains(this.realmSelectionList)) {
                    this.children.add(this.realmSelectionList);
                }
                final RealmsServer dgn6 = this.findServer(this.selectedServerId);
                this.playButton.active = this.shouldPlayButtonBeActive(dgn6);
            }
            this.showingPopup = false;
        }
        super.render(dfj, integer2, integer3, float4);
        if (this.toolTip != null) {
            this.renderMousehoverTooltip(dfj, this.toolTip, integer2, integer3);
        }
        if (this.trialsAvailable && !this.createdTrial && this.shouldShowPopup()) {
            this.minecraft.getTextureManager().bind(RealmsMainScreen.TRIAL_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            final int integer4 = 8;
            final int integer5 = 8;
            int integer6 = 0;
            if ((Util.getMillis() / 800L & 0x1L) == 0x1L) {
                integer6 = 8;
            }
            GuiComponent.blit(dfj, this.createTrialButton.x + this.createTrialButton.getWidth() - 8 - 4, this.createTrialButton.y + this.createTrialButton.getHeight() / 2 - 4, 0.0f, (float)integer6, 8, 8, 8, 16);
        }
    }
    
    private void drawRealmsLogo(final PoseStack dfj, final int integer2, final int integer3) {
        this.minecraft.getTextureManager().bind(RealmsMainScreen.LOGO_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.pushMatrix();
        RenderSystem.scalef(0.5f, 0.5f, 0.5f);
        GuiComponent.blit(dfj, integer2 * 2, integer3 * 2 - 5, 0.0f, 0.0f, 200, 50, 200, 50);
        RenderSystem.popMatrix();
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.isOutsidePopup(double1, double2) && this.popupOpenedByUser) {
            this.popupOpenedByUser = false;
            return this.justClosedPopup = true;
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    private boolean isOutsidePopup(final double double1, final double double2) {
        final int integer6 = this.popupX0();
        final int integer7 = this.popupY0();
        return double1 < integer6 - 5 || double1 > integer6 + 315 || double2 < integer7 - 5 || double2 > integer7 + 171;
    }
    
    private void drawPopup(final PoseStack dfj, final int integer2, final int integer3) {
        final int integer4 = this.popupX0();
        final int integer5 = this.popupY0();
        if (!this.showingPopup) {
            this.carouselIndex = 0;
            this.carouselTick = 0;
            this.hasSwitchedCarouselImage = true;
            this.updateButtonStates(null);
            if (this.children.contains(this.realmSelectionList)) {
                final GuiEventListener dmf7 = this.realmSelectionList;
                if (!this.children.remove(dmf7)) {
                    RealmsMainScreen.LOGGER.error(new StringBuilder().append("Unable to remove widget: ").append(dmf7).toString());
                }
            }
            NarrationHelper.now(RealmsMainScreen.POPUP_TEXT.getString());
        }
        if (this.hasFetchedServers) {
            this.showingPopup = true;
        }
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 0.7f);
        RenderSystem.enableBlend();
        this.minecraft.getTextureManager().bind(RealmsMainScreen.DARKEN_LOCATION);
        final int integer6 = 0;
        final int integer7 = 32;
        GuiComponent.blit(dfj, 0, 32, 0.0f, 0.0f, this.width, this.height - 40 - 32, 310, 166);
        RenderSystem.disableBlend();
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        this.minecraft.getTextureManager().bind(RealmsMainScreen.POPUP_LOCATION);
        GuiComponent.blit(dfj, integer4, integer5, 0.0f, 0.0f, 310, 166, 310, 166);
        if (!RealmsMainScreen.teaserImages.isEmpty()) {
            this.minecraft.getTextureManager().bind((ResourceLocation)RealmsMainScreen.teaserImages.get(this.carouselIndex));
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer4 + 7, integer5 + 7, 0.0f, 0.0f, 195, 152, 195, 152);
            if (this.carouselTick % 95 < 5) {
                if (!this.hasSwitchedCarouselImage) {
                    this.carouselIndex = (this.carouselIndex + 1) % RealmsMainScreen.teaserImages.size();
                    this.hasSwitchedCarouselImage = true;
                }
            }
            else {
                this.hasSwitchedCarouselImage = false;
            }
        }
        this.formattedPopup.renderLeftAlignedNoShadow(dfj, this.width / 2 + 52, integer5 + 7, 10, 5000268);
    }
    
    private int popupX0() {
        return (this.width - 310) / 2;
    }
    
    private int popupY0() {
        return this.height / 2 - 80;
    }
    
    private void drawInvitationPendingIcon(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6, final boolean boolean7) {
        final int integer6 = this.numberOfPendingInvites;
        final boolean boolean8 = this.inPendingInvitationArea(integer2, integer3);
        final boolean boolean9 = boolean7 && boolean6;
        if (boolean9) {
            final float float12 = 0.25f + (1.0f + Mth.sin(this.animTick * 0.5f)) * 0.25f;
            int integer7 = 0xFF000000 | (int)(float12 * 64.0f) << 16 | (int)(float12 * 64.0f) << 8 | (int)(float12 * 64.0f) << 0;
            this.fillGradient(dfj, integer4 - 2, integer5 - 2, integer4 + 18, integer5 + 18, integer7, integer7);
            integer7 = (0xFF000000 | (int)(float12 * 255.0f) << 16 | (int)(float12 * 255.0f) << 8 | (int)(float12 * 255.0f) << 0);
            this.fillGradient(dfj, integer4 - 2, integer5 - 2, integer4 + 18, integer5 - 1, integer7, integer7);
            this.fillGradient(dfj, integer4 - 2, integer5 - 2, integer4 - 1, integer5 + 18, integer7, integer7);
            this.fillGradient(dfj, integer4 + 17, integer5 - 2, integer4 + 18, integer5 + 18, integer7, integer7);
            this.fillGradient(dfj, integer4 - 2, integer5 + 17, integer4 + 18, integer5 + 18, integer7, integer7);
        }
        this.minecraft.getTextureManager().bind(RealmsMainScreen.INVITE_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final boolean boolean10 = boolean7 && boolean6;
        final float float13 = boolean10 ? 16.0f : 0.0f;
        GuiComponent.blit(dfj, integer4, integer5 - 6, float13, 0.0f, 15, 25, 31, 25);
        final boolean boolean11 = boolean7 && integer6 != 0;
        if (boolean11) {
            final int integer8 = (Math.min(integer6, 6) - 1) * 8;
            final int integer9 = (int)(Math.max(0.0f, Math.max(Mth.sin((10 + this.animTick) * 0.57f), Mth.cos(this.animTick * 0.35f))) * -6.0f);
            this.minecraft.getTextureManager().bind(RealmsMainScreen.INVITATION_ICONS_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            final float float14 = boolean8 ? 8.0f : 0.0f;
            GuiComponent.blit(dfj, integer4 + 4, integer5 + 4 + integer9, (float)integer8, float14, 8, 8, 48, 16);
        }
        final int integer8 = integer2 + 12;
        final int integer9 = integer3;
        final boolean boolean12 = boolean7 && boolean8;
        if (boolean12) {
            final Component nr18 = (integer6 == 0) ? RealmsMainScreen.NO_PENDING_INVITES_TEXT : RealmsMainScreen.PENDING_INVITES_TEXT;
            final int integer10 = this.font.width(nr18);
            this.fillGradient(dfj, integer8 - 3, integer9 - 3, integer8 + integer10 + 3, integer9 + 8 + 3, -1073741824, -1073741824);
            this.font.drawShadow(dfj, nr18, (float)integer8, (float)integer9, -1);
        }
    }
    
    private boolean inPendingInvitationArea(final double double1, final double double2) {
        int integer6 = this.width / 2 + 50;
        int integer7 = this.width / 2 + 66;
        int integer8 = 11;
        int integer9 = 23;
        if (this.numberOfPendingInvites != 0) {
            integer6 -= 3;
            integer7 += 3;
            integer8 -= 5;
            integer9 += 5;
        }
        return integer6 <= double1 && double1 <= integer7 && integer8 <= double2 && double2 <= integer9;
    }
    
    public void play(final RealmsServer dgn, final Screen doq) {
        if (dgn != null) {
            try {
                if (!this.connectLock.tryLock(1L, TimeUnit.SECONDS)) {
                    return;
                }
                if (this.connectLock.getHoldCount() > 1) {
                    return;
                }
            }
            catch (InterruptedException interruptedException4) {
                return;
            }
            this.dontSetConnectedToRealms = true;
            this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(doq, new GetServerDetailsTask(this, doq, dgn, this.connectLock)));
        }
    }
    
    private boolean isSelfOwnedServer(final RealmsServer dgn) {
        return dgn.ownerUUID != null && dgn.ownerUUID.equals(this.minecraft.getUser().getUuid());
    }
    
    private boolean isSelfOwnedNonExpiredServer(final RealmsServer dgn) {
        return this.isSelfOwnedServer(dgn) && !dgn.expired;
    }
    
    private void drawExpired(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsMainScreen.EXPIRED_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            this.setTooltip(RealmsMainScreen.SERVER_EXPIRED_TOOLTIP);
        }
    }
    
    private void drawExpiring(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
        this.minecraft.getTextureManager().bind(RealmsMainScreen.EXPIRES_SOON_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        if (this.animTick % 20 < 10) {
            GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 20, 28);
        }
        else {
            GuiComponent.blit(dfj, integer2, integer3, 10.0f, 0.0f, 10, 28, 20, 28);
        }
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            if (integer6 <= 0) {
                this.setTooltip(RealmsMainScreen.SERVER_EXPIRES_SOON_TOOLTIP);
            }
            else if (integer6 == 1) {
                this.setTooltip(RealmsMainScreen.SERVER_EXPIRES_IN_DAY_TOOLTIP);
            }
            else {
                this.setTooltip(new TranslatableComponent("mco.selectServer.expires.days", new Object[] { integer6 }));
            }
        }
    }
    
    private void drawOpen(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsMainScreen.ON_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            this.setTooltip(RealmsMainScreen.SERVER_OPEN_TOOLTIP);
        }
    }
    
    private void drawClose(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        this.minecraft.getTextureManager().bind(RealmsMainScreen.OFF_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        GuiComponent.blit(dfj, integer2, integer3, 0.0f, 0.0f, 10, 28, 10, 28);
        if (integer4 >= integer2 && integer4 <= integer2 + 9 && integer5 >= integer3 && integer5 <= integer3 + 27 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            this.setTooltip(RealmsMainScreen.SERVER_CLOSED_TOOLTIP);
        }
    }
    
    private void drawLeave(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        boolean boolean7 = false;
        if (integer4 >= integer2 && integer4 <= integer2 + 28 && integer5 >= integer3 && integer5 <= integer3 + 28 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            boolean7 = true;
        }
        this.minecraft.getTextureManager().bind(RealmsMainScreen.LEAVE_ICON_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float8 = boolean7 ? 28.0f : 0.0f;
        GuiComponent.blit(dfj, integer2, integer3, float8, 0.0f, 28, 28, 56, 28);
        if (boolean7) {
            this.setTooltip(RealmsMainScreen.LEAVE_SERVER_TOOLTIP);
            this.hoveredElement = HoveredElement.LEAVE;
        }
    }
    
    private void drawConfigure(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5) {
        boolean boolean7 = false;
        if (integer4 >= integer2 && integer4 <= integer2 + 28 && integer5 >= integer3 && integer5 <= integer3 + 28 && integer5 < this.height - 40 && integer5 > 32 && !this.shouldShowPopup()) {
            boolean7 = true;
        }
        this.minecraft.getTextureManager().bind(RealmsMainScreen.CONFIGURE_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float8 = boolean7 ? 28.0f : 0.0f;
        GuiComponent.blit(dfj, integer2, integer3, float8, 0.0f, 28, 28, 56, 28);
        if (boolean7) {
            this.setTooltip(RealmsMainScreen.CONFIGURE_SERVER_TOOLTIP);
            this.hoveredElement = HoveredElement.CONFIGURE;
        }
    }
    
    protected void renderMousehoverTooltip(final PoseStack dfj, final List<Component> list, final int integer3, final int integer4) {
        if (list.isEmpty()) {
            return;
        }
        int integer5 = 0;
        int integer6 = 0;
        for (final Component nr9 : list) {
            final int integer7 = this.font.width(nr9);
            if (integer7 > integer6) {
                integer6 = integer7;
            }
        }
        int integer8 = integer3 - integer6 - 5;
        final int integer9 = integer4;
        if (integer8 < 0) {
            integer8 = integer3 + 12;
        }
        for (final Component nr10 : list) {
            final int integer10 = integer9 - ((integer5 == 0) ? 3 : 0) + integer5;
            this.fillGradient(dfj, integer8 - 3, integer10, integer8 + integer6 + 3, integer9 + 8 + 3 + integer5, -1073741824, -1073741824);
            this.font.drawShadow(dfj, nr10, (float)integer8, (float)(integer9 + integer5), 16777215);
            integer5 += 10;
        }
    }
    
    private void renderMoreInfo(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final boolean boolean6) {
        boolean boolean7 = false;
        if (integer2 >= integer4 && integer2 <= integer4 + 20 && integer3 >= integer5 && integer3 <= integer5 + 20) {
            boolean7 = true;
        }
        this.minecraft.getTextureManager().bind(RealmsMainScreen.QUESTIONMARK_LOCATION);
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        final float float9 = boolean6 ? 20.0f : 0.0f;
        GuiComponent.blit(dfj, integer4, integer5, float9, 0.0f, 20, 20, 40, 20);
        if (boolean7) {
            this.setTooltip(RealmsMainScreen.SERVER_INFO_TOOLTIP);
        }
    }
    
    private void renderNews(final PoseStack dfj, final int integer2, final int integer3, final boolean boolean4, final int integer5, final int integer6, final boolean boolean7, final boolean boolean8) {
        boolean boolean9 = false;
        if (integer2 >= integer5 && integer2 <= integer5 + 20 && integer3 >= integer6 && integer3 <= integer6 + 20) {
            boolean9 = true;
        }
        this.minecraft.getTextureManager().bind(RealmsMainScreen.NEWS_LOCATION);
        if (boolean8) {
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        }
        else {
            RenderSystem.color4f(0.5f, 0.5f, 0.5f, 1.0f);
        }
        final boolean boolean10 = boolean8 && boolean7;
        final float float12 = boolean10 ? 20.0f : 0.0f;
        GuiComponent.blit(dfj, integer5, integer6, float12, 0.0f, 20, 20, 40, 20);
        if (boolean9 && boolean8) {
            this.setTooltip(RealmsMainScreen.NEWS_TOOLTIP);
        }
        if (boolean4 && boolean8) {
            final int integer7 = boolean9 ? 0 : ((int)(Math.max(0.0f, Math.max(Mth.sin((10 + this.animTick) * 0.57f), Mth.cos(this.animTick * 0.35f))) * -6.0f));
            this.minecraft.getTextureManager().bind(RealmsMainScreen.INVITATION_ICONS_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer5 + 10, integer6 + 2 + integer7, 40.0f, 0.0f, 8, 8, 48, 16);
        }
    }
    
    private void renderLocal(final PoseStack dfj) {
        final String string3 = "LOCAL!";
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)(this.width / 2 - 25), 20.0f, 0.0f);
        RenderSystem.rotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        RenderSystem.scalef(1.5f, 1.5f, 1.5f);
        this.font.draw(dfj, "LOCAL!", 0.0f, 0.0f, 8388479);
        RenderSystem.popMatrix();
    }
    
    private void renderStage(final PoseStack dfj) {
        final String string3 = "STAGE!";
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float)(this.width / 2 - 25), 20.0f, 0.0f);
        RenderSystem.rotatef(-20.0f, 0.0f, 0.0f, 1.0f);
        RenderSystem.scalef(1.5f, 1.5f, 1.5f);
        this.font.draw(dfj, "STAGE!", 0.0f, 0.0f, -256);
        RenderSystem.popMatrix();
    }
    
    public RealmsMainScreen newScreen() {
        final RealmsMainScreen dft2 = new RealmsMainScreen(this.lastScreen);
        dft2.init(this.minecraft, this.width, this.height);
        return dft2;
    }
    
    public static void updateTeaserImages(final ResourceManager acf) {
        final Collection<ResourceLocation> collection2 = acf.listResources("textures/gui/images", (Predicate<String>)(string -> string.endsWith(".png")));
        RealmsMainScreen.teaserImages = (List<ResourceLocation>)collection2.stream().filter(vk -> vk.getNamespace().equals("realms")).collect(ImmutableList.toImmutableList());
    }
    
    private void setTooltip(final Component... arr) {
        this.toolTip = (List<Component>)Arrays.asList((Object[])arr);
    }
    
    private void pendingButtonPress(final Button dlg) {
        this.minecraft.setScreen(new RealmsPendingInvitesScreen(this.lastScreen));
    }
    
    static {
        LOGGER = LogManager.getLogger();
        ON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/on_icon.png");
        OFF_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/off_icon.png");
        EXPIRED_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expired_icon.png");
        EXPIRES_SOON_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/expires_soon_icon.png");
        LEAVE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/leave_icon.png");
        INVITATION_ICONS_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invitation_icons.png");
        INVITE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invite_icon.png");
        WORLDICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/world_icon.png");
        LOGO_LOCATION = new ResourceLocation("realms", "textures/gui/title/realms.png");
        CONFIGURE_LOCATION = new ResourceLocation("realms", "textures/gui/realms/configure_icon.png");
        QUESTIONMARK_LOCATION = new ResourceLocation("realms", "textures/gui/realms/questionmark.png");
        NEWS_LOCATION = new ResourceLocation("realms", "textures/gui/realms/news_icon.png");
        POPUP_LOCATION = new ResourceLocation("realms", "textures/gui/realms/popup.png");
        DARKEN_LOCATION = new ResourceLocation("realms", "textures/gui/realms/darken.png");
        CROSS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/cross_icon.png");
        TRIAL_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/trial_icon.png");
        BUTTON_LOCATION = new ResourceLocation("minecraft", "textures/gui/widgets.png");
        NO_PENDING_INVITES_TEXT = new TranslatableComponent("mco.invites.nopending");
        PENDING_INVITES_TEXT = new TranslatableComponent("mco.invites.pending");
        TRIAL_MESSAGE_LINES = (List)ImmutableList.of(new TranslatableComponent("mco.trial.message.line1"), new TranslatableComponent("mco.trial.message.line2"));
        SERVER_UNITIALIZED_TEXT = new TranslatableComponent("mco.selectServer.uninitialized");
        SUBSCRIPTION_EXPIRED_TEXT = new TranslatableComponent("mco.selectServer.expiredList");
        SUBSCRIPTION_RENEW_TEXT = new TranslatableComponent("mco.selectServer.expiredRenew");
        TRIAL_EXPIRED_TEXT = new TranslatableComponent("mco.selectServer.expiredTrial");
        SUBSCRIPTION_CREATE_TEXT = new TranslatableComponent("mco.selectServer.expiredSubscribe");
        SELECT_MINIGAME_PREFIX = new TranslatableComponent("mco.selectServer.minigame").append(" ");
        POPUP_TEXT = new TranslatableComponent("mco.selectServer.popup");
        SERVER_EXPIRED_TOOLTIP = new TranslatableComponent("mco.selectServer.expired");
        SERVER_EXPIRES_SOON_TOOLTIP = new TranslatableComponent("mco.selectServer.expires.soon");
        SERVER_EXPIRES_IN_DAY_TOOLTIP = new TranslatableComponent("mco.selectServer.expires.day");
        SERVER_OPEN_TOOLTIP = new TranslatableComponent("mco.selectServer.open");
        SERVER_CLOSED_TOOLTIP = new TranslatableComponent("mco.selectServer.closed");
        LEAVE_SERVER_TOOLTIP = new TranslatableComponent("mco.selectServer.leave");
        CONFIGURE_SERVER_TOOLTIP = new TranslatableComponent("mco.selectServer.configure");
        SERVER_INFO_TOOLTIP = new TranslatableComponent("mco.selectServer.info");
        NEWS_TOOLTIP = new TranslatableComponent("mco.news");
        RealmsMainScreen.teaserImages = (List<ResourceLocation>)ImmutableList.of();
        REALMS_DATA_FETCHER = new RealmsDataFetcher();
        RealmsMainScreen.lastScrollYPosition = -1;
    }
    
    enum HoveredElement {
        NONE, 
        EXPIRED, 
        LEAVE, 
        CONFIGURE;
    }
    
    class RealmSelectionList extends RealmsObjectSelectionList<RealmsMainScreen.Entry> {
        private boolean showingMessage;
        
        public RealmSelectionList() {
            super(RealmsMainScreen.this.width, RealmsMainScreen.this.height, 32, RealmsMainScreen.this.height - 40, 36);
        }
        
        @Override
        public void clear() {
            super.clear();
            this.showingMessage = false;
        }
        
        public int addMessageEntry(final RealmsMainScreen.Entry b) {
            this.showingMessage = true;
            return this.addEntry(b);
        }
        
        public boolean isFocused() {
            return RealmsMainScreen.this.getFocused() == this;
        }
        
        @Override
        public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
            if (integer1 != 257 && integer1 != 32 && integer1 != 335) {
                return super.keyPressed(integer1, integer2, integer3);
            }
            final ObjectSelectionList.Entry a5 = ((AbstractSelectionList<ObjectSelectionList.Entry>)this).getSelected();
            if (a5 == null) {
                return super.keyPressed(integer1, integer2, integer3);
            }
            return a5.mouseClicked(0.0, 0.0, 0);
        }
        
        @Override
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (integer == 0 && double1 < this.getScrollbarPosition() && double2 >= this.y0 && double2 <= this.y1) {
                final int integer2 = RealmsMainScreen.this.realmSelectionList.getRowLeft();
                final int integer3 = this.getScrollbarPosition();
                final int integer4 = (int)Math.floor(double2 - this.y0) - this.headerHeight + (int)this.getScrollAmount() - 4;
                final int integer5 = integer4 / this.itemHeight;
                if (double1 >= integer2 && double1 <= integer3 && integer5 >= 0 && integer4 >= 0 && integer5 < this.getItemCount()) {
                    this.itemClicked(integer4, integer5, double1, double2, this.width);
                    RealmsMainScreen.this.clicks += 7;
                    this.selectItem(integer5);
                }
                return true;
            }
            return super.mouseClicked(double1, double2, integer);
        }
        
        @Override
        public void selectItem(final int integer) {
            this.setSelectedItem(integer);
            if (integer == -1) {
                return;
            }
            RealmsServer dgn3;
            if (this.showingMessage) {
                if (integer == 0) {
                    NarrationHelper.now(I18n.get("mco.trial.message.line1"), I18n.get("mco.trial.message.line2"));
                    dgn3 = null;
                }
                else {
                    if (integer - 1 >= RealmsMainScreen.this.realmsServers.size()) {
                        RealmsMainScreen.this.selectedServerId = -1L;
                        return;
                    }
                    dgn3 = (RealmsServer)RealmsMainScreen.this.realmsServers.get(integer - 1);
                }
            }
            else {
                if (integer >= RealmsMainScreen.this.realmsServers.size()) {
                    RealmsMainScreen.this.selectedServerId = -1L;
                    return;
                }
                dgn3 = (RealmsServer)RealmsMainScreen.this.realmsServers.get(integer);
            }
            RealmsMainScreen.this.updateButtonStates(dgn3);
            if (dgn3 == null) {
                RealmsMainScreen.this.selectedServerId = -1L;
                return;
            }
            if (dgn3.state == RealmsServer.State.UNINITIALIZED) {
                NarrationHelper.now(I18n.get("mco.selectServer.uninitialized") + I18n.get("mco.gui.button"));
                RealmsMainScreen.this.selectedServerId = -1L;
                return;
            }
            RealmsMainScreen.this.selectedServerId = dgn3.id;
            if (RealmsMainScreen.this.clicks >= 10 && RealmsMainScreen.this.playButton.active) {
                RealmsMainScreen.this.play(RealmsMainScreen.this.findServer(RealmsMainScreen.this.selectedServerId), RealmsMainScreen.this);
            }
            NarrationHelper.now(I18n.get("narrator.select", dgn3.name));
        }
        
        @Override
        public void setSelected(@Nullable final RealmsMainScreen.Entry b) {
            super.setSelected(b);
            final int integer3 = this.children().indexOf(b);
            if (!this.showingMessage || integer3 > 0) {
                final RealmsServer dgn4 = (RealmsServer)RealmsMainScreen.this.realmsServers.get(integer3 - (this.showingMessage ? 1 : 0));
                RealmsMainScreen.this.selectedServerId = dgn4.id;
                RealmsMainScreen.this.updateButtonStates(dgn4);
            }
        }
        
        @Override
        public void itemClicked(final int integer1, int integer2, final double double3, final double double4, final int integer5) {
            if (this.showingMessage) {
                if (integer2 == 0) {
                    RealmsMainScreen.this.popupOpenedByUser = true;
                    return;
                }
                --integer2;
            }
            if (integer2 >= RealmsMainScreen.this.realmsServers.size()) {
                return;
            }
            final RealmsServer dgn9 = (RealmsServer)RealmsMainScreen.this.realmsServers.get(integer2);
            if (dgn9 == null) {
                return;
            }
            if (dgn9.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.selectedServerId = -1L;
                Minecraft.getInstance().setScreen(new RealmsCreateRealmScreen(dgn9, RealmsMainScreen.this));
            }
            else {
                RealmsMainScreen.this.selectedServerId = dgn9.id;
            }
            if (RealmsMainScreen.this.hoveredElement == HoveredElement.CONFIGURE) {
                RealmsMainScreen.this.selectedServerId = dgn9.id;
                RealmsMainScreen.this.configureClicked(dgn9);
            }
            else if (RealmsMainScreen.this.hoveredElement == HoveredElement.LEAVE) {
                RealmsMainScreen.this.selectedServerId = dgn9.id;
                RealmsMainScreen.this.leaveClicked(dgn9);
            }
            else if (RealmsMainScreen.this.hoveredElement == HoveredElement.EXPIRED) {
                RealmsMainScreen.this.onRenew();
            }
        }
        
        @Override
        public int getMaxPosition() {
            return this.getItemCount() * 36;
        }
        
        @Override
        public int getRowWidth() {
            return 300;
        }
    }
    
    abstract class Entry extends ObjectSelectionList.Entry<Entry> {
        private Entry() {
        }
    }
    
    class TrialEntry extends Entry {
        private TrialEntry() {
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderTrialItem(dfj, integer2, integer4, integer3, integer7, integer8);
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            RealmsMainScreen.this.popupOpenedByUser = true;
            return true;
        }
        
        private void renderTrialItem(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6) {
            final int integer7 = integer4 + 8;
            int integer8 = 0;
            boolean boolean10 = false;
            if (integer3 <= integer5 && integer5 <= (int)RealmsMainScreen.this.realmSelectionList.getScrollAmount() && integer4 <= integer6 && integer6 <= integer4 + 32) {
                boolean10 = true;
            }
            int integer9 = 8388479;
            if (boolean10 && !RealmsMainScreen.this.shouldShowPopup()) {
                integer9 = 6077788;
            }
            for (final Component nr13 : RealmsMainScreen.TRIAL_MESSAGE_LINES) {
                GuiComponent.drawCenteredString(dfj, RealmsMainScreen.this.font, nr13, RealmsMainScreen.this.width / 2, integer7 + integer8, integer9);
                integer8 += 10;
            }
        }
    }
    
    class ServerEntry extends Entry {
        private final RealmsServer serverData;
        
        public ServerEntry(final RealmsServer dgn) {
            this.serverData = dgn;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            this.renderMcoServerItem(this.serverData, dfj, integer4, integer3, integer7, integer8);
        }
        
        public boolean mouseClicked(final double double1, final double double2, final int integer) {
            if (this.serverData.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.selectedServerId = -1L;
                RealmsMainScreen.this.minecraft.setScreen(new RealmsCreateRealmScreen(this.serverData, RealmsMainScreen.this));
            }
            else {
                RealmsMainScreen.this.selectedServerId = this.serverData.id;
            }
            return true;
        }
        
        private void renderMcoServerItem(final RealmsServer dgn, final PoseStack dfj, final int integer3, final int integer4, final int integer5, final int integer6) {
            this.renderLegacy(dgn, dfj, integer3 + 36, integer4, integer5, integer6);
        }
        
        private void renderLegacy(final RealmsServer dgn, final PoseStack dfj, final int integer3, final int integer4, final int integer5, final int integer6) {
            if (dgn.state == RealmsServer.State.UNINITIALIZED) {
                RealmsMainScreen.this.minecraft.getTextureManager().bind(RealmsMainScreen.WORLDICON_LOCATION);
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.enableAlphaTest();
                GuiComponent.blit(dfj, integer3 + 10, integer4 + 6, 0.0f, 0.0f, 40, 20, 40, 20);
                final float float8 = 0.5f + (1.0f + Mth.sin(RealmsMainScreen.this.animTick * 0.25f)) * 0.25f;
                final int integer7 = 0xFF000000 | (int)(127.0f * float8) << 16 | (int)(255.0f * float8) << 8 | (int)(127.0f * float8);
                GuiComponent.drawCenteredString(dfj, RealmsMainScreen.this.font, RealmsMainScreen.SERVER_UNITIALIZED_TEXT, integer3 + 10 + 40 + 75, integer4 + 12, integer7);
                return;
            }
            final int integer8 = 225;
            final int integer7 = 2;
            if (dgn.expired) {
                RealmsMainScreen.this.drawExpired(dfj, integer3 + 225 - 14, integer4 + 2, integer5, integer6);
            }
            else if (dgn.state == RealmsServer.State.CLOSED) {
                RealmsMainScreen.this.drawClose(dfj, integer3 + 225 - 14, integer4 + 2, integer5, integer6);
            }
            else if (RealmsMainScreen.this.isSelfOwnedServer(dgn) && dgn.daysLeft < 7) {
                RealmsMainScreen.this.drawExpiring(dfj, integer3 + 225 - 14, integer4 + 2, integer5, integer6, dgn.daysLeft);
            }
            else if (dgn.state == RealmsServer.State.OPEN) {
                RealmsMainScreen.this.drawOpen(dfj, integer3 + 225 - 14, integer4 + 2, integer5, integer6);
            }
            if (!RealmsMainScreen.this.isSelfOwnedServer(dgn) && !RealmsMainScreen.overrideConfigure) {
                RealmsMainScreen.this.drawLeave(dfj, integer3 + 225, integer4 + 2, integer5, integer6);
            }
            else {
                RealmsMainScreen.this.drawConfigure(dfj, integer3 + 225, integer4 + 2, integer5, integer6);
            }
            if (!"0".equals(dgn.serverPing.nrOfPlayers)) {
                final String string10 = new StringBuilder().append(ChatFormatting.GRAY).append("").append(dgn.serverPing.nrOfPlayers).toString();
                RealmsMainScreen.this.font.draw(dfj, string10, (float)(integer3 + 207 - RealmsMainScreen.this.font.width(string10)), (float)(integer4 + 3), 8421504);
                if (integer5 >= integer3 + 207 - RealmsMainScreen.this.font.width(string10) && integer5 <= integer3 + 207 && integer6 >= integer4 + 1 && integer6 <= integer4 + 10 && integer6 < RealmsMainScreen.this.height - 40 && integer6 > 32 && !RealmsMainScreen.this.shouldShowPopup()) {
                    RealmsMainScreen.this.setTooltip(new TextComponent(dgn.serverPing.playerList));
                }
            }
            if (RealmsMainScreen.this.isSelfOwnedServer(dgn) && dgn.expired) {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                RenderSystem.enableBlend();
                RealmsMainScreen.this.minecraft.getTextureManager().bind(RealmsMainScreen.BUTTON_LOCATION);
                RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                Component nr10;
                Component nr11;
                if (dgn.expiredTrial) {
                    nr10 = RealmsMainScreen.TRIAL_EXPIRED_TEXT;
                    nr11 = RealmsMainScreen.SUBSCRIPTION_CREATE_TEXT;
                }
                else {
                    nr10 = RealmsMainScreen.SUBSCRIPTION_EXPIRED_TEXT;
                    nr11 = RealmsMainScreen.SUBSCRIPTION_RENEW_TEXT;
                }
                final int integer9 = RealmsMainScreen.this.font.width(nr11) + 17;
                final int integer10 = 16;
                final int integer11 = integer3 + RealmsMainScreen.this.font.width(nr10) + 8;
                final int integer12 = integer4 + 13;
                boolean boolean16 = false;
                if (integer5 >= integer11 && integer5 < integer11 + integer9 && integer6 > integer12 && (integer6 <= integer12 + 16 & integer6 < RealmsMainScreen.this.height - 40) && integer6 > 32 && !RealmsMainScreen.this.shouldShowPopup()) {
                    boolean16 = true;
                    RealmsMainScreen.this.hoveredElement = HoveredElement.EXPIRED;
                }
                final int integer13 = boolean16 ? 2 : 1;
                GuiComponent.blit(dfj, integer11, integer12, 0.0f, (float)(46 + integer13 * 20), integer9 / 2, 8, 256, 256);
                GuiComponent.blit(dfj, integer11 + integer9 / 2, integer12, (float)(200 - integer9 / 2), (float)(46 + integer13 * 20), integer9 / 2, 8, 256, 256);
                GuiComponent.blit(dfj, integer11, integer12 + 8, 0.0f, (float)(46 + integer13 * 20 + 12), integer9 / 2, 8, 256, 256);
                GuiComponent.blit(dfj, integer11 + integer9 / 2, integer12 + 8, (float)(200 - integer9 / 2), (float)(46 + integer13 * 20 + 12), integer9 / 2, 8, 256, 256);
                RenderSystem.disableBlend();
                final int integer14 = integer4 + 11 + 5;
                final int integer15 = boolean16 ? 16777120 : 16777215;
                RealmsMainScreen.this.font.draw(dfj, nr10, (float)(integer3 + 2), (float)(integer14 + 1), 15553363);
                GuiComponent.drawCenteredString(dfj, RealmsMainScreen.this.font, nr11, integer11 + integer9 / 2, integer14 + 1, integer15);
            }
            else {
                if (dgn.worldType == RealmsServer.WorldType.MINIGAME) {
                    final int integer16 = 13413468;
                    final int integer17 = RealmsMainScreen.this.font.width(RealmsMainScreen.SELECT_MINIGAME_PREFIX);
                    RealmsMainScreen.this.font.draw(dfj, RealmsMainScreen.SELECT_MINIGAME_PREFIX, (float)(integer3 + 2), (float)(integer4 + 12), 13413468);
                    RealmsMainScreen.this.font.draw(dfj, dgn.getMinigameName(), (float)(integer3 + 2 + integer17), (float)(integer4 + 12), 7105644);
                }
                else {
                    RealmsMainScreen.this.font.draw(dfj, dgn.getDescription(), (float)(integer3 + 2), (float)(integer4 + 12), 7105644);
                }
                if (!RealmsMainScreen.this.isSelfOwnedServer(dgn)) {
                    RealmsMainScreen.this.font.draw(dfj, dgn.owner, (float)(integer3 + 2), (float)(integer4 + 12 + 11), 5000268);
                }
            }
            RealmsMainScreen.this.font.draw(dfj, dgn.getName(), (float)(integer3 + 2), (float)(integer4 + 1), 16777215);
            RealmsTextureManager.withBoundFace(dgn.ownerUUID, () -> {
                RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
                GuiComponent.blit(dfj, integer3 - 36, integer4, 32, 32, 8.0f, 8.0f, 8, 8, 64, 64);
                GuiComponent.blit(dfj, integer3 - 36, integer4, 32, 32, 40.0f, 8.0f, 8, 8, 64, 64);
            });
        }
    }
    
    class PendingInvitesButton extends Button implements TickableWidget {
        public PendingInvitesButton() {
            super(RealmsMainScreen.this.width / 2 + 47, 6, 22, 22, TextComponent.EMPTY, dlg -> RealmsMainScreen.this.pendingButtonPress(dlg));
        }
        
        @Override
        public void tick() {
            this.setMessage(new TranslatableComponent((RealmsMainScreen.this.numberOfPendingInvites == 0) ? "mco.invites.nopending" : "mco.invites.pending"));
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RealmsMainScreen.this.drawInvitationPendingIcon(dfj, integer2, integer3, this.x, this.y, this.isHovered(), this.active);
        }
    }
    
    class NewsButton extends Button {
        public NewsButton() {
            RealmsPersistence.RealmsPersistenceData a3;
            super(RealmsMainScreen.this.width - 62, 6, 20, 20, TextComponent.EMPTY, dlg -> {
                if (RealmsMainScreen.this.newsLink == null) {
                    return;
                }
                else {
                    Util.getPlatform().openUri(RealmsMainScreen.this.newsLink);
                    if (RealmsMainScreen.this.hasUnreadNews) {
                        a3 = RealmsPersistence.readFile();
                        RealmsMainScreen.this.hasUnreadNews = (a3.hasUnreadNews = false);
                        RealmsPersistence.writeFile(a3);
                    }
                    return;
                }
            });
            this.setMessage(new TranslatableComponent("mco.news"));
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RealmsMainScreen.this.renderNews(dfj, integer2, integer3, RealmsMainScreen.this.hasUnreadNews, this.x, this.y, this.isHovered(), this.active);
        }
    }
    
    class ShowPopupButton extends Button {
        public ShowPopupButton() {
            super(RealmsMainScreen.this.width - 37, 6, 20, 20, new TranslatableComponent("mco.selectServer.info"), dlg -> RealmsMainScreen.this.popupOpenedByUser = !RealmsMainScreen.this.popupOpenedByUser);
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RealmsMainScreen.this.renderMoreInfo(dfj, integer2, integer3, this.x, this.y, this.isHovered());
        }
    }
    
    class CloseButton extends Button {
        public CloseButton() {
            super(RealmsMainScreen.this.popupX0() + 4, RealmsMainScreen.this.popupY0() + 4, 12, 12, new TranslatableComponent("mco.selectServer.close"), dlg -> RealmsMainScreen.this.onClosePopup());
        }
        
        @Override
        public void renderButton(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
            RealmsMainScreen.this.minecraft.getTextureManager().bind(RealmsMainScreen.CROSS_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            final float float5 = this.isHovered() ? 12.0f : 0.0f;
            GuiComponent.blit(dfj, this.x, this.y, 0.0f, float5, 12, 12, 12, 24);
            if (this.isMouseOver(integer2, integer3)) {
                RealmsMainScreen.this.setTooltip(this.getMessage());
            }
        }
    }
}
