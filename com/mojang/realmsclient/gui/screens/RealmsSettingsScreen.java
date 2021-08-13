package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Button;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsSettingsScreen extends RealmsScreen {
    private static final Component NAME_LABEL;
    private static final Component DESCRIPTION_LABEL;
    private final RealmsConfigureWorldScreen configureWorldScreen;
    private final RealmsServer serverData;
    private Button doneButton;
    private EditBox descEdit;
    private EditBox nameEdit;
    private RealmsLabel titleLabel;
    
    public RealmsSettingsScreen(final RealmsConfigureWorldScreen dhp, final RealmsServer dgn) {
        this.configureWorldScreen = dhp;
        this.serverData = dgn;
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
        this.descEdit.tick();
        this.doneButton.active = !this.nameEdit.getValue().trim().isEmpty();
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        final int integer2 = this.width / 2 - 106;
        this.doneButton = this.<Button>addButton(new Button(integer2 - 2, RealmsScreen.row(12), 106, 20, new TranslatableComponent("mco.configure.world.buttons.done"), dlg -> this.save()));
        this.<Button>addButton(new Button(this.width / 2 + 2, RealmsScreen.row(12), 106, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.configureWorldScreen)));
        final String string3 = (this.serverData.state == RealmsServer.State.OPEN) ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
        Component nr3;
        Component nr4;
        final Button dlg4 = new Button(this.width / 2 - 53, RealmsScreen.row(0), 106, 20, new TranslatableComponent(string3), dlg -> {
            if (this.serverData.state == RealmsServer.State.OPEN) {
                nr3 = new TranslatableComponent("mco.configure.world.close.question.line1");
                nr4 = new TranslatableComponent("mco.configure.world.close.question.line2");
                this.minecraft.setScreen(new RealmsLongConfirmationScreen(boolean1 -> {
                    if (boolean1) {
                        this.configureWorldScreen.closeTheWorld(this);
                    }
                    else {
                        this.minecraft.setScreen(this);
                    }
                }, RealmsLongConfirmationScreen.Type.Info, nr3, nr4, true));
            }
            else {
                this.configureWorldScreen.openTheWorld(false, this);
            }
            return;
        });
        this.<Button>addButton(dlg4);
        (this.nameEdit = new EditBox(this.minecraft.font, integer2, RealmsScreen.row(4), 212, 20, null, new TranslatableComponent("mco.configure.world.name"))).setMaxLength(32);
        this.nameEdit.setValue(this.serverData.getName());
        this.<EditBox>addWidget(this.nameEdit);
        this.magicalSpecialHackyFocus(this.nameEdit);
        (this.descEdit = new EditBox(this.minecraft.font, integer2, RealmsScreen.row(8), 212, 20, null, new TranslatableComponent("mco.configure.world.description"))).setMaxLength(32);
        this.descEdit.setValue(this.serverData.getDescription());
        this.<EditBox>addWidget(this.descEdit);
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.configure.world.settings.title"), this.width / 2, 17, 16777215));
        this.narrateLabels();
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.configureWorldScreen);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.titleLabel.render(this, dfj);
        this.font.draw(dfj, RealmsSettingsScreen.NAME_LABEL, (float)(this.width / 2 - 106), (float)RealmsScreen.row(3), 10526880);
        this.font.draw(dfj, RealmsSettingsScreen.DESCRIPTION_LABEL, (float)(this.width / 2 - 106), (float)RealmsScreen.row(7), 10526880);
        this.nameEdit.render(dfj, integer2, integer3, float4);
        this.descEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    public void save() {
        this.configureWorldScreen.saveSettings(this.nameEdit.getValue(), this.descEdit.getValue());
    }
    
    static {
        NAME_LABEL = new TranslatableComponent("mco.configure.world.name");
        DESCRIPTION_LABEL = new TranslatableComponent("mco.configure.world.description");
    }
}
