package com.mojang.realmsclient.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.realms.NarrationHelper;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TextComponent;
import com.mojang.realmsclient.exception.RealmsServiceException;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.realms.RealmsScreen;

public class RealmsGenericErrorScreen extends RealmsScreen {
    private final Screen nextScreen;
    private Component line1;
    private Component line2;
    
    public RealmsGenericErrorScreen(final RealmsServiceException dhf, final Screen doq) {
        this.nextScreen = doq;
        this.errorMessage(dhf);
    }
    
    public RealmsGenericErrorScreen(final Component nr, final Screen doq) {
        this.nextScreen = doq;
        this.errorMessage(nr);
    }
    
    public RealmsGenericErrorScreen(final Component nr1, final Component nr2, final Screen doq) {
        this.nextScreen = doq;
        this.errorMessage(nr1, nr2);
    }
    
    private void errorMessage(final RealmsServiceException dhf) {
        if (dhf.errorCode == -1) {
            this.line1 = new TextComponent(new StringBuilder().append("An error occurred (").append(dhf.httpResultCode).append("):").toString());
            this.line2 = new TextComponent(dhf.httpResponseContent);
        }
        else {
            this.line1 = new TextComponent(new StringBuilder().append("Realms (").append(dhf.errorCode).append("):").toString());
            final String string3 = new StringBuilder().append("mco.errorMessage.").append(dhf.errorCode).toString();
            this.line2 = (I18n.exists(string3) ? new TranslatableComponent(string3) : Component.nullToEmpty(dhf.errorMsg));
        }
    }
    
    private void errorMessage(final Component nr) {
        this.line1 = new TextComponent("An error occurred: ");
        this.line2 = nr;
    }
    
    private void errorMessage(final Component nr1, final Component nr2) {
        this.line1 = nr1;
        this.line2 = nr2;
    }
    
    public void init() {
        NarrationHelper.now(this.line1.getString() + ": " + this.line2.getString());
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height - 52, 200, 20, new TextComponent("Ok"), dlg -> this.minecraft.setScreen(this.nextScreen)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.line1, this.width / 2, 80, 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, this.line2, this.width / 2, 100, 16711680);
        super.render(dfj, integer2, integer3, float4);
    }
}
