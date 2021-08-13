package net.minecraft.client.gui.screens;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;

public class DisconnectedScreen extends Screen {
    private final Component reason;
    private MultiLineLabel message;
    private final Screen parent;
    private int textHeight;
    
    public DisconnectedScreen(final Screen doq, final Component nr2, final Component nr3) {
        super(nr2);
        this.message = MultiLineLabel.EMPTY;
        this.parent = doq;
        this.reason = nr3;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    protected void init() {
        this.message = MultiLineLabel.create(this.font, this.reason, this.width - 50);
        final int lineCount = this.message.getLineCount();
        this.font.getClass();
        this.textHeight = lineCount * 9;
        final int integer1 = this.width / 2 - 100;
        final int n = this.height / 2 + this.textHeight / 2;
        this.font.getClass();
        this.<Button>addButton(new Button(integer1, Math.min(n + 9, this.height - 30), 200, 20, new TranslatableComponent("gui.toMenu"), dlg -> this.minecraft.setScreen(this.parent)));
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        final Font font = this.font;
        final Component title = this.title;
        final int integer4 = this.width / 2;
        final int n = this.height / 2 - this.textHeight / 2;
        this.font.getClass();
        GuiComponent.drawCenteredString(dfj, font, title, integer4, n - 9 * 2, 11184810);
        this.message.renderCentered(dfj, this.width / 2, this.height / 2 - this.textHeight / 2);
        super.render(dfj, integer2, integer3, float4);
    }
}
