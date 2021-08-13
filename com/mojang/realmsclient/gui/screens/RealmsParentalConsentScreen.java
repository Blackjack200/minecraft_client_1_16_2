package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.Util;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsParentalConsentScreen extends RealmsScreen {
    private static final Component MESSAGE;
    private final Screen nextScreen;
    private MultiLineLabel messageLines;
    
    public RealmsParentalConsentScreen(final Screen doq) {
        this.messageLines = MultiLineLabel.EMPTY;
        this.nextScreen = doq;
    }
    
    public void init() {
        NarrationHelper.now(RealmsParentalConsentScreen.MESSAGE.getString());
        final Component nr2 = new TranslatableComponent("mco.account.update");
        final Component nr3 = CommonComponents.GUI_BACK;
        final int integer4 = Math.max(this.font.width(nr2), this.font.width(nr3)) + 30;
        final Component nr4 = new TranslatableComponent("mco.account.privacy.info");
        final int integer5 = (int)(this.font.width(nr4) * 1.2);
        this.<Button>addButton(new Button(this.width / 2 - integer5 / 2, RealmsScreen.row(11), integer5, 20, nr4, dlg -> Util.getPlatform().openUri("https://minecraft.net/privacy/gdpr/")));
        this.<Button>addButton(new Button(this.width / 2 - (integer4 + 5), RealmsScreen.row(13), integer4, 20, nr2, dlg -> Util.getPlatform().openUri("https://minecraft.net/update-account")));
        this.<Button>addButton(new Button(this.width / 2 + 5, RealmsScreen.row(13), integer4, 20, nr3, dlg -> this.minecraft.setScreen(this.nextScreen)));
        this.messageLines = MultiLineLabel.create(this.font, RealmsParentalConsentScreen.MESSAGE, (int)Math.round(this.width * 0.9));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.messageLines.renderCentered(dfj, this.width / 2, 15, 15, 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        MESSAGE = new TranslatableComponent("mco.account.privacyinfo");
    }
}
