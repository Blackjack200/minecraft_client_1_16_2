package net.minecraft.client.gui.screens;

import java.util.Iterator;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.GuiComponent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.MultiLineLabel;
import net.minecraft.network.chat.Component;

public class ConfirmScreen extends Screen {
    private final Component title2;
    private MultiLineLabel message;
    protected Component yesButton;
    protected Component noButton;
    private int delayTicker;
    protected final BooleanConsumer callback;
    
    public ConfirmScreen(final BooleanConsumer booleanConsumer, final Component nr2, final Component nr3) {
        this(booleanConsumer, nr2, nr3, CommonComponents.GUI_YES, CommonComponents.GUI_NO);
    }
    
    public ConfirmScreen(final BooleanConsumer booleanConsumer, final Component nr2, final Component nr3, final Component nr4, final Component nr5) {
        super(nr2);
        this.message = MultiLineLabel.EMPTY;
        this.callback = booleanConsumer;
        this.title2 = nr3;
        this.yesButton = nr4;
        this.noButton = nr5;
    }
    
    @Override
    public String getNarrationMessage() {
        return super.getNarrationMessage() + ". " + this.title2.getString();
    }
    
    @Override
    protected void init() {
        super.init();
        this.<Button>addButton(new Button(this.width / 2 - 155, this.height / 6 + 96, 150, 20, this.yesButton, dlg -> this.callback.accept(true)));
        this.<Button>addButton(new Button(this.width / 2 - 155 + 160, this.height / 6 + 96, 150, 20, this.noButton, dlg -> this.callback.accept(false)));
        this.message = MultiLineLabel.create(this.font, this.title2, this.width - 50);
    }
    
    @Override
    public void render(final PoseStack dfj, final int integer2, final int integer3, final float float4) {
        this.renderBackground(dfj);
        GuiComponent.drawCenteredString(dfj, this.font, this.title, this.width / 2, 70, 16777215);
        this.message.renderCentered(dfj, this.width / 2, 90);
        super.render(dfj, integer2, integer3, float4);
    }
    
    public void setDelay(final int integer) {
        this.delayTicker = integer;
        for (final AbstractWidget dle4 : this.buttons) {
            dle4.active = false;
        }
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
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public boolean keyPressed(final int integer1, final int integer2, final int integer3) {
        if (integer1 == 256) {
            this.callback.accept(false);
            return true;
        }
        return super.keyPressed(integer1, integer2, integer3);
    }
}
