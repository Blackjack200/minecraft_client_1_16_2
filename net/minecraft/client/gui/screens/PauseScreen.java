package net.minecraft.client.gui.screens;

import net.minecraft.Util;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.realms.RealmsBridge;
import net.minecraft.SharedConstants;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.client.gui.screens.advancements.AdvancementsScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class PauseScreen extends Screen {
    private final boolean showPauseMenu;
    
    public PauseScreen(final boolean boolean1) {
        super(boolean1 ? new TranslatableComponent("menu.game") : new TranslatableComponent("menu.paused"));
        this.showPauseMenu = boolean1;
    }
    
    @Override
    protected void init() {
        if (this.showPauseMenu) {
            this.createPauseMenu();
        }
    }
    
    private void createPauseMenu() {
        final int integer2 = -16;
        final int integer3 = 98;
        this.<Button>addButton(new Button(this.width / 2 - 102, this.height / 4 + 24 - 16, 204, 20, new TranslatableComponent("menu.returnToGame"), dlg -> {
            this.minecraft.setScreen(null);
            this.minecraft.mouseHandler.grabMouse();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 102, this.height / 4 + 48 - 16, 98, 20, new TranslatableComponent("gui.advancements"), dlg -> this.minecraft.setScreen(new AdvancementsScreen(this.minecraft.player.connection.getAdvancements()))));
        this.<Button>addButton(new Button(this.width / 2 + 4, this.height / 4 + 48 - 16, 98, 20, new TranslatableComponent("gui.stats"), dlg -> this.minecraft.setScreen(new StatsScreen(this, this.minecraft.player.getStats()))));
        final String string4 = SharedConstants.getCurrentVersion().isStable() ? "https://aka.ms/javafeedback?ref=game" : "https://aka.ms/snapshotfeedback?ref=game";
        final String string5;
        this.<Button>addButton(new Button(this.width / 2 - 102, this.height / 4 + 72 - 16, 98, 20, new TranslatableComponent("menu.sendFeedback"), dlg -> this.minecraft.setScreen(new ConfirmLinkScreen(boolean2 -> {
            if (boolean2) {
                Util.getPlatform().openUri(string5);
            }
            this.minecraft.setScreen(this);
        }, string5, true))));
        this.<Button>addButton(new Button(this.width / 2 + 4, this.height / 4 + 72 - 16, 98, 20, new TranslatableComponent("menu.reportBugs"), dlg -> this.minecraft.setScreen(new ConfirmLinkScreen(boolean1 -> {
            if (boolean1) {
                Util.getPlatform().openUri("https://aka.ms/snapshotbugs?ref=game");
            }
            this.minecraft.setScreen(this);
        }, "https://aka.ms/snapshotbugs?ref=game", true))));
        this.<Button>addButton(new Button(this.width / 2 - 102, this.height / 4 + 96 - 16, 98, 20, new TranslatableComponent("menu.options"), dlg -> this.minecraft.setScreen(new OptionsScreen(this, this.minecraft.options))));
        final Button dlg2 = this.<Button>addButton(new Button(this.width / 2 + 4, this.height / 4 + 96 - 16, 98, 20, new TranslatableComponent("menu.shareToLan"), dlg -> this.minecraft.setScreen(new ShareToLanScreen(this))));
        dlg2.active = (this.minecraft.hasSingleplayerServer() && !this.minecraft.getSingleplayerServer().isPublished());
        final boolean boolean3;
        final boolean boolean4;
        Minecraft minecraft;
        final GenericDirtMessageScreen doq;
        RealmsBridge eoc5;
        Minecraft minecraft2;
        final JoinMultiplayerScreen screen;
        final Button dlg3 = this.<Button>addButton(new Button(this.width / 2 - 102, this.height / 4 + 120 - 16, 204, 20, new TranslatableComponent("menu.returnToMenu"), dlg -> {
            boolean3 = this.minecraft.isLocalServer();
            boolean4 = this.minecraft.isConnectedToRealms();
            dlg.active = false;
            this.minecraft.level.disconnect();
            if (boolean3) {
                minecraft = this.minecraft;
                new GenericDirtMessageScreen(new TranslatableComponent("menu.savingLevel"));
                minecraft.clearLevel(doq);
            }
            else {
                this.minecraft.clearLevel();
            }
            if (boolean3) {
                this.minecraft.setScreen(new TitleScreen());
            }
            else if (boolean4) {
                eoc5 = new RealmsBridge();
                eoc5.switchToRealms(new TitleScreen());
            }
            else {
                minecraft2 = this.minecraft;
                new JoinMultiplayerScreen(new TitleScreen());
                minecraft2.setScreen(screen);
            }
            return;
        }));
        if (!this.minecraft.isLocalServer()) {
            dlg3.setMessage(new TranslatableComponent("menu.disconnect"));
        }
    }
    
    @Override
    public void tick() {
        super.tick();
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        if (this.showPauseMenu) {
            this.renderBackground(dfj);
            GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 40, 16777215);
        }
        else {
            GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 10, 16777215);
        }
        super.render(dfj, integer2, integer3, float4);
    }
}
