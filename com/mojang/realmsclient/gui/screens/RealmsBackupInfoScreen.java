package com.mojang.realmsclient.gui.screens;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import java.util.Locale;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import com.mojang.realmsclient.dto.Backup;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.realms.RealmsScreen;

public class RealmsBackupInfoScreen extends RealmsScreen {
    private final Screen lastScreen;
    private final Backup backup;
    private BackupInfoList backupInfoList;
    
    public RealmsBackupInfoScreen(final Screen doq, final Backup dgd) {
        this.lastScreen = doq;
        this.backup = dgd;
    }
    
    @Override
    public void tick() {
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.<Button>addButton(new Button(this.width / 2 - 100, this.height / 4 + 120 + 24, 200, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.<BackupInfoList>addWidget(this.backupInfoList = new BackupInfoList(this.minecraft));
        this.magicalSpecialHackyFocus(this.backupInfoList);
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
        GuiComponent.drawCenteredString(dfj, this.font, "Changes from last backup", this.width / 2, 10, 16777215);
        this.backupInfoList.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private Component checkForSpecificMetadata(final String string1, final String string2) {
        final String string3 = string1.toLowerCase(Locale.ROOT);
        if (string3.contains("game") && string3.contains("mode")) {
            return this.gameModeMetadata(string2);
        }
        if (string3.contains("game") && string3.contains("difficulty")) {
            return this.gameDifficultyMetadata(string2);
        }
        return new TextComponent(string2);
    }
    
    private Component gameDifficultyMetadata(final String string) {
        try {
            return RealmsSlotOptionsScreen.DIFFICULTIES[Integer.parseInt(string)];
        }
        catch (Exception exception3) {
            return new TextComponent("UNKNOWN");
        }
    }
    
    private Component gameModeMetadata(final String string) {
        try {
            return RealmsSlotOptionsScreen.GAME_MODES[Integer.parseInt(string)];
        }
        catch (Exception exception3) {
            return new TextComponent("UNKNOWN");
        }
    }
    
    class BackupInfoListEntry extends ObjectSelectionList.Entry<BackupInfoListEntry> {
        private final String key;
        private final String value;
        
        public BackupInfoListEntry(final String string2, final String string3) {
            this.key = string2;
            this.value = string3;
        }
        
        @Override
        public void render(final PoseStack dfj, final int integer2, final int integer3, final int integer4, final int integer5, final int integer6, final int integer7, final int integer8, final boolean boolean9, final float float10) {
            final Font dkr12 = RealmsBackupInfoScreen.this.minecraft.font;
            GuiComponent.drawString(dfj, dkr12, this.key, integer4, integer3, 10526880);
            GuiComponent.drawString(dfj, dkr12, RealmsBackupInfoScreen.this.checkForSpecificMetadata(this.key, this.value), integer4, integer3 + 12, 16777215);
        }
    }
    
    class BackupInfoList extends ObjectSelectionList<BackupInfoListEntry> {
        public BackupInfoList(final Minecraft djw) {
            super(djw, RealmsBackupInfoScreen.this.width, RealmsBackupInfoScreen.this.height, 32, RealmsBackupInfoScreen.this.height - 64, 36);
            this.setRenderSelection(false);
            if (RealmsBackupInfoScreen.this.backup.changeList != null) {
                RealmsBackupInfoScreen.this.backup.changeList.forEach((string1, string2) -> this.addEntry(new BackupInfoListEntry(string1, string2)));
            }
        }
    }
}
