package com.mojang.realmsclient.gui.screens;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.screens.TitleScreen;
import com.mojang.realmsclient.gui.RealmsDataFetcher;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.realms.RealmsScreen;

public class RealmsNotificationsScreen extends RealmsScreen {
    private static final ResourceLocation INVITE_ICON_LOCATION;
    private static final ResourceLocation TRIAL_ICON_LOCATION;
    private static final ResourceLocation NEWS_ICON_LOCATION;
    private static final RealmsDataFetcher REALMS_DATA_FETCHER;
    private volatile int numberOfPendingInvites;
    private static boolean checkedMcoAvailability;
    private static boolean trialAvailable;
    private static boolean validClient;
    private static boolean hasUnreadNews;
    
    public void init() {
        this.checkIfMcoEnabled();
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
    }
    
    @Override
    public void tick() {
        if ((!this.getRealmsNotificationsEnabled() || !this.inTitleScreen() || !RealmsNotificationsScreen.validClient) && !RealmsNotificationsScreen.REALMS_DATA_FETCHER.isStopped()) {
            RealmsNotificationsScreen.REALMS_DATA_FETCHER.stop();
            return;
        }
        if (RealmsNotificationsScreen.validClient && this.getRealmsNotificationsEnabled()) {
            RealmsNotificationsScreen.REALMS_DATA_FETCHER.initWithSpecificTaskList();
            if (RealmsNotificationsScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.PENDING_INVITE)) {
                this.numberOfPendingInvites = RealmsNotificationsScreen.REALMS_DATA_FETCHER.getPendingInvitesCount();
            }
            if (RealmsNotificationsScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.TRIAL_AVAILABLE)) {
                RealmsNotificationsScreen.trialAvailable = RealmsNotificationsScreen.REALMS_DATA_FETCHER.isTrialAvailable();
            }
            if (RealmsNotificationsScreen.REALMS_DATA_FETCHER.isFetchedSinceLastTry(RealmsDataFetcher.Task.UNREAD_NEWS)) {
                RealmsNotificationsScreen.hasUnreadNews = RealmsNotificationsScreen.REALMS_DATA_FETCHER.hasUnreadNews();
            }
            RealmsNotificationsScreen.REALMS_DATA_FETCHER.markClean();
        }
    }
    
    private boolean getRealmsNotificationsEnabled() {
        return this.minecraft.options.realmsNotifications;
    }
    
    private boolean inTitleScreen() {
        return this.minecraft.screen instanceof TitleScreen;
    }
    
    private void checkIfMcoEnabled() {
        if (!RealmsNotificationsScreen.checkedMcoAvailability) {
            RealmsNotificationsScreen.checkedMcoAvailability = true;
            new Thread("Realms Notification Availability checker #1") {
                public void run() {
                    final RealmsClient dfy2 = RealmsClient.create();
                    try {
                        final RealmsClient.CompatibleVersionResponse a3 = dfy2.clientCompatible();
                        if (a3 != RealmsClient.CompatibleVersionResponse.COMPATIBLE) {
                            return;
                        }
                    }
                    catch (RealmsServiceException dhf3) {
                        if (dhf3.httpResultCode != 401) {
                            RealmsNotificationsScreen.checkedMcoAvailability = false;
                        }
                        return;
                    }
                    RealmsNotificationsScreen.validClient = true;
                }
            }.start();
        }
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (RealmsNotificationsScreen.validClient) {
            this.drawIcons(dfj, integer2, integer3);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    private void drawIcons(final PoseStack dfj, final int integer2, final int integer3) {
        final int integer4 = this.numberOfPendingInvites;
        final int integer5 = 24;
        final int integer6 = this.height / 4 + 48;
        final int integer7 = this.width / 2 + 80;
        final int integer8 = integer6 + 48 + 2;
        int integer9 = 0;
        if (RealmsNotificationsScreen.hasUnreadNews) {
            this.minecraft.getTextureManager().bind(RealmsNotificationsScreen.NEWS_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.pushMatrix();
            RenderSystem.scalef(0.4f, 0.4f, 0.4f);
            GuiComponent.blit(dfj, (int)((integer7 + 2 - integer9) * 2.5), (int)(integer8 * 2.5), 0.0f, 0.0f, 40, 40, 40, 40);
            RenderSystem.popMatrix();
            integer9 += 14;
        }
        if (integer4 != 0) {
            this.minecraft.getTextureManager().bind(RealmsNotificationsScreen.INVITE_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            GuiComponent.blit(dfj, integer7 - integer9, integer8 - 6, 0.0f, 0.0f, 15, 25, 31, 25);
            integer9 += 16;
        }
        if (RealmsNotificationsScreen.trialAvailable) {
            this.minecraft.getTextureManager().bind(RealmsNotificationsScreen.TRIAL_ICON_LOCATION);
            RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f);
            int integer10 = 0;
            if ((Util.getMillis() / 800L & 0x1L) == 0x1L) {
                integer10 = 8;
            }
            GuiComponent.blit(dfj, integer7 + 4 - integer9, integer8 + 4, 0.0f, (float)integer10, 8, 8, 8, 16);
        }
    }
    
    @Override
    public void removed() {
        RealmsNotificationsScreen.REALMS_DATA_FETCHER.stop();
    }
    
    static {
        INVITE_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/invite_icon.png");
        TRIAL_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/trial_icon.png");
        NEWS_ICON_LOCATION = new ResourceLocation("realms", "textures/gui/realms/news_notification_mainscreen.png");
        REALMS_DATA_FETCHER = new RealmsDataFetcher();
    }
}
