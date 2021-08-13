package com.mojang.realmsclient.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.realms.RealmsLabel;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsResetNormalWorldScreen extends RealmsScreen {
    private static final Component SEED_LABEL;
    private static final Component[] LEVEL_TYPES;
    private final RealmsResetWorldScreen lastScreen;
    private RealmsLabel titleLabel;
    private EditBox seedEdit;
    private Boolean generateStructures;
    private Integer levelTypeIndex;
    private Component buttonTitle;
    
    public RealmsResetNormalWorldScreen(final RealmsResetWorldScreen dic, final Component nr) {
        this.generateStructures = true;
        this.levelTypeIndex = 0;
        this.lastScreen = dic;
        this.buttonTitle = nr;
    }
    
    @Override
    public void tick() {
        this.seedEdit.tick();
        super.tick();
    }
    
    public void init() {
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
        this.<RealmsLabel>addWidget(this.titleLabel = new RealmsLabel(new TranslatableComponent("mco.reset.world.generate"), this.width / 2, 17, 16777215));
        (this.seedEdit = new EditBox(this.minecraft.font, this.width / 2 - 100, RealmsScreen.row(2), 200, 20, null, new TranslatableComponent("mco.reset.world.seed"))).setMaxLength(32);
        this.<EditBox>addWidget(this.seedEdit);
        this.setInitialFocus(this.seedEdit);
        this.<Button>addButton(new Button(this.width / 2 - 102, RealmsScreen.row(4), 205, 20, this.levelTypeTitle(), dlg -> {
            this.levelTypeIndex = (this.levelTypeIndex + 1) % RealmsResetNormalWorldScreen.LEVEL_TYPES.length;
            dlg.setMessage(this.levelTypeTitle());
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 102, RealmsScreen.row(6) - 2, 205, 20, this.generateStructuresTitle(), dlg -> {
            this.generateStructures = !this.generateStructures;
            dlg.setMessage(this.generateStructuresTitle());
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 102, RealmsScreen.row(12), 97, 20, this.buttonTitle, dlg -> this.lastScreen.resetWorld(new RealmsResetWorldScreen.ResetWorldInfo(this.seedEdit.getValue(), this.levelTypeIndex, this.generateStructures))));
        this.<Button>addButton(new Button(this.width / 2 + 8, RealmsScreen.row(12), 97, 20, CommonComponents.GUI_BACK, dlg -> this.minecraft.setScreen(this.lastScreen)));
        this.narrateLabels();
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
        this.titleLabel.render(this, dfj);
        this.font.draw(dfj, RealmsResetNormalWorldScreen.SEED_LABEL, (float)(this.width / 2 - 100), (float)RealmsScreen.row(1), 10526880);
        this.seedEdit.render(dfj, integer2, integer3, float4);
        super.render(dfj, integer2, integer3, float4);
    }
    
    private Component levelTypeTitle() {
        return new TranslatableComponent("selectWorld.mapType").append(" ").append(RealmsResetNormalWorldScreen.LEVEL_TYPES[this.levelTypeIndex]);
    }
    
    private Component generateStructuresTitle() {
        return CommonComponents.optionStatus(new TranslatableComponent("selectWorld.mapFeatures"), this.generateStructures);
    }
    
    static {
        SEED_LABEL = new TranslatableComponent("mco.reset.world.seed");
        LEVEL_TYPES = new Component[] { new TranslatableComponent("generator.default"), new TranslatableComponent("generator.flat"), new TranslatableComponent("generator.large_biomes"), new TranslatableComponent("generator.amplified") };
    }
}
