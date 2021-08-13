package com.mojang.realmsclient.gui.screens;

import net.minecraft.util.Mth;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.FormattedText;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.client.gui.components.Button;
import com.mojang.realmsclient.dto.RealmsServer;
import com.mojang.realmsclient.dto.RealmsWorldOptions;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsSlotOptionsScreen extends RealmsScreen {
    public static final Component[] DIFFICULTIES;
    public static final Component[] GAME_MODES;
    private static final Component TEXT_ON;
    private static final Component TEXT_OFF;
    private static final Component GAME_MODE_LABEL;
    private static final Component NAME_LABEL;
    private EditBox nameEdit;
    protected final RealmsConfigureWorldScreen parent;
    private int column1X;
    private int columnWidth;
    private int column2X;
    private final RealmsWorldOptions options;
    private final RealmsServer.WorldType worldType;
    private final int activeSlot;
    private int difficulty;
    private int gameMode;
    private Boolean pvp;
    private Boolean spawnNPCs;
    private Boolean spawnAnimals;
    private Boolean spawnMonsters;
    private Integer spawnProtection;
    private Boolean commandBlocks;
    private Boolean forceGameMode;
    private Button pvpButton;
    private Button spawnAnimalsButton;
    private Button spawnMonstersButton;
    private Button spawnNPCsButton;
    private SettingsSlider spawnProtectionButton;
    private Button commandBlocksButton;
    private Button forceGameModeButton;
    private RealmsLabel titleLabel;
    private RealmsLabel warningLabel;
    
    public RealmsSlotOptionsScreen(final RealmsConfigureWorldScreen dhp, final RealmsWorldOptions dgt, final RealmsServer.WorldType c, final int integer) {
        this.parent = dhp;
        this.options = dgt;
        this.worldType = c;
        this.activeSlot = integer;
    }
    
    @Override
    public void removed() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(false);
    }
    
    @Override
    public void tick() {
        this.nameEdit.tick();
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.minecraft.setScreen(this.parent);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    public void init() {
        this.columnWidth = 170;
        this.column1X = this.width / 2 - this.columnWidth;
        this.column2X = this.width / 2 + 10;
        this.difficulty = this.options.difficulty;
        this.gameMode = this.options.gameMode;
        if (this.worldType == RealmsServer.WorldType.NORMAL) {
            this.pvp = this.options.pvp;
            this.spawnProtection = this.options.spawnProtection;
            this.forceGameMode = this.options.forceGameMode;
            this.spawnAnimals = this.options.spawnAnimals;
            this.spawnMonsters = this.options.spawnMonsters;
            this.spawnNPCs = this.options.spawnNPCs;
            this.commandBlocks = this.options.commandBlocks;
        }
        else {
            Component nr2;
            if (this.worldType == RealmsServer.WorldType.ADVENTUREMAP) {
                nr2 = new TranslatableComponent("mco.configure.world.edit.subscreen.adventuremap");
            }
            else if (this.worldType == RealmsServer.WorldType.INSPIRATION) {
                nr2 = new TranslatableComponent("mco.configure.world.edit.subscreen.inspiration");
            }
            else {
                nr2 = new TranslatableComponent("mco.configure.world.edit.subscreen.experience");
            }
            this.warningLabel = new RealmsLabel(nr2, this.width / 2, 26, 16711680);
            this.pvp = true;
            this.spawnProtection = 0;
            this.forceGameMode = false;
            this.spawnAnimals = true;
            this.spawnMonsters = true;
            this.spawnNPCs = true;
            this.commandBlocks = true;
        }
        (this.nameEdit = new EditBox(this.minecraft.font, this.column1X + 2, RealmsScreen.row(1), this.columnWidth - 4, 20, null, new TranslatableComponent("mco.configure.world.edit.slot.name"))).setMaxLength(10);
        this.nameEdit.setValue(this.options.getSlotName(this.activeSlot));
        this.magicalSpecialHackyFocus(this.nameEdit);
        this.pvpButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(1), this.columnWidth, 20, this.pvpTitle(), dlg -> {
            this.pvp = !this.pvp;
            dlg.setMessage(this.pvpTitle());
            return;
        }));
        this.<Button>addButton(new Button(this.column1X, RealmsScreen.row(3), this.columnWidth, 20, this.gameModeTitle(), dlg -> {
            this.gameMode = (this.gameMode + 1) % RealmsSlotOptionsScreen.GAME_MODES.length;
            dlg.setMessage(this.gameModeTitle());
            return;
        }));
        this.spawnAnimalsButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(3), this.columnWidth, 20, this.spawnAnimalsTitle(), dlg -> {
            this.spawnAnimals = !this.spawnAnimals;
            dlg.setMessage(this.spawnAnimalsTitle());
            return;
        }));
        this.<Button>addButton(new Button(this.column1X, RealmsScreen.row(5), this.columnWidth, 20, this.difficultyTitle(), dlg -> {
            this.difficulty = (this.difficulty + 1) % RealmsSlotOptionsScreen.DIFFICULTIES.length;
            dlg.setMessage(this.difficultyTitle());
            if (this.worldType == RealmsServer.WorldType.NORMAL) {
                this.spawnMonstersButton.active = (this.difficulty != 0);
                this.spawnMonstersButton.setMessage(this.spawnMonstersTitle());
            }
            return;
        }));
        this.spawnMonstersButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(5), this.columnWidth, 20, this.spawnMonstersTitle(), dlg -> {
            this.spawnMonsters = !this.spawnMonsters;
            dlg.setMessage(this.spawnMonstersTitle());
            return;
        }));
        this.spawnProtectionButton = this.<SettingsSlider>addButton(new SettingsSlider(this.column1X, RealmsScreen.row(7), this.columnWidth, this.spawnProtection, 0.0f, 16.0f));
        this.spawnNPCsButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(7), this.columnWidth, 20, this.spawnNPCsTitle(), dlg -> {
            this.spawnNPCs = !this.spawnNPCs;
            dlg.setMessage(this.spawnNPCsTitle());
            return;
        }));
        this.forceGameModeButton = this.<Button>addButton(new Button(this.column1X, RealmsScreen.row(9), this.columnWidth, 20, this.forceGameModeTitle(), dlg -> {
            this.forceGameMode = !this.forceGameMode;
            dlg.setMessage(this.forceGameModeTitle());
            return;
        }));
        this.commandBlocksButton = this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(9), this.columnWidth, 20, this.commandBlocksTitle(), dlg -> {
            this.commandBlocks = !this.commandBlocks;
            dlg.setMessage(this.commandBlocksTitle());
            return;
        }));
        if (this.worldType != RealmsServer.WorldType.NORMAL) {
            this.pvpButton.active = false;
            this.spawnAnimalsButton.active = false;
            this.spawnNPCsButton.active = false;
            this.spawnMonstersButton.active = false;
            this.spawnProtectionButton.active = false;
            this.commandBlocksButton.active = false;
            this.forceGameModeButton.active = false;
        }
        if (this.difficulty == 0) {
            this.spawnMonstersButton.active = false;
        }
        this.<Button>addButton(new Button(this.column1X, RealmsScreen.row(13), this.columnWidth, 20, new TranslatableComponent("mco.configure.world.buttons.done"), dlg -> this.saveSettings()));
        this.<Button>addButton(new Button(this.column2X, RealmsScreen.row(13), this.columnWidth, 20, CommonComponents.GUI_CANCEL, dlg -> this.minecraft.setScreen(this.parent)));
        this.<EditBox>addWidget(this.nameEdit);
        this.titleLabel = this.<RealmsLabel>addWidget(new RealmsLabel(new TranslatableComponent("mco.configure.world.buttons.options"), this.width / 2, 17, 16777215));
        if (this.warningLabel != null) {
            this.<RealmsLabel>addWidget(this.warningLabel);
        }
        this.narrateLabels();
    }
    
    private Component difficultyTitle() {
        return new TranslatableComponent("options.difficulty").append(": ").append(RealmsSlotOptionsScreen.DIFFICULTIES[this.difficulty]);
    }
    
    private Component gameModeTitle() {
        return new TranslatableComponent("options.generic_value", new Object[] { RealmsSlotOptionsScreen.GAME_MODE_LABEL, RealmsSlotOptionsScreen.GAME_MODES[this.gameMode] });
    }
    
    private Component pvpTitle() {
        return new TranslatableComponent("mco.configure.world.pvp").append(": ").append(getOnOff(this.pvp));
    }
    
    private Component spawnAnimalsTitle() {
        return new TranslatableComponent("mco.configure.world.spawnAnimals").append(": ").append(getOnOff(this.spawnAnimals));
    }
    
    private Component spawnMonstersTitle() {
        if (this.difficulty == 0) {
            return new TranslatableComponent("mco.configure.world.spawnMonsters").append(": ").append(new TranslatableComponent("mco.configure.world.off"));
        }
        return new TranslatableComponent("mco.configure.world.spawnMonsters").append(": ").append(getOnOff(this.spawnMonsters));
    }
    
    private Component spawnNPCsTitle() {
        return new TranslatableComponent("mco.configure.world.spawnNPCs").append(": ").append(getOnOff(this.spawnNPCs));
    }
    
    private Component commandBlocksTitle() {
        return new TranslatableComponent("mco.configure.world.commandBlocks").append(": ").append(getOnOff(this.commandBlocks));
    }
    
    private Component forceGameModeTitle() {
        return new TranslatableComponent("mco.configure.world.forceGameMode").append(": ").append(getOnOff(this.forceGameMode));
    }
    
    private static Component getOnOff(final boolean boolean1) {
        return boolean1 ? RealmsSlotOptionsScreen.TEXT_ON : RealmsSlotOptionsScreen.TEXT_OFF;
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.font.draw(dfj, RealmsSlotOptionsScreen.NAME_LABEL, (float)(this.column1X + this.columnWidth / 2 - this.font.width(RealmsSlotOptionsScreen.NAME_LABEL) / 2), (float)(RealmsScreen.row(0) - 5), 16777215);
        this.titleLabel.render(this, dfj);
        if (this.warningLabel != null) {
            this.warningLabel.render(this, dfj);
        }
        this.nameEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private String getSlotName() {
        if (this.nameEdit.getValue().equals(this.options.getDefaultSlotName(this.activeSlot))) {
            return "";
        }
        return this.nameEdit.getValue();
    }
    
    private void saveSettings() {
        if (this.worldType == RealmsServer.WorldType.ADVENTUREMAP || this.worldType == RealmsServer.WorldType.EXPERIENCE || this.worldType == RealmsServer.WorldType.INSPIRATION) {
            this.parent.saveSlotSettings(new RealmsWorldOptions(this.options.pvp, this.options.spawnAnimals, this.options.spawnMonsters, this.options.spawnNPCs, this.options.spawnProtection, this.options.commandBlocks, this.difficulty, this.gameMode, this.options.forceGameMode, this.getSlotName()));
        }
        else {
            this.parent.saveSlotSettings(new RealmsWorldOptions(this.pvp, this.spawnAnimals, this.spawnMonsters, this.spawnNPCs, this.spawnProtection, this.commandBlocks, this.difficulty, this.gameMode, this.forceGameMode, this.getSlotName()));
        }
    }
    
    static {
        DIFFICULTIES = new Component[] { new TranslatableComponent("options.difficulty.peaceful"), new TranslatableComponent("options.difficulty.easy"), new TranslatableComponent("options.difficulty.normal"), new TranslatableComponent("options.difficulty.hard") };
        GAME_MODES = new Component[] { new TranslatableComponent("selectWorld.gameMode.survival"), new TranslatableComponent("selectWorld.gameMode.creative"), new TranslatableComponent("selectWorld.gameMode.adventure") };
        TEXT_ON = new TranslatableComponent("mco.configure.world.on");
        TEXT_OFF = new TranslatableComponent("mco.configure.world.off");
        GAME_MODE_LABEL = new TranslatableComponent("selectWorld.gameMode");
        NAME_LABEL = new TranslatableComponent("mco.configure.world.edit.slot.name");
    }
    
    class SettingsSlider extends AbstractSliderButton {
        private final double minValue;
        private final double maxValue;
        
        public SettingsSlider(final int integer2, final int integer3, final int integer4, final int integer5, final float float6, final float float7) {
            super(integer2, integer3, integer4, 20, TextComponent.EMPTY, 0.0);
            this.minValue = float6;
            this.maxValue = float7;
            this.value = (Mth.clamp((float)integer5, float6, float7) - float6) / (float7 - float6);
            this.updateMessage();
        }
        
        public void applyValue() {
            if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active) {
                return;
            }
            RealmsSlotOptionsScreen.this.spawnProtection = (int)Mth.lerp(Mth.clamp(this.value, 0.0, 1.0), this.minValue, this.maxValue);
        }
        
        @Override
        protected void updateMessage() {
            this.setMessage(new TranslatableComponent("mco.configure.world.spawnProtection").append(": ").append((RealmsSlotOptionsScreen.this.spawnProtection == 0) ? new TranslatableComponent("mco.configure.world.off") : new TextComponent(String.valueOf(RealmsSlotOptionsScreen.this.spawnProtection))));
        }
        
        @Override
        public void onClick(final double double1, final double double2) {
        }
        
        @Override
        public void onRelease(final double double1, final double double2) {
        }
    }
}
