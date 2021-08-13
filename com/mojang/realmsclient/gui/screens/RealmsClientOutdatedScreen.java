package com.mojang.realmsclient.gui.screens;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsClientOutdatedScreen extends RealmsScreen {
    private static final Component OUTDATED_TITLE;
    private static final Component[] OUTDATED_MESSAGES;
    private static final Component INCOMPATIBLE_TITLE;
    private static final Component[] INCOMPATIBLE_MESSAGES;
    private final Screen lastScreen;
    private final boolean outdated;
    
    public RealmsClientOutdatedScreen(final Screen doq, final boolean boolean2) {
        this.lastScreen = doq;
        this.outdated = boolean2;
    }
    
    public void init() {
        this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(12), 200, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        Component nr6;
        Component[] arr7;
        if (this.outdated) {
            nr6 = RealmsClientOutdatedScreen.INCOMPATIBLE_TITLE;
            arr7 = RealmsClientOutdatedScreen.INCOMPATIBLE_MESSAGES;
        }
        else {
            nr6 = RealmsClientOutdatedScreen.OUTDATED_TITLE;
            arr7 = RealmsClientOutdatedScreen.OUTDATED_MESSAGES;
        }
        GuiComponent.drawCenteredString(dfj, this.font, nr6, this.width / 2, RealmsScreen.row(3), 16711680);
        for (int integer4 = 0; integer4 < arr7.length; ++integer4) {
            GuiComponent.drawCenteredString(dfj, this.font, arr7[integer4], this.width / 2, RealmsScreen.row(5) + integer4 * 12, 16777215);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 257 || integer1 == 335 || integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    static {
        OUTDATED_TITLE = new TranslatableComponent("mco.client.outdated.title");
        OUTDATED_MESSAGES = new Component[] { new TranslatableComponent("mco.client.outdated.msg.line1"), new TranslatableComponent("mco.client.outdated.msg.line2") };
        INCOMPATIBLE_TITLE = new TranslatableComponent("mco.client.incompatible.title");
        INCOMPATIBLE_MESSAGES = new Component[] { new TranslatableComponent("mco.client.incompatible.msg.line1"), new TranslatableComponent("mco.client.incompatible.msg.line2"), new TranslatableComponent("mco.client.incompatible.msg.line3") };
    }
}
