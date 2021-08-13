package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Calendar;
import java.text.DateFormat;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.NarrationHelper;
import com.mojang.realmsclient.dto.Subscription;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsSubscriptionInfoScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final Component SUBSCRIPTION_TITLE;
    private static final Component SUBSCRIPTION_START_LABEL;
    private static final Component TIME_LEFT_LABEL;
    private static final Component DAYS_LEFT_LABEL;
    private static final Component SUBSCRIPTION_EXPIRED_TEXT;
    private static final Component SUBSCRIPTION_LESS_THAN_A_DAY_TEXT;
    private static final Component MONTH_SUFFIX;
    private static final Component MONTHS_SUFFIX;
    private static final Component DAY_SUFFIX;
    private static final Component DAYS_SUFFIX;
    private final Screen lastScreen;
    private final RealmsServer serverData;
    private final Screen mainScreen;
    private Component daysLeft;
    private String startDate;
    private Subscription.SubscriptionType type;
    
    public RealmsSubscriptionInfoScreen(final Screen doq1, final RealmsServer dgn, final Screen doq3) {
        this.lastScreen = doq1;
        this.serverData = dgn;
        this.mainScreen = doq3;
    }
    
    public void init() {
        this.getSubscription(this.serverData.id);
        NarrationHelper.now(RealmsSubscriptionInfoScreen.SUBSCRIPTION_TITLE.getString(), RealmsSubscriptionInfoScreen.SUBSCRIPTION_START_LABEL.getString(), this.startDate, RealmsSubscriptionInfoScreen.TIME_LEFT_LABEL.getString(), this.daysLeft.getString());
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        final String string3;
        this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(6), 200, 20, new TranslatableComponent("mco.configure.world.subscription.extend"), dlg -> {
            string3 = "https://aka.ms/ExtendJavaRealms?subscriptionId=" + this.serverData.remoteSubscriptionId + "&profileId=" + this.minecraft.getUser().getUuid();
            this.minecraft.keyboardHandler.setClipboard(string3);
            Util.getPlatform().openUri(string3);
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(12), 200, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
        if (this.serverData.expired) {
            final Component nr3;
            final Component nr4;
            this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(10), 200, 20, new TranslatableComponent("mco.configure.world.delete.button"), dlg -> {
                nr3 = new TranslatableComponent("mco.configure.world.delete.question.line1");
                nr4 = new TranslatableComponent("mco.configure.world.delete.question.line2");
                this.minecraft.setScreen(new RealmsLongConfirmationScreen(this::deleteRealm, RealmsLongConfirmationScreen.Type.Warning, nr3, nr4, true));
            }));
        }
    }
    
    private void deleteRealm(final boolean boolean1) {
        if (boolean1) {
            new Thread("Realms-delete-realm") {
                public void run() {
                    try {
                        final RealmsClient dfy2 = RealmsClient.create();
                        dfy2.deleteWorld(RealmsSubscriptionInfoScreen.this.serverData.id);
                    }
                    catch (RealmsServiceException dhf2) {
                        RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't delete world");
                        RealmsSubscriptionInfoScreen.LOGGER.error(dhf2);
                    }
                    RealmsSubscriptionInfoScreen.this.minecraft.execute(() -> RealmsSubscriptionInfoScreen.this.minecraft.setScreen(RealmsSubscriptionInfoScreen.this.mainScreen));
                }
            }.start();
        }
        this.minecraft.setScreen(this);
    }
    
    private void getSubscription(final long long1) {
        final RealmsClient dfy4 = RealmsClient.create();
        try {
            final Subscription dgx5 = dfy4.subscriptionFor(long1);
            this.daysLeft = this.daysLeftPresentation(dgx5.daysLeft);
            this.startDate = localPresentation(dgx5.startDate);
            this.type = dgx5.type;
        }
        catch (RealmsServiceException dhf5) {
            RealmsSubscriptionInfoScreen.LOGGER.error("Couldn't get subscription");
            this.minecraft.setScreen(new RealmsGenericErrorScreen(dhf5, this.lastScreen));
        }
    }
    
    private static String localPresentation(final long long1) {
        final Calendar calendar3 = (Calendar)new GregorianCalendar(TimeZone.getDefault());
        calendar3.setTimeInMillis(long1);
        return DateFormat.getDateTimeInstance().format(calendar3.getTime());
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final int integer4 = this.width / 2 - 100;
        GuiComponent.drawCenteredString(dfj, this.font, RealmsSubscriptionInfoScreen.SUBSCRIPTION_TITLE, this.width / 2, 17, 16777215);
        this.font.draw(dfj, RealmsSubscriptionInfoScreen.SUBSCRIPTION_START_LABEL, (float)integer4, (float)RealmsScreen.row(0), 10526880);
        this.font.draw(dfj, this.startDate, (float)integer4, (float)RealmsScreen.row(1), 16777215);
        if (this.type == Subscription.SubscriptionType.NORMAL) {
            this.font.draw(dfj, RealmsSubscriptionInfoScreen.TIME_LEFT_LABEL, (float)integer4, (float)RealmsScreen.row(3), 10526880);
        }
        else if (this.type == Subscription.SubscriptionType.RECURRING) {
            this.font.draw(dfj, RealmsSubscriptionInfoScreen.DAYS_LEFT_LABEL, (float)integer4, (float)RealmsScreen.row(3), 10526880);
        }
        this.font.draw(dfj, this.daysLeft, (float)integer4, (float)RealmsScreen.row(4), 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private Component daysLeftPresentation(final int integer) {
        if (integer == -1 && this.serverData.expired) {
            return RealmsSubscriptionInfoScreen.SUBSCRIPTION_EXPIRED_TEXT;
        }
        if (integer <= 1) {
            return RealmsSubscriptionInfoScreen.SUBSCRIPTION_LESS_THAN_A_DAY_TEXT;
        }
        final int integer2 = integer / 30;
        final int integer3 = integer % 30;
        final MutableComponent nx5 = new TextComponent("");
        if (integer2 > 0) {
            nx5.append(Integer.toString(integer2)).append(" ");
            if (integer2 == 1) {
                nx5.append(RealmsSubscriptionInfoScreen.MONTH_SUFFIX);
            }
            else {
                nx5.append(RealmsSubscriptionInfoScreen.MONTHS_SUFFIX);
            }
        }
        if (integer3 > 0) {
            if (integer2 > 0) {
                nx5.append(", ");
            }
            nx5.append(Integer.toString(integer3)).append(" ");
            if (integer3 == 1) {
                nx5.append(RealmsSubscriptionInfoScreen.DAY_SUFFIX);
            }
            else {
                nx5.append(RealmsSubscriptionInfoScreen.DAYS_SUFFIX);
            }
        }
        return nx5;
    }
    
    static {
        LOGGER = LogManager.getLogger();
        SUBSCRIPTION_TITLE = new TranslatableComponent("mco.configure.world.subscription.title");
        SUBSCRIPTION_START_LABEL = new TranslatableComponent("mco.configure.world.subscription.start");
        TIME_LEFT_LABEL = new TranslatableComponent("mco.configure.world.subscription.timeleft");
        DAYS_LEFT_LABEL = new TranslatableComponent("mco.configure.world.subscription.recurring.daysleft");
        SUBSCRIPTION_EXPIRED_TEXT = new TranslatableComponent("mco.configure.world.subscription.expired");
        SUBSCRIPTION_LESS_THAN_A_DAY_TEXT = new TranslatableComponent("mco.configure.world.subscription.less_than_a_day");
        MONTH_SUFFIX = new TranslatableComponent("mco.configure.world.subscription.month");
        MONTHS_SUFFIX = new TranslatableComponent("mco.configure.world.subscription.months");
        DAY_SUFFIX = new TranslatableComponent("mco.configure.world.subscription.day");
        DAYS_SUFFIX = new TranslatableComponent("mco.configure.world.subscription.days");
    }
}
