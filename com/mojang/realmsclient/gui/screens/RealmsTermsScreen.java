package com.mojang.realmsclient.gui.screens;

import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import org.apache.logging.log4j.LogManager;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import com.mojang.realmsclient.exception.RealmsServiceException;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.GetServerDetailsTask;
import java.util.concurrent.locks.ReentrantLock;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.RealmsMainScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsTermsScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final Component TITLE;
    private static final Component TERMS_STATIC_TEXT;
    private static final Component TERMS_LINK_TEXT;
    private final Screen lastScreen;
    private final RealmsMainScreen mainScreen;
    private final RealmsServer realmsServer;
    private boolean onLink;
    private final String realmsToSUrl = "https://minecraft.net/realms/terms";
    
    public RealmsTermsScreen(final Screen doq, final RealmsMainScreen dft, final RealmsServer dgn) {
        this.lastScreen = doq;
        this.mainScreen = dft;
        this.realmsServer = dgn;
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        final int integer2 = this.width / 4 - 2;
        this.<Button>addButton(new Button(this.width / 4, RealmsScreen.row(12), integer2, 20, new TranslatableComponent("mco.terms.buttons.agree"), dlg -> this.agreedToTos()));
        this.<Button>addButton(new Button(this.width / 2 + 4, RealmsScreen.row(12), integer2, 20, new TranslatableComponent("mco.terms.buttons.disagree"), dlg -> this.minecraft.setScreen(this.lastScreen)));
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
    
    private void agreedToTos() {
        final RealmsClient dfy2 = RealmsClient.create();
        try {
            dfy2.agreeToTos();
            this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new GetServerDetailsTask(this.mainScreen, this.lastScreen, this.realmsServer, new ReentrantLock())));
        }
        catch (RealmsServiceException dhf3) {
            RealmsTermsScreen.LOGGER.error("Couldn't agree to TOS");
        }
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.onLink) {
            this.minecraft.keyboardHandler.setClipboard("https://minecraft.net/realms/terms");
            Util.getPlatform().openUri("https://minecraft.net/realms/terms");
            return true;
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, RealmsTermsScreen.TITLE, this.width / 2, 17, 16777215);
        this.font.draw(dfj, RealmsTermsScreen.TERMS_STATIC_TEXT, (float)(this.width / 2 - 120), (float)RealmsScreen.row(5), 16777215);
        final int integer4 = this.font.width(RealmsTermsScreen.TERMS_STATIC_TEXT);
        final int integer5 = this.width / 2 - 121 + integer4;
        final int integer6 = RealmsScreen.row(5);
        final int integer7 = integer5 + this.font.width(RealmsTermsScreen.TERMS_LINK_TEXT) + 1;
        final int n = integer6 + 1;
        this.font.getClass();
        final int integer8 = n + 9;
        this.onLink = (integer5 <= integer2 && integer2 <= integer7 && integer6 <= integer3 && integer3 <= integer8);
        this.font.draw(dfj, RealmsTermsScreen.TERMS_LINK_TEXT, (float)(this.width / 2 - 120 + integer4), (float)RealmsScreen.row(5), this.onLink ? 7107012 : 3368635);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        TITLE = new TranslatableComponent("mco.terms.title");
        TERMS_STATIC_TEXT = new TranslatableComponent("mco.terms.sentence.1");
        TERMS_LINK_TEXT = new TextComponent(" ").append(new TranslatableComponent("mco.terms.sentence.2").withStyle(Style.EMPTY.withUnderlined(true)));
    }
}
