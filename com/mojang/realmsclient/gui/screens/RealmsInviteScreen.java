package com.mojang.realmsclient.gui.screens;

import org.apache.logging.log4j.LogManager;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.realms.NarrationHelper;
import com.mojang.realmsclient.client.RealmsClient;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.TranslatableComponent;
import javax.annotation.Nullable;
import net.minecraft.client.gui.screens.Screen;
import com.mojang.realmsclient.dto.RealmsServer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Logger;
import net.minecraft.realms.RealmsScreen;

public class RealmsInviteScreen extends RealmsScreen {
    private static final Logger LOGGER;
    private static final Component NAME_LABEL;
    private static final Component NO_SUCH_PLAYER_ERROR_TEXT;
    private EditBox profileName;
    private final RealmsServer serverData;
    private final RealmsConfigureWorldScreen configureScreen;
    private final Screen lastScreen;
    @Nullable
    private Component errorMsg;
    
    public RealmsInviteScreen(final RealmsConfigureWorldScreen dhp, final Screen doq, final RealmsServer dgn) {
        this.configureScreen = dhp;
        this.lastScreen = doq;
        this.serverData = dgn;
    }
    
    @Override
    public void tick() {
        this.profileName.tick();
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.<EditBox>addWidget(this.profileName = new EditBox(this.minecraft.font, this.width / 2 - 100, RealmsScreen.row(2), 200, 20, null, new TranslatableComponent("mco.configure.world.invite.profile.name")));
        this.setInitialFocus(this.profileName);
        this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(10), 200, 20, new TranslatableComponent("mco.configure.world.buttons.invite"), dlg -> this.onInvite()));
        this.<Button>addButton(new Button(this.width / 2 - 100, RealmsScreen.row(12), 200, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    private void onInvite() {
        final RealmsClient dfy2 = RealmsClient.create();
        if (this.profileName.getValue() == null || this.profileName.getValue().isEmpty()) {
            this.showError(RealmsInviteScreen.NO_SUCH_PLAYER_ERROR_TEXT);
            return;
        }
        try {
            final RealmsServer dgn3 = dfy2.invite(this.serverData.id, this.profileName.getValue().trim());
            if (dgn3 != null) {
                this.serverData.players = dgn3.players;
                this.minecraft.setScreen(new RealmsPlayerScreen(this.configureScreen, this.serverData));
            }
            else {
                this.showError(RealmsInviteScreen.NO_SUCH_PLAYER_ERROR_TEXT);
            }
        }
        catch (Exception exception3) {
            RealmsInviteScreen.LOGGER.error("Couldn't invite user");
            this.showError(RealmsInviteScreen.NO_SUCH_PLAYER_ERROR_TEXT);
        }
    }
    
    private void showError(final Component nr) {
        this.errorMsg = nr;
        NarrationHelper.now(nr.getString());
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
        this.font.draw(dfj, RealmsInviteScreen.NAME_LABEL, (float)(this.width / 2 - 100), (float)RealmsScreen.row(1), 10526880);
        if (this.errorMsg != null) {
            GuiComponent.drawCenteredString(dfj, this.font, this.errorMsg, this.width / 2, RealmsScreen.row(5), 16711680);
        }
        this.profileName.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    static {
        LOGGER = LogManager.getLogger();
        NAME_LABEL = new TranslatableComponent("mco.configure.world.invite.profile.name");
        NO_SUCH_PLAYER_ERROR_TEXT = new TranslatableComponent("mco.configure.world.players.error");
    }
}
