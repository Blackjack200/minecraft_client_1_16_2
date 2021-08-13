package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.realmsclient.util.task.LongRunningTask;
import com.mojang.realmsclient.util.task.WorldCreationTask;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import com.mojang.realmsclient.RealmsMainScreen;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsCreateRealmScreen extends RealmsScreen {
    private static final Component NAME_LABEL;
    private static final Component DESCRIPTION_LABEL;
    private final RealmsServer server;
    private final RealmsMainScreen lastScreen;
    private EditBox nameBox;
    private EditBox descriptionBox;
    private Button createButton;
    private RealmsLabel createRealmLabel;
    
    public RealmsCreateRealmScreen(final RealmsServer dgn, final RealmsMainScreen dft) {
        this.server = dgn;
        this.lastScreen = dft;
    }
    
    @Override
    public void tick() {
        if (this.nameBox != null) {
            this.nameBox.tick();
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.tick();
        }
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.createButton = this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 17, 97, 20, new TranslatableComponent("mco.create.world"), dlg -> this.createWorld()));
        this.<Button>addButton(new Button(this.width / 2 + 5, this.height / 4 + 120 + 17, 95, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.createButton.active = false;
        this.<EditBox>addWidget(this.nameBox = new EditBox(this.minecraft.font, this.width / 2 - 100, 65, 200, 20, null, new TranslatableComponent("mco.configure.world.name")));
        this.setInitialFocus(this.nameBox);
        this.<EditBox>addWidget(this.descriptionBox = new EditBox(this.minecraft.font, this.width / 2 - 100, 115, 200, 20, null, new TranslatableComponent("mco.configure.world.description")));
        this.<RealmsLabel>addWidget(this.createRealmLabel = new RealmsLabel(new TranslatableComponent("mco.selectServer.create"), this.width / 2, 11, 16777215));
        this.narrateLabels();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean charTyped(final char character, final int integer) {
        final boolean boolean4 = super.charTyped(character, integer);
        this.createButton.active = this.valid();
        return boolean4;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.lastScreen);
            return true;
        }
        final boolean boolean5 = super.keyPressed(integer1, integer2, integer3);
        this.createButton.active = this.valid();
        return boolean5;
    }
    
    private void createWorld() {
        if (this.valid()) {
            final RealmsResetWorldScreen dic2 = new RealmsResetWorldScreen(this.lastScreen, this.server, new TranslatableComponent("mco.selectServer.create"), new TranslatableComponent("mco.create.world.subtitle"), 10526880, new TranslatableComponent("mco.create.world.skip"), () -> this.minecraft.setScreen(this.lastScreen.newScreen()), () -> this.minecraft.setScreen(this.lastScreen.newScreen()));
            dic2.setResetTitle(new TranslatableComponent("mco.create.world.reset.title"));
            this.minecraft.setScreen(new RealmsLongRunningMcoTaskScreen(this.lastScreen, new WorldCreationTask(this.server.id, this.nameBox.getValue(), this.descriptionBox.getValue(), dic2)));
        }
    }
    
    private boolean valid() {
        return !this.nameBox.getValue().trim().isEmpty();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.createRealmLabel.render(this, dfj);
        this.font.draw(dfj, RealmsCreateRealmScreen.NAME_LABEL, (float)(this.width / 2 - 100), 52.0f, 10526880);
        this.font.draw(dfj, RealmsCreateRealmScreen.DESCRIPTION_LABEL, (float)(this.width / 2 - 100), 102.0f, 10526880);
        if (this.nameBox != null) {
            this.nameBox.render(dfj, integer2, integer3, float4);
        }
        if (this.descriptionBox != null) {
            this.descriptionBox.render(dfj, integer2, integer3, float4);
        }
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        NAME_LABEL = new TranslatableComponent("mco.configure.world.name");
        DESCRIPTION_LABEL = new TranslatableComponent("mco.configure.world.description");
    }
}
