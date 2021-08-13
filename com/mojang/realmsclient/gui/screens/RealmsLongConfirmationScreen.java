package com.mojang.realmsclient.gui.screens;

import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.realms.NarrationHelper;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.network.chat.Component;
import net.minecraft.realms.RealmsScreen;

public class RealmsLongConfirmationScreen extends RealmsScreen {
    private final Type type;
    private final Component line2;
    private final Component line3;
    protected final BooleanConsumer callback;
    private final boolean yesNoQuestion;
    
    public RealmsLongConfirmationScreen(final BooleanConsumer booleanConsumer, final Type a, final Component nr3, final Component nr4, final boolean boolean5) {
        this.callback = booleanConsumer;
        this.type = a;
        this.line2 = nr3;
        this.line3 = nr4;
        this.yesNoQuestion = boolean5;
    }
    
    public void init() {
        NarrationHelper.now(this.type.text, this.line2.getString(), this.line3.getString());
        if (this.yesNoQuestion) {
            this.<Button>addButton(new Button(this.width / 2 - 105, RealmsScreen.row(8), 100, 20, CommonComponents.GUI_YES, dlg -> this.callback.accept(true)));
            this.<Button>addButton(new Button(this.width / 2 + 5, RealmsScreen.row(8), 100, 20, CommonComponents.GUI_NO, dlg -> this.callback.accept(false)));
        }
        else {
            this.<Button>addButton(new Button(this.width / 2 - 50, RealmsScreen.row(8), 100, 20, new TranslatableComponent("mco.gui.ok"), dlg -> this.callback.accept(true)));
        }
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.callback.accept(false);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.type.text, this.width / 2, RealmsScreen.row(2), this.type.colorCode);
        GuiComponent.drawCenteredString(dfj, this.font, this.line2, this.width / 2, RealmsScreen.row(4), 16777215);
        GuiComponent.drawCenteredString(dfj, this.font, this.line3, this.width / 2, RealmsScreen.row(6), 16777215);
        super.render(dfj, integer2, integer3, float4);
    }
    
    public enum Type {
        Warning("Warning!", 16711680), 
        Info("Info!", 8226750);
        
        public final int colorCode;
        public final String text;
        
        private Type(final String string3, final int integer4) {
            this.text = string3;
            this.colorCode = integer4;
        }
    }
}
