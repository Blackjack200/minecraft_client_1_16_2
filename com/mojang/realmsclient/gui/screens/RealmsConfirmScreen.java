package com.mojang.realmsclient.gui.screens;

import java.util.Iterator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.realms.RealmsScreen;

public class RealmsConfirmScreen extends RealmsScreen {
    protected BooleanConsumer callback;
    private final Component title1;
    private final Component title2;
    private int delayTicker;
    
    public RealmsConfirmScreen(final BooleanConsumer booleanConsumer, final Component nr2, final Component nr3) {
        this.callback = booleanConsumer;
        this.title1 = nr2;
        this.title2 = nr3;
    }
    
    public void init() {
        this.<Button>addButton(new Button(this.width / 2 - 105, RealmsScreen.row(9), 100, 20, CommonComponents.GUI_YES, dlg -> this.callback.accept(true)));
        this.<Button>addButton(new Button(this.width / 2 + 5, RealmsScreen.row(9), 100, 20, CommonComponents.GUI_NO, dlg -> this.callback.accept(false)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title1, this.width / 2, RealmsScreen.row(3), 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, this.title2, this.width / 2, RealmsScreen.row(5), 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    @Override
    public void tick() {
        super.tick();
        final int delayTicker = this.delayTicker - 1;
        this.delayTicker = delayTicker;
        if (delayTicker == 0) {
            for (final AbstractWidget dle3 : this.buttons) {
                dle3.active = true;
            }
        }
    }
}
