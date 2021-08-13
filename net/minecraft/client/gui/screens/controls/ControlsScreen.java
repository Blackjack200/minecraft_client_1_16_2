package net.minecraft.client.gui.screens.controls;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.Util;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.Option;
import net.minecraft.client.gui.screens.MouseSettingsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.OptionsSubScreen;

public class ControlsScreen extends OptionsSubScreen {
    public KeyMapping selectedKey;
    public long lastKeySelection;
    private ControlList controlList;
    private Button resetButton;
    
    public ControlsScreen(final Screen doq, final Options dka) {
        super(doq, dka, new TranslatableComponent("controls.title"));
    }
    
    @Override
    protected void init() {
        this.<Button>addButton(new Button(this.width / 2 - 155, 18, 150, 20, new TranslatableComponent("options.mouse_settings"), dlg -> this.minecraft.setScreen(new MouseSettingsScreen(this, this.options))));
        this.<AbstractWidget>addButton(Option.AUTO_JUMP.createButton(this.options, this.width / 2 - 155 + 160, 18, 150));
        this.controlList = new ControlList(this, this.minecraft);
        this.children.add(this.controlList);
        final KeyMapping[] keyMappings;
        int length;
        int i = 0;
        KeyMapping djt6;
        this.resetButton = this.<Button>addButton(new Button(this.width / 2 - 155, this.height - 29, 150, 20, new TranslatableComponent("controls.resetAll"), dlg -> {
            keyMappings = this.options.keyMappings;
            for (length = keyMappings.length; i < length; ++i) {
                djt6 = keyMappings[i];
                djt6.setKey(djt6.getDefaultKey());
            }
            KeyMapping.resetMapping();
            return;
        }));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height - 29, 150, 20, CommonComponents.GUI_DONE, dlg -> this.minecraft.setScreen(this.lastScreen)));
    }
    
    @Override
    public boolean mouseClicked(final double double1, final double double2, final int integer) {
        if (this.selectedKey != null) {
            this.options.setKey(this.selectedKey, InputConstants.Type.MOUSE.getOrCreate(integer));
            this.selectedKey = null;
            KeyMapping.resetMapping();
            return true;
        }
        return super.mouseClicked(double1, double2, integer);
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (this.selectedKey != null) {
            if (integer1 == 256) {
                this.options.setKey(this.selectedKey, InputConstants.UNKNOWN);
            }
            else {
                this.options.setKey(this.selectedKey, InputConstants.getKey(integer1, integer2));
            }
            this.selectedKey = null;
            this.lastKeySelection = Util.getMillis();
            KeyMapping.resetMapping();
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        this.controlList.render(dfj, integer2, integer3, float4);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 8, 16777215);
        boolean boolean6 = false;
        for (final KeyMapping djt10 : this.options.keyMappings) {
            if (!djt10.isDefault()) {
                boolean6 = true;
                break;
            }
        }
        this.resetButton.active = boolean6;
        super.render(dfj, integer2, integer3, float4);
    }
}
